package com.example.bookmanagementsystem.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookmanagementsystem.data.Book
import com.example.bookmanagementsystem.viewmodel.BookEditViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookEditView(
    viewModel: BookEditViewModel = viewModel(),
    id: Int?,
    onBookSubmit: () -> Unit,
    ) {

    // Get book by ID if editing
    if (id != null) {
        viewModel.getBookByID(id)
    }

    val existingBook by viewModel.currentBook.collectAsStateWithLifecycle()

    // Text field states
    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("") }
    var pagesRead by remember { mutableIntStateOf(0) }
    var totalPages by remember { mutableIntStateOf( 1) }

    // Populate text fields with current book data if found
    LaunchedEffect(existingBook) {
        title = existingBook?.title ?: ""
        author = existingBook?.author ?: ""
        genre = existingBook?.genre ?: ""
        pagesRead = existingBook?.pagesRead ?: 0
        totalPages = existingBook?.pagesTotal ?: 1
    }

    // Snackbar coroutine
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Keyboard focus
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            TopAppBar(
                // Change title based on whether adding or editing book
                title = { Text(if (id != null) {"Editing book $id"} else {"Add book"}) },
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Book Title
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Book Title") },
                placeholder = { Text("Title: ") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
            )

            // Book Author
            OutlinedTextField(
                value = author,
                onValueChange = { author = it },
                label = { Text("Book Author") },
                placeholder = { Text("Author: ") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
            )

            // Book Genre
            OutlinedTextField(
                value = genre,
                onValueChange = { genre = it },
                label = { Text("Book Genre (Optional)") },
                placeholder = { Text("Genre: ") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
            )

            // Book Pages Read
            OutlinedTextField(
                value = pagesRead.toString(),
                onValueChange = {
                    // Ints only, empty string defaults to 0
                    // Can't add more pages than int max value
                    try {
                        if (it.isDigitsOnly()) { pagesRead = it.toInt() }
                    } catch (e: NumberFormatException) {
                        if (it == "") { pagesRead = 0 }
                    }
                },
                // Limit keyboard input to numbers only
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text("Read pages (Optional)") },
                placeholder = { Text("Read pages: ") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
            )

            // Book Total Pages
            OutlinedTextField(
                value = totalPages.toString(),
                onValueChange = {
                    // Ints only, empty string defaults to 0
                    // Can't add more pages than int max value
                    try {
                        if (it.isDigitsOnly()) { totalPages = it.toInt() }
                    } catch (e: NumberFormatException) {
                        if (it == "") { totalPages = 0 }
                    }
                },
                // Limit keyboard input to numbers only
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text("Total pages") },
                placeholder = { Text("Page count: ") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        // Hide keyboard
                        focusManager.clearFocus()

                        // Validate user input
                        if (title.isEmpty() || author.isEmpty() || totalPages < 1) {
                            scope.launch {
                                snackbarHostState.currentSnackbarData?.dismiss()
                                snackbarHostState.showSnackbar("Title, Author, and Total Pages fields cannot be empty.")
                            }
                        }
                        // Pages read cannot be greater than total pages
                        else if (pagesRead > totalPages) {
                            scope.launch {
                                snackbarHostState.currentSnackbarData?.dismiss()
                                snackbarHostState.showSnackbar("Read pages cannot be greater than total pages.")
                            }
                        }
                        // Create new book object
                        else {
                            val newBook = Book(
                                id = id ?: 0,
                                title = title,
                                author = author,
                                genre = if (genre == "") { "No genre" } else { genre },
                                pagesRead = pagesRead,
                                pagesTotal = totalPages
                            )

                            // Adding / Update snackbar
                            scope.launch {
                                snackbarHostState.currentSnackbarData?.dismiss()
                                snackbarHostState.showSnackbar(
                                    if (id != null) {
                                        "Book $id updated."
                                    } else {
                                        "Book added to library."
                                    }
                                )
                            }

                            // Add or update book
                            if (id != null) {
                                viewModel.updateBook(newBook)
                            } else {
                                viewModel.addBook(newBook)
                            }
                            // navigate back
                            onBookSubmit()
                        }
                    },

                    ) {
                    // Change button text based on whether adding or updating book
                    Text(
                        if (id != null) { "Update Book" } else { "Add Book" }
                    )
                }
            }
        }
    }
}