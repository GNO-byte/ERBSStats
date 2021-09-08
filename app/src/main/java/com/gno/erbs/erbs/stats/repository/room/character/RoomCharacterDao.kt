package com.gno.erbs.erbs.stats.repository.room.character

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gno.erbs.erbs.stats.repository.room.character.RoomCharacter

@Dao
interface RoomCharacterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCharacter(character: RoomCharacter)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCharacters(characters: List<RoomCharacter>)

    @Query("SELECT * FROM RoomCharacter")
    fun getCharacters(): List<RoomCharacter>


}