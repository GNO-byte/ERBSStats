package com.gno.erbs.erbs.stats.repository.room

import android.os.Bundle
import android.os.Parcel
import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.gno.erbs.erbs.stats.di.Iso8601Format
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@ProvidedTypeConverter
class DateTypeConverter @Inject constructor(@Iso8601Format private val iso8601Format: SimpleDateFormat) {

    @TypeConverter
    fun fromTimestamp(value: String?): Date? {
        return value?.let {
            val dataTimeFormat = iso8601Format
            dataTimeFormat.parse(it)
        }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): String? {
        return date?.let {
            val dataTimeFormat = iso8601Format
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