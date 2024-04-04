package com.example.spotifysearch.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Playlists(
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
    val items: List<Playlist>
)

@Keep
data class Playlist(
    @SerializedName("id")
    val id: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("images")
    val images: List<Images>,
    @SerializedName("name")
    val name: String,
    @SerializedName("owner")
    val owner: Owner,
    @SerializedName("tracks")
    val tracks: TrackTotal,
    @SerializedName("type")
    val type: String
)

@Keep
data class Owner(
    @SerializedName("id")
    val id: String,
    @SerializedName("followers")
    val followers: Followers,
    @SerializedName("type")
    val type: String,
    @SerializedName("display_name")
    val displayName: String
)

@Keep
data class TrackTotal(
    @SerializedName("total")
    val total: Int
)
