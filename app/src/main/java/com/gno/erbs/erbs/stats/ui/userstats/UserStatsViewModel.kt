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

    fun loadUserStats(userNumber: String, seasonId: String) {

        scope.launch {
            val characters = DataRepository.getCharacters()
            val userStats = DataRepository.getUserStats(userNumber, seasonId, characters)
            userStatsLiveData.postValue(userStats)
            userCharactersStatsLiveData.postValue(DataRepository.getUserCharactersStats(userStats))
            userGamesLiveData.postValue(DataRepository.getUserGames(userNumber, userStats))
        }

    }
}
