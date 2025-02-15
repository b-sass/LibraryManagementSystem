package com.example.bookmanagementsystem.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {

    @Query("SELECT * FROM book")
    fun getAllBooks(): Flow<List<Book>>

    @Query("SELECT * FROM book WHERE id = :id")
    fun getBookById(id: Int): Flow<Book>

    @Query("SELECT * FROM book WHERE title LIKE :title")
    suspend fun getBookByTitle(title: String): Book

    @Insert
    suspend fun addBook(book: Book)

    @Update
    suspend fun updateBook(book: Book)

    @Delete
    suspend fun deleteBook(book: Book)
}