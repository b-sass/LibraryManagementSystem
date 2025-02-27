package com.example.bookmanagementsystem.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.bookmanagementsystem.view.BookItemView
import com.example.bookmanagementsystem.view.BookListView
import com.example.bookmanagementsystem.view.BookEditView
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
            onAddButtonClicked = {navController.navigate(BookEdit())},
        ) }
        composable<BookEdit> { backStackEntry ->
            val args = backStackEntry.toRoute<BookEdit>()
            BookEditView(
                id = args.bookID,
                onBookSubmit = {navController.popBackStack()},
            )
        }
        composable<BookItem> { backStackEntry ->
            val args = backStackEntry.toRoute<BookItem>()
            BookItemView(
                id = args.bookID,
                onUpdateButtonClicked = {navController.navigate(BookEdit(args.bookID))},
                onBackButtonClicked = {navController.popBackStack()},
                onDeleteButtonClicked = {navController.popBackStack()}
            )
        }
    }
}

// Views
@Serializable
object BookList
@Serializable
data class BookEdit(
    val bookID: Int? = null
)
@Serializable
data class BookItem(
    val bookID: Int
)
