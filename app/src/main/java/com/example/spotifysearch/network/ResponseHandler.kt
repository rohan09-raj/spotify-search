package com.example.spotifysearch.network

import com.example.spotifysearch.network.models.ErrorBody
import com.example.spotifysearch.network.models.ErrorResponse
import com.example.spotifysearch.network.models.Resource
import retrofit2.Response

suspend fun <T> executeRetrofitApi(call: suspend () -> Response<T>): Resource<T> {
    return try {
        val response = call()
        when {
            response.isSuccessful -> {
                val body = response.body()
                if (body != null) {
                    Resource.Success(body)
                } else {
                    Resource.Error(
                        ErrorResponse(
                            ErrorBody(
                                status = response.code(),
                                message = response.message()
                            )
                        )
                    )
                }
            }

            else -> {
                Resource.Error(
                    ErrorResponse(
                        ErrorBody(
                            status = response.code(),
                            message = response.message()
                        )
                    )
                )
            }
        }
    } catch (throwable: Throwable) {
        Resource.Error(
            ErrorResponse(
                ErrorBody(
                    status = throwable.hashCode(),
                    message = throwable.message
                )
            )
        )
    }
}