package com.example.spotifysearch.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Tracks(
    @SerializedName("href")
    val href: String,
    @SerializedName("limit")
    val limit: Int,
    @SerializedName("next")
    val next: String,
    @SerializedName("offset")
    val offset: Int,
    @SerializedName("previous")
    val previous: String,
    @SerializedName("total")
    val total: Int,
    @SerializedName("items")
    val items: List<Track>
)

@Keep
data class Track(
    @SerializedName("id")
    val id: String,
    @SerializedName("album")
    val album: Album,
    @SerializedName("artists")
    val artists: List<Artist>,
    @SerializedName("duration_ms")
    val durationMs: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("type")
    val type: String
)
