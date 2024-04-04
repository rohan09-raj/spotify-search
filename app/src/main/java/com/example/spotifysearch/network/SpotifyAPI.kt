package com.example.spotifysearch.network

import android.provider.SyncStateContract
import com.example.spotifysearch.model.SearchResponse
import com.example.spotifysearch.model.TokenResponse
import com.example.spotifysearch.utils.Constants
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface SpotifyAPI {

    companion object{
        const val BASE_URL = Constants.SPOTIFY_API_URL
    }

    @Headers("Accept: application/json")
    @POST(Constants.SPOTIFY_ACCOUNTS_URL + "api/token")
    @FormUrlEncoded
    suspend fun getAccessToken(
        @Field("grant_type") grantType: String,
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String
    ): TokenResponse

    @Headers("Accept: application/json")
    @GET("/search")
    suspend fun getSearchResults(
        @Header("authorization") token: String,
        @Query("q") query: String,
        @Query("type") type: String,
        @Query("limit") limit: Int
    ): SearchResponse
}