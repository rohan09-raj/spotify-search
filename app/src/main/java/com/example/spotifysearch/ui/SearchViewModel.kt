package com.example.spotifysearch.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotifysearch.data.SearchRepository
import com.example.spotifysearch.database.dao.LastSearchDao
import com.example.spotifysearch.model.Album
import com.example.spotifysearch.model.Artist
import com.example.spotifysearch.model.ArtistAlbums
import com.example.spotifysearch.model.ArtistRelatedArtists
import com.example.spotifysearch.model.ArtistTopTracks
import com.example.spotifysearch.model.Playlist
import com.example.spotifysearch.model.SearchItem
import com.example.spotifysearch.model.SearchResponse
import com.example.spotifysearch.model.TokenResponse
import com.example.spotifysearch.model.Track
import com.example.spotifysearch.model.database.LastSearchItem
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
    private val sharedPreference: SharedPreference,
    private val lastSearchDao: LastSearchDao
) : ViewModel() {

    private val _searchResultsResource = MutableLiveData<Resource<SearchResponse>>()
    val searchResultsResource: LiveData<Resource<SearchResponse>>
        get() = _searchResultsResource

    private val _lastSearch = MutableLiveData<List<SearchItem>>()
    val lastSearch: LiveData<List<SearchItem>>
        get() = _lastSearch

    private val _albumResource = MutableLiveData<Resource<Album>>()
    val albumResource: LiveData<Resource<Album>>
        get() = _albumResource

    private val _artistResource = MutableLiveData<Resource<Artist>>()
    val artistResource: LiveData<Resource<Artist>>
        get() = _artistResource

    private val _playlistResource = MutableLiveData<Resource<Playlist>>()
    val playlistResource: LiveData<Resource<Playlist>>
        get() = _playlistResource

    private val _trackResource = MutableLiveData<Resource<Track>>()
    val trackResource: LiveData<Resource<Track>>
        get() = _trackResource

    private val _artistTopTracksResource = MutableLiveData<Resource<ArtistTopTracks>>()
    val artistTopTracksResource: LiveData<Resource<ArtistTopTracks>>
        get() = _artistTopTracksResource

    private val _artistAlbumsResource = MutableLiveData<Resource<ArtistAlbums>>()
    val artistAlbumsResource: LiveData<Resource<ArtistAlbums>>
        get() = _artistAlbumsResource

    private val _relatedArtistsResource = MutableLiveData<Resource<ArtistRelatedArtists>>()
    val relatedArtistsResource: LiveData<Resource<ArtistRelatedArtists>>
        get() = _relatedArtistsResource

    fun getAccessToken() {
        viewModelScope.launch {
            try {
                searchRepository.getAccessToken(
                    clientId = Constants.CLIENT_ID,
                    clientSecret = Constants.CLIENT_SECRET,
                    grantType = Constants.GRANT_TYPE
                ).collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            val tokenResponse = resource.data

                            sharedPreference.accessToken = tokenResponse.accessToken
                            sharedPreference.tokenType = tokenResponse.tokenType
                            sharedPreference.expiresIn =
                                (Date().time / 1000) + tokenResponse.expiresIn.toLong()
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
                }
            } catch (e: Exception) {
                Log.d(Constants.OAUTH_TAG, "getSearchResults: error $e")
            }
        }
    }

    fun insertLastSearch(searchItem: SearchItem) {
        viewModelScope.launch {
            try {
                lastSearchDao.insert(
                    LastSearchItem(
                        id = searchItem.id,
                        image = searchItem.image,
                        title = searchItem.title,
                        type = searchItem.type,
                        names = searchItem.names?.joinToString(separator = ",")
                    )
                )
            } catch (e: Exception) {
                Log.d(Constants.OAUTH_TAG, "insertLastSearch: error $e")
            }
        }
    }

    fun getLastSearch() {
        viewModelScope.launch {
            val lastSearchItems = lastSearchDao.getLastSearch().map { lastSearchItem ->
                SearchItem(
                    id = lastSearchItem.id,
                    image = lastSearchItem.image,
                    title = lastSearchItem.title,
                    type = lastSearchItem.type,
                    names = lastSearchItem.names?.split(",")?.toList()
                )
            }
            _lastSearch.value = lastSearchItems
        }
    }

    fun getAlbum(id: String) {
        viewModelScope.launch {
            try {
                val token = "${sharedPreference.tokenType} ${sharedPreference.accessToken}"
                searchRepository.getAlbum(
                    token = token,
                    id = id
                ).collect { resource ->
                    _albumResource.value = resource
                }
            } catch (e: Exception) {
                Log.d(Constants.OAUTH_TAG, "getAlbum: error $e")
            }
        }
    }

    fun getArtist(id: String) {
        viewModelScope.launch {
            try {
                val token = "${sharedPreference.tokenType} ${sharedPreference.accessToken}"
                searchRepository.getArtist(
                    token = token,
                    id = id
                ).collect { resource ->
                    _artistResource.value = resource
                }
            } catch (e: Exception) {
                Log.d(Constants.OAUTH_TAG, "getArtist: error $e")
            }
        }
    }

    fun getArtistTopTracks(id: String) {
        viewModelScope.launch {
            try {
                val token = "${sharedPreference.tokenType} ${sharedPreference.accessToken}"
                searchRepository.getArtistTopTracks(
                    token = token,
                    id = id
                ).collect { resource ->
                    _artistTopTracksResource.value = resource
                }
            } catch (e: Exception) {
                Log.d(Constants.OAUTH_TAG, "getArtistTopTracks: error $e")
            }
        }
    }

    fun getArtistAlbums(id: String) {
        viewModelScope.launch {
            try {
                val token = "${sharedPreference.tokenType} ${sharedPreference.accessToken}"
                searchRepository.getArtistAlbums(
                    token = token,
                    id = id
                ).collect { resource ->
                    _artistAlbumsResource.value = resource
                }
            } catch (e: Exception) {
                Log.d(Constants.OAUTH_TAG, "getArtistAlbums: error $e")
            }
        }
    }

    fun getRelatedArtists(id: String) {
        viewModelScope.launch {
            try {
                val token = "${sharedPreference.tokenType} ${sharedPreference.accessToken}"
                searchRepository.getRelatedArtists(
                    token = token,
                    id = id
                ).collect { resource ->
                    _relatedArtistsResource.value = resource
                }
            } catch (e: Exception) {
                Log.d(Constants.OAUTH_TAG, "getRelatedArtists: error $e")
            }
        }
    }

    fun getPlaylist(id: String) {
        viewModelScope.launch {
            try {
                val token = "${sharedPreference.tokenType} ${sharedPreference.accessToken}"
                searchRepository.getPlaylist(
                    token = token,
                    id = id
                ).collect { resource ->
                    _playlistResource.value = resource
                }
            } catch (e: Exception) {
                Log.d(Constants.OAUTH_TAG, "getPlaylist: error $e")
            }
        }
    }

    fun getTrack(id: String) {
        viewModelScope.launch {
            try {
                val token = "${sharedPreference.tokenType} ${sharedPreference.accessToken}"
                searchRepository.getTrack(
                    token = token,
                    id = id
                ).collect { resource ->
                    _trackResource.value = resource
                }
            } catch (e: Exception) {
                Log.d(Constants.OAUTH_TAG, "getTrack: error $e")
            }
        }
    }
}