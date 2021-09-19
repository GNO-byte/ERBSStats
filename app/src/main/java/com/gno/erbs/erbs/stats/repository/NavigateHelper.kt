package com.gno.erbs.erbs.stats.repository

import android.os.Bundle
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigateHelper @Inject constructor(
    private val dataRepository: DataRepository
) {

    fun go(navController: NavController, navId: Int, bundle: Bundle? = null) {
        navController.navigate(navId, bundle)
        GlobalScope.launch(Dispatchers.IO) {

            dataRepository.addHistory(navId, bundle)
        }
    }
}