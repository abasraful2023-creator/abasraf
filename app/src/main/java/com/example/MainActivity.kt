package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.data.AppDatabase
import com.example.data.FinanceRepository
import com.example.ui.FinanceApp
import com.example.ui.FinanceViewModel
import com.example.ui.FinanceViewModelFactory
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    
    private val database by lazy { AppDatabase.getDatabase(applicationContext) }
    private val repository by lazy {
        FinanceRepository(
            walletDao = database.walletDao(),
            transactionDao = database.transactionDao(),
            budgetDao = database.budgetDao()
        )
    }
    
    private val viewModel: FinanceViewModel by viewModels {
        FinanceViewModelFactory(application, repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        enableEdgeToEdge()
        
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    FinanceApp(viewModel = viewModel)
                }
            }
        }
    }
    
    override fun onStop() {
        super.onStop()
        // Proactively locks the app on backgrounding if a security pin is configured
        viewModel.lockApp()
    }
}
