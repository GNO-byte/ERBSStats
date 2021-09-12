package com.gno.erbs.erbs.stats.repository.room.history

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RoomHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHistory(history: RoomHistory)

    @Query("SELECT * FROM RoomHistory ORDER BY datetime(date) DESC LIMIT 5")
    fun getLast5Histories(): Flow<List<RoomHistory>?>

    // @Query("DELETE FROM RoomHistory ORDER BY datetime(date) LIMIT 0, 95")
    @Query("DELETE FROM RoomHistory WHERE id NOT IN (SELECT id FROM (SELECT id FROM RoomHistory ORDER BY datetime(date) DESC LIMIT 5 ))")
    fun delete95lastHistories()

    @Query("SELECT COUNT(id) FROM RoomHistory")
    fun getRowCount(): Int

}