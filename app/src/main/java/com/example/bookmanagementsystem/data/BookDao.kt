package com.example.bookmanagementsystem.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface BookDao {

    @Query("SELECT * FROM book")
    fun getAllBooks(): List<Book>

    @Query("SELECT * FROM book WHERE id = :id")
    fun getBookById(id: Int): Book

    @Query("SELECT * FROM book WHERE title LIKE :title")
    fun getBookByTitle(title: String): Book

    @Insert
    fun addBook(book: Book)

    @Update
    fun updateBook(book: Book)
}