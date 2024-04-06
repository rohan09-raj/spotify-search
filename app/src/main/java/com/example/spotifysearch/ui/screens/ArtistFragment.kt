package com.example.spotifysearch.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.spotifysearch.R
import com.example.spotifysearch.databinding.FragmentArtistBinding
import com.example.spotifysearch.model.Artist
import com.example.spotifysearch.model.ArtistDetailItem
import com.example.spotifysearch.model.SearchItem
import com.example.spotifysearch.network.models.Resource
import com.example.spotifysearch.ui.SearchViewModel
import com.example.spotifysearch.ui.items.ItemArtistDetail
import com.example.spotifysearch.ui.items.ItemBanner
import com.example.spotifysearch.ui.items.ItemDetail
import com.example.spotifysearch.ui.items.ItemSubHeader
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Section
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArtistFragment : Fragment() {

    private lateinit var binding: FragmentArtistBinding
    private val viewModel: SearchViewModel by activityViewModels()
    private var detailsAdapter = GroupAdapter<GroupieViewHolder>()
    private var detailsSection = Section()
    private var albumsSection = Section()
    private var topTracksSection = Section()
    private var relatedArtistsSection = Section()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentArtistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = arguments?.getString("id")
        if (id != null) {
            viewModel.getArtist(id)
            viewModel.getArtistTopTracks(id)
            viewModel.getArtistAlbums(id)
            viewModel.getRelatedArtists(id)
            observeArtist()
        } else {
            Toast.makeText(requireContext(), "Something went wrong!", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }

        binding.ibBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun observeArtist() {
        viewModel.artistResource.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    val artist = resource.data
                    setArtistData(artist)
                }

                is Resource.Error -> {
                    Toast.makeText(
                        requireContext(),
                        resource.errorResponse.error?.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is Resource.Loading -> {
                    // Show loading
                }
            }
        }

        viewModel.artistTopTracksResource.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    val topTracks = resource.data
                    topTracksSection.setHeader(ItemSubHeader("Top Tracks"))
                    topTracksSection.replaceAll(
                        topTracks.tracks.mapIndexed { index, track ->
                            ItemArtistDetail(
                                item = ArtistDetailItem(
                                    id = track.id,
                                    index = index + 1,
                                    title = track.name,
                                    image = track.album.images.firstOrNull()?.url,
                                    names = track.artists.map { it.name }
                                ),
                                onClick = {
                                    val fragment = TrackFragment()
                                    val bundle = Bundle()
                                    bundle.putString("id", track.id)
                                    fragment.arguments = bundle
                                    findNavController().navigate(
                                        R.id.action_artist_to_track,
                                        bundle
                                    )
                                }
                            )
                        }
                    )
                }

                is Resource.Error -> {
                    Toast.makeText(
                        requireContext(),
                        resource.errorResponse.error?.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is Resource.Loading -> {
                    // Show loading
                }
            }
        }

        viewModel.artistAlbumsResource.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    val albums = resource.data
                    albumsSection.setHeader(ItemSubHeader("Albums"))
                    albumsSection.replaceAll(
                        albums.items.mapIndexed { index, album ->
                            ItemArtistDetail(
                                item = ArtistDetailItem(
                                    id = album.id,
                                    index = index + 1,
                                    title = album.name,
                                    image = album.images.firstOrNull()?.url,
                                    names = album.artists.map { it.name }
                                ),
                                onClick = {
                                    val fragment = AlbumFragment()
                                    val bundle = Bundle()
                                    bundle.putString("id", album.id)
                                    fragment.arguments = bundle
                                    findNavController().navigate(
                                        R.id.action_artist_to_album,
                                        bundle
                                    )
                                }
                            )
                        }
                    )
                }

                is Resource.Error -> {
                    Toast.makeText(
                        requireContext(),
                        resource.errorResponse.error?.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is Resource.Loading -> {
                    // Show loading
                }
            }
        }

        viewModel.relatedArtistsResource.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    val relatedArtists = resource.data
                    relatedArtistsSection.setHeader(ItemSubHeader("Related Artists"))
                    relatedArtistsSection.replaceAll(
                        relatedArtists.artists.mapIndexed { index, artist ->
                            ItemArtistDetail(
                                item = ArtistDetailItem(
                                    id = artist.id,
                                    index = index + 1,
                                    title = artist.name,
                                    image = artist.images.firstOrNull()?.url,
                                    names = artist.genres
                                ),
                                onClick = {
                                    val fragment = ArtistFragment()
                                    val bundle = Bundle()
                                    bundle.putString("id", artist.id)
                                    fragment.arguments = bundle
                                    findNavController().navigate(
                                        R.id.action_artist_to_artist,
                                        bundle
                                    )
                                }
                            )
                        }
                    )
                }

                is Resource.Error -> {
                    Toast.makeText(
                        requireContext(),
                        resource.errorResponse.error?.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is Resource.Loading -> {
                    // Show loading
                }
            }
        }
    }

    private fun setArtistData(artist: Artist) {
        binding.rvArtist.adapter = detailsAdapter
        detailsAdapter.replaceAll(
            listOf(
                detailsSection,
                albumsSection,
                topTracksSection,
                relatedArtistsSection
            )
        )
        detailsSection.replaceAll(
            listOf(
                ItemBanner(
                    title = artist.name,
                    image = artist.images.firstOrNull()?.url ?: ""
                ),
                ItemDetail(
                    metric = "${artist.followers.total} Followers",
                    items = artist.genres
                )
            )
        )
        viewModel.insertLastSearch(
            SearchItem(
                id = artist.id,
                image = artist.images.firstOrNull()?.url,
                title = artist.name,
                type = artist.type.replaceFirstChar { it.uppercase() },
                names = artist.genres
            )
        )
    }
}