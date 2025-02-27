package com.example.bookmanagementsystem.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookmanagementsystem.data.Book
import com.example.bookmanagementsystem.data.DatabaseInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BookEditViewModel(app: Application): AndroidViewModel(app) {
    private val bookDB = DatabaseInstance.getDatabase(app.applicationContext).bookDao()

    private val _currentBook = MutableStateFlow<Book?>(null)
    var currentBook = _currentBook.asStateFlow()

    fun addBook(book: Book) {
        viewModelScope.launch {
            bookDB.addBook(book)
        }
    }

    fun getBookByID(id: Int) {
        viewModelScope.launch {
            bookDB.getBookById(id).flowOn(Dispatchers.IO).collect { book: Book ->
                _currentBook.update { book }
            }
        }
    }

    fun updateBook(book: Book) {
        viewModelScope.launch {
            bookDB.updateBook(book)
        }
    }
}