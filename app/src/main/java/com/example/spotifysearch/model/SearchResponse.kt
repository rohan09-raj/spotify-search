package com.example.spotifysearch.model

data class SearchResponse(
    val albums: Albums,
    val artists: Artists,
    val playlists: Playlists,
    val tracks: Tracks
)