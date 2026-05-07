package com.example.finalproject.model

/**
 * Root API response object returned from the Apex Legends API.
 *
 * The API wraps player information inside a "data" object,
 * so this class mirrors the JSON response structure exactly.
 */
data class PlayerStats(

    /**
     * Main player data container returned by the API.
     */
    val data: PlayerData
)

/**
 * Contains player profile information and stat segments.
 *
 * Segments are grouped collections of statistics provided
 * by the API such as overall stats, ranked stats, or legends.
 */
data class PlayerData(

    /**
     * Basic account/platform information.
     */
    val platformInfo: PlatformInfo,

    /**
     * List of stat categories returned by the API.
     */
    val segments: List<Segment>
)

/**
 * Stores information about the player's account.
 */
data class PlatformInfo(

    /**
     * Public username displayed on the player's platform.
     */
    val platformUserHandle: String
)

/**
 * Represents a statistics section from the API response.
 *
 * The API returns stats as key-value pairs where the key
 * identifies the stat type and the value contains formatted data.
 */
data class Segment(

    /**
     * Collection of player statistics.
     *
     * Example keys:
     * - kills
     * - wins
     * - damage
     * - kd
     */
    val stats: Map<String, StatValue>
)

/**
 * Represents an individual stat returned by the API.
 *
 * displayValue is used because the API provides
 * already-formatted text values for display in the UI.
 */
data class StatValue(

    /**
     * Formatted stat value shown to users.
     */
    val displayValue: String
)