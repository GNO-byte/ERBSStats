package com.gno.erbs.erbs.stats.di.module

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.gno.erbs.erbs.stats.di.component.ActivityComponent
import com.gno.erbs.erbs.stats.repository.AesopService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module()
class NetworkModule{

    @Singleton
    fun provideAesopService(): AesopService =  Retrofit.Builder()
            .baseUrl(AesopService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create()


}
