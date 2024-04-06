package com.example.spotifysearch.model

data class ArtistDetailItem(
    val id: String,
    val index: Int,
    val image: String? = null,
    val title: String? = null,
    val names: List<String>? = emptyList()
)