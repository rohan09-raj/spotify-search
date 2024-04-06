package com.example.spotifysearch.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Artists(
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
    val items: List<Artist>
)

@Keep
data class ArtistTopTracks (
    @SerializedName("tracks")
    val tracks: List<Track>
)

@Keep
data class ArtistAlbums (
    @SerializedName("items")
    val items: List<Album>
)

@Keep
data class ArtistRelatedArtists (
    @SerializedName("artists")
    val artists: List<Artist>
)

@Keep
data class Artist(
    @SerializedName("id")
    val id: String,
    @SerializedName("followers")
    val followers: Followers,
    @SerializedName("genres")
    val genres: List<String>,
    @SerializedName("images")
    val images: List<Images>,
    @SerializedName("name")
    val name: String,
    @SerializedName("type")
    val type: String
)
