package com.example.bookmanagementsystem.data

import android.content.Context
import androidx.room.Room


object DatabaseInstance {
    private var instance: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        if (instance == null) {
            instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "book-database"
            ).build()
        }
        return instance!!
    }
}