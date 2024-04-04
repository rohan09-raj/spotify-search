package com.example.spotifysearch.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Followers(
    @SerializedName("total")
    val total: Int
)