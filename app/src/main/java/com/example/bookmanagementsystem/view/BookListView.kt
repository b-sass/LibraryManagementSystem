package com.example.bookmanagementsystem.view

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookmanagementsystem.viewmodel.BookListViewModel
import com.example.bookmanagementsystem.dialogs.FilterDialog
import com.example.bookmanagementsystem.dialogs.SortDialog
import java.util.Locale

@Composable
fun BookListView(
    viewModel: BookListViewModel = viewModel(),
    onBookItemClicked: (bookID: Int) -> Unit,
    onAddButtonClicked: () -> Unit,
) {
    val books by viewModel.books.collectAsStateWithLifecycle()

    var searchToggle by remember {mutableStateOf(false)}
    var query by remember { mutableStateOf("")}

    var filter by remember { mutableStateOf(false) }
    var sort by remember { mutableStateOf(false) }

    // Filter books according to applied filters
    val filteredBooks by remember { derivedStateOf {
        if (viewModel.appliedFilters.isEmpty()) {
            books
        } else {
            books.filter { viewModel.appliedFilters.contains(it.genre) }
        }
    }}

    // Sort already filtered books according to the current sort
    val sortedBooks = if (viewModel.appliedSort[1] == "Descending") {
        if (viewModel.appliedSort[0] == "Title") { filteredBooks.sortedByDescending { it.title.lowercase(Locale.getDefault()) } }
        else {
            filteredBooks.sortedByDescending {
                when (viewModel.appliedSort[0]) {
                    "Pages read" -> it.pagesRead
                    "Pages total" -> it.pagesTotal
                    "Progress" -> it.pagesRead / it.pagesTotal
                    else -> it.pagesRead
                }
            }
        }
    // Ascending sort
    } else {
        if (viewModel.appliedSort[0] == "Title") { filteredBooks.sortedBy { it.title.lowercase(Locale.getDefault()) } }
        else {
            filteredBooks.sortedBy {
                when (viewModel.appliedSort[0]) {
                    "Pages read" -> it.pagesRead
                    "Pages total" -> it.pagesTotal
                    "Progress" -> it.pagesRead * 100 / it.pagesTotal
                    else -> it.pagesRead
                }
            }
        }
    }

    Scaffold(
        topBar = {
            if (searchToggle) {
                // Turn off search on back press
                BackHandler {
                    query = ""
                    searchToggle = false
                    viewModel.getBooks()
                }

                // Query book database based on search input
                SearchBar(query, onQueryChange = {
                    query = it
                    if (query == "") {
                        viewModel.getBooks()
                    } else { viewModel.searchBooks("%$query%") }
                // Reset search bar on close and display all books
            },  onSearchClose = {
                    query = ""
                    searchToggle = false
                    viewModel.getBooks()
            })
            // Show default bar when search isn't used
            } else {
                DefaultBar(
                    onSearch = { searchToggle = true },
                    onSortButtonClicked = { sort = true },
                    onFilterButtonClicked = { filter = true }
                )
            }
        },
        // Move to add screen on button press
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { onAddButtonClicked() },
            ) {
                Icon(Icons.Filled.Add, "Add book")
                Spacer(modifier = Modifier.padding(4.dp))
                Text("Add book")
            }
        },
    ) { innerPadding ->

        // Filter dialog
        if (filter) {
            FilterDialog(viewModel) { filter = false }
        }

        // Sorting Dialog
        if (sort) {
            SortDialog(viewModel, onDismissRequest = { sort = false })
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
        ) {
            // Display total number of books
            if (books.isEmpty() && query.isEmpty()) {
                Text(
                    text = "You don't have any books in your library.",
                    modifier = Modifier
                        .padding(8.dp)
                )
            }
            // Differentiate between singular and plural books
            else if (books.isNotEmpty() && query.isEmpty()) {
                Text(
                    text = "You have ${books.size} ${
                        if (books.size == 1) { "book" } else { "books" }
                    } in your library",
                    modifier = Modifier.padding(8.dp)
                )
            }

            // Display list of books
            if (sortedBooks.isNotEmpty()) {
                LazyColumn {
                    items(sortedBooks) { book ->
                        Column(
                            modifier = Modifier.clickable { onBookItemClicked(book.id) }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f)
                                ) {
                                    Text(
                                        text = book.title,
                                        modifier = Modifier
                                            // Animate text if too long
                                            .basicMarquee()
                                    )
                                    Text(
                                        text = book.author,
                                        modifier = Modifier
                                            // Animate text if too long
                                            .basicMarquee()
                                    )
                                }
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f),
                                    horizontalAlignment = Alignment.End
                                ) {
                                    // Change text colour depending on whether book is finished
                                    Text(book.genre)
                                    Text(
                                        text = "Read: ${book.pagesRead} | Total: ${book.pagesTotal}",
                                        color = if (book.pagesRead == book.pagesTotal) { Color.Green } else { MaterialTheme.colorScheme.onSurface }
                                    )
                                }
                            }
                        }
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DefaultBar(onSearch: () -> Unit, onSortButtonClicked: () -> Unit, onFilterButtonClicked: () -> Unit) {
    TopAppBar(
        title = { Text("MAD Library") },
        actions = {
            // App Bar buttons
            IconButton(onClick = {onSearch()}) { Icon(Icons.Filled.Search, "Search") }
            IconButton(onClick = {onSortButtonClicked()}) { Icon(Icons.AutoMirrored.Filled.Sort, "Sort") }
            IconButton(onClick = {onFilterButtonClicked()}) { Icon(Icons.Filled.FilterList, "Filter") }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchBar(query: String, onQueryChange: (String) -> Unit = {}, onSearchClose: () -> Unit) {
    TopAppBar(
        // Close search bar on back icon press
        navigationIcon = { IconButton(onClick = {onSearchClose()}) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") }},
        title = { TextField(
            value = query,
            onValueChange = {onQueryChange(it)},
            placeholder = { Text("Search") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.Transparent),
            // Reset search bar colours
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
            ),
        )}
    )
}