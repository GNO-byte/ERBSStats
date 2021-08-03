package com.gno.erbs.erbs.stats.model.erbs.userstats

data class CharacterStat(
    val averageRank: Int? = null,
    val characterCode: Int?,
    val maxKillings: Int?,
    val top3: Int?,
    val top3Rate: Int?,
    val totalGames: Int?,
    val usages: Int?,
    val wins: Int?,

    //additional param
    var characterName: String?,
    var matchingTeamMode: Int?,
    var WebLinkImage: String?,
    var isHead: Boolean = false,
    var headName: String?

) {
    constructor(isHead: Boolean, headName: String) :
            this(
                null, null, null, null, null,
                null, null, null, null, null,
                null, isHead, headName
            )
}