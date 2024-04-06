package com.example.spotifysearch.data

import com.example.spotifysearch.model.Album
import com.example.spotifysearch.model.Artist
import com.example.spotifysearch.model.Playlist
import com.example.spotifysearch.model.SearchResponse
import com.example.spotifysearch.model.TokenResponse
import com.example.spotifysearch.model.Track
import com.example.spotifysearch.network.models.Resource
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    suspend fun getAccessToken(
        clientId: String,
        clientSecret: String,
        grantType: String
    ): Flow<Resource<TokenResponse>>

    suspend fun search(
        token: String,
        query: String,
        type: String,
        limit: Int? = null
    ): Flow<Resource<SearchResponse>>

    suspend fun getAlbum(
        token: String,
        id: String
    ): Flow<Resource<Album>>

    suspend fun getArtist(
        token: String,
        id: String
    ): Flow<Resource<Artist>>

    suspend fun getPlaylist(
        token: String,
        id: String
    ): Flow<Resource<Playlist>>

    suspend fun getTrack(
        token: String,
        id: String
    ): Flow<Resource<Track>>
}