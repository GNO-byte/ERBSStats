package com.gno.erbs.erbs.stats.model.erbs.matches.item.special

import com.gno.erbs.erbs.stats.model.erbs.matches.item.SearchItem

data class ItemSpecial(
    override val code: Int?,
    val consumeCount: Float?,
    val craftAnimTrigger: String?,
    val exclusiveProducer: Float?,
    val initialCount: Float?,
    val itemGrade: String?,
    val itemType: String?,
    val makeMaterial1: Float?,
    val makeMaterial2: Float?,
    override val name: String?,
    val specialItemType: String?,
    val stackable: Float?,
    val summonCode: Float?
) : SearchItem