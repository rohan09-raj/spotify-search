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
import com.example.spotifysearch.model.SearchResponse
import com.example.spotifysearch.preferences.SharedPreference
import com.example.spotifysearch.ui.SearchViewModel
import com.example.spotifysearch.ui.items.ItemHeaderLastSearch
import com.example.spotifysearch.ui.items.ItemSearched
import com.example.spotifysearch.ui.items.ItemSubHeader
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
        searchAdapter.replaceAll(
            listOf(
                lastSearchSection,
                artistSection,
                albumSection,
                playlistSection,
                trackSection
            )
        )
    }

    private fun setLastSearchResults() {
        lastSearchSection.setHeader(ItemHeaderLastSearch())
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
                lastSearchSection.replaceAll(lastSearchItems)
                if (lastSearchItems.isNotEmpty()) {
                    binding.llEmpty.visibility = View.GONE
                    binding.rvSearch.visibility = View.VISIBLE
                } else {
                    binding.llEmpty.visibility = View.VISIBLE
                    binding.rvSearch.visibility = View.GONE
                }
            }
        }
    }

    private fun setSearchResults() {
        viewModel.searchResults.observe(viewLifecycleOwner) { data ->
            if (data != null) {
                addSearchSections(data)
            }
        }
    }

    private fun addSearchSections(searchResponse: SearchResponse) {
        artistSection.setHeader(ItemSubHeader(getString(R.string.artist)))
        albumSection.setHeader(ItemSubHeader(getString(R.string.album)))
        playlistSection.setHeader(ItemSubHeader(getString(R.string.playlist)))
        trackSection.setHeader(ItemSubHeader(getString(R.string.track)))

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

        searchAdapter.remove(lastSearchSection)
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