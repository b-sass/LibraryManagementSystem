package com.example.bookmanagementsystem.data

import androidx.room.TypeConverter
import java.util.Date

class DateConverter {

    // Convert timestamp to date
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    // Convert date to timestamp
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}