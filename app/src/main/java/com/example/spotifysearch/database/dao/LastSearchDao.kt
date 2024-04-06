package com.example.spotifysearch.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.spotifysearch.model.database.LastSearchItem

@Dao
interface LastSearchDao {
    @Query("SELECT * FROM last_search")
    suspend fun getLastSearch(): MutableList<LastSearchItem>

    @Insert
    suspend fun insert(lastSearch: LastSearchItem)

    @Delete
    suspend fun delete(lastSearch: LastSearchItem)

    @Update
    suspend fun update(lastSearch: LastSearchItem)
}