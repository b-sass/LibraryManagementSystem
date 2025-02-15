package com.example.bookmanagementsystem.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookmanagementsystem.data.Book
import com.example.bookmanagementsystem.data.DatabaseInstance
import kotlinx.coroutines.launch

class BookAddViewModel(app: Application): AndroidViewModel(app) {
    private val bookDB = DatabaseInstance.getDatabase(app.applicationContext).bookDao()

    fun addBook(book: Book) {
        viewModelScope.launch {
            bookDB.addBook(book)
        }
    }
}