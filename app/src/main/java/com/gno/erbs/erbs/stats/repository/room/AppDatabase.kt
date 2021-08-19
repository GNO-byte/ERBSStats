package com.gno.erbs.erbs.stats.repository.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.gno.erbs.erbs.stats.repository.room.cache.RoomCache
import com.gno.erbs.erbs.stats.repository.room.cache.RoomCacheDao
import com.gno.erbs.erbs.stats.repository.room.character.RoomCharacter
import com.gno.erbs.erbs.stats.repository.room.character.RoomCharacterDao
import com.gno.erbs.erbs.stats.repository.room.corecharacter.RoomCoreCharacter
import com.gno.erbs.erbs.stats.repository.room.corecharacter.RoomCoreCharacterDao
import com.gno.erbs.erbs.stats.repository.room.corecharacter.RoomCoreSkill
import com.gno.erbs.erbs.stats.repository.room.corecharacter.RoomCoreWeapon
import com.gno.erbs.erbs.stats.repository.room.update.RoomUpdate
import com.gno.erbs.erbs.stats.repository.room.update.RoomUpdateDao

@Database(
    entities = [RoomCharacter::class,
        RoomCoreCharacter::class,
        RoomCoreSkill::class,
        RoomCoreWeapon::class,
        RoomUpdate::class,
        RoomCache::class], version = 1
)
@TypeConverters(DateTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun roomCharacterDao(): RoomCharacterDao
    abstract fun roomCoreCharacterDao(): RoomCoreCharacterDao
    abstract fun roomUpdateDao(): RoomUpdateDao
    abstract fun roomCacheDao(): RoomCacheDao

    companion object {
        var INSTANCE: AppDatabase? = null

        fun getAppDataBase(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE =
                        Room.databaseBuilder(context, AppDatabase::class.java, "ErbsDB").build()
                }
            }
            return INSTANCE
        }

        fun destroyDataBase() {
            INSTANCE = null
        }
    }
}