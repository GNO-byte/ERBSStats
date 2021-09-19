package com.gno.erbs.erbs.stats.di.module

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.gno.erbs.erbs.stats.di.SharedPreferencesApp
import com.gno.erbs.erbs.stats.di.SharedPreferencesValue
import com.gno.erbs.erbs.stats.di.component.ActivityComponent
import com.gno.erbs.erbs.stats.repository.DataRepository
import com.gno.erbs.erbs.stats.repository.FirebaseService
import com.gno.erbs.erbs.stats.repository.room.RoomService
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Singleton

@Module(subcomponents = [ActivityComponent::class])
class AppModule {

    @Singleton
    @Provides
    fun provideDataRepositoryLiveData(
        context: Context,
        dataRepositoryBuilderFactory: DataRepository.Builder.Factory,
        firebaseServiceBuilder: FirebaseService.Builder,
        roomServiceBuilder: RoomService.Builder,
    ): LiveData<DataRepository> = liveData {
        withContext(Dispatchers.IO) {
            emit(
                dataRepositoryBuilderFactory.create(
                    firebaseServiceBuilder.build(context), roomServiceBuilder.build(context)
                ).build(context)
            )
        }
    }

    @Singleton
    @Provides
    fun provideDataRepository(liveDataRepository: LiveData<DataRepository>): DataRepository =
        checkNotNull(liveDataRepository.value)


    @Provides
    @SharedPreferencesApp
    fun provideSharedPreferencesApp(context: Context) =
        context.getSharedPreferences("VALUE", Context.MODE_PRIVATE)

    @Provides
    @SharedPreferencesValue
    fun provideSharedPreferencesValue(context: Context) =
        context.getSharedPreferences("VALUE", Context.MODE_PRIVATE)

}
