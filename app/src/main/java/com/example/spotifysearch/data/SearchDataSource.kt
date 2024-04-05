package com.example.spotifysearch.data

import com.example.spotifysearch.model.SearchResponse
import com.example.spotifysearch.model.TokenResponse
import com.example.spotifysearch.network.SpotifyAPI
import com.example.spotifysearch.network.executeRetrofitApi
import com.example.spotifysearch.network.models.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchDataSource @Inject constructor(
    private val spotifyAPI: SpotifyAPI
) : SearchRepository {

    override suspend fun getAccessToken(
        clientId: String,
        clientSecret: String,
        grantType: String
    ): Flow<Resource<TokenResponse>> {
        return flow {
            emit(Resource.Loading())
            val response = executeRetrofitApi {
                spotifyAPI.getAccessToken(
                    clientId = clientId,
                    clientSecret = clientSecret,
                    grantType = grantType
                )
            }

            when (response) {
                is Resource.Success -> {
                    emit(Resource.Success(response.data))
                }

                is Resource.Error -> {
                    emit(Resource.Error(response.errorResponse))
                }

                else -> {}
            }
        }
    }

    override suspend fun search(
        token: String,
        query: String,
        type: String,
        limit: Int?
    ): Flow<Resource<SearchResponse>> {
        return flow {
            emit(Resource.Loading())
            val response = executeRetrofitApi {
                spotifyAPI.getSearchResults(
                    token = token,
                    query = query,
                    type = type,
                    limit = limit
                )
            }

            when (response) {
                is Resource.Success -> {
                    emit(Resource.Success(response.data))
                }

                is Resource.Error -> {
                    emit(Resource.Error(response.errorResponse))
                }

                else -> {}
            }
        }
    }
}