package com.example.bookmanagementsystem.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bookmanagementsystem.view.BookAddView
import com.example.bookmanagementsystem.view.BookListView
import kotlinx.serialization.Serializable

@Composable
fun AppNavigation(ctx: Context) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = BookList
    ) {
        composable<BookList> { BookListView(ctx, onBookItemClicked = {navController.navigate(BookAdd)}) }
        composable<BookAdd> { BookAddView() }
    }
}

@Serializable
object BookList
@Serializable
object BookAdd