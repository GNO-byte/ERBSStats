package com.gno.erbs.erbs.stats.repository.room

import android.content.Context
import androidx.room.TypeConverter
import java.util.*

class DateTypeConverter {

    @TypeConverter
    fun fromTimestamp(value: String?): Date? {
        return value?.let {
            val dataTimeFormat = RoomService.getIso8601Format()
            dataTimeFormat.parse(it)
        }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): String? {
        return date?.let {
            val dataTimeFormat = RoomService.getIso8601Format()
            val text = dataTimeFormat.format(it)
            text.substring(0, 22) + ":" + text.substring(22)
        }

    }
}