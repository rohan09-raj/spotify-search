package com.example.spotifysearch.di

import com.example.spotifysearch.data.SearchDataSource
import com.example.spotifysearch.data.SearchRepository
import com.example.spotifysearch.network.SpotifyAPI
import com.example.spotifysearch.network.SpotifyAPI.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideSpotifyApi(retrofit: Retrofit): SpotifyAPI = retrofit.create(SpotifyAPI::class.java)

    @Provides
    @Singleton
    fun provideSearchRepository(spotifyAPI: SpotifyAPI): SearchRepository =
        SearchDataSource(spotifyAPI)
}