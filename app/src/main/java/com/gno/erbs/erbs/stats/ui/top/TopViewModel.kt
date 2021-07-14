package com.gno.erbs.erbs.stats.ui.top

import androidx.lifecycle.MutableLiveData
import com.gno.erbs.erbs.stats.model.erbs.rank.Rank
import com.gno.erbs.erbs.stats.repository.DataRepository
import com.gno.erbs.erbs.stats.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class TopViewModel : BaseViewModel() {
    val ranksLiveData = MutableLiveData<List<Rank>>()

    fun getTopRanks(seasonId: String, teamMode: String) {
        scope.launch {
            ranksLiveData.postValue(DataRepository.getTopRanks(seasonId, teamMode))
        }
    }
}