package com.gno.erbs.erbs.stats.ui.search

import androidx.lifecycle.*
import com.gno.erbs.erbs.stats.model.FoundObject
import com.gno.erbs.erbs.stats.model.FoundObjectsTypes
import com.gno.erbs.erbs.stats.repository.DataRepository
import com.gno.erbs.erbs.stats.ui.top.TopViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchViewModel(
    private val searchString: String,
    private val dataRepository: DataRepository
) : ViewModel() {

    val foundObjectLiveData: LiveData<List<FoundObject?>> = liveData {
        withContext(Dispatchers.IO) {
            val foundObjects = mutableListOf<FoundObject?>()
            foundObjects.findCharacter(searchString)
            foundObjects.findPlayer(searchString)
            emit(foundObjects)
        }
    }

    private fun MutableList<FoundObject?>.findCharacter(searchString: String) {
        dataRepository.getCharacter(searchString)?.let { characters ->
            characters.forEach {
                if (it.name != null && it.code != null) this.add(
                    FoundObject(
                        it.name,
                        FoundObjectsTypes.CHARACTER,
                        it.code
                    )
                )
            }
        }
    }

    private suspend fun MutableList<FoundObject?>.findPlayer(searchString: String) {
        dataRepository.getUser(searchString)?.let { user ->
            if (user.nickname != "Not Found") this.add(
                FoundObject(
                    user.nickname,
                    FoundObjectsTypes.PLAYER,
                    user.userNum
                )
            )
        } ?: this.add(null)
    }


    class SearchViewModelFactory @AssistedInject constructor(
        @Assisted("searchString") private val searchString: String,
        private val dataRepository: DataRepository
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            require(modelClass == SearchViewModel::class.java)
            return SearchViewModel(searchString,dataRepository) as T
        }

        @AssistedFactory
        interface Factory {
            fun create(
                @Assisted("searchString") searchString: String,
            ): SearchViewModelFactory
        }
    }

}
