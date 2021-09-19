package com.gno.erbs.erbs.stats.di

import com.gno.erbs.erbs.stats.repository.FirebaseService
import com.gno.erbs.erbs.stats.repository.ImageService
import com.gno.erbs.erbs.stats.repository.drive.DriveService
import dagger.Binds
import javax.inject.Qualifier


@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ActivityContext

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ERBSOkHttpClient

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class CoreImageLinkStructureId

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class CoreCharactersId

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Drive

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Firebase

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class SharedPreferencesValue

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class SharedPreferencesApp

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Iso8601Format


