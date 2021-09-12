package com.gno.erbs.erbs.stats.model.erbs.rank

import com.google.gson.annotations.SerializedName

data class Rank(
    val mmr: Int,
    val nickname: String,
    @SerializedName("rank")
    val rankNumber: Int,
    val userNum: Int,

    var rankTierImageWebLink: String?
)