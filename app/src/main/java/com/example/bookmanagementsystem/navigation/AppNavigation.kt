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
        // Book List View
        composable<BookList> { BookListView(
            onBookItemClicked = {navController.navigate(BookItem(it))},
            // Reuse book edit view to add new books
            onAddButtonClicked = {navController.navigate(BookEdit())},
        ) }

        // Book Edit View
        composable<BookEdit> { backStackEntry ->
            val args = backStackEntry.toRoute<BookEdit>()
            BookEditView(
                id = args.bookID,
                onBookSubmit = {navController.popBackStack()},
            )
        }

        // Book Item VIew
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
    // Possible values passed to the Book Edit view
    val bookID: Int? = null
)
@Serializable
data class BookItem(
    // Value passed to the Book Item view
    val bookID: Int
)
