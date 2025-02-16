package com.example.bookmanagementsystem

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.bookmanagementsystem.navigation.AppNavigation
import com.example.bookmanagementsystem.ui.theme.BookManagementSystemTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BookManagementSystemTheme {
                AppNavigation()
            }
        }
    }
}