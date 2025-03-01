package com.example.bookmanagementsystem.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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

    var appliedFilters by mutableStateOf(emptyList<String>())
    var appliedSort by mutableStateOf(listOf("Title", "Ascending"))

    init {
        viewModelScope.launch {
            bookDB = DatabaseInstance.getDatabase(app.applicationContext).bookDao()
            getBooks()
        }
        appliedFilters = emptyList()
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

    fun getGenres(): Set<String> {
        var genres: Set<String> = emptySet()

        _books.value.forEach { book ->
            genres = genres.plus(book.genre)
        }
        return genres
    }

    fun updateFilters(filters: List<String>) {
        appliedFilters = filters
    }

    fun filterBooks(books: List<Book>): List<Book> {
        val filteredBooks = if (appliedFilters.isEmpty()) {
            books
        } else {
            books.filter { book ->
                appliedFilters.contains(book.genre)
            }
        }
        return filteredBooks
    }
}