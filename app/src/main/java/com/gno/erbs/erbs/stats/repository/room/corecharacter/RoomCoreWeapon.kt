package com.gno.erbs.erbs.stats.repository.room.corecharacter

import androidx.room.*
import com.gno.erbs.erbs.stats.repository.room.corecharacter.RoomCoreCharacter


@Entity(
    foreignKeys = [
        ForeignKey(
            entity = RoomCoreCharacter::class,
            parentColumns = ["code"],
            childColumns =  ["characterCode"]
        )
    ]
)
data class RoomCoreWeapon(
    @PrimaryKey
    val id: String,
    @ColumnInfo(index = true)
    val characterCode: Int,
    val name: String?
)