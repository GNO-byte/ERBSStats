package com.gno.erbs.erbs.stats.di.component

import android.content.Context
import com.gno.erbs.erbs.stats.ui.SplashScreenActivity
import com.gno.erbs.erbs.stats.di.ActivityContext
import com.gno.erbs.erbs.stats.di.module.ActivityModule
import com.gno.erbs.erbs.stats.di.module.FragmentModule
import com.gno.erbs.erbs.stats.ui.MainActivity
import com.gno.erbs.erbs.stats.ui.top.TopFragment
import com.gno.erbs.erbs.stats.ui.top.TopViewModel
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(modules = [FragmentModule::class])
interface FragmentComponent {

    fun inject(topFragment: TopFragment)

    @Subcomponent.Builder
    interface Builder {
        fun build(): FragmentComponent
    }
}