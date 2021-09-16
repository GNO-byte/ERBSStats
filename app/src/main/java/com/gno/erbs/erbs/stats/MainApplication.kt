package com.gno.erbs.erbs.stats

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.gno.erbs.erbs.stats.di.component.AppComponent
import com.gno.erbs.erbs.stats.di.component.DaggerAppComponent
import com.gno.erbs.erbs.stats.repository.DataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class MainApplication : Application() {

    lateinit var appComponent: AppComponent

    @Inject
    lateinit var isInitialized: MutableLiveData<Boolean>

    override fun onCreate() {
        super.onCreate()
        appComponent =
            DaggerAppComponent.builder().context(context = this).build().also {
                it.inject(this)
            }
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

val Context.appComponent: AppComponent
    get() = when (this) {
        is MainApplication -> appComponent
        else -> this.applicationContext.appComponent
    }

