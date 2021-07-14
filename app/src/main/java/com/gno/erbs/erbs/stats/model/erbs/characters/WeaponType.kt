package com.gno.erbs.erbs.stats.model.erbs.characters

data class WeaponType(
    val assistance: Int?,
    val attack: Int?,
    val character: String?,
    val characterCode: Int?,
    val controlDifficulty: Int?,
    val defense: Int?,
    val disruptor: Int?,
    val mastery: String?,
    val move: Int?,

    //additional param
    var weaponTypeImageWebLink: String?,
    var name: String?
)