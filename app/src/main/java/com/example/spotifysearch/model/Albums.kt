package com.example.spotifysearch.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Albums(
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
    val items: List<Album>
)

@Keep
data class Album(
    @SerializedName("id")
    val id: String,
    @SerializedName("album_type")
    val albumType: String,
    @SerializedName("total_tracks")
    val totalTracks: Int,
    @SerializedName("images")
    val images: List<Images>,
    @SerializedName("name")
    val name: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("artists")
    val artists: List<Artist>
)