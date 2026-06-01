package com.example.ui

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.BudgetEntity
import com.example.data.TransactionEntity
import com.example.data.WalletEntity
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*
import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.auth.GoogleAuthUtil
import com.google.android.gms.auth.UserRecoverableAuthException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope

// Pastel colors for the category pie chart
val PieChartColors = listOf(
    Color(0xFF00796B), Color(0xFF26A69A), Color(0xFF2E7D32),
    Color(0xFFF57C00), Color(0xFFD32F2F), Color(0xFF1565C0),
    Color(0xFF6A1B9A), Color(0xFFAD1457), Color(0xFF8D6E63),
    Color(0xFF37474F), Color(0xFFFBC02D), Color(0xFF00838F)
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FinanceApp(viewModel: FinanceViewModel) {
    val isLocked by viewModel.isAppLocked.collectAsStateWithLifecycle()
    val lang by viewModel.appLanguage.collectAsStateWithLifecycle()
    val context = LocalContext.current

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (isLocked) {
            LockScreen(
                onUnlock = { pin ->
                    val success = viewModel.unlockApp(pin)
                    if (!success) {
                        Toast.makeText(context, Translation.get("wrong_pin", lang), Toast.LENGTH_SHORT).show()
                    }
                    success
                },
                isPinSet = viewModel.isPinEnabled(),
                lang = lang
            )
        } else {
            MainHub(viewModel = viewModel)
        }
    }
}

/**
 * 1. PIN Lock Screen view when application security is active.
 */
