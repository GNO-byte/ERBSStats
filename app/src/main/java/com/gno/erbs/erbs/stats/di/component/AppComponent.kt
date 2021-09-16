package com.gno.erbs.erbs.stats.di.component

import android.content.Context
import com.gno.erbs.erbs.stats.MainApplication
import com.gno.erbs.erbs.stats.di.module.AppModule
import com.gno.erbs.erbs.stats.di.module.NetworkModule
import com.gno.erbs.erbs.stats.ui.top.TopFragment
import dagger.BindsInstance
import dagger.Component

@Component(modules = [AppModule::class,NetworkModule::class])
interface AppComponent {

    fun inject(MainApplication: MainApplication)

    fun activityComponent(): ActivityComponent.Builder

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(context: Context): Builder

        fun build(): AppComponent
    }
}