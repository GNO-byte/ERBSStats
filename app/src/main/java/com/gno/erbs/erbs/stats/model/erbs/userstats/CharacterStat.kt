package com.gno.erbs.erbs.stats.model.erbs.userstats

data class CharacterStat(
    val averageRank: Int,
    val characterCode: Int,
    val maxKillings: Int,
    val top3: Int,
    val top3Rate: Int,
    val totalGames: Int,
    val usages: Int,
    val wins: Int,

    //additional param
    var characterName: String?,
    var matchingTeamMode: Int?,
    var WebLinkImage: String?
)