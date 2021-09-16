package com.gno.erbs.erbs.stats.di.component

import android.content.Context
import com.gno.erbs.erbs.stats.ui.SplashScreenActivity
import com.gno.erbs.erbs.stats.di.ActivityContext
import com.gno.erbs.erbs.stats.di.module.ActivityModule
import com.gno.erbs.erbs.stats.ui.MainActivity
import com.gno.erbs.erbs.stats.ui.top.TopFragment
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(modules = [ActivityModule::class])
interface ActivityComponent {

    fun inject(mainActivity: MainActivity)

    fun fragmentComponent(): FragmentComponent.Builder

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun context(@ActivityContext context: Context): Builder

        fun build(): ActivityComponent

    }
}