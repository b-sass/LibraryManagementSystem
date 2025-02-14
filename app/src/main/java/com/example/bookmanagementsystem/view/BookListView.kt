package com.example.bookmanagementsystem.view

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.room.Room
import com.example.bookmanagementsystem.data.DatabaseInstance

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookListView(ctx: Context, onBookItemClicked: () -> Unit) {

    val db = DatabaseInstance.getDatabase(ctx);

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Book List") }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            items(50) { _ ->
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
                            onBookItemClicked()
                        }
                ) {
                    Text("Book Title")
                    Text("Book Author")
                    Text("Book Genre")
                    Text("Progress: 95%")
                }
                HorizontalDivider()
            }
        }
    }
}