package com.example.finalproject.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.finalproject.model.Player

/**
 * Data Access Object (DAO) for Player entities.
 *
 * This interface defines all database operations related
 * to player data. Room automatically generates the
 * implementation during compilation.
 */
@Dao
interface PlayerDao {

    /**
     * Inserts a player into the local database.
     *
     * REPLACE strategy is used so that if a player with the
     * same primary key already exists, the old data is replaced
     * with the newest API data.
     *
     * suspend is used because database operations should run
     * asynchronously to avoid blocking the UI thread.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayer(player: Player)

    /**
     * Retrieves a player from the database by username.
     *
     * LiveData is returned so the UI can automatically observe
     * and react to changes in player data without manually refreshing.
     */
    @Query("SELECT * FROM players WHERE username = :username LIMIT 1")
    fun getPlayer(username: String): LiveData<Player>
}