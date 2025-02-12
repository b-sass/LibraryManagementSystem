package com.example.bookmanagementsystem.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import java.util.Date

@Entity
data class Book(
    @PrimaryKey val id: Int,
    val title: String,
    val author: String,
    val genre: String?,
    val dateAdded: Date,
    var pagesRead: Int?,
    val pagesTotal: Int,
)
