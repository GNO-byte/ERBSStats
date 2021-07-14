package com.gno.erbs.erbs.stats.model.erbs.matches.item.consumable

data class ItemConsumable(
    val attackPowerByBuff: Int?,
    val code: Int?,
    val consumableTag: String?,
    val consumableType: String?,
    val craftAnimTrigger: String?,
    val defenseByBuff: Int?,
    val exclusiveProducer: Int?,
    val heal: Int?,
    val hpRecover: Int?,
    val initialCount: Int?,
    val itemGrade: String?,
    val itemType: String?,
    val makeMaterial1: Int?,
    val makeMaterial2: Int?,
    val name: String?,
    val spRecover: Int?,
    val stackable: Int?
)