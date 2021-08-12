package com.gno.erbs.erbs.stats.model.firebase

import com.google.firebase.storage.StorageReference

data class FolderContent(
    val folders: List<StorageReference> = mutableListOf(),
    val files: List<StorageReference> = mutableListOf()
)
