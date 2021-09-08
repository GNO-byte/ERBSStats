package com.gno.erbs.erbs.stats

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gno.erbs.erbs.stats.repository.DataRepository
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber

class MainApplication : Application() {

    val isInitialized = MutableLiveData<Boolean>()

    override fun onCreate() {
        super.onCreate()
        init()
    }

    private fun init() {

        // init timber
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        GlobalScope.launch(Dispatchers.Default) {

            DataRepository(applicationContext)
            DataRepository.initData(applicationContext)
            isInitialized.postValue(true)
        }

    }
}