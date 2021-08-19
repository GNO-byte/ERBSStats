package com.gno.erbs.erbs.stats.repository.room.corecharacter

import androidx.room.*
import com.gno.erbs.erbs.stats.repository.room.corecharacter.RoomCoreCharacter

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
    @PrimaryKey
    val id: String,
    @ColumnInfo(index = true)
    val characterCode: Int,
    val description: String?,
    val group: Int?,
    val key: String?,
    val name: String?,
    val type: String?,
    var image: String?,

    //additional param
    var videoLink: String?


)