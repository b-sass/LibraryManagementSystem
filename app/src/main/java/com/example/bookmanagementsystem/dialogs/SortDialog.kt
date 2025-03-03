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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.bookmanagementsystem.viewmodel.BookListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortDialog(
    viewModel: BookListViewModel,
    onDismissRequest: () -> Unit = {},
) {

    val currentSort = remember { mutableStateListOf<String>() }
    currentSort.addAll(viewModel.appliedSort)

    val sorts = listOf(
        "Title",
        "Pages read",
        "Pages total",
        "Progress"
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
                Text(text = "Sort by", fontWeight = FontWeight.Bold)

                for (sort in sorts) {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                            if (currentSort[0] != sort) {
                                currentSort.clear()
                                currentSort.addAll(listOf(sort, "Ascending"))
                            } else {
                                val sortDirection = when (currentSort[1]) {
                                    "Ascending" -> "Descending"
                                    "Descending" -> "Ascending"
                                    else -> "Ascending"
                                }
                                currentSort.clear()
                                currentSort.addAll(listOf(sort, sortDirection))
                            }
                        }
                    ) {
                        Text(
                            text = sort,
                            modifier = Modifier.padding(4.dp)
                        )
                        if (currentSort[0] == sort) {
                            Spacer(modifier = Modifier.padding(8.dp))
                            when (currentSort[1]) {
                                "Ascending" -> Icon(
                                    Icons.Filled.ArrowUpward,
                                    contentDescription = "Sort ascending"
                                )
                                "Descending" -> Icon(
                                    Icons.Filled.ArrowDownward,
                                    contentDescription = "Sort descending"
                                )
                            }
                        }
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Cancel",
                        modifier = Modifier.clickable { onDismissRequest() }
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
                    Text(
                        text = "Apply",
                        modifier = Modifier.clickable {
                            viewModel.appliedSort = currentSort
                            onDismissRequest()
                        }
                    )
                }
            }
        }
    }
}