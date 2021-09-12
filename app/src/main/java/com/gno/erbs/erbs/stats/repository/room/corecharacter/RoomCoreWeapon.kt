package com.gno.erbs.erbs.stats.repository.room.corecharacter

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RoomCoreWeapon(
    @PrimaryKey
    val id: String,
    @ColumnInfo(index = true)
    val name: String?
)