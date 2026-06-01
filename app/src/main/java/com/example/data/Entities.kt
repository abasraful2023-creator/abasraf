package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wallets")
data class WalletEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,         // e.g. "নগদ", "বিকাশ", "রকেট", "ব্যাংক একাউন্ট"
    val balance: Double,      // Current balance inside this wallet
    val type: String          // "CASH", "BKASH", "NAGAD", "BANK", "OTHER"
)

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,        // Expense/Income description
    val amount: Double,       // Amount transacted
    val isIncome: Boolean,    // true if Income, false if Expense
    val category: String,     // e.g. "খাবার", "যাতায়াত", "বেতন"
    val timestamp: Long,      // Date of transaction
    val walletId: Long,       // Associated wallet ID
    val notes: String = ""    // Optional notes
)

@Entity(tableName = "budgets")
data class BudgetEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val category: String,     // E.g. "খাবার"
    val amount: Double        // Budget limit set for this category
)