@Composable
fun LockScreen(
    onUnlock: (String) -> Boolean,
    isPinSet: Boolean,
    lang: String
) {
    var enteredPin by remember { mutableStateOf("") }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .statusBarsPadding()
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Upper section
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 40.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Lock",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(80.dp)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape)
                    .padding(16.dp)
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = if (isPinSet) Translation.get("app_secured", lang) else Translation.get("set_new_pin", lang),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = if (isPinSet) Translation.get("enter_4_digit_pin", lang) else Translation.get("secure_app_4_digit", lang),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Password dot representations
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (i in 0..3) {
                    val isActive = i < enteredPin.length
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(
                                if (isActive) MaterialTheme.colorScheme.primary 
                                else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
                            )
                    )
                }
            }
        }

        // Pin logical Board (Grid)
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(bottom = 24.dp)
        ) {
            val rows = listOf(
                listOf("1", "2", "3"),
                listOf("4", "5", "6"),
                listOf("7", "8", "9"),
                listOf("C", "0", "⌫")
            )
            
            rows.forEach { row ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    modifier = Modifier.fillMaxWidth(0.85f)
                ) {
                    row.forEach { char ->
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1.25f)
                                .clip(RoundedCornerShape(16.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                .clickable {
                                    when (char) {
                                        "C" -> enteredPin = ""
                                        "⌫" -> {
                                            if (enteredPin.isNotEmpty()) {
                                                enteredPin = enteredPin.substring(0, enteredPin.length - 1)
                                            }
                                        }
                                        else -> {
                                            if (enteredPin.length < 4) {
                                                enteredPin += char
                                                if (enteredPin.length == 4) {
                                                    val correct = onUnlock(enteredPin)
                                                    if (!correct) {
                                                        enteredPin = ""
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                .padding(12.dp)
                                .testTag("pin_btn_$char")
                        ) {
                            Text(
                                text = char,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * 2. Unlocked Main Hub holding the Tab Scaffold and Floating Action Sheets.
 */
@Composable
fun MainHub(viewModel: FinanceViewModel) {
    var selectedTab by remember { mutableStateOf(0) }
    var showAddDialog by remember { mutableStateOf(false) }
    val wallets by viewModel.wallets.collectAsStateWithLifecycle()
    val lang by viewModel.appLanguage.collectAsStateWithLifecycle()

    val context = LocalContext.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Dashboard") },
                    label = { Text(Translation.get("tab_dashboard", lang)) }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Default.List, contentDescription = "History") },
                    label = { Text(Translation.get("tab_transactions", lang)) }
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(Icons.Default.CompareArrows, contentDescription = "Wallets") },
                    label = { Text(Translation.get("tab_wallets", lang)) }
                )
                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                    label = { Text(Translation.get("tab_settings", lang)) }
                )
            }
        },
        floatingActionButton = {
            if (wallets.isNotEmpty()) {
                FloatingActionButton(
                    onClick = { showAddDialog = true },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.testTag("floating_add_tx_btn")
                ) {
                    Icon(Icons.Default.Add, contentDescription = Translation.get("add_transaction", lang))
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (selectedTab) {
                0 -> DashboardTab(viewModel = viewModel)
                1 -> TransactionsTab(viewModel = viewModel)
                2 -> WalletsTab(viewModel = viewModel)
                3 -> SettingsTab(viewModel = viewModel)
            }

            if (showAddDialog) {
                AddTransactionDialog(
                    viewModel = viewModel,
                    onDismiss = { showAddDialog = false }
                )
            }
        }
    }
}

/**
 * 3. Dashboard Tab showing dynamic cards, custom Canvas graph & doughnut pie charts.
 */
@Composable
fun DashboardTab(viewModel: FinanceViewModel) {
    val txs by viewModel.transactions.collectAsStateWithLifecycle()
    val wallets by viewModel.wallets.collectAsStateWithLifecycle()
    val budgets by viewModel.budgets.collectAsStateWithLifecycle()
    val lang by viewModel.appLanguage.collectAsStateWithLifecycle()

    var editingTx by remember { mutableStateOf<TransactionEntity?>(null) }
    var showAdjustBalance by remember { mutableStateOf(false) }

    // Calculate basic totals
    val totalBalance = wallets.sumOf { it.balance }
    
    val totalIncome = txs.filter { it.isIncome }.sumOf { it.amount }
    val totalExpense = txs.filter { !it.isIncome }.sumOf { it.amount }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // App header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = Translation.get("app_title", lang),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = Translation.get("app_subtitle", lang),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                }
                
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Logo Star",
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        // Summary balance widget (Hero style card)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = Translation.get("total_balance", lang),
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                            )
                            Text(
                                text = "৳ ${String.format("%,.2f", totalBalance)}",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                        IconButton(
                            onClick = { showAdjustBalance = true },
                            modifier = Modifier.testTag("edit_main_balance_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Main Balance",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Income small panel
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.TrendingUp,
                                contentDescription = "Income",
                                tint = Color.Green,
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(Color.White.copy(alpha = 0.15f), CircleShape)
                                    .padding(6.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = Translation.get("total_income", lang),
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                                )
                                Text(
                                    text = "৳ ${String.format("%,.0f", totalIncome)}",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }

                        // Expense small panel
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.TrendingDown,
                                contentDescription = "Expense",
                                tint = Color(0xFFFF8A80),
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(Color.White.copy(alpha = 0.15f), CircleShape)
                                    .padding(6.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = Translation.get("total_expense", lang),
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                                )
                                Text(
                                    text = "৳ ${String.format("%,.0f", totalExpense)}",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }

        // Budget Warning Checklist Alert
        item {
            val activeExpenses = txs.filter { !it.isIncome }
            val exceededBudgets = budgets.filter { b ->
                val spent = activeExpenses.filter { it.category == b.category }.sumOf { it.amount }
                spent > b.amount
            }

            if (exceededBudgets.isNotEmpty()) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Warning",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = Translation.get("budget_exceeded_title", lang),
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                fontSize = 14.sp
                            )
                            val listStr = exceededBudgets.joinToString { Translation.get(it.category, lang) }
                            Text(
                                text = "$listStr ${Translation.get("budget_exceeded_msg", lang)}",
                                color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.8f),
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }

        // Section: Visual Analytics (Pie/Doughnut Chart of expenses)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = Translation.get("visual_analytics", lang),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    val expenseTxs = txs.filter { !it.isIncome }
                    if (expenseTxs.isEmpty()) {
                        // Empty State placeholder with helpful message
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = "Empty",
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                                modifier = Modifier.size(56.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = Translation.get("no_expenses_logged", lang),
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            )
                            Text(
                                text = Translation.get("add_expense_hint", lang),
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.35f)
                            )
                        }
                    } else {
                        // Gather category aggregations
                        val categorySums = expenseTxs.groupBy { it.category }
                            .mapValues { it.value.sumOf { tx -> tx.amount } }
                        
                        val totalExp = categorySums.values.sum()
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Doughnut chart Canvas
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(130.dp)
                                    .weight(1.2f)
                            ) {
                                Canvas(modifier = Modifier.size(110.dp)) {
                                    var startAngle = 0f
                                    categorySums.entries.forEachIndexed { index, entry ->
                                        val sweep = (entry.value / totalExp) * 360f
                                        val color = PieChartColors[index % PieChartColors.size]
                                        drawArc(
                                            color = color,
                                            startAngle = startAngle,
                                            sweepAngle = sweep.toFloat(),
                                            useCenter = false,
                                            style = Stroke(width = 24.dp.toPx())
                                        )
                                        startAngle += sweep.toFloat()
                                    }
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = Translation.get("total_expense", lang),
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                    )
                                    Text(
                                        text = "৳${String.format("%.0f", totalExp)}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                }
                            }

                            // Legends panel
                            Column(
                                modifier = Modifier
                                    .weight(1.8f)
                                    .padding(start = 12.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                categorySums.entries.take(5).forEachIndexed { index, entry ->
                                    val percent = (entry.value / totalExp) * 100
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(10.dp)
                                                    .clip(CircleShape)
                                                    .background(PieChartColors[index % PieChartColors.size])
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = Translation.get(entry.key, lang),
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Medium,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                        Text(
                                            text = "${String.format("%.0f", percent)}%",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                        )
                                    }
                                }
                                if (categorySums.size > 5) {
                                    val moreText = when(lang) {
                                        "bn" -> "+ আরো ${categorySums.size - 5} টি খাত"
                                        "tl" -> "+ higit pang ${categorySums.size - 5} kategorya"
                                        "ta" -> "+ மேலும் ${categorySums.size - 5} பிரிவுகள்"
                                        else -> "+ ${categorySums.size - 5} more categories"
                                    }
                                    Text(
                                        text = moreText,
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.padding(start = 16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Section: Recent transactions
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = Translation.get("recent_transactions", lang),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }

        if (txs.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = Translation.get("no_transactions", lang),
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        } else {
            items(txs.take(4)) { tx ->
                val associatedWallet = wallets.find { it.id == tx.walletId }
                TransactionItem(
                    tx = tx,
                    walletName = associatedWallet?.name ?: Translation.get("unknown_wallet", lang),
                    onDelete = { viewModel.deleteTransaction(tx) },
                    onEdit = { editingTx = tx },
                    lang = lang
                )
            }
        }
    }

    if (editingTx != null) {
        EditTransactionDialog(
            viewModel = viewModel,
            tx = editingTx!!,
            onDismiss = { editingTx = null }
        )
    }

    if (showAdjustBalance) {
        AdjustMainBalanceDialog(
            viewModel = viewModel,
            onDismiss = { showAdjustBalance = false }
        )
    }
}

/**
 * Dialog to adjust existing wallets and see live total balance calculations and changes.
 */
@Composable
fun AdjustMainBalanceDialog(
    viewModel: FinanceViewModel,
    onDismiss: () -> Unit
) {
    val lang by viewModel.appLanguage.collectAsStateWithLifecycle()
    val wallets by viewModel.wallets.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // Store state of new balances per wallet. Key: walletId, Value: String representation of amount
    var initialBalancesMap by remember(wallets) {
        mutableStateOf(wallets.associate { it.id to it.balance.toString() })
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(24.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    Text(
                        text = Translation.get("edit_balance_title", lang),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                if (wallets.isEmpty()) {
                    item {
                        Text(
                            text = Translation.get("err_no_wallets_edit", lang),
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(vertical = 12.dp)
                        )
                    }
                    item {
                        OutlinedButton(
                            onClick = onDismiss,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(Translation.get("cancel", lang))
                        }
                    }
                } else {
                    item {
                        Text(
                            text = Translation.get("adjust_balances_desc", lang),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }

                    // Display all wallets to adjust
                    items(wallets) { wallet ->
                        val currentBalanceText = initialBalancesMap[wallet.id] ?: "0"
                        OutlinedTextField(
                            value = currentBalanceText,
                            onValueChange = { newValue ->
                                initialBalancesMap = initialBalancesMap.toMutableMap().apply {
                                    put(wallet.id, newValue)
                                }
                            },
                            label = { Text("${wallet.name} (${wallet.type})") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("adjust_wallet_balance_${wallet.id}"),
                            shape = RoundedCornerShape(10.dp)
                        )
                    }

                    // Real-time Total Balance Preview
                    item {
                        val currentSum = wallets.sumOf { wallet ->
                            val textValue = initialBalancesMap[wallet.id] ?: ""
                            textValue.toDoubleOrNull() ?: 0.0
                        }
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = Translation.get("total_balance", lang),
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "৳ ${String.format("%,.2f", currentSum)}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedButton(
                                onClick = onDismiss,
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(Translation.get("cancel", lang))
                            }

                            Button(
                                onClick = {
                                    var anyInvalid = false
                                    wallets.forEach { wallet ->
                                        val textValue = initialBalancesMap[wallet.id] ?: ""
                                        val dValue = textValue.toDoubleOrNull()
                                        if (dValue == null || dValue < 0) {
                                            anyInvalid = true
                                        }
                                    }

                                    if (anyInvalid) {
                                        Toast.makeText(
                                            context,
                                            Translation.get("err_negative_balance", lang),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        wallets.forEach { wallet ->
                                            val textValue = initialBalancesMap[wallet.id] ?: ""
                                            val dValue = textValue.toDoubleOrNull() ?: 0.0
                                            if (dValue != wallet.balance) {
                                                viewModel.updateWallet(wallet.copy(balance = dValue))
                                            }
                                        }
                                        Toast.makeText(
                                            context,
                                            Translation.get("success_edit_wallet", lang),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        onDismiss()
                                    }
                                },
                                modifier = Modifier
                                    .weight(1.5f)
                                    .testTag("submit_adjust_balance_btn"),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(Translation.get("save_adjustments_btn", lang))
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * 4. Transactions List Tab supporting full search queries, filters, and category badges.
 */
@Composable
fun TransactionsTab(viewModel: FinanceViewModel) {
    val txs by viewModel.filteredTransactions.collectAsStateWithLifecycle()
    val allTxs by viewModel.transactions.collectAsStateWithLifecycle()
    val query by viewModel.searchQuery.collectAsStateWithLifecycle()
    val wallets by viewModel.wallets.collectAsStateWithLifecycle()
    val selectedCat by viewModel.selectedFilterCategory.collectAsStateWithLifecycle()
    val selectedWalletId by viewModel.selectedFilterWallet.collectAsStateWithLifecycle()
    val lang by viewModel.appLanguage.collectAsStateWithLifecycle()

    var editingTx by remember { mutableStateOf<TransactionEntity?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = Translation.get("transactions_record", lang),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = Translation.get("transactions_record_desc", lang),
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Search Bar
        OutlinedTextField(
            value = query,
            onValueChange = { viewModel.updateSearchQuery(it) },
            placeholder = { Text(Translation.get("search_placeholder", lang)) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = { viewModel.updateSearchQuery("") }) {
                        Icon(Icons.Default.Delete, contentDescription = "Clear", tint = MaterialTheme.colorScheme.error)
                    }
                }
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("search_tx_input"),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Horizontal Category filter list
        val categories = listOf("সব খাত") + allTxs.map { it.category }.distinct()
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(categories) { cat ->
                val isSelected = (cat == "সব খাত" && selectedCat == null) || (cat == selectedCat)
                val labelText = if (cat == "সব খাত") Translation.get("all_categories", lang) else Translation.get(cat, lang)
                FilterChip(
                    selected = isSelected,
                    onClick = {
                        if (cat == "সব খাত") {
                            viewModel.selectFilterCategory(null)
                        } else {
                            viewModel.selectFilterCategory(cat)
                        }
                    },
                    label = { Text(labelText) }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Wallet selections row
        val walletOptions = listOf(WalletEntity(id = -1, name = "সব ওয়ালেট", balance = 0.0, type = "")) + wallets
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(walletOptions) { wallet ->
                val isSelected = (wallet.id == -1L && selectedWalletId == null) || (wallet.id == selectedWalletId)
                val labelText = if (wallet.name == "সব ওয়ালেট") Translation.get("all_wallets", lang) else wallet.name
                FilterChip(
                    selected = isSelected,
                    onClick = {
                        if (wallet.id == -1L) {
                            viewModel.selectFilterWallet(null)
                        } else {
                            viewModel.selectFilterWallet(wallet.id)
                        }
                    },
                    label = { Text(labelText) }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Full listings
        if (txs.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "No results",
                        tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f),
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = Translation.get("no_search_results", lang),
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(txs) { tx ->
                    val walletName = wallets.find { it.id == tx.walletId }?.name ?: Translation.get("unknown_wallet", lang)
                    TransactionItem(
                        tx = tx,
                        walletName = walletName,
                        onDelete = { viewModel.deleteTransaction(tx) },
                        onEdit = { editingTx = tx },
                        lang = lang
                    )
                }
            }
        }
    }

    if (editingTx != null) {
        EditTransactionDialog(
            viewModel = viewModel,
            tx = editingTx!!,
            onDismiss = { editingTx = null }
        )
    }
}

/**
 * Single Transaction UI list row representation
 */
@Composable
fun TransactionItem(
    tx: TransactionEntity,
    walletName: String,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    lang: String = "bn"
) {
    val sdf = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
    val formattedDate = sdf.format(Date(tx.timestamp))

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Colored icon based on Income vs Expense
                Icon(
                    imageVector = if (tx.isIncome) Icons.Default.TrendingUp else Icons.Default.TrendingDown,
                    contentDescription = null,
                    tint = if (tx.isIncome) IncomeGreen else ExpenseRed,
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            if (tx.isIncome) IncomeGreenLight else ExpenseRedLight,
                            CircleShape
                        )
                        .padding(8.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = tx.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = Translation.get(tx.category, lang),
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium
                        )
                        Box(
                            modifier = Modifier
                                .size(4.dp)
                                .background(Color.Gray, CircleShape)
                        )
                        Text(
                            text = walletName,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    }
                    if (tx.notes.isNotEmpty()) {
                        Text(
                            text = "${Translation.get("note_label", lang)}: ${tx.notes}",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }
            }

            // Price and Edit/Delete icons
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "${if (tx.isIncome) "+" else "-"} ৳${String.format("%.0f", tx.amount)}",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp,
                        color = if (tx.isIncome) IncomeGreen else ExpenseRed
                    )
                    Text(
                        text = formattedDate,
                        fontSize = 10.sp,
                        color = Color.Gray
                    )
                }

                IconButton(
                    onClick = onEdit,
                    modifier = Modifier.size(24.dp).testTag("edit_tx_${tx.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "সম্পাদনা",
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                        modifier = Modifier.size(18.dp)
                    )
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(24.dp).testTag("delete_tx_${tx.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "ডিলিট",
                        tint = Color.Red.copy(alpha = 0.6f),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

/**
 * 5. Wallets and Accounts tab with money transfer support.
 */
@Composable
fun WalletsTab(viewModel: FinanceViewModel) {
    val wallets by viewModel.wallets.collectAsStateWithLifecycle()
    val lang by viewModel.appLanguage.collectAsStateWithLifecycle()
    var showWalletCreate by remember { mutableStateOf(false) }
    var editingWallet by remember { mutableStateOf<WalletEntity?>(null) }

    // Money Transfer inputs
    var sourceWalletId by remember { mutableStateOf<Long?>(null) }
    var targetWalletId by remember { mutableStateOf<Long?>(null) }
    var transferAmount by remember { mutableStateOf("") }
    var transferNotes by remember { mutableStateOf("") }

    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = Translation.get("wallets_title", lang),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = Translation.get("wallets_desc", lang),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                }
            }
        }

        // Horizontal Row of Wallets
        item {
            if (wallets.isEmpty()) {
                Text(Translation.get("no_wallets", lang))
            } else {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(wallets) { wallet ->
                        WalletCard(
                            wallet = wallet,
                            onDelete = { viewModel.deleteWallet(wallet.id) },
                            onEdit = { editingWallet = wallet }
                        )
                    }
                }
            }
        }

        // Action Buttons
        item {
            Button(
                onClick = { showWalletCreate = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("add_wallet_btn"),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
                Spacer(modifier = Modifier.width(8.dp))
                Text(Translation.get("add_wallet", lang))
            }
        }

        // Transfer Panel / Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = Translation.get("fund_transfer_title", lang),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    // Source selection
                    var sourceExpanded by remember { mutableStateOf(false) }
                    val currentSource = wallets.find { it.id == sourceWalletId }
                    
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedButton(
                            onClick = { sourceExpanded = true },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(currentSource?.name ?: Translation.get("transfer_from_placeholder", lang))
                        }
                        DropdownMenu(
                            expanded = sourceExpanded,
                            onDismissRequest = { sourceExpanded = false }
                        ) {
                            wallets.forEach { w ->
                                DropdownMenuItem(
                                    text = { Text("${w.name} (৳${w.balance})") },
                                    onClick = {
                                        sourceWalletId = w.id
                                        sourceExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    // Destination Selection
                    var targetExpanded by remember { mutableStateOf(false) }
                    val currentTarget = wallets.find { it.id == targetWalletId }
                    
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedButton(
                            onClick = { targetExpanded = true },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(currentTarget?.name ?: Translation.get("transfer_to_placeholder", lang))
                        }
                        DropdownMenu(
                            expanded = targetExpanded,
                            onDismissRequest = { targetExpanded = false }
                        ) {
                            wallets.forEach { w ->
                                DropdownMenuItem(
                                    text = { Text("${w.name} (৳${w.balance})") },
                                    onClick = {
                                        targetWalletId = w.id
                                        targetExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    // Amount Textfield
                    OutlinedTextField(
                        value = transferAmount,
                        onValueChange = { transferAmount = it },
                        label = { Text(Translation.get("transfer_amount_label", lang)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("transfer_amount_input"),
                        shape = RoundedCornerShape(10.dp)
                    )

                    // Optional Notes
                    OutlinedTextField(
                        value = transferNotes,
                        onValueChange = { transferNotes = it },
                        label = { Text(Translation.get("transfer_notes_label", lang)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Button(
                        onClick = {
                            val amount = transferAmount.toDoubleOrNull() ?: 0.0
                            val src = sourceWalletId
                            val dst = targetWalletId
                            
                            if (src == null || dst == null) {
                                Toast.makeText(context, Translation.get("err_select_wallets", lang), Toast.LENGTH_SHORT).show()
                            } else if (src == dst) {
                                Toast.makeText(context, Translation.get("err_same_wallet", lang), Toast.LENGTH_SHORT).show()
                            } else if (amount <= 0.0) {
                                Toast.makeText(context, Translation.get("err_invalid_amount", lang), Toast.LENGTH_SHORT).show()
                            } else {
                                viewModel.transferFunds(src, dst, amount, transferNotes)
                                transferAmount = ""
                                transferNotes = ""
                                sourceWalletId = null
                                targetWalletId = null
                                Toast.makeText(context, Translation.get("success_transfer", lang), Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("exec_transfer_btn"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(Translation.get("transfer_confirm_btn", lang))
                    }
                }
            }
        }
    }

    if (showWalletCreate) {
        AddWalletDialog(
            viewModel = viewModel,
            onDismiss = { showWalletCreate = false }
        )
    }

    if (editingWallet != null) {
        EditWalletDialog(
            viewModel = viewModel,
            wallet = editingWallet!!,
            onDismiss = { editingWallet = null }
        )
    }
}

/**
 * Single Wallet Card UI display representation
 */
@Composable
fun WalletCard(
    wallet: WalletEntity,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(180.dp)
            .height(115.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = when (wallet.type) {
                        "CASH" -> Icons.Default.ShoppingCart
                        "BANK" -> Icons.Default.Home
                        "BKASH", "NAGAD" -> Icons.Default.ArrowBack
                        else -> Icons.Default.Star
                    },
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    IconButton(
                        onClick = onEdit,
                        modifier = Modifier.size(20.dp).testTag("edit_wallet_${wallet.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Wallet",
                            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(20.dp).testTag("delete_wallet_${wallet.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Wallet",
                            tint = Color.Red.copy(alpha = 0.5f),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
            
            Column {
                Text(
                    text = wallet.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "৳ ${String.format("%,.0f", wallet.balance)}",
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

/**
 * 6. Budgets and Security Settings tab.
 */
@Composable
fun SettingsTab(viewModel: FinanceViewModel) {
    val budgets by viewModel.budgets.collectAsStateWithLifecycle()
    val txs by viewModel.transactions.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val googleEmail by viewModel.googleAccountEmail.collectAsStateWithLifecycle()
    val backupStatus by viewModel.backupStatus.collectAsStateWithLifecycle()
    val lang by viewModel.appLanguage.collectAsStateWithLifecycle()

    val gso = remember {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestScopes(Scope("https://www.googleapis.com/auth/drive.file"))
            .build()
    }
    val googleSignInClient = remember { GoogleSignIn.getClient(context, gso) }

    val recoveryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.triggerBackup()
        }
    }

    val signInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            if (account != null) {
                val email = account.email ?: "Unknown"
                viewModel.viewModelScope.launch(Dispatchers.IO) {
                    try {
                        val token = GoogleAuthUtil.getToken(
                            context,
                            account.account ?: android.accounts.Account(email, "com.google"),
                            "oauth2:https://www.googleapis.com/auth/drive.file"
                        )
                        viewModel.setGoogleAccessToken(email, token)
                    } catch (recoverable: UserRecoverableAuthException) {
                        kotlinx.coroutines.withContext(Dispatchers.Main) {
                            recoverable.intent?.let { recoveryLauncher.launch(it) }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            } else {
                Toast.makeText(context, "গুগল সাইন-ইন অসফল হয়েছে", Toast.LENGTH_SHORT).show()
            }
        } catch (e: ApiException) {
            Toast.makeText(context, "ত্রুটি: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    // Set budget states
    var budgetCategory by remember { mutableStateOf("খাবার") }
    var budgetAmount by remember { mutableStateOf("") }
    var editingBudget by remember { mutableStateOf<BudgetEntity?>(null) }
    
    // Safety PIN Lock configurations
    var securityPinText by remember { mutableStateOf("") }
    val isPinSetup = viewModel.isPinEnabled()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = Translation.get("budget_settings_title", lang),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = Translation.get("budget_settings_desc", lang),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
        }

        // 🌐 Language Selection Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.15f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = Translation.get("app_language_title", lang),
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.primary
                    )

                    var langExpanded by remember { mutableStateOf(false) }
                    val currentLangName = Translation.languages.find { it.code == lang }?.name ?: "English"

                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedButton(
                            onClick = { langExpanded = true },
                            modifier = Modifier.fillMaxWidth().testTag("language_selector_dropdown"),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(currentLangName, fontWeight = FontWeight.Medium)
                                Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
                            }
                        }

                        DropdownMenu(
                            expanded = langExpanded,
                            onDismissRequest = { langExpanded = false },
                            modifier = Modifier.fillMaxWidth(0.85f)
                        ) {
                            Translation.languages.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option.name, fontWeight = FontWeight.Bold) },
                                    onClick = {
                                        viewModel.setLanguage(option.code)
                                        langExpanded = false
                                        val successMsg = if (option.code == "bn") "ভাষা পরিবর্তন সম্পন্ন হয়েছে!" else if (option.code == "tl") "Matagumpay na binago ang wika!" else if (option.code == "ta") "மொழி வெற்றிகரமாக மாற்றப்பட்டது!" else "Language changed successfully!"
                                        Toast.makeText(context, successMsg, Toast.LENGTH_SHORT).show()
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        // Set Budget limit form
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = Translation.get("set_budget", lang),
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.primary
                    )

                    // Category Selector
                    val cats = listOf("খাবার", "যাতায়াত", "বাড়ি ভাড়া", "ইউটিলিটি বিল", "চিকিৎসা", "শিক্ষা", "বিনোদন", "শপিং", "ঋণ পরিশোধ", "অন্যান্য")
                    var catExpanded by remember { mutableStateOf(false) }

                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedButton(
                            onClick = { catExpanded = true },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(Translation.get("budget_category", lang) + Translation.get(budgetCategory, lang))
                        }
                        DropdownMenu(
                            expanded = catExpanded,
                            onDismissRequest = { catExpanded = false }
                        ) {
                            cats.forEach { c ->
                                DropdownMenuItem(
                                    text = { Text(Translation.get(c, lang)) },
                                    onClick = {
                                        budgetCategory = c
                                        catExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    // Budget Amount Input
                    OutlinedTextField(
                        value = budgetAmount,
                        onValueChange = { budgetAmount = it },
                        label = { Text(Translation.get("max_budget_amount", lang)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("budget_amount_input"),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Button(
                        onClick = {
                            val limit = budgetAmount.toDoubleOrNull() ?: 0.0
                            if (limit <= 0.0) {
                                val errMsg = if (lang == "bn") "সঠিক পজেটিভ বাজেট সীমা দিন" else if (lang == "tl") "Maglagay ng wastong limitasyon sa badyet" else if (lang == "ta") "சரியான பட்ஜெட் வரம்பை உள்ளிடவும்" else "Please enter a valid positive budget limit"
                                Toast.makeText(context, errMsg, Toast.LENGTH_SHORT).show()
                            } else {
                                viewModel.setBudget(budgetCategory, limit)
                                budgetAmount = ""
                                val successMsg = if (lang == "bn") "${Translation.get(budgetCategory, lang)} খাতে বাজেট সীমা সেট হয়েছে!" else if (lang == "tl") "Naitakda ang badyet para sa ${Translation.get(budgetCategory, lang)}!" else if (lang == "ta") "${Translation.get(budgetCategory, lang)} பிரிவில் பட்ஜெட் அமைக்கப்பட்டது!" else "Budget set for ${Translation.get(budgetCategory, lang)}!"
                                Toast.makeText(context, successMsg, Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("save_budget_btn"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(Translation.get("save_budget", lang))
                    }
                }
            }
        }

        // Active Budgets Trackers with progress bars shifting green to warning red!
        item {
            Text(
                text = Translation.get("active_budgets", lang),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        if (budgets.isEmpty()) {
            item {
                Text(
                    text = Translation.get("no_budgets", lang),
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
        } else {
            items(budgets) { b ->
                // Calculate actual spent
                val spent = txs.filter { !it.isIncome && it.category == b.category }.sumOf { it.amount }
                val ratio = if (spent >= b.amount) 1.0f else (spent / b.amount).toFloat()
                val isLimitClose = ratio >= 0.8f

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = Translation.get(b.category, lang),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                                Text(
                                    text = "${Translation.get("budget_spent", lang)}: ৳${String.format("%.0f", spent)} / ${Translation.get("budget_limit", lang)}: ৳${String.format("%.0f", b.amount)}",
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                IconButton(onClick = { editingBudget = b }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Edit Budget", tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f), modifier = Modifier.size(18.dp))
                                }
                                IconButton(onClick = { viewModel.deleteBudget(b.category) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete Budget", tint = Color.Red.copy(alpha = 0.5f), modifier = Modifier.size(18.dp))
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        // Custom color-coded progressive indicator
                        LinearProgressIndicator(
                            progress = { ratio },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = if (ratio >= 1.0f) ExpenseRed else if (isLimitClose) WarningAmber else IncomeGreen,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                        
                        if (ratio >= 1.0f) {
                            Text(
                                text = Translation.get("budget_exceeded", lang),
                                color = ExpenseRed,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        } else if (isLimitClose) {
                            Text(
                                text = Translation.get("budget_close", lang),
                                color = WarningAmber,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }
        }

        // Security Set PIN Card block
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = Translation.get("security_lock_title", lang),
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )

                    Text(
                        text = if (isPinSetup) Translation.get("security_lock_desc_active", lang)
                               else Translation.get("security_lock_desc_inactive", lang),
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                    )

                    if (!isPinSetup) {
                        OutlinedTextField(
                            value = securityPinText,
                            onValueChange = {
                                if (it.length <= 4 && it.all { char -> char.isDigit() }) {
                                    securityPinText = it
                                }
                            },
                            label = { Text(Translation.get("new_pin_label", lang)) },
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("pin_setup_input"),
                            shape = RoundedCornerShape(10.dp)
                        )

                        Button(
                            onClick = {
                                if (securityPinText.length != 4) {
                                    val errPinMsg = if (lang == "bn") "চারটি সংখ্যা দিন (E.g. 1234)" else if (lang == "tl") "Maglagay ng 4 na numero" else if (lang == "ta") "4 இலக்கங்களை உள்ளிடவும்" else "Please enter exactly 4 digits"
                                    Toast.makeText(context, errPinMsg, Toast.LENGTH_SHORT).show()
                                } else {
                                    viewModel.setPin(securityPinText)
                                    securityPinText = ""
                                    val successPinMsg = if (lang == "bn") "সিকিউরিটি পিন চালু হয়েছে!" else if (lang == "tl") "Aktibo na ang PIN!" else if (lang == "ta") "பாதுகாப்பு பின் குறியீடு செயல்படுத்தப்பட்டது!" else "Security PIN enabled successfully!"
                                    Toast.makeText(context, successPinMsg, Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("enable_pin_btn"),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(Translation.get("enable_pin_btn", lang))
                        }
                    } else {
                        Button(
                            onClick = {
                                viewModel.disablePin()
                                val successPinMsg = if (lang == "bn") "সিকিউরিটি পিন নিষ্ক্রিয় করা হয়েছে।" else if (lang == "tl") "Naka-disable na ang PIN!" else if (lang == "ta") "பாதுகாப்பு பின் குறியீடு முடக்கப்பட்டது." else "Security PIN disabled successfully."
                                Toast.makeText(context, successPinMsg, Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("disable_pin_btn"),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(Translation.get("disable_pin_btn", lang), color = Color.White)
                        }
                    }
                }
            }
        }

        // Export card panel (Excel/CSV Export tool)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Export Excel",
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(36.dp)
                    )
                    Text(
                        text = Translation.get("export_title", lang),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = Translation.get("export_desc", lang),
                        textAlign = TextAlign.Center,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                    Button(
                        onClick = { viewModel.exportData(context) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("export_csv_btn"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(Translation.get("export_btn", lang))
                    }
                }
            }
        }

        // ☁️ Google Drive Cloud Backup & Restore Panel
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CloudQueue,
                        contentDescription = "Cloud Backup",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(38.dp)
                    )
                    Text(
                        text = Translation.get("cloud_backup_title", lang),
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = Translation.get("cloud_backup_desc", lang),
                        textAlign = TextAlign.Center,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                    )

                    if (googleEmail == null) {
                        // Sign-in option
                        Button(
                            onClick = {
                                val signInIntent = googleSignInClient.signInIntent
                                signInIntent?.let { signInLauncher.launch(it) }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("connect_google_drive_btn"),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(Translation.get("connect_google_btn", lang))
                        }
                    } else {
                        // Connected State
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.AccountCircle,
                                        contentDescription = "Connected Gmail",
                                        tint = Color.Gray,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "${Translation.get("signed_in_as", lang)} ${googleEmail ?: ""}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                                TextButton(onClick = { viewModel.disconnectGoogleAccount() }) {
                                    Text(Translation.get("disconnect_btn", lang), color = Color.Red, fontSize = 11.sp)
                                }
                            }
                        }

                        // Backup & Restore Actions
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Button(
                                onClick = { viewModel.triggerBackup() },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("drive_backup_btn"),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(Translation.get("backup_now_btn", lang), fontSize = 11.sp)
                            }
                            Button(
                                onClick = { viewModel.triggerRestore() },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("drive_restore_btn"),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(Translation.get("restore_now_btn", lang), fontSize = 11.sp)
                            }
                        }
                    }

                    // Status display text
                    backupStatus?.let { status ->
                        Text(
                            text = status,
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        }
    }

    if (editingBudget != null) {
        EditBudgetDialog(
            viewModel = viewModel,
            budget = editingBudget!!,
            onDismiss = { editingBudget = null }
        )
    }
}

/**
 * Dialogue Overlay for insertion of a new transaction.
 */
@Composable
fun AddTransactionDialog(
    viewModel: FinanceViewModel,
    onDismiss: () -> Unit
) {
    val lang by viewModel.appLanguage.collectAsStateWithLifecycle()
    var isIncome by remember { mutableStateOf(false) }
    var amount by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    val wallets by viewModel.wallets.collectAsStateWithLifecycle()
    var selectedWalletId by remember { mutableStateOf(if (wallets.isNotEmpty()) wallets.first().id else -1L) }

    val expenseCats = listOf("খাবার", "যাতায়াত", "বাড়ি ভাড়া", "ইউটিলিটি বিল", "চিকিৎসা", "শিক্ষা", "বিনোদন", "শপিং", "ঋণ পরিশোধ", "অন্যান্য")
    val incomeCats = listOf("বেতন", "ব্যবসা", "ফ্রিল্যান্সিং", "উপহার", "অন্যান্য")

    var selectedCategory by remember(isIncome) {
        mutableStateOf(if (isIncome) incomeCats.first() else expenseCats.first())
    }

    val context = LocalContext.current

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(24.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    Text(
                        text = Translation.get("title_add_tx", lang),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                // Tab Switcher between Expense and Income
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { isIncome = false }
                                .background(if (!isIncome) ExpenseRed else Color.Transparent)
                                .padding(vertical = 10.dp)
                                .testTag("tab_expense")
                        ) {
                            Text(
                                Translation.get("tag_expense_btn", lang), 
                                color = if (!isIncome) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { isIncome = true }
                                .background(if (isIncome) IncomeGreen else Color.Transparent)
                                .padding(vertical = 10.dp)
                                .testTag("tab_income")
                        ) {
                            Text(
                                Translation.get("tag_income_btn", lang), 
                                color = if (isIncome) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }

                // Amount text input
                item {
                    OutlinedTextField(
                        value = amount,
                        onValueChange = { amount = it },
                        label = { Text(Translation.get("amount_lbl", lang)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("tx_amount_input"),
                        shape = RoundedCornerShape(10.dp)
                    )
                }

                // Description/Title
                item {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text(Translation.get("title_lbl", lang)) },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("tx_title_input"),
                        shape = RoundedCornerShape(10.dp)
                    )
                }

                // Dynamic Category select list
                item {
                    val activeCategories = if (isIncome) incomeCats else expenseCats
                    var catExpanded by remember { mutableStateOf(false) }

                    Text(Translation.get("category_lbl", lang), fontSize = 12.sp, color = Color.Gray)
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedButton(
                            onClick = { catExpanded = true },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(Translation.get(selectedCategory, lang))
                        }
                        DropdownMenu(
                            expanded = catExpanded,
                            onDismissRequest = { catExpanded = false }
                        ) {
                            activeCategories.forEach { c ->
                                DropdownMenuItem(
                                    text = { Text(Translation.get(c, lang)) },
                                    onClick = {
                                        selectedCategory = c
                                        catExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                // Wallet/Account Select option
                item {
                    var walletExpanded by remember { mutableStateOf(false) }
                    val currentWallet = wallets.find { it.id == selectedWalletId } ?: wallets.firstOrNull()

                    Text(Translation.get("associated_wallet_lbl", lang), fontSize = 12.sp, color = Color.Gray)
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedButton(
                            onClick = { walletExpanded = true },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(currentWallet?.name ?: Translation.get("select_wallet_placeholder", lang))
                        }
                        DropdownMenu(
                            expanded = walletExpanded,
                            onDismissRequest = { walletExpanded = false }
                        ) {
                            wallets.forEach { w ->
                                DropdownMenuItem(
                                    text = { Text("${w.name} (৳${w.balance})") },
                                    onClick = {
                                        selectedWalletId = w.id
                                        walletExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                // Notes text field
                item {
                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text(Translation.get("write_additional_notes_placeholder", lang)) },
                        singleLine = false,
                        maxLines = 2,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )
                }

                // Final CTA action buttons
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = onDismiss,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(Translation.get("cancel_close_btn", lang))
                        }
                        
                        Button(
                            onClick = {
                                val dAmount = amount.toDoubleOrNull() ?: 0.0
                                if (dAmount <= 0) {
                                    Toast.makeText(context, Translation.get("err_log_amount", lang), Toast.LENGTH_SHORT).show()
                                } else if (selectedWalletId == -1L) {
                                    Toast.makeText(context, Translation.get("err_associated_wallet_missing", lang), Toast.LENGTH_SHORT).show()
                                } else {
                                    viewModel.addTransaction(
                                        title = title,
                                        amount = dAmount,
                                        isIncome = isIncome,
                                        category = selectedCategory,
                                        timestamp = System.currentTimeMillis(),
                                        walletId = selectedWalletId,
                                        notes = notes
                                    )
                                    Toast.makeText(context, Translation.get("success_added_record", lang), Toast.LENGTH_SHORT).show()
                                    onDismiss()
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("save_tx_confirm_btn"),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(Translation.get("save_record_btn", lang))
                        }
                    }
                }
            }
        }
    }
}

/**
 * Dialogue popup overlay to configure new custom account wallets.
 */
@Composable
fun AddWalletDialog(
    viewModel: FinanceViewModel,
    onDismiss: () -> Unit
) {
    val lang by viewModel.appLanguage.collectAsStateWithLifecycle()
    var name by remember { mutableStateOf("") }
    var balance by remember { mutableStateOf("") }
    var walletType by remember { mutableStateOf("CASH") }

    val context = LocalContext.current

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = Translation.get("title_add_wallet", lang),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.primary
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(Translation.get("wallet_name_lbl", lang)) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("wallet_name_input"),
                    shape = RoundedCornerShape(10.dp)
                )

                OutlinedTextField(
                    value = balance,
                    onValueChange = { balance = it },
                    label = { Text(Translation.get("initial_balance_lbl", lang)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("wallet_balance_input"),
                    shape = RoundedCornerShape(10.dp)
                )

                // Select Types
                var expandedType by remember { mutableStateOf(false) }
                val types = listOf("CASH", "BANK", "BKASH", "NAGAD", "OTHER")

                Text(Translation.get("wallet_category_lbl", lang), fontSize = 12.sp, color = Color.Gray)
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(
                        onClick = { expandedType = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(walletType)
                    }
                    DropdownMenu(
                        expanded = expandedType,
                        onDismissRequest = { expandedType = false }
                    ) {
                        types.forEach { t ->
                            DropdownMenuItem(
                                text = { Text(t) },
                                onClick = {
                                    walletType = t
                                    expandedType = false
                                }
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(Translation.get("cancel_close_btn", lang))
                    }
                    
                    Button(
                        onClick = {
                            val startBalance = balance.toDoubleOrNull() ?: 0.0
                            if (name.isEmpty()) {
                                Toast.makeText(context, Translation.get("err_wallet_name_empty", lang), Toast.LENGTH_SHORT).show()
                            } else if (startBalance < 0) {
                                Toast.makeText(context, Translation.get("err_negative_balance", lang), Toast.LENGTH_SHORT).show()
                            } else {
                                viewModel.addWallet(name, startBalance, walletType)
                                Toast.makeText(context, Translation.get("success_add_wallet", lang), Toast.LENGTH_SHORT).show()
                                onDismiss()
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("submit_wallet_btn"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(Translation.get("save_record_btn", lang))
                    }
                }
            }
        }
    }
}

@Composable
fun EditTransactionDialog(
    viewModel: FinanceViewModel,
    tx: TransactionEntity,
    onDismiss: () -> Unit
) {
    val lang by viewModel.appLanguage.collectAsStateWithLifecycle()
    var isIncome by remember { mutableStateOf(tx.isIncome) }
    var amount by remember { mutableStateOf(tx.amount.toString()) }
    var title by remember { mutableStateOf(tx.title) }
    var notes by remember { mutableStateOf(tx.notes) }

    val wallets by viewModel.wallets.collectAsStateWithLifecycle()
    var selectedWalletId by remember { mutableStateOf(tx.walletId) }

    val expenseCats = listOf("খাবার", "যাতায়াত", "বাড়ি ভাড়া", "ইউটিলিটি বিল", "চিকিৎসা", "শিক্ষা", "বিনোদন", "শপিং", "ঋণ পরিশোধ", "অন্যান্য")
    val incomeCats = listOf("বেতন", "ব্যবসা", "ফ্রিল্যান্সিং", "উপহার", "অন্যান্য")

    var selectedCategory by remember(isIncome) {
        mutableStateOf(if (isIncome) {
            if (incomeCats.contains(tx.category)) tx.category else incomeCats.first()
        } else {
            if (expenseCats.contains(tx.category)) tx.category else expenseCats.first()
        })
    }

    val context = LocalContext.current

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(24.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    Text(
                        text = Translation.get("title_edit_tx", lang),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { isIncome = false }
                                .background(if (!isIncome) ExpenseRed else Color.Transparent)
                                .padding(vertical = 10.dp)
                        ) {
                            Text(
                                Translation.get("tag_expense_btn", lang), 
                                color = if (!isIncome) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { isIncome = true }
                                .background(if (isIncome) IncomeGreen else Color.Transparent)
                                .padding(vertical = 10.dp)
                        ) {
                            Text(
                                Translation.get("tag_income_btn", lang), 
                                color = if (isIncome) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }

                item {
                    OutlinedTextField(
                        value = amount,
                        onValueChange = { amount = it },
                        label = { Text(Translation.get("amount_lbl", lang)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("edit_tx_amount_input"),
                        shape = RoundedCornerShape(10.dp)
                    )
                }

                item {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text(Translation.get("title_lbl", lang)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("edit_tx_title_input"),
                        shape = RoundedCornerShape(10.dp)
                    )
                }

                item {
                    val activeCategories = if (isIncome) incomeCats else expenseCats
                    var catExpanded by remember { mutableStateOf(false) }

                    Text(Translation.get("category_lbl", lang), fontSize = 12.sp, color = Color.Gray)
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedButton(
                            onClick = { catExpanded = true },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(Translation.get(selectedCategory, lang))
                        }
                        DropdownMenu(
                            expanded = catExpanded,
                            onDismissRequest = { catExpanded = false }
                        ) {
                            activeCategories.forEach { c ->
                                DropdownMenuItem(
                                    text = { Text(Translation.get(c, lang)) },
                                    onClick = {
                                        selectedCategory = c
                                        catExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                item {
                    var walletExpanded by remember { mutableStateOf(false) }
                    val currentWallet = wallets.find { it.id == selectedWalletId } ?: wallets.firstOrNull()

                    Text(Translation.get("associated_wallet_lbl", lang), fontSize = 12.sp, color = Color.Gray)
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedButton(
                            onClick = { walletExpanded = true },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(currentWallet?.name ?: Translation.get("select_wallet_placeholder", lang))
                        }
                        DropdownMenu(
                            expanded = walletExpanded,
                            onDismissRequest = { walletExpanded = false }
                        ) {
                            wallets.forEach { w ->
                                DropdownMenuItem(
                                    text = { Text("${w.name} (৳${w.balance})") },
                                    onClick = {
                                        selectedWalletId = w.id
                                        walletExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                item {
                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text(Translation.get("write_additional_notes_placeholder", lang)) },
                        singleLine = false,
                        maxLines = 2,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = onDismiss,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(Translation.get("cancel_close_btn", lang))
                        }
                        
                        Button(
                            onClick = {
                                val dAmount = amount.toDoubleOrNull() ?: 0.0
                                if (dAmount <= 0) {
                                    Toast.makeText(context, Translation.get("err_log_amount", lang), Toast.LENGTH_SHORT).show()
                                } else {
                                    viewModel.updateTransaction(
                                        newTx = tx.copy(
                                            title = title.ifEmpty { if (isIncome) Translation.get("tag_income_btn", lang) else Translation.get("tag_expense_btn", lang) },
                                            amount = dAmount,
                                            isIncome = isIncome,
                                            category = selectedCategory,
                                            walletId = selectedWalletId,
                                            notes = notes
                                        ),
                                        oldTx = tx
                                    )
                                    Toast.makeText(context, Translation.get("success_edit_record", lang), Toast.LENGTH_SHORT).show()
                                    onDismiss()
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("submit_edit_tx_btn"),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(Translation.get("save_record_btn", lang))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EditWalletDialog(
    viewModel: FinanceViewModel,
    wallet: WalletEntity,
    onDismiss: () -> Unit
) {
    val lang by viewModel.appLanguage.collectAsStateWithLifecycle()
    var name by remember { mutableStateOf(wallet.name) }
    var balance by remember { mutableStateOf(wallet.balance.toString()) }
    var walletType by remember { mutableStateOf(wallet.type) }

    val context = LocalContext.current

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = Translation.get("title_edit_wallet", lang),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.primary
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(Translation.get("wallet_name_lbl", lang)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("edit_wallet_name_input"),
                    shape = RoundedCornerShape(10.dp)
                )

                OutlinedTextField(
                    value = balance,
                    onValueChange = { balance = it },
                    label = { Text(Translation.get("initial_balance_lbl", lang)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("edit_wallet_balance_input"),
                    shape = RoundedCornerShape(10.dp)
                )

                var expandedType by remember { mutableStateOf(false) }
                val types = listOf("CASH", "BANK", "BKASH", "NAGAD", "OTHER")

                Text(Translation.get("wallet_category_lbl", lang), fontSize = 12.sp, color = Color.Gray)
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(
                        onClick = { expandedType = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(walletType)
                    }
                    DropdownMenu(
                        expanded = expandedType,
                        onDismissRequest = { expandedType = false }
                    ) {
                        types.forEach { t ->
                            DropdownMenuItem(
                                text = { Text(t) },
                                onClick = {
                                    walletType = t
                                    expandedType = false
                                }
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(Translation.get("cancel_close_btn", lang))
                    }
                    
                    Button(
                        onClick = {
                            val editBalance = balance.toDoubleOrNull() ?: 0.0
                            if (name.isEmpty()) {
                                Toast.makeText(context, Translation.get("err_wallet_name_empty", lang), Toast.LENGTH_SHORT).show()
                            } else if (editBalance < 0) {
                                Toast.makeText(context, Translation.get("err_negative_balance", lang), Toast.LENGTH_SHORT).show()
                            } else {
                                viewModel.updateWallet(wallet.copy(name = name, balance = editBalance, type = walletType))
                                Toast.makeText(context, Translation.get("success_edit_wallet", lang), Toast.LENGTH_SHORT).show()
                                onDismiss()
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("submit_edit_wallet_btn"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(Translation.get("save_record_btn", lang))
                    }
                }
            }
        }
    }
}

@Composable
fun EditBudgetDialog(
    viewModel: FinanceViewModel,
    budget: BudgetEntity,
    onDismiss: () -> Unit
) {
    val lang by viewModel.appLanguage.collectAsStateWithLifecycle()
    var amount by remember { mutableStateOf(budget.amount.toString()) }
    val context = LocalContext.current
    
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(24.dp)
         ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = Translation.get("title_edit_budget", lang),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Text(
                    text = "${Translation.get("category_lbl", lang)}: ${Translation.get(budget.category, lang)}",
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp
                )

                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text(Translation.get("new_budget_limit_lbl", lang)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("edit_budget_amount_input"),
                    shape = RoundedCornerShape(10.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(Translation.get("cancel_close_btn", lang))
                    }
                    
                    Button(
                        onClick = {
                            val limit = amount.toDoubleOrNull() ?: 0.0
                            if (limit <= 0.0) {
                                Toast.makeText(context, Translation.get("err_positive_budget", lang), Toast.LENGTH_SHORT).show()
                            } else {
                                viewModel.setBudget(budget.category, limit)
                                val successMsg = when(lang) {
                                    "bn" -> "${Translation.get(budget.category, lang)} খাতের বাজেট পরিবর্তন করা হয়েছে!"
                                    "tl" -> "Nabago ang badyet para sa ${Translation.get(budget.category, lang)}!"
                                    "ta" -> "${Translation.get(budget.category, lang)} பிரிவிற்கான வரவுசெலவு திட்டம் மாற்றியமைக்கப்பட்டது!"
                                    else -> "Budget updated for ${Translation.get(budget.category, lang)}!"
                                }
                                Toast.makeText(context, successMsg, Toast.LENGTH_SHORT).show()
                                onDismiss()
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("submit_edit_budget_btn"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(Translation.get("save_record_btn", lang))
                    }
                }
            }
        }
    }
}
