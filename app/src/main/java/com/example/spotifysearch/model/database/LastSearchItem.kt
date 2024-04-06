package com.example.spotifysearch.model.database

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "last_search", primaryKeys = ["id"])
data class LastSearchItem(
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "image")
    val image: String? = null,
    @ColumnInfo(name = "title")
    val title: String? = null,
    @ColumnInfo(name = "type")
    val type: String? = null,
    @ColumnInfo(name = "names")
    val names: String? = null
)