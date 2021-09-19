package com.gno.erbs.erbs.stats.di.module

import android.content.Context
import com.gno.erbs.erbs.stats.di.CoreCharactersId
import com.gno.erbs.erbs.stats.di.CoreImageLinkStructureId
import com.gno.erbs.erbs.stats.di.ERBSOkHttpClient
import com.gno.erbs.erbs.stats.repository.*
import com.gno.erbs.erbs.stats.repository.drive.DriveService
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.http.apache.ApacheHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.drive.Drive
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import timber.log.Timber
import javax.inject.Singleton
import dagger.Binds




@Module()
class NetworkModule {

    @Singleton
    @Provides
    fun provideAesopService(): AesopService = Retrofit.Builder()
        .baseUrl(AesopService.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build().create()

    ///////

    @Singleton
    @Provides
    fun provideERBSService(
        @ERBSOkHttpClient okHttpClient: OkHttpClient
    ): ERBSService = Retrofit.Builder()
        .baseUrl(ERBSService.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build().create()

    @Provides
    @ERBSOkHttpClient
    fun provideERBSOkHttpClient(context: Context, keyHelper: KeysHelper): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                    .header("x-api-key", keyHelper.getApiKey(context))
                val request = requestBuilder.build()
                chain.proceed(request)
            }.addInterceptor { chain ->
                val request = chain.request()
                var response = chain.proceed(request)
                var tryCount = 0
                while (!response.isSuccessful && tryCount < 3) {
                    Timber.e("Request is not successful - $tryCount")
                    tryCount++
                    response.close()
                    response = chain.proceed(request)
                }
                response
            }.dispatcher(
                Dispatcher().apply
                { maxRequestsPerHost = 1 }
            ).build()

    ///////
    @Provides
    fun provideFirebaseStorage(): FirebaseStorage = FirebaseStorage.getInstance()

    @Provides
    fun provideRealtimeDatabase(): FirebaseDatabase = FirebaseDatabase.getInstance()

    ///////
    @Provides
    @CoreImageLinkStructureId
    fun provideCoreImageLinkStructureId(context: Context, keyHelper: KeysHelper): String =
        keyHelper.getDriveCoreImageLinkStructureId(context)

    @Provides
    @CoreCharactersId
    fun provideCoreCharactersId(context: Context, keyHelper: KeysHelper): String =
        keyHelper.getDriveCoreCharactersId(context)

    @Provides
    fun provideDrive(context: Context, keyHelper: KeysHelper): Drive {

        val credential =
            GoogleCredential.fromStream(keyHelper.getDriveKey(context).byteInputStream())
                .createScoped(listOf("https://www.googleapis.com/auth/drive"))

        return Drive.Builder(ApacheHttpTransport(), JacksonFactory.getDefaultInstance(), credential)
            .setApplicationName("ERBS")
            .build()

    }

}
