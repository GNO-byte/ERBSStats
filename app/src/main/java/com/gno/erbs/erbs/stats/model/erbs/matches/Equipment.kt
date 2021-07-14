package com.gno.erbs.erbs.stats.model.erbs.matches

import com.google.gson.annotations.SerializedName

data class Equipment(
    @SerializedName("0")
    val item1Id: Int,
    @SerializedName("1")
    val item2Id: Int,
    @SerializedName("2")
    val item3Id: Int,
    @SerializedName("3")
    val item4Id: Int,
    @SerializedName("4")
    val item5Id: Int,
    @SerializedName("5")
    val item6Id: Int,

    var item1WebLink: String,
    var item2WebLink: String,
    var item3WebLink: String,
    var item4WebLink: String,
    var item5WebLink: String,
    var item6WebLink: String,

    )