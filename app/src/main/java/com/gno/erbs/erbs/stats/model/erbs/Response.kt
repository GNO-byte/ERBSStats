package com.gno.erbs.erbs.stats.model.erbs

import com.google.gson.annotations.SerializedName

data class Response<T>(
    val code: Int,
    val message: String,
    @SerializedName(value="topRanks", alternate= ["userRank", "users", "userStats","userGames","data","user"])
    val result: T?,
    val next: String?
)