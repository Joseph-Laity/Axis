package com.example.finalproject.repository

import android.util.Log
import com.example.finalproject.model.Player
import com.example.finalproject.network.NetworkResult
import com.example.finalproject.network.RetrofitInstance
import retrofit2.HttpException
import java.io.IOException
import java.util.Locale

/**
 * Repository layer responsible for handling all player data operations.
 *
 * In the MVVM architecture, the repository acts as the single source
 * of truth between the ViewModel and the data layer.
 *
 * Responsibilities:
 * - Makes API requests
 * - Parses and cleans API responses
 * - Handles network errors
 * - Converts raw API data into Player objects used by the UI
 */
class PlayerRepository {

    /**
     * API authentication key used for requests to the
     * unofficial Apex Legends API.
     *
     * NOTE:
     * In production applications, API keys should never
     * be hardcoded directly in source files.
     */
    private val API_KEY = "a096f4630c367115432a19c942d38da2"

    /**
     * Fetches player statistics from the API.
     *
     * Returns:
     * - Success -> Player data
     * - Failure -> Error information
     */
    suspend fun getPlayer(username: String): NetworkResult<Player> {

        return try {

            Log.d("API_DEBUG", "Fetching stats for: $username")

            /**
             * Sends API request using Retrofit.
             */
            val response = RetrofitInstance.api.getPlayerStats(
                apiKey = API_KEY,
                username = username,
                platform = "PC",
                merge = "1",
                version = "5"
            )

            /**
             * Global contains account/rank information.
             */
            val global = response.global
                ?: return NetworkResult.Failure(Exception("NO_DATA"))

            /**
             * Total contains lifetime statistics.
             */
            val total = response.total

            /**
             * Logs all available stat keys returned by the API.
             *
             * The unofficial API does not always return consistent
             * key names, so logging helps identify missing or renamed stats.
             */
            total?.keys?.forEach {
                Log.d("API_DEBUG", "Available key in total: $it")
            }

            /**
             * Helper function used to safely retrieve stats
             * using multiple possible API key names.
             *
             * The API is inconsistent between accounts and platforms,
             * so multiple fallback keys are checked.
             */
            fun getStat(
                possibleKeys: List<String>,
                default: String,
                isDecimal: Boolean = false
            ): String {

                for (key in possibleKeys) {

                    /**
                     * Case-insensitive key lookup.
                     */
                    val entry = total?.entries?.find {
                        it.key.equals(key, ignoreCase = true)
                    }

                    val stat = entry?.value ?: continue

                    /**
                     * First attempts to use formatted display values
                     * directly from the API.
                     */
                    val display = stat.displayValue?.trim()

                    /**
                     * API sometimes returns invalid hidden values such as -1.
                     * These are filtered out before displaying to users.
                     */
                    if (
                        !display.isNullOrBlank() &&
                        display != "-1" &&
                        display != "-1.0" &&
                        display != "-1.00"
                    ) {

                        /**
                         * Formats decimal stats such as K/D ratio.
                         */
                        if (isDecimal) {

                            val floatVal = display
                                .replace(",", "")
                                .toFloatOrNull()

                            if (floatVal != null) {
                                return String.format(
                                    Locale.US,
                                    "%.1f",
                                    floatVal
                                )
                            }
                        }

                        return display
                    }

                    /**
                     * Fallback to raw numeric value if displayValue is missing.
                     */
                    val value = stat.value ?: continue

                    /**
                     * API uses negative values to indicate missing data.
                     */
                    if (value <= -1.0) continue

                    return if (isDecimal) {

                        String.format(Locale.US, "%.1f", value)

                    } else {

                        value.toLong().toString()
                    }
                }

                /**
                 * Returns default value if no matching stat exists.
                 */
                return default
            }

            // -------------------------------------------------
            // Rank Parsing
            // -------------------------------------------------

            /**
             * Rank data is nested and inconsistent depending
             * on whether the player has ranked data available.
             */
            val rank = global.rank

            val rankStr =
                if (
                    rank != null &&
                    !rank.rankName.isNullOrBlank() &&
                    rank.rankName != "None"
                ) {

                    /**
                     * Attempts to retrieve rank division safely.
                     */
                    val div = try {

                        val d = rank.rankDiv

                        if (d != null && !d.isJsonNull) {
                            d.asString
                        } else {
                            ""
                        }

                    } catch (e: Exception) {
                        ""
                    }

                    /**
                     * Prevents displaying divisions for unranked users.
                     */
                    if (rank.rankName == "Unranked") {
                        "Unranked"
                    } else {
                        "${rank.rankName} $div".trim()
                    }

                } else {
                    "Unranked"
                }

            // -------------------------------------------------
            // Stat Extraction
            // -------------------------------------------------

            /**
             * Multiple fallback keys are checked because
             * the API may return different tracker names
             * depending on the player's profile configuration.
             */
            val kills = getStat(
                listOf(
                    "kills",
                    "br_kills",
                    "arena_kills",
                    "specialEvent_kills"
                ),
                "0"
            )

            val wins = getStat(
                listOf(
                    "wins",
                    "br_wins",
                    "games_won",
                    "top3"
                ),
                "0"
            )

            val damage = getStat(
                listOf(
                    "damage",
                    "br_damage",
                    "specialEvent_damage"
                ),
                "0"
            )

            /**
             * K/D ratio formatted to one decimal place.
             */
            val kd = getStat(
                listOf(
                    "kd",
                    "kdr",
                    "kill_death_ratio"
                ),
                "0.0",
                true
            )

            /**
             * Win rate may not always include the percent symbol,
             * so one is appended if necessary.
             */
            val winRateRaw = getStat(
                listOf(
                    "winrate",
                    "win_rate",
                    "br_winrate"
                ),
                "0%"
            )

            val winRate =
                if (
                    winRateRaw != "0%" &&
                    !winRateRaw.contains("%")
                ) {
                    "$winRateRaw%"
                } else {
                    winRateRaw
                }

            // -------------------------------------------------
            // Final Player Object
            // -------------------------------------------------

            /**
             * Converts raw API data into the Player model
             * used by the UI layer.
             */
            val player = Player(
                username = global.name ?: username,
                kd = kd,
                wins = wins,
                kills = kills,
                winRate = winRate,
                damage = damage,
                rank = rankStr
            )

            Log.d("API_DEBUG", "Final Parsed Player: $player")

            /**
             * Returns successful result.
             */
            NetworkResult.Success(player)

        } catch (e: IOException) {

            /**
             * Usually triggered by no internet connection
             * or failed network communication.
             */
            Log.e("API_ERROR", "Network error: ${e.message}")

            NetworkResult.Failure(Exception("NETWORK"))

        } catch (e: HttpException) {

            /**
             * Triggered when server returns HTTP errors
             * such as 404, 403, or 500.
             */
            Log.e("API_ERROR", "HTTP error: ${e.code()}")

            NetworkResult.Failure(Exception("HTTP_${e.code()}"))

        } catch (e: Exception) {

            /**
             * Catch-all fallback for unexpected errors.
             */
            Log.e("API_ERROR", "General error: ${e.message}", e)

            NetworkResult.Failure(e)
        }
    }
}