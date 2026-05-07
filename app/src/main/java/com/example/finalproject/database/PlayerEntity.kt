package com.example.finalproject.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room database entity representing a stored Apex Legends player.
 *
 * Each object in this class becomes a row inside the "players" table.
 * The username is used as the unique primary key so duplicate players
 * are not stored multiple times.
 *
 * String types are currently used for stats because the API returns
 * formatted display values rather than strictly numeric data.
 */
@Entity(tableName = "players")
data class PlayerEntity(

    /**
     * Unique identifier for each player.
     */
    @PrimaryKey
    val username: String,

    /**
     * Total player kills retrieved from the API.
     */
    val kills: String,

    /**
     * Total player wins retrieved from the API.
     */
    val wins: String,

    /**
     * Player kill/death ratio.
     */
    val kd: String
)