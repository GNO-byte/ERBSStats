package com.gno.erbs.erbs.stats.repository.room.update

import androidx.room.*

@Dao
interface RoomUpdateDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTableUpdate(update: RoomUpdate)

    @Transaction
    @Query("SELECT * FROM RoomUpdate")
    fun getTableUpdate(): List<RoomUpdate>

}