package com.example.bookmanagementsystem.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    onBookItemClicked: () -> Unit,
    onAddButtonClicked: () -> Unit
) {
    val books by viewModel.books.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Book List") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAddButtonClicked() },
            ) {
                Icon(Icons.Filled.Add, "Add book")
            }
        }
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
                                onBookItemClicked()
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
                            Text(book.genre ?: "")
                            Text("Read: ${book.pagesRead} | Total: ${book.pagesTotal}")

                        }

                    }
                    HorizontalDivider()
                }
            }
        }
    }
}