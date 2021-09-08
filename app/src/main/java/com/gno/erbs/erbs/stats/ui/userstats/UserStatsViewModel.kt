package com.gno.erbs.erbs.stats.ui.userstats

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gno.erbs.erbs.stats.model.Season
import com.gno.erbs.erbs.stats.model.erbs.matches.UserGame
import com.gno.erbs.erbs.stats.model.erbs.userstats.CharacterStat
import com.gno.erbs.erbs.stats.model.erbs.userstats.UserStats
import com.gno.erbs.erbs.stats.repository.DataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserStatsViewModel : ViewModel() {

    val userStatsLiveData = MutableLiveData<List<UserStats>?>()
    val userCharactersStatsLiveData = MutableLiveData<List<CharacterStat>>()
    val userGamesLiveData = MutableLiveData<List<UserGame>>()
    val updateLiveData = MutableLiveData<Boolean>()

    private var userId: String? = null
    //private val mmrMap = mutableMapOf<Int, Int>()

    private var nextPage: String? = ""
    private var downloadCompleted = false

    fun loadUserStats(userNumber: String?, seasonId: String?, context: Context) {

        val userNumber = userNumber?.also {
            setDefaultUserID(context, it)
        } ?: getDefaultUserID(context)

        val seasonId = seasonId?.also {
            DataRepository.setDefaultSeasonId(context, it)
        } ?: DataRepository.getDefaultSeasonId(context)

        if (!downloadCompleted) {
            viewModelScope.launch(Dispatchers.IO) {
                val characters = DataRepository.getCharacters()
                val userStats = DataRepository.getUserStats(userNumber, seasonId, characters)
                userStatsLiveData.postValue(userStats?.sortedBy {
                    it.matchingTeamMode
                })

                userStats?.let {
                    userCharactersStatsLiveData.postValue(
                        DataRepository.getUserCharactersStats(
                            userStats
                        )
                    )
                    userId = userNumber
                    //  if (it.isNotEmpty()) mmrMap += it[0].matchingTeamMode to it[0].mmr
                    suspendLoadMatches()
                }
                downloadCompleted = true
            }
        }
    }

    fun loadMatches() {
        viewModelScope.launch(Dispatchers.IO) {
            suspendLoadMatches()
        }
    }

    private suspend fun suspendLoadMatches() {
        val thisUserId = userId
        if (thisUserId != null) {
            val matches = userGamesLiveData.value?.toMutableList() ?: mutableListOf()
            val responseUserGame = DataRepository.getUserGames(thisUserId, nextPage ?: "")
            nextPage = responseUserGame?.next
            matches.addAll(responseUserGame?.result?.toList() ?: listOf())
            userGamesLiveData.postValue(matches.toList())
        }
    }

    fun getDefaultUserID(context: Context) = DataRepository.getDefaultUserNumber(context)

    fun setDefaultUserID(context: Context, userId: String) =
        DataRepository.setDefaultUserNumber(context, userId)

    fun getCurrentSeason(context: Context) =
        Season.findById(DataRepository.getDefaultSeasonId(context))

    fun changeSeason(seasonName: String, context: Context) {
        Season.findByTitle(seasonName)?.let {
            updateLiveData.postValue(true)
            DataRepository.setDefaultSeasonId(context, it.id)

            userId = null
            //mmrMap.clear()
            nextPage = ""
            downloadCompleted = false

            loadUserStats(
                null,
                it.id,
                context
            )
        }
    }
}

