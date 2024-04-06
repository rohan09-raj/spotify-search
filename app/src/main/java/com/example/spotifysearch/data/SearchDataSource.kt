package com.example.spotifysearch.data

import com.example.spotifysearch.model.Album
import com.example.spotifysearch.model.Artist
import com.example.spotifysearch.model.ArtistAlbums
import com.example.spotifysearch.model.ArtistRelatedArtists
import com.example.spotifysearch.model.ArtistTopTracks
import com.example.spotifysearch.model.Playlist
import com.example.spotifysearch.model.SearchResponse
import com.example.spotifysearch.model.TokenResponse
import com.example.spotifysearch.model.Track
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

    override suspend fun getAlbum(token: String, id: String): Flow<Resource<Album>> {
        return flow {
            emit(Resource.Loading())
            val response = executeRetrofitApi {
                spotifyAPI.getAlbum(
                    token = token,
                    id = id
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

    override suspend fun getArtist(token: String, id: String): Flow<Resource<Artist>> {
        return flow {
            emit(Resource.Loading())
            val response = executeRetrofitApi {
                spotifyAPI.getArtist(
                    token = token,
                    id = id
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

    override suspend fun getArtistTopTracks(
        token: String,
        id: String
    ): Flow<Resource<ArtistTopTracks>> {
        return flow {
            emit(Resource.Loading())
            val response = executeRetrofitApi {
                spotifyAPI.getArtistTopTracks(
                    token = token,
                    id = id
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

    override suspend fun getArtistAlbums(token: String, id: String): Flow<Resource<ArtistAlbums>> {
        return flow {
            emit(Resource.Loading())
            val response = executeRetrofitApi {
                spotifyAPI.getArtistAlbums(
                    token = token,
                    id = id
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

    override suspend fun getRelatedArtists(
        token: String,
        id: String
    ): Flow<Resource<ArtistRelatedArtists>> {
        return flow {
            emit(Resource.Loading())
            val response = executeRetrofitApi {
                spotifyAPI.getRelatedArtists(
                    token = token,
                    id = id
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

    override suspend fun getPlaylist(token: String, id: String): Flow<Resource<Playlist>> {
        return flow {
            emit(Resource.Loading())
            val response = executeRetrofitApi {
                spotifyAPI.getPlaylist(
                    token = token,
                    id = id
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

    override suspend fun getTrack(token: String, id: String): Flow<Resource<Track>> {
        return flow {
            emit(Resource.Loading())
            val response = executeRetrofitApi {
                spotifyAPI.getTrack(
                    token = token,
                    id = id
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