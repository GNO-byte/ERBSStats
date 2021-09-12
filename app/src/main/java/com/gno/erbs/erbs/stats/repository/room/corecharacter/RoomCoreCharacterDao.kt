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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRoomCoreCharacterSkillCrossRef(coreWeapon: List<RoomCoreCharacterSkillCrossRef>)

    @Transaction
    fun insertCoreCharacterFull(roomCoreCharacter: RoomCoreCharacter) {
        insertCoreCharacter(roomCoreCharacter)

        roomCoreCharacter.skills?.let {
            insertCoreSkill(it)
        }
        roomCoreCharacter.weapons?.let { roomCoreWeapons ->
            insertCoreWeapon(roomCoreWeapons)
            insertRoomCoreCharacterSkillCrossRef(roomCoreWeapons.map {
                RoomCoreCharacterSkillCrossRef(roomCoreCharacter.code, it.id)
            })
        }
    }

}