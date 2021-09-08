package com.gno.erbs.erbs.stats.repository.room.history

import android.os.Bundle
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*


@Entity
data class RoomHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val date: Date,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    val bundle: Bundle? = null,
    val navigateId: Int
    )

