package com.example.bookmanagementsystem.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.bookmanagementsystem.view.BookItemView
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
            onBookItemClicked = {navController.navigate(BookItem(it))},
            onAddButtonClicked = {navController.navigate(BookItem())},
//            onFilterButtonClicked = {navController.navigate(route = Filter)}
        ) }
        composable<BookItem> { backStackEntry ->
            val args = backStackEntry.toRoute<BookItem>()
            BookItemView(
                id = args.bookID,
                onBookSubmit = {navController.popBackStack()}
            )
        }
    }
}

// Views
@Serializable
object BookList
@Serializable
data class BookItem(
    val bookID: Int? = null
)
