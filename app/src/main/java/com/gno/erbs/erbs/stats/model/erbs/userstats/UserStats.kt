package com.gno.erbs.erbs.stats.model.erbs.userstats

data class UserStats(
    val averageAssistants: Double,
    val averageHunts: Double,
    val averageKills: Double,
    val averageRank: Double,
    val characterStats: List<CharacterStat>,
    val matchingMode: Int,
    val matchingTeamMode: Int,
    val mmr: Int,
    val nickname: String,
    val rank: Int,
    val rankPercent: Double,
    val rankSize: Int,
    val seasonId: Int,
    val top1: Double,
    val top2: Double,
    val top3: Double,
    val top5: Double,
    val top7: Double,
    val totalGames: Int,
    val totalWins: Int,
    val userNum: Int,

    //additional param
    var rankTierImageWebLink: String?,
    var topCharacterHalfImageWebLink: String?
)