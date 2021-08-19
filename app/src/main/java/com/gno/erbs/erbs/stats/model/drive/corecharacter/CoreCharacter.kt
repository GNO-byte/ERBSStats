package com.gno.erbs.erbs.stats.model.drive.corecharacter

data class CoreCharacter(
    val code: Int?,
    val background: String?,
    val name: String?,
    val skills: List<CoreSkill>?,
    val weapons: List<CoreWeapon>?
)