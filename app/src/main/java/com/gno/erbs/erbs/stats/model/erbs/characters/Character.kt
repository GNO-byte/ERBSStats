package com.gno.erbs.erbs.stats.model.erbs.characters

data class Character(
    val attackPower: Double?,
    val attackSpeed: Double?,
    val attackSpeedLimit: Double?,
    val attackSpeedMin: Double?,
    val code: Int?,
    val criticalStrikeChance: Double?,
    val defense: Double?,
    val hpRegen: Double?,
    val initExtraPoint: Int?,
    val maxExtraPoint: Int?,
    val maxHp: Double?,
    val maxSp: Double?,
    val moveSpeed: Double?,
    val name: String?,
    val radius: Double?,
    val resource: String?,
    val sightRange: Int?,
    val spRegen: Double?,
    val uiHeight: Double?,

    //additional param
    var iconWebLink: String?
)