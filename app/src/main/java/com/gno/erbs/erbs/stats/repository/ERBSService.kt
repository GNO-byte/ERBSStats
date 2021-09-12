package com.gno.erbs.erbs.stats.repository

import android.content.Context
import com.gno.erbs.erbs.stats.model.erbs.Response
import com.gno.erbs.erbs.stats.model.erbs.characters.Character
import com.gno.erbs.erbs.stats.model.erbs.characters.CharacterLevelUpStat
import com.gno.erbs.erbs.stats.model.erbs.characters.SkillVideo
import com.gno.erbs.erbs.stats.model.erbs.characters.WeaponType
import com.gno.erbs.erbs.stats.model.erbs.matches.UserGame
import com.gno.erbs.erbs.stats.model.erbs.matches.item.armor.ItemArmor
import com.gno.erbs.erbs.stats.model.erbs.matches.item.consumable.ItemConsumable
import com.gno.erbs.erbs.stats.model.erbs.matches.item.misc.ItemMisc
import com.gno.erbs.erbs.stats.model.erbs.matches.item.special.ItemSpecial
import com.gno.erbs.erbs.stats.model.erbs.matches.item.weapon.ItemWeapon
import com.gno.erbs.erbs.stats.model.erbs.nickname.User
import com.gno.erbs.erbs.stats.model.erbs.rank.Rank
import com.gno.erbs.erbs.stats.model.erbs.userstats.UserStats
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import timber.log.Timber

interface ERBSService {

    @GET("v1/rank/top/{seasonId}/{matchingTeamMode}")
    suspend fun getTopRanksResponse(
        @Path("seasonId") seasonId: String,
        @Path("matchingTeamMode") teamMode: String,
    ): Response<List<Rank>>

    @GET("v1/user/stats/{userNum}/{seasonId}")
    suspend fun getUserStatsResponse(
        @Path("userNum") userNumber: String,
        @Path("seasonId") seasonId: String,
    ): Response<List<UserStats>>

    @GET("/v1/user/nickname")
    suspend fun getUserResponse(
        @Query("query") userName: String
    ): Response<User>

    @GET("/v1/rank/{userNum}/{seasonId}/{matchingTeamMode}")
    suspend fun getTopUserRanksResponse(
        @Path("userNum") userNumber: String,
        @Path("seasonId") seasonId: String,
        @Path("matchingTeamMode") teamMode: String,
    ): Response<List<Rank>>

    @GET("v1/user/games/{userNum}")
    suspend fun getUserGamesResponse(
        @Path("userNum") userNumber: String,
        @Query("next") nextPage: String
    ): Response<List<UserGame>>

    @GET("/v1/games/{gameId}")
    suspend fun getGameResponse(
        @Path("gameId") gameId: String
    ): Response<List<UserGame>>

    @GET("/v1/data/Character")
    suspend fun getCharacters(): Response<List<Character>>

    @GET("/v1/data/CharacterLevelUpStat")
    suspend fun getCharacterLevelUpStat(): Response<List<CharacterLevelUpStat>>

    @GET("/v1/data/CharacterAttributes")
    suspend fun getCharacterWeaponTypes(): Response<List<WeaponType>>

    @GET("/v1/data/ItemWeapon")
    suspend fun getItemsWeapon(): Response<List<ItemWeapon>>

    @GET("/v1/data/ItemArmor")
    suspend fun getItemsArmor(): Response<List<ItemArmor>>

    @GET("/v1/data/ItemConsumable")
    suspend fun getItemsConsumable(): Response<List<ItemConsumable>>

    @GET("/v1/data/ItemSpecial")
    suspend fun getItemsSpecial(): Response<List<ItemSpecial>>
    //including Ingredients

    @GET("/v1/data/ItemMisc")
    suspend fun getItemsMisc(): Response<ItemMisc>

    @GET("/v1/data/CharacterSkillVideos")
    suspend fun getCharacterSkillVideos(): Response<List<SkillVideo>>

    companion object {

        private const val BASE_URL = "https://open-api.bser.io"
        private var INSTANCE: ERBSService? = null

        operator fun invoke(context: Context): ERBSService {
            if (INSTANCE == null) {
                INSTANCE = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(createDefaultClient(KeysHelper.getApiKey(context)))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create()
            }
            return INSTANCE ?: throw IllegalStateException("ERBSService must be initialized")

        }

        private fun createDefaultClient(apiKey: String): OkHttpClient {

            val httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor { chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                    .header("x-api-key", apiKey)
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
            }.dispatcher(Dispatcher().apply { maxRequestsPerHost = 1 })

            return httpClient.build()
        }


    }

}