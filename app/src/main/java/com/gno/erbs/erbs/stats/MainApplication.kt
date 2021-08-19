package com.gno.erbs.erbs.stats

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gno.erbs.erbs.stats.repository.DataRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainApplication : Application() {

    val isInitialized = MutableLiveData<Boolean>()

    override fun onCreate() {
        super.onCreate()
        init()
    }

    private fun init() {
        GlobalScope.launch {
            DataRepository(applicationContext)
            isInitialized.postValue(true)
        }


    }
}