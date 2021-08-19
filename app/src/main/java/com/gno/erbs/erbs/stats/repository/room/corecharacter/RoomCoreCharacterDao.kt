package com.gno.erbs.erbs.stats.repository.room.corecharacter

import androidx.room.*

@Dao
interface RoomCoreCharacterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCoreCharacter(coreCharacter: RoomCoreCharacter)

    @Transaction
    @Query("SELECT * FROM RoomCoreCharacter")
    fun getCoreCharactersSkillWeapon(): List<RoomCoreCharactersSkillWeapon>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCoreSkill(coreSkill: List<RoomCoreSkill>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCoreWeapon(coreWeapon: List<RoomCoreWeapon>)

    @Transaction
    fun insertCoreCharacterFull(roomCoreCharacter: RoomCoreCharacter) {
        insertCoreCharacter(roomCoreCharacter)
        roomCoreCharacter.skills?.let { insertCoreSkill(it) }
        roomCoreCharacter.weapons?.let { insertCoreWeapon(it) }
    }

}