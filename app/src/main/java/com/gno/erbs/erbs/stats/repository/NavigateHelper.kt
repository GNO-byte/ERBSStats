package com.gno.erbs.erbs.stats.repository

import android.os.Bundle
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object NavigateHelper {

    fun go(navController: NavController, navId: Int, bundle: Bundle? = null) {
        navController.navigate(navId, bundle)
        GlobalScope.launch(Dispatchers.IO) {

            DataRepository.addHistory(navId, bundle)
        }
    }
}