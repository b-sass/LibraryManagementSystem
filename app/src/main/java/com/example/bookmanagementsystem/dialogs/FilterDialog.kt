package com.example.bookmanagementsystem.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
fun FilterDialog(
    viewModel: BookListViewModel,
    onDismissRequest: () -> Unit = {},
) {
    val appliedFilters = remember { mutableStateListOf<String>() }
    val genres = remember { mutableStateListOf<String>() }

    appliedFilters.addAll(viewModel.appliedFilters)

    LaunchedEffect(genres) {
        genres.addAll(viewModel.getGenres())
    }

    BasicAlertDialog(
        onDismissRequest = { onDismissRequest() },
    ) {
        Surface(
            shape = MaterialTheme.shapes.large,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Filter by genre", fontWeight = FontWeight.Bold)
                for (genre in genres) {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                            .clickable {
                                if (appliedFilters.contains(genre)) {
                                    appliedFilters.remove(genre)
                                } else {
                                    appliedFilters.add(genre)
                                }
                            },
                    ) {
                        Checkbox(
                            checked = appliedFilters.contains(genre),
                            onCheckedChange = {
                                if (appliedFilters.contains(genre)) {
                                    appliedFilters.remove(genre)
                                } else {
                                    appliedFilters.add(genre)
                                }
                            }
                        )
                        Spacer(modifier = Modifier.padding(8.dp))
                        Text(genre)
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
                            viewModel.updateFilters(appliedFilters)
                            onDismissRequest()
                        }
                    )
                }
            }
        }
    }
}