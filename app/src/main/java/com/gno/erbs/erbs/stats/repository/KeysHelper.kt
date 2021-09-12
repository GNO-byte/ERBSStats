package com.gno.erbs.erbs.stats.repository

import android.content.Context

object KeysHelper {

    fun getApiKey(context: Context): String {
        return getLocalValue("API_KEY", context)
    }

    fun getDriveKey(context: Context): String {
        return getLocalValue("DRIVE_KEY", context)
    }

    fun getDriveCoreImageLinkStructureId(context: Context): String {
        return getLocalValue("DRIVE_CORE_IMAGE_LINK_STRUCTURE_ID", context)
    }


    fun getDriveCoreCharactersId(context: Context): String {
        return getLocalValue("CORE_CHARACTERS_ID", context)
    }

    private fun getLocalValue(name: String, context: Context): String {
        return context.getString(
            context.resources.getIdentifier(
                name,
                "string",
                context.packageName
            )
        )
    }
}