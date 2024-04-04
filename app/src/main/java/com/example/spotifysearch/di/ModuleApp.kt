package com.example.spotifysearch.di

import android.content.Context
import androidx.room.Room
import com.example.spotifysearch.database.AppDatabase
import com.example.spotifysearch.network.SpotifyAPI
import com.example.spotifysearch.network.SpotifyAPI.Companion.BASE_URL
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
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideSpotifyApi(retrofit: Retrofit): SpotifyAPI = retrofit.create(SpotifyAPI::class.java)

    @Singleton
    @Provides
    fun providesAppDatabase(@ApplicationContext context: Context): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        AppDatabase.DB_NAME
    ).build()
}