package com.gno.erbs.erbs.stats.repository

import android.content.Context
import com.gno.erbs.erbs.stats.repository.room.Converter
import com.gno.erbs.erbs.stats.repository.room.RoomService
import javax.inject.Inject

interface AsyncInitialized {
    suspend fun initData(context: Context)
}