package com.gno.erbs.erbs.stats.ui.search

import androidx.lifecycle.MutableLiveData
import com.gno.erbs.erbs.stats.model.FoundObject
import com.gno.erbs.erbs.stats.model.FoundObjectsTypes
import com.gno.erbs.erbs.stats.repository.DataRepository
import com.gno.erbs.erbs.stats.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class SearchViewModel : BaseViewModel() {

    val foundObjectLiveData = MutableLiveData<List<FoundObject>?>()


    fun find(searchString: String) {

        scope.launch {
            val foundObjects = mutableListOf<FoundObject>()
            foundObjects.findCharacter(searchString)
            foundObjects.findPlayer(searchString)
            foundObjectLiveData.postValue(foundObjects)
        }
    }

    private suspend fun MutableList<FoundObject>.findCharacter(searchString: String) {
        val foundCharacters = DataRepository.getCharacter(searchString)
        foundCharacters?.let{ thisFoundCharacters ->
            thisFoundCharacters.forEach {
                this.add(FoundObject(it.name,FoundObjectsTypes.USER,it.code))
            }
        }
    }

    private suspend fun MutableList<FoundObject>.findPlayer(searchString: String) {
        val users = DataRepository.getUser(searchString)
        users.let{ thisUsers ->
            thisUsers.forEach {
                this.add(FoundObject(it.nickname,FoundObjectsTypes.PLAYER,it.userNum))
            }
        }
    }


}
