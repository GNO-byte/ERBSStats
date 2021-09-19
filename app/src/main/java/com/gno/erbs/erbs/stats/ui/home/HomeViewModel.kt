package com.gno.erbs.erbs.stats.ui.home

import androidx.lifecycle.*
import com.gno.erbs.erbs.stats.R
import com.gno.erbs.erbs.stats.model.MenuObject
import com.gno.erbs.erbs.stats.repository.DataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HomeViewModel(dataRepository: DataRepository) : ViewModel() {


    val menuObjectLiveData: LiveData<List<MenuObject>?> = liveData {
        emit(
            listOf(
                MenuObject("Top", R.id.nav_top),
                MenuObject("Characters", R.id.nav_characters)
            )
        )
    }
    val illustrationLiveData: LiveData<List<String>?> = liveData {

        withContext(Dispatchers.IO) {
            emit(dataRepository.getIllustrations())
        }

    }

    class HomeViewModelFactory @Inject constructor(
        private val dataRepository: DataRepository
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            require(modelClass == HomeViewModel::class.java)
            return HomeViewModel(dataRepository) as T
        }
    }
}