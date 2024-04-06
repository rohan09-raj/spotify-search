package com.example.spotifysearch.ui.screens

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.spotifysearch.R
import com.example.spotifysearch.databinding.FragmentSearchBinding
import com.example.spotifysearch.model.SearchItem
import com.example.spotifysearch.model.SearchResponse
import com.example.spotifysearch.network.models.Resource
import com.example.spotifysearch.preferences.SharedPreference
import com.example.spotifysearch.ui.SearchViewModel
import com.example.spotifysearch.ui.items.ItemHeaderLastSearch
import com.example.spotifysearch.ui.items.ItemSearchSubHeader
import com.example.spotifysearch.ui.items.ItemSearched
import com.example.spotifysearch.utils.Constants
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Section
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchViewModel by activityViewModels()
    private var lastQuerySearchAt: Long = 0L
    private var queryJob: Job? = null
    private var searchAdapter = GroupAdapter<GroupieViewHolder>()
    private var lastSearchSection = Section()
    private var albumSection = Section()
    private var artistSection = Section()
    private var playlistSection = Section()
    private var trackSection = Section()

    @Inject
    lateinit var sharedPreference: SharedPreference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (sharedPreference.expiresIn < (Date().time / 1000)) {
            viewModel.getAccessToken()
        }
        viewModel.getLastSearch()
        setRecyclerView()
        setLastSearchResults()
        setSearchResults()

        val queryListener = object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    viewModel.getSearchResults(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrEmpty()) {
                    lastQuerySearchAt = Date().time
                    queryJob?.cancel()
                    queryJob = getEndTypingIndicatorInactivity(query = newText)
                }
                return true
            }
        }

        binding.svSearch.setOnQueryTextListener(queryListener)
    }

    private fun setRecyclerView() {
        binding.rvSearch.adapter = searchAdapter
        searchAdapter.replaceAll(
            listOf(
                artistSection,
                albumSection,
                playlistSection,
                trackSection,
                lastSearchSection
            )
        )
    }

    private fun setLastSearchResults() {
        lastSearchSection.setHeader(ItemHeaderLastSearch())
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.lastSearch.collectLatest { data ->
                if (data.isNotEmpty()) {
                    binding.llEmpty.visibility = View.GONE
                    binding.rvSearch.visibility = View.VISIBLE
                } else {
                    binding.llEmpty.visibility = View.VISIBLE
                    binding.rvSearch.visibility = View.GONE
                    return@collectLatest
                }

                val lastSearchItems = data.map { item ->
                    ItemSearched(
                        item = item,
                        isLastSearchItem = true,
                        onDeleteClick = {
                            viewModel.deleteLastSearchItem(item)
                        },
                        onClick = {
                            when (item.type?.lowercase()) {
                                Constants.SearchType.ALBUM -> {
                                    val fragment = AlbumFragment()
                                    val bundle = Bundle()
                                    bundle.putString("id", item.id)
                                    fragment.arguments = bundle
                                    findNavController().navigate(
                                        R.id.action_search_to_album,
                                        bundle
                                    )
                                }

                                Constants.SearchType.ARTIST -> {
                                    val fragment = ArtistFragment()
                                    val bundle = Bundle()
                                    bundle.putString("id", item.id)
                                    fragment.arguments = bundle
                                    findNavController().navigate(
                                        R.id.action_search_to_artist,
                                        bundle
                                    )
                                }

                                Constants.SearchType.PLAYLIST -> {
                                    val fragment = PlaylistFragment()
                                    val bundle = Bundle()
                                    bundle.putString("id", item.id)
                                    fragment.arguments = bundle
                                    findNavController().navigate(
                                        R.id.action_search_to_playlist,
                                        bundle
                                    )
                                }

                                Constants.SearchType.TRACK -> {
                                    val fragment = TrackFragment()
                                    val bundle = Bundle()
                                    bundle.putString("id", item.id)
                                    fragment.arguments = bundle
                                    findNavController().navigate(
                                        R.id.action_search_to_track,
                                        bundle
                                    )
                                }
                            }
                        }
                    )
                }
                lastSearchSection.replaceAll(lastSearchItems)
            }
        }
    }

    private fun setSearchResults() {
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.searchResultsResource.collectLatest { resource ->
                when (resource) {
                    is Resource.Success -> {
                        addSearchSections(resource.data)
                    }

                    is Resource.Error -> {
                        Log.d(
                            Constants.OAUTH_TAG,
                            "getSearchResults: error ${resource.errorResponse}"
                        )
                    }

                    is Resource.Loading -> {
                        // Show loading
                    }
                }
            }
        }
    }

    private fun addSearchSections(searchResponse: SearchResponse) {
        artistSection.setHeader(ItemSearchSubHeader(getString(R.string.artist)))
        albumSection.setHeader(ItemSearchSubHeader(getString(R.string.album)))
        playlistSection.setHeader(ItemSearchSubHeader(getString(R.string.playlist)))
        trackSection.setHeader(ItemSearchSubHeader(getString(R.string.track)))

        val albums =
            if (searchResponse.albums.items.isNotEmpty()) searchResponse.albums.items.map { album ->
                val searchItem = SearchItem(
                    id = album.id,
                    image = album.images.firstOrNull()?.url,
                    title = album.name,
                    type = album.type.replaceFirstChar { it.uppercase() },
                    names = album.artists.map { it.name }
                )
                ItemSearched(
                    item = searchItem,
                    onClick = {
                        val fragment = AlbumFragment()
                        val bundle = Bundle()
                        bundle.putString("id", searchItem.id)
                        fragment.arguments = bundle
                        findNavController().navigate(
                            R.id.action_search_to_album,
                            bundle
                        )
                    }
                )
            } else emptyList()
        val artists = if (searchResponse.artists.items.isNotEmpty()) {
            searchResponse.artists.items.map { artist ->
                val searchItem = SearchItem(
                    id = artist.id,
                    image = artist.images.firstOrNull()?.url,
                    title = artist.name,
                    type = artist.type.replaceFirstChar { it.uppercase() }
                )
                ItemSearched(
                    item = searchItem,
                    onClick = {
                        val fragment = ArtistFragment()
                        val bundle = Bundle()
                        bundle.putString("id", searchItem.id)
                        fragment.arguments = bundle
                        findNavController().navigate(
                            R.id.action_search_to_artist,
                            bundle
                        )
                    }
                )
            }

        } else emptyList()

        val playlists = if (searchResponse.playlists.items.isNotEmpty()) {
            searchResponse.playlists.items.map { playlist ->
                val searchItem = SearchItem(
                    id = playlist.id,
                    image = playlist.images.firstOrNull()?.url,
                    title = playlist.name,
                    type = playlist.type.replaceFirstChar { it.uppercase() },
                    names = listOf(playlist.owner.displayName)
                )
                ItemSearched(
                    item = searchItem,
                    onClick = {
                        val fragment = PlaylistFragment()
                        val bundle = Bundle()
                        bundle.putString("id", searchItem.id)
                        fragment.arguments = bundle
                        findNavController().navigate(
                            R.id.action_search_to_playlist,
                            bundle
                        )
                    }
                )
            }
        } else emptyList()

        val tracks = if (searchResponse.tracks.items.isNotEmpty()) {
            searchResponse.tracks.items.map { track ->
                val searchItem = SearchItem(
                    id = track.id,
                    image = track.album.images.firstOrNull()?.url,
                    title = track.name,
                    type = track.type.replaceFirstChar { it.uppercase() },
                    names = track.artists.map { it.name }
                )
                ItemSearched(
                    item = searchItem,
                    onClick = {
                        val fragment = TrackFragment()
                        val bundle = Bundle()
                        bundle.putString("id", searchItem.id)
                        fragment.arguments = bundle
                        findNavController().navigate(
                            R.id.action_search_to_track,
                            bundle
                        )
                    }
                )
            }
        } else emptyList()

        artistSection.replaceAll(artists)
        albumSection.replaceAll(albums)
        playlistSection.replaceAll(playlists)
        trackSection.replaceAll(tracks)
        if (artists.isNotEmpty() || albums.isNotEmpty() || playlists.isNotEmpty() || tracks.isNotEmpty()) {
            binding.llEmpty.visibility = View.GONE
            binding.rvSearch.visibility = View.VISIBLE
        } else {
            binding.llEmpty.visibility = View.VISIBLE
            binding.rvSearch.visibility = View.GONE
        }
    }

    private fun getEndTypingIndicatorInactivity(query: String): Job {
        return CoroutineScope(Dispatchers.Default).launch {
            while (isActive) {
                delay(100)
                val currentTime = Date().time
                val duration = currentTime.minus(lastQuerySearchAt)
                if (duration > 500) {
                    viewModel.getSearchResults(query)
                    queryJob?.cancel()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        queryJob?.cancel()
    }
}