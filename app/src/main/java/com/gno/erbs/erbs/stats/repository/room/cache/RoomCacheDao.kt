package com.gno.erbs.erbs.stats.repository.room.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gno.erbs.erbs.stats.repository.room.character.RoomCharacter

@Dao
interface RoomCacheDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCache(cache: RoomCache)

    @Query("SELECT * FROM RoomCache WHERE name = :name")
    fun getCache(name: String): RoomCache


}