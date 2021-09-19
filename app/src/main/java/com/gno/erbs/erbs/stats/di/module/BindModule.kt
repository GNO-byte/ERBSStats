package com.gno.erbs.erbs.stats.di.module

import com.gno.erbs.erbs.stats.di.Drive
import com.gno.erbs.erbs.stats.di.Firebase
import com.gno.erbs.erbs.stats.repository.FirebaseService
import com.gno.erbs.erbs.stats.repository.ImageService
import com.gno.erbs.erbs.stats.repository.drive.DriveService
import dagger.Binds
import dagger.Module

@Module
abstract class BindModule {

//    @Binds
//    @Drive
//    internal abstract fun provideFirstPresenter(driveService: DriveService): ImageService

    @Binds
    internal abstract fun provideFirstPresenter(firebaseService: FirebaseService): ImageService

}