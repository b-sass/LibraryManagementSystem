package com.example.bookmanagementsystem.view

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookmanagementsystem.data.Book
import com.example.bookmanagementsystem.viewmodel.BookListViewModel
import com.example.bookmanagementsystem.dialogs.FilterDialog
import com.example.bookmanagementsystem.dialogs.SortDialog

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

    val filteredBooks by remember { derivedStateOf {
        if (viewModel.appliedFilters.isEmpty()) {
            books
        } else {
            books.filter { viewModel.appliedFilters.contains(it.genre) }
        }
    }}

    Scaffold(
        topBar = {
            if (searchToggle) {
                SearchBar(query, onQueryChange = {
                    query = it
                    if (query == "") {
                        viewModel.getBooks()
                    } else { viewModel.searchBooks("%$query%") }
            },  onSearchClose = {
                    query = ""
                    searchToggle = false
                    viewModel.getBooks()
            })
            } else {
                DefaultBar(
                    onSearch = { searchToggle = true },
                    onSortButtonClicked = { sort = true },
                    onFilterButtonClicked = { filter = true }
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAddButtonClicked() },
            ) {
                Icon(Icons.Filled.Add, "Add book")
            }
        },
    ) { innerPadding ->

//         Filter dialog
        if (filter) {
            FilterDialog(viewModel) { filter = false }
        }

        // Sorting Dialog
        if (sort) {
            SortDialog(onDismissRequest = { sort = false })
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
            if (filteredBooks.isNotEmpty()) {
                LazyColumn {
                    items(filteredBooks) { book ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .clickable {
                                    onBookItemClicked(book.id)
                                }
                        ) {
                            Column {
                                Row {
                                    Text(book.title)
                                    Text(book.genre)

                                }
                                Row {
                                    Text(book.author)
                                    Text("Read: ${book.pagesRead} | Total: ${book.pagesTotal}")

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
        navigationIcon = { IconButton(onClick = {onSearchClose()}) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") }},
        title = { TextField(
            value = query,
            onValueChange = {onQueryChange(it)},
            placeholder = { Text("Search") },
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