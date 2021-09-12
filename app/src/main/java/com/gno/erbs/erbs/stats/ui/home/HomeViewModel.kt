package com.gno.erbs.erbs.stats.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gno.erbs.erbs.stats.R
import com.gno.erbs.erbs.stats.model.MenuObject
import com.gno.erbs.erbs.stats.repository.DataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    val menuObjectLiveData = MutableLiveData<List<MenuObject>?>()
    val illustrationLiveData = MutableLiveData<List<String>?>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            menuObjectLiveData.postValue(
                listOf(
                    MenuObject("Top", R.id.nav_top),
                    MenuObject("Characters", R.id.nav_characters)
                )

            )
            illustrationLiveData.postValue(DataRepository.getIllustrations())
        }
    }
}