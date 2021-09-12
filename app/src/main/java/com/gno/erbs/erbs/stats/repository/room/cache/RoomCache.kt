package com.gno.erbs.erbs.stats.repository.room.cache

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class RoomCache(
    @PrimaryKey
    val name: String,
    val wevLink: String,
    val date: Date
)
