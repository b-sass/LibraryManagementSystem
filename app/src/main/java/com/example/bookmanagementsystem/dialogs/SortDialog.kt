package com.example.bookmanagementsystem.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bookmanagementsystem.viewmodel.BookListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortDialog(
    viewModel: BookListViewModel,
    onDismissRequest: () -> Unit = {},
) {

    val sorts = listOf(
        "Title",
        "Pages read",
        "Pages total",
    )

    BasicAlertDialog(
        onDismissRequest = { onDismissRequest() },
    ) {
        Surface(
            shape = MaterialTheme.shapes.large,
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ){
                Text("Sort by")

                for (sort in sorts) {
                    Row() {
                        Text(sort)
                        Icon(Icons.Filled.ArrowUpward, contentDescription = "Sort ascending")
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Cancel",
                        Modifier.clickable { onDismissRequest() }
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
                    Text(
                        text = "Apply",
                        Modifier.clickable { onDismissRequest() }
                    )
                }
            }
        }
    }
}