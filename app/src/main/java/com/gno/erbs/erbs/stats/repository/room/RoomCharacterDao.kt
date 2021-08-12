package com.gno.erbs.erbs.stats.repository.room

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

interface RoomCharacterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGender(character: RoomCharacter)

    @Query("SELECT * FROM RoomCharacter")
    fun getGenderByName(): List<RoomCharacter>



}