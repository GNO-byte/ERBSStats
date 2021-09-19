package com.gno.erbs.erbs.stats.di.module

import android.content.Context
import android.os.Build
import androidx.room.Room
import com.gno.erbs.erbs.stats.di.Iso8601Format
import com.gno.erbs.erbs.stats.repository.room.AppDatabase
import com.gno.erbs.erbs.stats.repository.room.DateTypeConverter
import dagger.Module
import dagger.Provides
import java.text.SimpleDateFormat
import java.util.*

@Module
class DataBaseModule {

    @Provides
    fun provideAppDatabase(context: Context, dateTypeConverter: DateTypeConverter): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, AppDatabase.DB_NAME)
            .addTypeConverter(dateTypeConverter).build()

    @Provides
    fun provideLocale(context: Context): Locale =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            context.resources.configuration.locales.get(0)
        else context.resources.configuration.locale

    @Provides
    @Iso8601Format
    fun provideIso8601Format(locale: Locale): SimpleDateFormat = SimpleDateFormat(
        "yyyy-MM-dd'T'HH:mm:ssZ", locale
    )

//    private var roomCharacterDao: RoomCharacterDao? = null
//    private var roomCoreCharacterDao: RoomCoreCharacterDao? = null
//    private var roomUpdateDao: RoomUpdateDao? = null
//    private var roomCacheDao: RoomCacheDao? = null
//    private var roomHistoryDao: RoomHistoryDao? = null
//    private var locale: Locale? = null

//    operator fun invoke(context: Context): RoomService {
//        db = AppDatabase.getAppDataBase(context)
//
//        roomCharacterDao = db?.roomCharacterDao()
//        roomCoreCharacterDao = db?.roomCoreCharacterDao()
//        roomUpdateDao = db?.roomUpdateDao()
//        roomCacheDao = db?.roomCacheDao()
//        roomHistoryDao = db?.roomHistoryDao()
//
//        roomHistoryDao?.let {
//            if (it.getRowCount() >= 100) it.delete95lastHistories()
//        }
//
//        locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
//            context.resources.configuration.locales.get(0)
//        else context.resources.configuration.locale
//
//        return this
//    }


}