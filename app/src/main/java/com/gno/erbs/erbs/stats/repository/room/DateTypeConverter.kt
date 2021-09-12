package com.gno.erbs.erbs.stats.repository.room

import android.os.Bundle
import android.os.Parcel
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

    @TypeConverter
    fun fromBundle(value: ByteArray?): Bundle? {
        return value?.let {
            val parcel = Parcel.obtain()
            parcel.unmarshall(value, 0, value.size)
            parcel.setDataPosition(0)
            val bundle = parcel.readBundle(Thread.currentThread().contextClassLoader)
            parcel.recycle()
            bundle
        }
    }

    @TypeConverter
    fun bundleToByteArray(bundle: Bundle?): ByteArray? {
        val parcel = Parcel.obtain()
        parcel.writeBundle(bundle)
        val bytes = parcel.marshall()
        parcel.recycle()
        return bytes

    }
}