package com.example.spotifysearch.network

import com.example.spotifysearch.model.Album
import com.example.spotifysearch.model.Artist
import com.example.spotifysearch.model.Playlist
import com.example.spotifysearch.model.SearchResponse
import com.example.spotifysearch.model.TokenResponse
import com.example.spotifysearch.model.Track
import com.example.spotifysearch.utils.Constants
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
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
    ): Response<TokenResponse>

    @Headers("Accept: application/json")
    @GET("search")
    suspend fun getSearchResults(
        @Header("authorization") token: String,
        @Query("q") query: String,
        @Query("type") type: String,
        @Query("limit") limit: Int?
    ): Response<SearchResponse>

    @Headers("Accept: application/json")
    @GET("albums/{id}")
    suspend fun getAlbum(
        @Header("authorization") token: String,
        @Path("id") id: String
    ): Response<Album>

    @Headers("Accept: application/json")
    @GET("artists/{id}")
    suspend fun getArtist(
        @Header("authorization") token: String,
        @Path("id") id: String
    ): Response<Artist>

    @Headers("Accept: application/json")
    @GET("artists/{id}/top-tracks")
    suspend fun getArtistTopTracks(
        @Header("authorization") token: String,
        @Path("id") id: String
    ): Response<SearchResponse>

    @Headers("Accept: application/json")
    @GET("artists/{id}/albums")
    suspend fun getArtistAlbums(
        @Header("authorization") token: String,
        @Path("id") id: String
    ): Response<SearchResponse>

    @Headers("Accept: application/json")
    @GET("artists/{id}/related-artists")
    suspend fun getRelatedArtists(
        @Header("authorization") token: String,
        @Path("id") id: String
    ): Response<SearchResponse>

    @Headers("Accept: application/json")
    @GET("playlists/{id}")
    suspend fun getPlaylist(
        @Header("authorization") token: String,
        @Path("id") id: String
    ): Response<Playlist>

    @Headers("Accept: application/json")
    @GET("tracks/{id}")
    suspend fun getTrack(
        @Header("authorization") token: String,
        @Path("id") id: String
    ): Response<Track>

}