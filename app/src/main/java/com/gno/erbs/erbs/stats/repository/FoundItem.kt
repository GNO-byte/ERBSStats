package com.gno.erbs.erbs.stats.repository

import com.gno.erbs.erbs.stats.repository.room.RoomService
import com.gno.erbs.erbs.stats.repository.room.cache.RoomCache
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.*

data class FoundItem(
    val name: String,
    private var webLink: String? = null,
    private val storageReference: StorageReference? = null,
    private val path: String? = null
) {

    suspend fun getWebLink(): String? {

        return webLink.let {
            webLink = storageReference?.downloadUrl?.await()?.toString()
            webLink
        }
    }

    suspend fun getWebLink(roomService: RoomService?): String? {

        var currentWebLink = webLink

        if (currentWebLink == null) {

            roomService?.let { roomService ->
                val currentDate = roomService.getCurrentDate()
                val cache = roomService.getcache("$path/$name")

                currentWebLink = cache?.date?.let {
                    if (currentDate.afterTheDay(cache.date)) loadUrl(roomService, currentDate)
                    else cache.wevLink
                } ?: loadUrl(roomService, currentDate)

            } ?: run { currentWebLink = getWebLink() }
        }

        return currentWebLink

    }

    private fun Date.afterTheDay(whenDate: Date): Boolean {

        val whenDateCalendar: Calendar =
            Calendar.getInstance().apply {
                time =
                    whenDate
            }

        val thisDate = this

        val thisDateCalendar: Calendar =
            Calendar.getInstance().apply {
                time = thisDate
            }

        val whenDateYear = whenDateCalendar.get(Calendar.YEAR)
        val whenDateDay = whenDateCalendar.get(Calendar.DAY_OF_YEAR)

        val thisDateYear = thisDateCalendar.get(Calendar.YEAR)
        val thisDateDay = thisDateCalendar.get(Calendar.DAY_OF_YEAR)

        return when {
            thisDateYear > whenDateYear -> true
            thisDateYear == whenDateYear && thisDateDay > whenDateDay -> true
            else -> false
        }
    }

    private suspend fun loadUrl(
        roomService: RoomService,
        currentDate: Date
    ): String? {
        val currentWebLink = storageReference?.downloadUrl?.await()?.toString()
        GlobalScope.launch {
            currentWebLink?.let {
                GlobalScope.launch {
                    roomService.addCache(RoomCache("$path/$name", it, currentDate))

                }
            }
        }
        return currentWebLink
    }

}
