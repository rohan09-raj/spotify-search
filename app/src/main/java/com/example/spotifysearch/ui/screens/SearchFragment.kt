package com.example.spotifysearch.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.spotifysearch.R
import com.example.spotifysearch.databinding.FragmentSearchBinding
import com.example.spotifysearch.model.SearchItem
import com.example.spotifysearch.preferences.SharedPreference
import com.example.spotifysearch.ui.SearchViewModel
import com.example.spotifysearch.ui.items.ItemHeaderLastSearch
import com.example.spotifysearch.ui.items.ItemSearched
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Section
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel by lazy {
        ViewModelProvider(requireActivity())[SearchViewModel::class.java]
    }
    private var lastQuerySearchAt: Long = 0L
    private var queryJob: Job? = null
    private var searchAdapter = GroupAdapter<GroupieViewHolder>()
    private var searchResultSection = Section()

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
                    viewModel.getSearchResults(
                        query,
                        listOf("album", "artist", "playlist", "track")
                    )
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
        searchAdapter.replaceAll(listOf(searchResultSection))
    }

    private fun setLastSearchResults() {
        searchResultSection.setHeader(ItemHeaderLastSearch())
        viewModel.lastSearch.observe(viewLifecycleOwner) { data ->
            if (data != null) {
                val lastSearchItems = data.map { item ->
                    ItemSearched(
                        item = item,
                        onClick = {
                            when (item.type?.lowercase()) {
                                "album" -> {
                                    val fragment = AlbumFragment()
                                    val bundle = Bundle()
                                    bundle.putString("id", item.id)
                                    fragment.arguments = bundle
                                    findNavController().navigate(
                                        R.id.action_search_to_album,
                                        bundle
                                    )
                                }

                                "artist" -> {
                                    val fragment = ArtistFragment()
                                    val bundle = Bundle()
                                    bundle.putString("id", item.id)
                                    fragment.arguments = bundle
                                    findNavController().navigate(
                                        R.id.action_search_to_artist,
                                        bundle
                                    )
                                }

                                "playlist" -> {
                                    val fragment = PlaylistFragment()
                                    val bundle = Bundle()
                                    bundle.putString("id", item.id)
                                    fragment.arguments = bundle
                                    findNavController().navigate(
                                        R.id.action_search_to_playlist,
                                        bundle
                                    )
                                }

                                "track" -> {
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
                searchResultSection.replaceAll(lastSearchItems)
            }
        }
    }

    private fun setSearchResults() {
//        searchResultSection.removeHeader()
        viewModel.searchResults.observe(viewLifecycleOwner) { data ->
            if (data != null) {
                val searchItems: MutableList<SearchItem> = mutableListOf()

                val albums = if (data.albums.items.isNotEmpty()) data.albums.items.map { album ->
                    SearchItem(
                        id = album.id,
                        image = album.images.firstOrNull()?.url,
                        title = album.name,
                        type = album.type.replaceFirstChar { it.uppercase() },
                        names = album.artists.map { it.name }
                    )
                } else emptyList()
                val artists = if (data.artists.items.isNotEmpty()) {
                    data.artists.items.map { artist ->
                        SearchItem(
                            id = artist.id,
                            image = artist.images.firstOrNull()?.url,
                            title = artist.name,
                            type = artist.type.replaceFirstChar { it.uppercase() }
                        )
                    }
                } else emptyList()

                val playlists = if (data.playlists.items.isNotEmpty()) {
                    data.playlists.items.map { playlist ->
                        SearchItem(
                            id = playlist.id,
                            image = playlist.images.firstOrNull()?.url,
                            title = playlist.name,
                            type = playlist.type.replaceFirstChar { it.uppercase() },
                            names = listOf(playlist.owner.displayName)
                        )
                    }
                } else emptyList()

                val tracks = if (data.tracks.items.isNotEmpty()) {
                    data.tracks.items.map { track ->
                        SearchItem(
                            id = track.id,
                            image = track.album.images.firstOrNull()?.url,
                            title = track.name,
                            type = track.type.replaceFirstChar { it.uppercase() },
                            names = track.artists.map { it.name }
                        )
                    }
                } else emptyList()

                searchItems.addAll(albums)
                searchItems.addAll(artists)
                searchItems.addAll(playlists)
                searchItems.addAll(tracks)
                searchItems.sortBy { it.id }

                val searchedItems = searchItems.toList().map { item ->
                    ItemSearched(
                        item = item,
                        onClick = {
                            when (item.type?.lowercase()) {
                                "album" -> {
                                    val fragment = AlbumFragment()
                                    val bundle = Bundle()
                                    bundle.putString("id", item.id)
                                    fragment.arguments = bundle
                                    findNavController().navigate(
                                        R.id.action_search_to_album,
                                        bundle
                                    )
                                }

                                "artist" -> {
                                    val fragment = ArtistFragment()
                                    val bundle = Bundle()
                                    bundle.putString("id", item.id)
                                    fragment.arguments = bundle
                                    findNavController().navigate(
                                        R.id.action_search_to_artist,
                                        bundle
                                    )
                                }

                                "playlist" -> {
                                    val fragment = PlaylistFragment()
                                    val bundle = Bundle()
                                    bundle.putString("id", item.id)
                                    fragment.arguments = bundle
                                    findNavController().navigate(
                                        R.id.action_search_to_playlist,
                                        bundle
                                    )
                                }

                                "track" -> {
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

                searchResultSection.replaceAll(searchedItems)
            }
        }
    }

    private fun getEndTypingIndicatorInactivity(query: String): Job {
        return CoroutineScope(Dispatchers.Default).launch {
            while (isActive) {
                delay(100)
                val currentTime = Date().time
                val duration = currentTime.minus(lastQuerySearchAt)
                if (duration > 500) {
                    viewModel.getSearchResults(
                        query,
                        listOf("album", "artist", "playlist", "track")
                    )
                    queryJob?.cancel()
                }
            }
        }
    }
}