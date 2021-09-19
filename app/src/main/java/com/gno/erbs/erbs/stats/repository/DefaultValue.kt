package com.gno.erbs.erbs.stats.repository

import android.content.Context
import android.content.SharedPreferences
import com.gno.erbs.erbs.stats.di.SharedPreferencesValue
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultValue @Inject constructor(
    @SharedPreferencesValue private val sharedPreferencesValue: SharedPreferences
) {

    fun getDataVersion(): Int {
        return sharedPreferencesValue
            .getInt("DATA_VERSION", 0)
    }

    fun setDataVersion(value: Int) {
        sharedPreferencesValue.edit()
            .putInt("DATA_VERSION", value)
            .apply()
    }

    fun getSeasonId(): String {
        return getValue("SEASON_ID", "0")
    }

    fun setSeasonId(value: String) {
        setValue("SEASON_ID", value)
    }

    fun getMatchingTeamMode(): String {
        return getValue("MATCHING_TEAM_MODE", "1")
    }

    fun setMatchingTeamMode(value: String) {
        setValue("MATCHING_TEAM_MODE", value)
    }

    fun getUserNumber(): String {
        return getValue("GET_USER_NUMBER", "0")
    }

    fun setUserNumber(value: String) {
        setValue("GET_USER_NUMBER", value)
    }

    private fun getValue(key: String, defValue: String) =
        sharedPreferencesValue.getString(key, defValue)
            ?: defValue


    private fun setValue(key: String, value: String) {
        sharedPreferencesValue.edit().putString(key, value)
            .apply()
    }

    fun setDefaultValues() {
        setSeasonId("5")
        setMatchingTeamMode("1")
        setUserNumber("1")
    }


}

