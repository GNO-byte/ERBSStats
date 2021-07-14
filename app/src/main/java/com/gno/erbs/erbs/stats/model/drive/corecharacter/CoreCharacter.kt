package com.gno.erbs.erbs.stats.model.drive.corecharacter

data class CoreCharacter(
    val background: String?,
    val code: Int?,
    val name: String?,
    val skills: List<CoreSkill>?,
    val weapons: List<CoreWeapon>?
)