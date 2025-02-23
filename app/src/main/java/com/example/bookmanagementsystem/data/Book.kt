package com.example.bookmanagementsystem.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Book(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val author: String,
    val genre: String = "No genre",
    val dateAdded: Date = Date(),
    var pagesRead: String = "0",
    val pagesTotal: String,
)