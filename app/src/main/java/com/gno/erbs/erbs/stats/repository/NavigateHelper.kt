package com.gno.erbs.erbs.stats.repository

import android.content.Context
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.gno.erbs.erbs.stats.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.Dispatcher

object NavigateHelper {

    fun go(navController: NavController,navId: Int, bundle: Bundle?=null) {
        navController.navigate(navId, bundle)
        GlobalScope.launch(Dispatchers.IO) {

            DataRepository.addHistory(navId,bundle)
        }
    }
}