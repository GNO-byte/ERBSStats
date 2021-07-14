package com.gno.erbs.erbs.stats.model

enum class TeamMode(val title: String, val value: Int) {
    SOLO("Solo",1),
    DUO("Duo",2),
    SQUAD("Squad",3);

    override fun toString(): String {
        return title
    }

    companion object{
        fun findByValue(value: Int): TeamMode {
            return TeamMode.values().first{ it.value == value }
        }

    }

}