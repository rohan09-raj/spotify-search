package com.example.spotifysearch.network.models

sealed class Resource<out T> {
    data class Loading<out T>(val data: T? = null) : Resource<T>()
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error<out T>(
        val errorResponse: ErrorResponse
    ) : Resource<T>() {

        constructor(status: Int? = null, message: String? = null) : this(
            errorResponse = ErrorResponse(
                ErrorBody(
                    status = status,
                    message = message
                )
            )
        )
    }

    fun isLoading(): Boolean {
        return this is Loading
    }

    fun isSuccess(): Boolean {
        return this is Success
    }

    fun isError(): Boolean {
        return this is Error
    }
}