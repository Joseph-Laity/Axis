package com.example.finalproject.network

import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Query

// ----------------------
// RESPONSE MODELS
// ----------------------

/**
 * Root response object returned from the Apex Legends API.
 *
 * The API returns player information in multiple sections.
 * "global" contains general account/rank information while
 * "total" contains overall lifetime statistics.
 */
data class ApexResponse(

    /**
     * General player account information.
     */
    @SerializedName("global")
    val global: Global?,

    /**
     * Collection of lifetime player statistics.
     *
     * Stats are returned dynamically as key-value pairs
     * rather than fixed JSON properties.
     */
    @SerializedName("total")
    val total: Map<String, StatValue>?
)

/**
 * Stores overall account and ranking information.
 */
data class Global(

    /**
     * Player username.
     */
    @SerializedName("name")
    val name: String?,

    /**
     * Player account level.
     */
    @SerializedName("level")
    val level: Int?,

    /**
     * Battle Royale ranked information.
     */
    @SerializedName("rank")
    val rank: Rank?,

    /**
     * Arenas ranked information.
     */
    @SerializedName("arena")
    val arena: Rank?
)

/**
 * Represents ranked data returned by the API.
 */
data class Rank(

    /**
     * Rank tier name.
     *
     * Examples:
     * - Bronze
     * - Gold
     * - Diamond
     * - Predator
     */
    @SerializedName("rankName")
    val rankName: String?,

    /**
     * Rank division number.
     *
     * JsonElement is used because the API sometimes returns
     * this value as either a number or null depending on rank.
     */
    @SerializedName("rankDiv")
    val rankDiv: JsonElement?,

    /**
     * Ranked points (RP/AP).
     */
    @SerializedName("rankScore")
    val rankScore: Int?,

    /**
     * URL for the rank badge image.
     */
    @SerializedName("rankImg")
    val rankImg: String?
)

/**
 * Represents an individual player statistic.
 *
 * The API provides both raw numeric values and
 * pre-formatted display values for UI presentation.
 */
data class StatValue(

    /**
     * Raw numeric value of the statistic.
     */
    @SerializedName("value")
    val value: Double?,

    /**
     * Human-readable formatted stat value.
     */
    @SerializedName("displayValue")
    val displayValue: String?
)

// ----------------------
// API SERVICE
// ----------------------

/**
 * Retrofit API service used to communicate with the
 * unofficial Apex Legends REST API.
 *
 * This interface defines all HTTP requests used by the app.
 * Retrofit automatically generates the implementation.
 */
interface ApexApiService {

    /**
     * Retrieves player statistics from the API.
     *
     * Endpoint:
     * GET /bridge
     *
     * Query Parameters:
     * - auth: API authentication key
     * - player: Apex username
     * - platform: Gaming platform
     * - merge: Combines duplicate stat trackers
     * - version: API response version
     *
     * Default platform is set to PC because the application
     * currently targets PC Apex players.
     */
    @GET("bridge")
    suspend fun getPlayerStats(

        /**
         * Developer API authentication key.
         */
        @Query("auth") apiKey: String,

        /**
         * Username of the player being searched.
         */
        @Query("player") username: String,

        /**
         * Player platform.
         *
         * Supported values:
         * - PC
         * - PS4
         * - X1
         */
        @Query("platform") platform: String = "PC",

        /**
         * Merges duplicate stat trackers into a single value.
         */
        @Query("merge") merge: String = "1",

        /**
         * API response version.
         */
        @Query("version") version: String = "5"
    ): ApexResponse
}