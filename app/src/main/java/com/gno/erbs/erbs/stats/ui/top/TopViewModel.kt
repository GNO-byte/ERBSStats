package com.gno.erbs.erbs.stats.ui.top

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.gno.erbs.erbs.stats.di.ActivityContext
import com.gno.erbs.erbs.stats.model.Season
import com.gno.erbs.erbs.stats.model.erbs.rank.Rank
import com.gno.erbs.erbs.stats.repository.DataRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class TopViewModel constructor(
    private var seasonId: String?,
    private var teamMode: String?,
    private val dataRepository: DataRepository
) : ViewModel() {

    val ranksLiveData = MutableLiveData<List<Rank>?>()


    private var topList: List<Rank>? = null
    var currentList = mutableListOf<Rank>()
    var loading = false

    init {
        loadTopRanks()
    }

    fun loadTopRanks() {

        val seasonId = seasonId ?: dataRepository.getDefaultSeasonId()

        val teamMode = teamMode ?: dataRepository.getDefaultMatchingTeamMode()

        if (topList == null) {
            viewModelScope.launch(Dispatchers.IO) {
                currentList = mutableListOf()
                topList = dataRepository.getTopRanks(seasonId, teamMode)
                loadList(0, 19)
            }
        }
    }

    fun loadList(sizeFrom: Int, sizeBefore: Int) {
        topList?.let { topList ->
            val subList = topList.subList(sizeFrom, sizeBefore).toList()
            currentList.addAll(subList)
            ranksLiveData.postValue(currentList.toList())
        } ?: ranksLiveData.postValue(listOf())

    }

    fun getCurrentSeason(context: Context): Season? {
        return Season.findById(dataRepository.getDefaultSeasonId())
    }

    fun changeSeason(seasonName: String, context: Context) {

        Season.findByTitle(seasonName)?.let {
            dataRepository.setDefaultSeasonId(it.id)
            topList = null
            seasonId = it.id
            loadTopRanks()
        }
    }

    class TopViewModelFactory @AssistedInject constructor(
        @Assisted("seasonId") private val seasonId: String?,
        @Assisted("teamMode") private val teamMode: String?,
        private val dataRepository: DataRepository
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            require(modelClass == TopViewModel::class.java)
            return TopViewModel(seasonId, teamMode, dataRepository) as T
        }

        @AssistedFactory
        interface Factory {
            fun create(
                @Assisted("seasonId") seasonId: String?,
                @Assisted("teamMode") teamMode: String?,
            ): TopViewModelFactory
        }
    }

}
