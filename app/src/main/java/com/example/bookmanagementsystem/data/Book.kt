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
    val dateAdded: Date = Date(), // Date has to be converted into unix timestamp before storing in database
    var pagesRead: Int = 0,
    val pagesTotal: Int = 1,
)