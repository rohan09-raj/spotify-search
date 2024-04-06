package com.example.spotifysearch.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.spotifysearch.database.dao.LastSearchDao
import com.example.spotifysearch.model.database.LastSearchItem

@Database(
    entities = [LastSearchItem::class],
    version = 1,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun getLastSearchDao(): LastSearchDao

    companion object {
        const val DB_NAME = "app_database.db"
    }
}