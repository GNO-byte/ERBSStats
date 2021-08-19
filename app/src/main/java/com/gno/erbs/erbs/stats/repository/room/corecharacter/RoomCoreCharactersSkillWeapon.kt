package com.gno.erbs.erbs.stats.repository.room.corecharacter

import androidx.room.Embedded
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
        entityColumn = "characterCode"
    )
    val weapons: List<RoomCoreWeapon>,

    )