package com.gno.erbs.erbs.stats.repository

import android.content.Context

object DefaultValue {

    fun getDataVersion(context: Context): Int {
        return context.getSharedPreferences("VALUE", Context.MODE_PRIVATE)
            .getInt("DATA_VERSION", 0)
    }

    fun setDataVersion(context: Context, value: Int) {
        context.getSharedPreferences("VALUE", Context.MODE_PRIVATE).edit()
            .putInt("DATA_VERSION", value)
            .apply()
    }

    fun getSeasonId(context: Context): String {
        return getValue(context, "SEASON_ID", "0")
    }

    fun setSeasonId(context: Context, value: String) {
        setValue(context, "SEASON_ID", value)
    }

    fun getMatchingTeamMode(context: Context): String {
        return getValue(context, "MATCHING_TEAM_MODE", "1")
    }

    fun setMatchingTeamMode(context: Context, value: String) {
        setValue(context, "MATCHING_TEAM_MODE", value)
    }

    fun getUserNumber(context: Context): String {
        return getValue(context, "GET_USER_NUMBER", "0")
    }

    fun setUserNumber(context: Context, value: String) {
        setValue(context, "GET_USER_NUMBER", value)
    }

    private fun getValue(context: Context, key: String, defValue: String) =
        context.getSharedPreferences("VALUE", Context.MODE_PRIVATE).getString(key, defValue)
            ?: defValue


    private fun setValue(context: Context, key: String, value: String) {
        context.getSharedPreferences("VALUE", Context.MODE_PRIVATE).edit().putString(key, value)
            .apply()
    }

    fun setDefaultValues(context: Context) {
        setSeasonId(context, "3")
        setMatchingTeamMode(context, "1")
        setUserNumber(context, "1")
    }


}

