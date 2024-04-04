package com.example.spotifysearch.data

import com.example.spotifysearch.model.SearchResponse
import com.example.spotifysearch.model.TokenResponse
import com.example.spotifysearch.network.SpotifyAPI
import javax.inject.Inject

class SearchDataSource @Inject constructor(
    private val spotifyAPI: SpotifyAPI
) : SearchRepository {

    override suspend fun getAccessToken(
        clientId: String,
        clientSecret: String,
        grantType: String
    ): TokenResponse {
        return spotifyAPI.getAccessToken(
            clientId = clientId,
            clientSecret = clientSecret,
            grantType = grantType
        )
    }

    override suspend fun search(
        token: String,
        query: String,
        type: String,
        limit: Int
    ): SearchResponse {
        return spotifyAPI.getSearchResults(
            token = token,
            query = query,
            type = type,
            limit = limit
        )
    }
}