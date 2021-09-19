package com.gno.erbs.erbs.stats.di.component

import android.content.Context
import com.gno.erbs.erbs.stats.MainApplication
import com.gno.erbs.erbs.stats.di.module.*
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AppModule::class,
        NetworkModule::class,
        BindModule::class,
        DataBaseModule::class]
)
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