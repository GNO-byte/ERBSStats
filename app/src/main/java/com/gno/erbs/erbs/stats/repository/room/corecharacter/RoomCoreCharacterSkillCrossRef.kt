package com.gno.erbs.erbs.stats.repository.room.corecharacter

import androidx.room.Entity

@Entity(primaryKeys = ["code", "id"])
data class RoomCoreCharacterSkillCrossRef(
    val code: Int,
    val id: String
)
