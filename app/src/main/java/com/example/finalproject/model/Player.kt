package com.example.finalproject.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data model representing an Apex Legends player.
 *
 * This class serves two purposes:
 * 1. Represents player data received from the REST API.
 * 2. Stores player information locally using Room database.
 *
 * The class is annotated with @Entity so Room can generate
 * the corresponding SQLite table automatically.
 */
@Entity(tableName = "players")
data class Player(

    /**
     * Unique player identifier used as the primary key.
     *
     * Usernames are unique per platform and allow
     * cached player data to be updated instead of duplicated.
     */
    @PrimaryKey
    val username: String,

    /**
     * Player kill/death ratio.
     */
    val kd: String,

    /**
     * Total number of wins.
     */
    val wins: String,

    /**
     * Total lifetime kills.
     */
    val kills: String,

    /**
     * Overall win percentage.
     */
    val winRate: String,

    /**
     * Total lifetime damage dealt.
     */
    val damage: String,

    /**
     * Current ranked division or RP value.
     *
     * Some players may not expose ranked information,
     * so fallback values may be used by the repository.
     */
    val rank: String
)