package com.example.spotifysearch.model

data class SearchItem(
    val id: String,
    val image: String? = null,
    val title: String? = null,
    val type: String? = null,
    val names: List<String>? = emptyList()
)