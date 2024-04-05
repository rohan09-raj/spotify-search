package com.example.spotifysearch.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotifysearch.data.SearchRepository
import com.example.spotifysearch.model.SearchResponse
import com.example.spotifysearch.model.TokenResponse
import com.example.spotifysearch.network.models.Resource
import com.example.spotifysearch.preferences.SharedPreference
import com.example.spotifysearch.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
internal class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository,
    private val sharedPreference: SharedPreference
) : ViewModel() {

    private val _tokenResource = MutableLiveData<Resource<TokenResponse>>()
    val tokenResource: LiveData<Resource<TokenResponse>>
        get() = _tokenResource

    private val _tokenResponse = MutableLiveData<TokenResponse>()
    val tokenResponse: LiveData<TokenResponse>
        get() = _tokenResponse

    private val _searchResultsResource = MutableLiveData<Resource<SearchResponse>>()
    val searchResultsResource: LiveData<Resource<SearchResponse>>
        get() = _searchResultsResource

    private val _searchResults = MutableLiveData<SearchResponse>()
    val searchResults: LiveData<SearchResponse>
        get() = _searchResults

    fun getAccessToken() {
        viewModelScope.launch {
            try {
                searchRepository.getAccessToken(
                    clientId = Constants.CLIENT_ID,
                    clientSecret = Constants.CLIENT_SECRET,
                    grantType = Constants.GRANT_TYPE
                ).collect { resource ->
                    _tokenResource.value = resource
                    when (resource) {
                        is Resource.Success -> {
                            _tokenResponse.value = resource.data
                            Log.d(Constants.OAUTH_TAG, "getAccessToken: ${resource.data}")

                            sharedPreference.accessToken = _tokenResponse.value?.accessToken
                            sharedPreference.tokenType = _tokenResponse.value?.tokenType
                            sharedPreference.expiresIn =
                                (Date().time / 1000) + (_tokenResponse.value?.expiresIn?.toLong()
                                    ?: 3600L)
                        }

                        is Resource.Error -> {
                            Log.d(
                                Constants.OAUTH_TAG,
                                "getAccessToken: error ${resource.errorResponse}"
                            )
                        }

                        else -> {
                        }
                    }
                }
            } catch (e: Exception) {
                Log.d(Constants.OAUTH_TAG, "getAccessToken: error $e")
            }
        }
    }

    fun getSearchResults(query: String, type: List<String>) {
        viewModelScope.launch {
            try {
                val token = "${sharedPreference.tokenType} ${sharedPreference.accessToken}"
                val types = type.joinToString(separator = ",")

                searchRepository.search(
                    token = token,
                    query = query,
                    type = types,
                    limit = 20
                ).collect { resource ->
                    _searchResultsResource.value = resource
                    when (resource) {
                        is Resource.Success -> {
                            _searchResults.value = resource.data
                        }

                        is Resource.Error -> {
                            Log.d(
                                Constants.OAUTH_TAG,
                                "getSearchResults: error ${resource.errorResponse}"
                            )
                        }

                        else -> {
                        }
                    }
                }
            } catch (e: Exception) {
                Log.d(Constants.OAUTH_TAG, "getSearchResults: error $e")
            }
        }
    }
}