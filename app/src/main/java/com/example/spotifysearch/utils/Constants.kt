package com.example.spotifysearch.utils
import com.example.spotifysearch.BuildConfig

object Constants {
    const val APP_NAME = "Spotify Search"
    const val SPOTIFY_API_URL = "https://api.spotify.com/v1/"
    const val SPOTIFY_ACCOUNTS_URL = "https://accounts.spotify.com/"

    const val CLIENT_ID = BuildConfig.SPOTIFY_CLIENT_ID
    const val CLIENT_SECRET = BuildConfig.SPOTIFY_CLIENT_SECRET
    const val GRANT_TYPE = "client_credentials"

    const val OAUTH_TAG = "OAUTH_SPOTIFY"
}