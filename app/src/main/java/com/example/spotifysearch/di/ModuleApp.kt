package com.example.spotifysearch.di

import android.content.Context
import androidx.room.Room
import com.example.spotifysearch.data.SearchDataSource
import com.example.spotifysearch.data.SearchRepository
import com.example.spotifysearch.database.AppDatabase
import com.example.spotifysearch.network.SpotifyAPI
import com.example.spotifysearch.preferences.SharedPreference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
        .baseUrl(SpotifyAPI.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideSpotifyApi(retrofit: Retrofit): SpotifyAPI = retrofit.create(SpotifyAPI::class.java)

    @Provides
    @Singleton
    fun provideSearchRepository(spotifyAPI: SpotifyAPI): SearchRepository =
        SearchDataSource(spotifyAPI)

    @Provides
    @Singleton
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreference =
        SharedPreference(context)

    @Singleton
    @Provides
    fun providesAppDatabase(@ApplicationContext context: Context): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        AppDatabase.DB_NAME
    ).allowMainThreadQueries().build()

    @Singleton
    @Provides
    fun providesLastSearchDao(appDatabase: AppDatabase) = appDatabase.getLastSearchDao()
}