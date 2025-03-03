package com.example.bookmanagementsystem.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookmanagementsystem.viewmodel.BookItemViewModel
import com.example.bookmanagementsystem.dialogs.DeleteDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookItemView(
    viewModel: BookItemViewModel = viewModel(),
    id: Int,
    onUpdateButtonClicked: () -> Unit,
    onBackButtonClicked: () -> Unit,
    onDeleteButtonClicked: () -> Unit,
) {

    val book by viewModel.bookItem.collectAsStateWithLifecycle()
    viewModel.getBookByID(id)

    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("") }
    var dateAdded by remember { mutableStateOf("") }
    var pagesRead by remember { mutableIntStateOf(0) }
    var totalPages by remember { mutableIntStateOf(1) }

    var confirmDelete by remember { mutableStateOf(false) }

    // Populate book data from existing book
    LaunchedEffect(book) {
        title = book?.title ?: ""
        author = book?.author ?: ""
        genre = book?.genre ?: ""
        dateAdded = if (book == null) { "" } else { book!!.dateAdded.toString() }
        pagesRead = book?.pagesRead ?: 0
        totalPages = book?.pagesTotal ?: 1
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = { IconButton(onClick = { onBackButtonClicked() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } }
            )
        }
    ) { innerPadding ->

        // Make sure if user wants to delete book
        if (confirmDelete) {
            DeleteDialog(
                onConfirmation = {
                    viewModel.deleteBook(book!!)
                    confirmDelete = false
                    onDeleteButtonClicked()
                },
                onDismissRequest = { confirmDelete = false }
            )
        }

        // Display book data
        Column(modifier = Modifier.padding(innerPadding)) {
            Text("Title: $title")
            Text("Author: $author")
            Text("Genre: $genre")
            Text("Started reading: $dateAdded")
            Text("Release date: *Release Date*") // TODO
            Text("Pages read: $pagesRead")
            Text("Total pages: $totalPages")
            Spacer(modifier = Modifier.padding(8.dp))
            Row() {
                Button(onClick = { onUpdateButtonClicked() }) {
                    Text("Update book")
                }

                Spacer(modifier = Modifier.padding(8.dp))

                Button(
                    onClick = {
                        confirmDelete = true
                    }
                ) {
                    Text("Delete Book")
                }
            }
        }
    }
}