package com.example.spotifysearch.data

import com.example.spotifysearch.model.SearchResponse
import com.example.spotifysearch.model.TokenResponse
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
}