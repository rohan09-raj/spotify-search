package com.example.spotifysearch.data

import com.example.spotifysearch.model.SearchResponse
import com.example.spotifysearch.model.TokenResponse

interface SearchRepository {

    suspend fun getAccessToken(
        clientId: String,
        clientSecret: String,
        grantType: String
    ): TokenResponse

    suspend fun search(
        token: String,
        query: String,
        type: String,
        limit: Int
    ): SearchResponse
}