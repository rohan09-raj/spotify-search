package com.example.spotifysearch.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [],
    version = 1,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {

    companion object {
        const val DB_NAME = "app_database.db"
    }
}