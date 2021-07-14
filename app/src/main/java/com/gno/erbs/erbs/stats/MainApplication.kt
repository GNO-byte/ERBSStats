package com.gno.erbs.erbs.stats

import android.app.Application
import com.gno.erbs.erbs.stats.repository.DataRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        init()
    }

    private fun init() {

        DataRepository(applicationContext)
        GlobalScope.launch {
            DataRepository.initCoreDate()
        }

    }
}