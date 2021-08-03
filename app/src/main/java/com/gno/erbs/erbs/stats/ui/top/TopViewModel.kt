package com.gno.erbs.erbs.stats.ui.top

import androidx.lifecycle.MutableLiveData
import com.gno.erbs.erbs.stats.model.erbs.rank.Rank
import com.gno.erbs.erbs.stats.repository.DataRepository
import com.gno.erbs.erbs.stats.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class TopViewModel : BaseViewModel() {
    val ranksLiveData = MutableLiveData<List<Rank>?>()

    private var topList: List<Rank>? = null


    fun getTopRanks(seasonId: String, teamMode: String) {
        if (topList == null) {
            scope.launch {
                topList = DataRepository.getTopRanks(seasonId, teamMode)
                loadList(0, 19)
            }
        }
    }

    fun loadList(sizeFrom: Int, sizeBefore: Int) {
        val currentList = ranksLiveData.value?.toMutableList() ?: mutableListOf()
        topList?.let { thisTopList ->
            val subList = thisTopList.subList(sizeFrom, sizeBefore).toList()
            currentList.addAll(subList)
            ranksLiveData.postValue(currentList.toList())
        }


    }
}