package com.example.bookmanagementsystem.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavDirections
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bookmanagementsystem.view.BookAddView
import com.example.bookmanagementsystem.view.BookListView
import kotlinx.serialization.Serializable

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = BookList
    ) {
        composable<BookList> { BookListView(
            onBookItemClicked = {navController.navigate(BookAdd)},
            onAddButtonClicked = {navController.navigate(BookAdd)},
        ) }
        composable<BookAdd> { BookAddView(
            onBookSubmit = {navController.navigateUp()}
        ) }
    }
}

@Serializable
object BookList
@Serializable
object BookAdd