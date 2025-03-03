package com.example.bookmanagementsystem.view

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookmanagementsystem.viewmodel.BookItemViewModel
import com.example.bookmanagementsystem.dialogs.DeleteDialog
import java.text.DateFormat

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

    var showDelete by remember { mutableStateOf(false) }

    // Populate book data from existing book
    LaunchedEffect(book) {
        title = book?.title ?: ""
        author = book?.author ?: ""
        genre = book?.genre ?: ""
        // Format date to DD/MM/YYYY
        dateAdded = if (book == null) { "" } else {
            val df = DateFormat.getDateInstance(DateFormat.SHORT)
            df.format(book!!.dateAdded)
        }
        pagesRead = book?.pagesRead ?: 0
        totalPages = book?.pagesTotal ?: 1
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(
                    text = title,
                    // Animate text if too long
                    modifier = Modifier.basicMarquee()
                ) },
                navigationIcon = { IconButton(onClick = {
                    // Update page count on back icon press
                    viewModel.updatePageCount(pagesRead)
                    onBackButtonClicked()
                }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } }
            )
        }
    ) { innerPadding ->

        // Update page count on back button press
        BackHandler {
            viewModel.updatePageCount(pagesRead)
            onBackButtonClicked()
        }

        // Make sure if user wants to delete book
        if (showDelete) {
            DeleteDialog(
                onConfirmation = {
                    viewModel.deleteBook(book!!)
                    showDelete = false
                    onDeleteButtonClicked()
                },
                onDismissRequest = { showDelete = false }
            )
        }

        // Display book data
        Column(modifier = Modifier.padding(innerPadding)) {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                // Book Info
                Text("Title: $title")
                Text("Author: $author")
                Text("Genre: $genre")
                Text("Started reading: $dateAdded")
                Text("Pages read: $pagesRead")
                Text("Total pages: $totalPages")

                Spacer(modifier = Modifier.padding(8.dp))

                // Page count slider + buttons
                Row {
                    Button(
                        modifier = Modifier.weight(.3f),
                        onClick = {
                        if (pagesRead > 0) {
                            pagesRead--
                        }
                    }) {
                        Icon(Icons.Filled.Remove, "Decrement page count")
                    }

                    Slider(
                        value = pagesRead.toFloat(),
                        onValueChange = { pagesRead = it.toInt() },
                        valueRange = 0f..totalPages.toFloat(),
                        steps = totalPages,
                        modifier = Modifier.weight(1f),
                    )

                    Button(
                        modifier = Modifier.weight(.3f),
                        onClick = {
                        if (pagesRead < totalPages) {
                            pagesRead++
                        }
                    }) {
                        Icon(Icons.Filled.Add, "Increment page count")
                    }
                }

                Spacer(modifier = Modifier.padding(8.dp))

                // Display book progress
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (pagesRead == totalPages) {
                        Text(text = "You have finished reading this book!", color = Color.Green)
                    }
                    else {
                        // Show progress in percentage
                        Text("You have read ${pagesRead * 100 / totalPages}% of this book.")
                        Text("You have ${totalPages - pagesRead} pages left.")
                    }
                }

                Spacer(modifier = Modifier.padding(8.dp))

                // Update + Delete Buttons
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(onClick = {
                        // Update page count on update button press
                        viewModel.updatePageCount(pagesRead)
                        onUpdateButtonClicked()
                    }) {
                        Text("Update book")
                    }

                    Spacer(modifier = Modifier.padding(8.dp))

                    Button(
                        onClick = {
                            showDelete = true
                        }
                    ) {
                        Text("Delete Book")
                    }
                }
            }

        }
    }
}