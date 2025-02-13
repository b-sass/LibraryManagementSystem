package com.example.bookmanagementsystem.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Book(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val author: String,
    val genre: String?,
    val dateAdded: Date = Date(),
    var pagesRead: Int = 0,
    val pagesTotal: Int,
)