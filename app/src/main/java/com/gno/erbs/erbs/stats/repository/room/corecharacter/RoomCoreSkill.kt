package com.gno.erbs.erbs.stats.repository.room.corecharacter

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = RoomCoreCharacter::class,
            parentColumns = ["code"],
            childColumns = ["characterCode"]
        )
    ]
)
data class RoomCoreSkill(
    val id: String,
    @ColumnInfo(index = true)
    val characterCode: Int,
    val description: String?,
    @PrimaryKey
    val group: Int?,
    val key: String?,
    val name: String?,
    val type: String?,
    var image: String?,

    //additional param
    var videoLink: String?
)