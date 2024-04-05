package com.example.spotifysearch.network.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ErrorResponse(
    @SerializedName("error")
    val error: ErrorBody? = null
)

@Keep
data class ErrorBody(
    @SerializedName("status")
    val status: Int? = null,
    @SerializedName("message")
    val message : String? = null
)
