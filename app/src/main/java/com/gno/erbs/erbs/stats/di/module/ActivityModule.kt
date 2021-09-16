package com.gno.erbs.erbs.stats.di.module

import android.app.Activity
import android.content.Context
import com.gno.erbs.erbs.stats.databinding.ActivityMainBinding
import com.gno.erbs.erbs.stats.di.ActivityContext
import com.gno.erbs.erbs.stats.di.component.ActivityComponent
import com.gno.erbs.erbs.stats.di.component.FragmentComponent
import dagger.Module
import dagger.Provides

@Module(subcomponents = [FragmentComponent::class])
class ActivityModule {

    @Provides
    fun provideActivityMainBinding(@ActivityContext context: Context) =
        ActivityMainBinding.inflate((context as Activity).layoutInflater)

}
