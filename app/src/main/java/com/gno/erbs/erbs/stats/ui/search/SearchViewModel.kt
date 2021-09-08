package com.gno.erbs.erbs.stats.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gno.erbs.erbs.stats.model.FoundObject
import com.gno.erbs.erbs.stats.model.FoundObjectsTypes
import com.gno.erbs.erbs.stats.repository.DataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {

    val foundObjectLiveData = MutableLiveData<List<FoundObject?>>()
    private var downloadCompleted = false

    fun find(searchString: String) {
        if (!downloadCompleted) {
            viewModelScope.launch(Dispatchers.IO) {
                val foundObjects = mutableListOf<FoundObject?>()
                foundObjects.findCharacter(searchString)
                foundObjects.findPlayer(searchString)
                foundObjectLiveData.postValue(foundObjects)
                downloadCompleted = true
            }
        }
    }

    private fun MutableList<FoundObject?>.findCharacter(searchString: String) {
        DataRepository.getCharacter(searchString)?.let { characters ->
            characters.forEach {
                this.add(FoundObject(it.name, FoundObjectsTypes.CHARACTER, it.code))
            }
        }
    }

    private suspend fun MutableList<FoundObject?>.findPlayer(searchString: String) {
        DataRepository.getUser(searchString)?.let { user ->
            if (user.nickname != "Not Found") this.add(
                FoundObject(
                    user.nickname,
                    FoundObjectsTypes.PLAYER,
                    user.userNum
                )
            )
        } ?: this.add(null)
    }


}
