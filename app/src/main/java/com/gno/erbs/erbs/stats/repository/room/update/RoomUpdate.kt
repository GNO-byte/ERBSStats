package com.gno.erbs.erbs.stats.repository.room.update

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class RoomUpdate(
    @PrimaryKey
    val table: String,
    val date: Date,
    val version: Int
)
