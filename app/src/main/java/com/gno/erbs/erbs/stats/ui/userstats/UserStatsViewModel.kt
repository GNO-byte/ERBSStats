package com.gno.erbs.erbs.stats.ui.userstats

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.gno.erbs.erbs.stats.model.Season
import com.gno.erbs.erbs.stats.model.erbs.matches.UserGame
import com.gno.erbs.erbs.stats.model.erbs.userstats.CharacterStat
import com.gno.erbs.erbs.stats.model.erbs.userstats.UserStats
import com.gno.erbs.erbs.stats.repository.DataRepository
import com.gno.erbs.erbs.stats.ui.top.TopViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserStatsViewModel(
    private val userNumber: String?,
    private var seasonId: String?,
    private val dataRepository: DataRepository
) : ViewModel() {

    val userStatsLiveData = MutableLiveData<List<UserStats>?>()
    val userCharactersStatsLiveData = MutableLiveData<List<CharacterStat>>()
    val userGamesLiveData = MutableLiveData<List<UserGame>>()
    val updateLiveData = MutableLiveData<Boolean>()

    private var userId: String? = null
    //private val mmrMap = mutableMapOf<Int, Int>()

    private var nextPage: String? = ""
    private var downloadCompleted = false

    init {
        loadUserStats()
    }

    fun loadUserStats() {

        val userNumber = userNumber?.also {
            dataRepository.setDefaultUserNumber(it)
        } ?: dataRepository.getDefaultUserNumber()

        val seasonId = seasonId?.also {
            dataRepository.setDefaultSeasonId(it)
        } ?: dataRepository.getDefaultSeasonId()

        if (!downloadCompleted) {
            viewModelScope.launch(Dispatchers.IO) {
                val characters = dataRepository.getCharacters()
                val userStats = dataRepository.getUserStats(userNumber, seasonId, characters)
                userStatsLiveData.postValue(userStats?.sortedBy {
                    it.matchingTeamMode
                })

                userStats?.let {
                    userCharactersStatsLiveData.postValue(
                        dataRepository.getUserCharactersStats(
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
            val responseUserGame = dataRepository.getUserGames(thisUserId, nextPage ?: "")
            nextPage = responseUserGame?.next
            matches.addAll(responseUserGame?.result?.toList() ?: listOf())
            userGamesLiveData.postValue(matches.toList())
        }
    }

    fun getCurrentSeason() =
        Season.findById(dataRepository.getDefaultSeasonId())

    fun changeSeason(seasonName: String) {
        Season.findByTitle(seasonName)?.let {
            updateLiveData.postValue(true)
            dataRepository.setDefaultSeasonId(it.id)

            userId = null

            nextPage = ""
            downloadCompleted = false
            seasonId = seasonId


        }
    }

    class UserStatsViewModelFactory @AssistedInject constructor(
        @Assisted("userNumber") private val userNumber: String?,
        @Assisted("seasonId") private val seasonId: String?,
        private val dataRepository: DataRepository
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            require(modelClass == UserStatsViewModel::class.java)
            return UserStatsViewModel(userNumber, seasonId, dataRepository) as T
        }

        @AssistedFactory
        interface Factory {
            fun create(
                @Assisted("userNumber") userNumber: String?,
                @Assisted("seasonId") seasonId: String?
            ): UserStatsViewModelFactory
        }
    }

}

