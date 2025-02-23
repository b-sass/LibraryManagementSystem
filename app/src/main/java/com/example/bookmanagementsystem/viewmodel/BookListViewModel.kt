package com.example.bookmanagementsystem.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookmanagementsystem.data.Book
import com.example.bookmanagementsystem.data.BookDao
import com.example.bookmanagementsystem.data.DatabaseInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BookListViewModel(app: Application) : AndroidViewModel(app) {

    private var bookDB: BookDao? = null
    private val _books = MutableStateFlow(emptyList<Book>())
    val books = _books.asStateFlow()


    init {
        viewModelScope.launch {
            bookDB = DatabaseInstance.getDatabase(app.applicationContext).bookDao()
            getBooks()
        }
    }

    fun getBooks() {
        viewModelScope.launch {
            bookDB!!.getAllBooks().flowOn(Dispatchers.IO).collect { books: List<Book> ->
                _books.update { books }
            }
        }
    }

    fun searchBooks(query: String) {
        viewModelScope.launch {
            bookDB!!.getBooksByTitle(query).flowOn(Dispatchers.IO).collect { books: List<Book> ->
                _books.update { books }
            }
        }
    }
}