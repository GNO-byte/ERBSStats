package com.gno.erbs.erbs.stats.repository

import com.gno.erbs.erbs.stats.model.aesop.item.Item
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query

interface AesopService {

    @GET("aesop/item")
    suspend fun getItem(
        @Query("code") itemCode: Int,
    ): ArrayList<Item>

    companion object {

        private const val BASE_URL = "http://api.playeternalreturn.com/"
        private var INSTANCE: AesopService? = null

        operator fun invoke(): AesopService {
            return initialize()
        }

        private fun initialize(): AesopService {

            if (INSTANCE == null) {
                INSTANCE = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create()
            }

            return INSTANCE ?: throw IllegalStateException("AesopService must be initialized")

        }
    }
}

