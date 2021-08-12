package com.gno.erbs.erbs.stats.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gno.erbs.erbs.stats.R
import com.gno.erbs.erbs.stats.model.MenuObject
import com.gno.erbs.erbs.stats.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class HomeViewModel : BaseViewModel() {
    val menuObjectLiveData = MutableLiveData<List<MenuObject>?>()

    init {
        scope.launch {
            menuObjectLiveData.postValue(
                listOf(
                    MenuObject("Top", R.id.nav_top),
                    MenuObject("Characters",R.id.nav_characters)
                    ))
        }
    }
}