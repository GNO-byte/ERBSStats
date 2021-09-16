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
    seasonId: String?,
    teamMode: String?,
    context: Context
) : ViewModel() {

    val ranksLiveData = MutableLiveData<List<Rank>?>()


    private var topList: List<Rank>? = null
    var currentList = mutableListOf<Rank>()
    var loading = false

    init {
        loadTopRanks(seasonId, teamMode, context)
    }

    fun loadTopRanks(seasonId: String?, teamMode: String?, context: Context) {

        val seasonId = seasonId ?: DataRepository.getDefaultSeasonId(context)
        val teamMode = teamMode ?: DataRepository.getDefaultMatchingTeamMode(context)

        if (topList == null) {
            viewModelScope.launch(Dispatchers.IO) {
                currentList = mutableListOf()
                topList = DataRepository.getTopRanks(seasonId, teamMode)
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

        return Season.findById(DataRepository.getDefaultSeasonId(context))

    }

    fun changeSeason(seasonName: String, context: Context) {

        Season.findByTitle(seasonName)?.let {
            DataRepository.setDefaultSeasonId(context, it.id)
            topList = null
            loadTopRanks(it.id, null, context)
        }
    }

    class TopViewModelFactory @AssistedInject constructor(
        @Assisted("seasonId") private val seasonId: String?,
        @Assisted("teamMode") private val teamMode: String?,
        @ActivityContext private val context: Context
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            require(modelClass == TopViewModel::class.java)
            return TopViewModel(seasonId, teamMode, context) as T
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
