package com.example.ui

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class FinanceViewModel(
    application: Application,
    private val repository: FinanceRepository
) : AndroidViewModel(application) {

    // Central flow sources
    val wallets: StateFlow<List<WalletEntity>> = repository.allWallets
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val transactions: StateFlow<List<TransactionEntity>> = repository.allTransactions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val budgets: StateFlow<List<BudgetEntity>> = repository.allBudgets
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Search and filter states
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedFilterCategory = MutableStateFlow<String?>(null)
    val selectedFilterCategory = _selectedFilterCategory.asStateFlow()

    private val _selectedFilterWallet = MutableStateFlow<Long?>(null) // null = all wallets
    val selectedFilterWallet = _selectedFilterWallet.asStateFlow()

    // Transaction list filtered by search query, category and wallet
    val filteredTransactions: StateFlow<List<TransactionEntity>> = combine(
        transactions,
        searchQuery,
        selectedFilterCategory,
        selectedFilterWallet
    ) { txList, query, cat, walletId ->
        txList.filter { tx ->
            val matchesQuery = query.isEmpty() || 
                    tx.title.contains(query, ignoreCase = true) || 
                    tx.category.contains(query, ignoreCase = true) ||
                    tx.notes.contains(query, ignoreCase = true)
            val matchesCategory = cat == null || tx.category == cat
            val matchesWallet = walletId == null || tx.walletId == walletId
            matchesQuery && matchesCategory && matchesWallet
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Security PIN lock management
    private val sharedPrefs = application.getSharedPreferences("hisab_nikash_prefs", Context.MODE_PRIVATE)

    private val _isAppLocked = MutableStateFlow(false)
    val isAppLocked = _isAppLocked.asStateFlow()

    // Application Language Management
    private val _appLanguage = MutableStateFlow(sharedPrefs.getString("app_language", "bn") ?: "bn")
    val appLanguage = _appLanguage.asStateFlow()

    fun setLanguage(lang: String) {
        sharedPrefs.edit().putString("app_language", lang).apply()
        _appLanguage.value = lang
    }

    init {
        // Run seed database check on startup
        viewModelScope.launch {
            repository.seedDatabaseIfEmpty()
            // Check if app lock is enabled on startup
            if (isPinEnabled()) {
                _isAppLocked.value = true
            }
        }
    }

    fun isPinEnabled(): Boolean {
        return sharedPrefs.getString("app_pin", "")?.isNotEmpty() == true
    }

    fun getEnabledPin(): String {
        return sharedPrefs.getString("app_pin", "") ?: ""
    }

    fun setPin(pin: String) {
        sharedPrefs.edit().putString("app_pin", pin).apply()
        _isAppLocked.value = false
    }

    fun disablePin() {
        sharedPrefs.edit().remove("app_pin").apply()
        _isAppLocked.value = false
    }

    fun unlockApp(enteredPin: String): Boolean {
        val correctPin = getEnabledPin()
        return if (enteredPin == correctPin) {
            _isAppLocked.value = false
            true
        } else {
            false
        }
    }

    fun lockApp() {
        if (isPinEnabled()) {
            _isAppLocked.value = true
        }
    }

    // Search query setter
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectFilterCategory(category: String?) {
        _selectedFilterCategory.value = category
    }

    fun selectFilterWallet(walletId: Long?) {
        _selectedFilterWallet.value = walletId
    }

    // Core transaction modifiers
    fun addTransaction(
        title: String,
        amount: Double,
        isIncome: Boolean,
        category: String,
        timestamp: Long,
        walletId: Long,
        notes: String
    ) {
        viewModelScope.launch {
            val tx = TransactionEntity(
                title = title.ifEmpty { if (isIncome) "আয়" else "ব্যয়" },
                amount = amount,
                isIncome = isIncome,
                category = category,
                timestamp = timestamp,
                walletId = walletId,
                notes = notes
            )
            repository.addTransaction(tx)
        }
    }

    fun deleteTransaction(transaction: TransactionEntity) {
        viewModelScope.launch {
            repository.deleteTransaction(transaction)
        }
    }

    fun updateTransaction(newTx: TransactionEntity, oldTx: TransactionEntity) {
        viewModelScope.launch {
            repository.updateTransaction(newTx, oldTx)
        }
    }

    fun transferFunds(fromWalletId: Long, toWalletId: Long, amount: Double, notes: String) {
        viewModelScope.launch {
            repository.transferFunds(fromWalletId, toWalletId, amount, notes)
        }
    }

    // Wallet actions
    fun addWallet(name: String, balance: Double, type: String) {
        viewModelScope.launch {
            val wallet = WalletEntity(name = name, balance = balance, type = type)
            repository.addWallet(wallet)
        }
    }

    fun updateWallet(wallet: WalletEntity) {
        viewModelScope.launch {
            repository.updateWallet(wallet)
        }
    }

    fun deleteWallet(walletId: Long) {
        viewModelScope.launch {
            repository.deleteWallet(walletId)
        }
    }

    // Budget actions
    fun setBudget(category: String, amount: Double) {
        viewModelScope.launch {
            repository.setBudget(category, amount)
        }
    }

    fun deleteBudget(category: String) {
        viewModelScope.launch {
            repository.deleteBudgetByCategory(category)
        }
    }

    // Data Export Helper
    fun exportData(context: Context) {
        viewModelScope.launch {
            try {
                val (csvString, contentUri) = withContext(Dispatchers.IO) {
                    val currentTx = repository.allTransactions.first()
                    val currentWallets = repository.allWallets.first()
                    
                    val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
                    
                    // Generate CSV text
                    val csvBuilder = StringBuilder()
                    csvBuilder.append("আইডি,বিবরণ,পরিমাণ (৳),ধরন,খাত (Category),তারিখ,ওয়ালেট,নোট\n")
                    
                    currentTx.forEach { tx ->
                        val typeStr = if (tx.isIncome) "আয় (Income)" else "ব্যয় (Expense)"
                        val walletName = currentWallets.find { it.id == tx.walletId }?.name ?: "অজানা"
                        val formattedDate = sdf.format(Date(tx.timestamp))
                        
                        // Escape commas
                        val titleEscaped = tx.title.replace(",", " ")
                        val categoryEscaped = tx.category.replace(",", " ")
                        val notesEscaped = tx.notes.replace(",", " ").replace("\n", " ")
                        
                        csvBuilder.append("${tx.id},$titleEscaped,${tx.amount},$typeStr,$categoryEscaped,$formattedDate,$walletName,$notesEscaped\n")
                    }

                    val csvText = csvBuilder.toString()
                    val cachePath = File(context.cacheDir, "csv")
                    cachePath.mkdirs()
                    val csvFile = File(cachePath, "Hisab_Nikash_Ledger_${System.currentTimeMillis()}.csv")
                    csvFile.writeText(csvText, Charsets.UTF_8)
                    
                    val uri = FileProvider.getUriForFile(
                        context,
                        "${context.packageName}.fileprovider",
                        csvFile
                    )
                    Pair(csvText, uri)
                }
                
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/csv"
                    putExtra(Intent.EXTRA_SUBJECT, "হিসাব নিকাশ রিপোর্ট")
                    putExtra(Intent.EXTRA_STREAM, contentUri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                
                context.startActivity(Intent.createChooser(shareIntent, "এক্সপোর্ট ফাইল শেয়ার করুন"))
            } catch (e: Exception) {
                e.printStackTrace()
                // Fallback to sharing as text
                val shareTextIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_SUBJECT, "হিসাব নিকাশ রিপোর্ট")
                    putExtra(Intent.EXTRA_TEXT, "ফাইন্যান্স রিপোর্ট তৈরি করা হয়েছে:\n\nপার্সিং করতে সমস্যা হয়েছে।")
                }
                context.startActivity(Intent.createChooser(shareTextIntent, "এক্সপোর্ট রিপোর্ট শেয়ার করুন"))
            }
        }
    }

    // Google Drive States
    private val _googleAccountEmail = MutableStateFlow<String?>(null)
    val googleAccountEmail = _googleAccountEmail.asStateFlow()

    private val _backupStatus = MutableStateFlow<String?>(null)
    val backupStatus = _backupStatus.asStateFlow()

    private var currentAccessToken: String? = null

    fun setGoogleAccessToken(email: String, token: String) {
        _googleAccountEmail.value = email
        currentAccessToken = token
        _backupStatus.value = "গুগল সাইন-ইন সফল হয়েছে: $email"
    }

    fun disconnectGoogleAccount() {
        _googleAccountEmail.value = null
        currentAccessToken = null
        _backupStatus.value = null
    }

    fun triggerBackup() {
        val token = currentAccessToken
        if (token == null) {
            _backupStatus.value = "ব্যাকআপ ব্যর্থ: গুগল অ্যাক্সেস টোকেন নেই!"
            return
        }

        _backupStatus.value = "গুগল ড্রাইভে ব্যাকআপ আপলোড হচ্ছে..."

        viewModelScope.launch {
            try {
                val resultMessage = withContext(Dispatchers.IO) {
                    // Fetch the current state from repo
                    val currentWallets = repository.allWallets.first()
                    val currentTransactions = repository.allTransactions.first()
                    val currentBudgets = repository.allBudgets.first()

                    // Serialize
                    val payloadString = GoogleDriveBackupHelper.serializeBackup(
                        currentWallets,
                        currentTransactions,
                        currentBudgets
                    )

                    // Seek file in GDrive
                    var fileId = GoogleDriveBackupHelper.findBackupFileId(token)
                    if (fileId == null) {
                        fileId = GoogleDriveBackupHelper.createBackupFilePlaceholder(token)
                    }

                    if (fileId != null) {
                        val success = GoogleDriveBackupHelper.uploadBackupBytes(token, fileId, payloadString)
                        if (success) {
                            "গুগল ড্রাইভ ব্যাকআপ সফলভাবে সম্পন্ন হয়েছে!"
                        } else {
                            "ব্যাকআপ ব্যর্থ: ফাইল কনটেন্ট আপলোড করা যায়নি।"
                        }
                    } else {
                        "ব্যাকআপ ব্যর্থ: গুগল ড্রাইভে ফাইল তৈরি করা যায়নি।"
                    }
                }
                _backupStatus.value = resultMessage
            } catch (e: Exception) {
                _backupStatus.value = "ব্যাকআপে ত্রুটি ঘটেছে: ${e.localizedMessage}"
                e.printStackTrace()
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun triggerRestore() {
        val token = currentAccessToken
        if (token == null) {
            _backupStatus.value = "রিস্টোর ব্যর্থ: গুগল অ্যাকাউন্ট সংযুক্ত নেই!"
            return
        }

        _backupStatus.value = "গুগল ড্রাইভ থেকে ব্যাকআপ খোঁজা হচ্ছে..."

        viewModelScope.launch {
            try {
                val resultMessage = withContext(Dispatchers.IO) {
                    val fileId = GoogleDriveBackupHelper.findBackupFileId(token)
                    if (fileId == null) {
                        return@withContext "রিস্টোর ব্যর্থ: ড্রাইভে কোনো ব্যাকআপ ফাইল পাওয়া যায়নি!"
                    }

                    // Download backup file
                    val jsonContent = GoogleDriveBackupHelper.downloadBackupContent(token, fileId)
                    if (jsonContent == null) {
                        return@withContext "রিস্টোর ব্যর্থ: ব্যাকআপ ডাউনলোড করা যায়নি।"
                    }

                    val backupMap = GoogleDriveBackupHelper.deserializeBackup(jsonContent)
                    if (backupMap == null) {
                        return@withContext "রিস্টোর ব্যর্থ: ব্যাকআপ ফাইলের ফরম্যাট সঠিক নয়।"
                    }

                    val wallets = backupMap["wallets"] as? List<WalletEntity> ?: emptyList()
                    val transactions = backupMap["transactions"] as? List<TransactionEntity> ?: emptyList()
                    val budgets = backupMap["budgets"] as? List<BudgetEntity> ?: emptyList()

                    repository.restoreBackup(wallets, transactions, budgets)
                    "অভিনন্দন! সফলভাবে সব ডাটা রিস্টোর করা হয়েছে।"
                }
                _backupStatus.value = resultMessage
            } catch (e: Exception) {
                _backupStatus.value = "রিস্টোরে ত্রুটি ঘটেছে: ${e.localizedMessage}"
                e.printStackTrace()
            }
        }
    }
}

class FinanceViewModelFactory(
    private val application: Application,
    private val repository: FinanceRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FinanceViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FinanceViewModel(application, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
