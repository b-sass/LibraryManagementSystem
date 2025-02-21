package com.example.bookmanagementsystem.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookmanagementsystem.data.Book
import com.example.bookmanagementsystem.viewmodel.BookListViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookListView(
    viewModel: BookListViewModel = viewModel(),
    onBookItemClicked: (bookID: Int) -> Unit,
    onAddButtonClicked: () -> Unit
) {
    val books by viewModel.books.collectAsStateWithLifecycle()
    var searchToggle by remember {mutableStateOf(false)}


    Scaffold(
        topBar = {
            if (searchToggle) {
                searchBar(onSearchClose = {searchToggle = false})
            } else {
                defaultBar(onSearch = {searchToggle = true})
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
        if (books.isEmpty()) {
            Text("You have 0 books in your library.")
        }
        else {
            Text("Your books:")
            HorizontalDivider()
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
            ) {
                items(books) { book ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .clickable {
                                onBookItemClicked(book.id)
                            }
                    ){
                        Column {
                            Text(book.title)
                            Text(book.author)

                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Column(
                            horizontalAlignment = Alignment.End
                        ) {
                            Text("${book.id}")
                            Text("Read: ${book.pagesRead} | Total: ${book.pagesTotal}")

                        }
                    }
                    HorizontalDivider()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun defaultBar(onSearch: () -> Unit) {
    TopAppBar(
        title = { Text("MAD Library") },
        actions = {
            IconButton(onClick = {onSearch()}) { Icon(Icons.Filled.Search, "Search") }
            IconButton(onClick = {}) { Icon(Icons.AutoMirrored.Filled.Sort, "Sort") }
            IconButton(onClick = {}) { Icon(Icons.Filled.FilterList, "Filter") }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun searchBar(onQueryChange: (String) -> Unit = {}, onSearchClose: () -> Unit) {
    var searchQuery by remember {mutableStateOf("")}
    TopAppBar(
        navigationIcon = { IconButton(onClick = {onSearchClose()}) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") }},
        title = { TextField(
            value = "",
            onValueChange = {onQueryChange(it)},
            placeholder = { Text("Search") },
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.Transparent),
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
        )
        },
    )
}