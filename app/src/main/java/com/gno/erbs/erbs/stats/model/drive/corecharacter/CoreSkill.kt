package com.gno.erbs.erbs.stats.model.drive.corecharacter

data class CoreSkill(
    val id: String?,
    val description: String?,
    val group: Int?,
    val key: String?,
    val name: String?,
    val type: String?,
    var image: String?,

    //additional param
    var videoLink: String?
)