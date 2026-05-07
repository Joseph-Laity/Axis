package com.example.finalproject.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.finalproject.model.Player

/**
 * Main Room database class for the application.
 *
 * This database stores all locally cached Player objects.
 * Room automatically generates the implementation at runtime.
 *
 * Version 1 is currently used since the schema is still in
 * the initial development phase and no migrations are required yet.
 */
@Database(
    entities = [Player::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Provides access to Player database operations.
     *
     * The DAO (Data Access Object) contains all SQL queries
     * and database interaction methods related to Player data.
     */
    abstract fun playerDao(): PlayerDao
}