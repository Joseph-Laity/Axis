package com.example.finalproject.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Singleton object responsible for creating and managing
 * the Retrofit networking instance used throughout the app.
 *
 * Retrofit is used to communicate with the Apex Legends REST API.
 * A singleton pattern is used so only one Retrofit instance
 * exists during the application's lifecycle.
 */
object RetrofitInstance {

    /**
     * Base URL for the unofficial Apex Legends API.
     *
     * All endpoint paths defined inside ApexApiService
     * are appended to this URL automatically by Retrofit.
     */
    private const val BASE_URL = "https://api.mozambiquehe.re/"

    /**
     * Lazily initialized API service instance.
     *
     * lazy ensures Retrofit is only created when first needed,
     * improving startup performance and reducing memory usage.
     */
    val api: ApexApiService by lazy {

        Retrofit.Builder()

            /**
             * Sets the base URL for all API requests.
             */
            .baseUrl(BASE_URL)

            /**
             * Converts JSON responses into Kotlin objects
             * using the Gson serialization library.
             */
            .addConverterFactory(GsonConverterFactory.create())

            /**
             * Builds the Retrofit instance and creates
             * the API service implementation automatically.
             */
            .build()
            .create(ApexApiService::class.java)
    }
}