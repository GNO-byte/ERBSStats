package com.gno.erbs.erbs.stats.ui.userstats

import androidx.lifecycle.MutableLiveData
import com.gno.erbs.erbs.stats.model.erbs.matches.UserGame
import com.gno.erbs.erbs.stats.model.erbs.userstats.CharacterStat
import com.gno.erbs.erbs.stats.model.erbs.userstats.UserStats
import com.gno.erbs.erbs.stats.repository.DataRepository
import com.gno.erbs.erbs.stats.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class UserStatsViewModel : BaseViewModel() {

    val userStatsLiveData = MutableLiveData<List<UserStats>>()
    val userCharactersStatsLiveData = MutableLiveData<List<CharacterStat>>()
    val userGamesLiveData = MutableLiveData<List<UserGame>>()

    private var userId: String? = null
    private var mmr: Int? = null
    private var nextPage: String? = ""


    fun loadUserStats(userNumber: String, seasonId: String) {

        scope.launch {
            val characters = DataRepository.getCharacters()
            val userStats = DataRepository.getUserStats(userNumber, seasonId, characters)
            userStatsLiveData.postValue(userStats)
            userCharactersStatsLiveData.postValue(DataRepository.getUserCharactersStats(userStats))
            userId = userNumber
            mmr = userStats[0].mmr
            suspendLoadMatches()
        }
    }

    fun loadMatches() {
        scope.launch {
            suspendLoadMatches()
        }
    }

    private suspend fun suspendLoadMatches() {
        val thisUserId = userId
        val thisMmr = mmr
        if (thisUserId != null && thisMmr != null) {
            val matches = userGamesLiveData.value?.toMutableList() ?: mutableListOf()
            val responseUserGame = DataRepository.getUserGames(thisUserId, thisMmr, nextPage ?: "")
            nextPage = responseUserGame?.next
            matches.addAll(responseUserGame?.result?.toList() ?: listOf())
            userGamesLiveData.postValue(matches.toList())
        }
    }
}

