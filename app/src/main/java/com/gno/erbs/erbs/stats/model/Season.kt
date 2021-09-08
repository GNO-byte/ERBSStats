package com.gno.erbs.erbs.stats.model

import timber.log.Timber

enum class Season(val title: String, val id: String) {
    NORMAL("Normal","0"),
    SEASON_1("Season 1","1"),
    PRE_SEASON_2("Pre Season 2","2"),
    SEASON_2("Season 2","3"),
    SEASON_3("Season 3","5");

    companion object{
        fun findByTitle(title: String) = values().find { it.title == title }
        fun findById(title: String) = values().find { it.id == title }
    }
}