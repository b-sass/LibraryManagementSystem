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

class BookItemViewModel(app: Application) : AndroidViewModel(app) {

    private val bookDB = DatabaseInstance.getDatabase(app.applicationContext).bookDao()

    private val _bookItem = MutableStateFlow<Book?>(null)
    var bookItem = _bookItem.asStateFlow()

    fun getBookByID(id: Int) {
        viewModelScope.launch {
            bookDB.getBookById(id).flowOn(Dispatchers.IO).collect { book: Book ->
                _bookItem.update { book }
            }
        }
    }

    fun updatePageCount(pagesRead: Int) {
        viewModelScope.launch {
            _bookItem.value?.pagesRead = pagesRead
            bookDB.updateBook(_bookItem.value!!)
        }
    }

    fun deleteBook(book: Book) {
        viewModelScope.launch {
            bookDB.deleteBook(book)
        }
    }
}