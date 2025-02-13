package com.example.bookmanagementsystem.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookAddView() {

    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("") }
    var totalPages by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add book") }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Book Title") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
            )

            TextField(
                value = author,
                onValueChange = { author = it },
                label = { Text("Book Author") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
            )

            TextField(
                value = genre,
                onValueChange = { genre = it },
                label = { Text("Book Genre") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
            )

            TextField(
                value = totalPages,
                onValueChange = {
                    if (it.isDigitsOnly()) { totalPages = it }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text("Total pages") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
            )

            Button(
                onClick = {
                    if (listOf(title, author, totalPages).any { s -> s.isEmpty() }) {
                            scope.launch {
                                snackbarHostState.showSnackbar("Title, Author, and Total Pages fields cannot be empty.")
                            }
                        }
                    else {
                        title = ""
                        author = ""
                        genre = ""
                        totalPages = ""
                        scope.launch {
                            snackbarHostState.showSnackbar("Book added to library.")
                        }
                    }
                },

            ) {
                Text("Add Book")
            }
        }
    }
}