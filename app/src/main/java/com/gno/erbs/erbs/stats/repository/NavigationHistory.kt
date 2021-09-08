package com.gno.erbs.erbs.stats.repository

import android.os.Bundle
import androidx.navigation.NavController
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gno.erbs.erbs.stats.R
import java.util.*


data class NavigationHistory(
    val date: Date,
    val bundle: Bundle? = null,
    val navigateId: Int,

    ) {
    var name = when(navigateId){
        R.id.nav_user_stats -> "User - ${bundle?.getString("name", "")}"
        R.id.nav_character_detail -> "Character - ${bundle?.getString("name", "")}"
        R.id.nav_search -> "Search  - ${bundle?.getString("searchString", "")}"
        else -> ""
    }

}

