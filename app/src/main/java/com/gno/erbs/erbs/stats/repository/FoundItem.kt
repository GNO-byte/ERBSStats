package com.gno.erbs.erbs.stats.repository

import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await

data class FoundItem(
    val name: String,
    private val webLink: String? = null,
    private val storageReference: StorageReference? = null
) {

    suspend fun getWebLink(): String? {

        return webLink
            ?: storageReference?.downloadUrl?.await()?.toString()

    }
}
