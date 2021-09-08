package com.gno.erbs.erbs.stats.repository.room.corecharacter

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

class RoomCoreCharactersSkillWeapon (
    @Embedded
    val character: RoomCoreCharacter,
    @Relation(
        parentColumn = "code",
        entityColumn = "characterCode"
    )
    val skills: List<RoomCoreSkill>,
    @Relation(
        parentColumn = "code",
        entityColumn = "id",
        associateBy = Junction(RoomCoreCharacterSkillCrossRef::class)
    )
    val weapons: List<RoomCoreWeapon>,

    )