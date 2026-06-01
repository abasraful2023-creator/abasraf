package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FinanceRepository(
    private val walletDao: WalletDao,
    private val transactionDao: TransactionDao,
    private val budgetDao: BudgetDao
) {
    val allWallets: Flow<List<WalletEntity>> = walletDao.getAllWalletsFlow()
    val allTransactions: Flow<List<TransactionEntity>> = transactionDao.getAllTransactionsFlow()
    val allBudgets: Flow<List<BudgetEntity>> = budgetDao.getAllBudgetsFlow()

    suspend fun getWalletById(id: Long): WalletEntity? = withContext(Dispatchers.IO) {
        walletDao.getWalletById(id)
    }

    suspend fun seedDatabaseIfEmpty() = withContext(Dispatchers.IO) {
        val wallets = walletDao.getAllWallets()
        if (wallets.isEmpty()) {
            // Seed default wallets in Bangla with initial balance for a smooth onboarding experience
            walletDao.insertWallet(WalletEntity(name = "নগদ টাকা (Cash)", balance = 5000.0, type = "CASH"))
            walletDao.insertWallet(WalletEntity(name = "বিকাশ (bKash)", balance = 10000.0, type = "BKASH"))
            walletDao.insertWallet(WalletEntity(name = "ব্যাংক হিসাব (Bank)", balance = 25000.0, type = "BANK"))
            
            // Seed a couple of default budgets for demonstration
            budgetDao.insertBudget(BudgetEntity(category = "খাবার", amount = 4000.0))
            budgetDao.insertBudget(BudgetEntity(category = "যাতায়াত", amount = 1500.0))
        }
    }

    /**
     * Inserts a transaction and adjusts the corresponding wallet's balance.
     */
    suspend fun addTransaction(transaction: TransactionEntity) = withContext(Dispatchers.IO) {
        // 1. Insert the transaction
        transactionDao.insertTransaction(transaction)

        // 2. Adjust wallet balance
        val wallet = walletDao.getWalletById(transaction.walletId)
        if (wallet != null) {
            val newBalance = if (transaction.isIncome) {
                wallet.balance + transaction.amount
            } else {
                wallet.balance - transaction.amount
            }
            walletDao.updateWallet(wallet.copy(balance = newBalance))
        }
    }

    /**
     * Deletes a transaction and reverses its effect on the corresponding wallet's balance.
     */
    suspend fun deleteTransaction(transaction: TransactionEntity) = withContext(Dispatchers.IO) {
        // 1. Delete the transaction
        transactionDao.deleteTransaction(transaction)

        // 2. Rollback wallet balance
        val wallet = walletDao.getWalletById(transaction.walletId)
        if (wallet != null) {
            val newBalance = if (transaction.isIncome) {
                wallet.balance - transaction.amount // subtract the income back
            } else {
                wallet.balance + transaction.amount // add the expense back
            }
            walletDao.updateWallet(wallet.copy(balance = newBalance))
        }
    }

    /**
     * Executes a clean transfer from one wallet to another, updating balances and inserting twin transfer transactions.
     */
    suspend fun transferFunds(
        fromWalletId: Long,
        toWalletId: Long,
        amount: Double,
        notes: String = ""
    ): Boolean = withContext(Dispatchers.IO) {
        val fromWallet = walletDao.getWalletById(fromWalletId)
        val toWallet = walletDao.getWalletById(toWalletId)

        if (fromWallet != null && toWallet != null && fromWalletId != toWalletId && amount > 0) {
            // Update the from-wallet balance
            walletDao.updateWallet(fromWallet.copy(balance = fromWallet.balance - amount))
            
            // Update the to-wallet balance
            walletDao.updateWallet(toWallet.copy(balance = toWallet.balance + amount))

            val now = System.currentTimeMillis()

            // Construct outbound transfer record
            val outboundTx = TransactionEntity(
                title = "স্থানান্তর -> ${toWallet.name}",
                amount = amount,
                isIncome = false,
                category = "স্থানান্তর",
                timestamp = now,
                walletId = fromWalletId,
                notes = notes.ifEmpty { "টাকা স্থানান্তর করা হয়েছে" }
            )
            transactionDao.insertTransaction(outboundTx)

            // Construct inbound transfer record
            val inboundTx = TransactionEntity(
                title = "স্থানান্তর <- ${fromWallet.name}",
                amount = amount,
                isIncome = true,
                category = "স্থানান্তর",
                timestamp = now + 1, // small offset to display in correct order
                walletId = toWalletId,
                notes = notes.ifEmpty { "টাকা গ্রহণ করা হয়েছে" }
            )
            transactionDao.insertTransaction(inboundTx)

            return@withContext true
        }
        return@withContext false
    }

    // Wallet actions
    suspend fun addWallet(wallet: WalletEntity) = withContext(Dispatchers.IO) {
        walletDao.insertWallet(wallet)
    }

    suspend fun updateWallet(wallet: WalletEntity) = withContext(Dispatchers.IO) {
        walletDao.updateWallet(wallet)
    }

    suspend fun deleteWallet(walletId: Long) = withContext(Dispatchers.IO) {
        val wallet = walletDao.getWalletById(walletId)
        if (wallet != null) {
            // Option to delete transaction registry or leave orphaned. We safe delete them.
            transactionDao.deleteTransactionsByWallet(walletId)
            walletDao.deleteWallet(wallet)
        }
    }

    // Budget actions
    suspend fun setBudget(category: String, amount: Double) = withContext(Dispatchers.IO) {
        val existing = budgetDao.getBudgetByCategory(category)
        if (existing != null) {
            budgetDao.updateBudget(existing.copy(amount = amount))
        } else {
            budgetDao.insertBudget(BudgetEntity(category = category, amount = amount))
        }
    }

    suspend fun deleteBudgetByCategory(category: String) = withContext(Dispatchers.IO) {
        budgetDao.deleteBudgetByCategory(category)
    }

    suspend fun restoreBackup(
        wallets: List<WalletEntity>,
        transactions: List<TransactionEntity>,
        budgets: List<BudgetEntity>
    ) = withContext(Dispatchers.IO) {
        transactionDao.deleteAllTransactions()
        walletDao.deleteAllWallets()
        budgetDao.deleteAllBudgets()

        walletDao.insertAllWallets(wallets)
        transactionDao.insertAllTransactions(transactions)
        budgetDao.insertAllBudgets(budgets)
    }

    suspend fun updateTransaction(newTx: TransactionEntity, oldTx: TransactionEntity) = withContext(Dispatchers.IO) {
        // 1. Revert old transaction's impact on old wallet
        val oldWallet = walletDao.getWalletById(oldTx.walletId)
        if (oldWallet != null) {
            val revertedBalance = if (oldTx.isIncome) {
                oldWallet.balance - oldTx.amount
            } else {
                oldWallet.balance + oldTx.amount
            }
            walletDao.updateWallet(oldWallet.copy(balance = revertedBalance))
        }

        // 2. Insert the updated transaction
        transactionDao.insertTransaction(newTx)

        // 3. Apply new transaction's impact on new wallet
        val newWallet = walletDao.getWalletById(newTx.walletId)
        if (newWallet != null) {
            val finalBalance = if (newTx.isIncome) {
                newWallet.balance + newTx.amount
            } else {
                newWallet.balance - newTx.amount
            }
            walletDao.updateWallet(newWallet.copy(balance = finalBalance))
        }
    }
}
