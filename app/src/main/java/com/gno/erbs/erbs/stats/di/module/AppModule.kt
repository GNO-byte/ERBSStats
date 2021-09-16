package com.gno.erbs.erbs.stats.di.module

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.gno.erbs.erbs.stats.di.component.ActivityComponent
import dagger.Module
import dagger.Provides

@Module(subcomponents = [ActivityComponent::class])
class AppModule {
    @Provides
    fun provideBooleanMutableLiveData() = MutableLiveData<Boolean>()
}
