package com.example.spotifysearch.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.spotifysearch.databinding.FragmentPlaylistBinding
import com.example.spotifysearch.model.Playlist
import com.example.spotifysearch.model.SearchItem
import com.example.spotifysearch.network.models.Resource
import com.example.spotifysearch.ui.SearchViewModel
import com.example.spotifysearch.ui.items.ItemDetail
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Section
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaylistFragment : Fragment() {

    private lateinit var binding: FragmentPlaylistBinding
    private val viewModel by lazy {
        ViewModelProvider(requireActivity())[SearchViewModel::class.java]
    }
    private var detailsAdapter = GroupAdapter<GroupieViewHolder>()
    private var detailsSection = Section()
    private var albumsSection = Section()
    private var topTracksSection = Section()
    private var relatedArtistsSection = Section()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = arguments?.getString("id")
        if (id != null) {
            viewModel.getPlaylist(id)
            observePlaylist()
        } else {
            Toast.makeText(requireContext(), "Something went wrong!", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }

        binding.ibBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun observePlaylist() {
        viewModel.playlistResource.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    val playlist = resource.data
                    setPlaylistData(playlist)
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

    private fun setPlaylistData(playlist: Playlist) {
        binding.name = playlist.name
        Glide.with(this)
            .load(playlist.images.firstOrNull()?.url)
            .into(binding.ivPlaylist)

        binding.rvPlaylist.adapter = detailsAdapter
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
                ItemDetail(
                    metric = "${playlist.tracks.total} Tracks",
                    items = listOf(playlist.owner.displayName)
                )
            )
        )
        viewModel.insertLastSearch(
            SearchItem(
                id = playlist.id,
                image = playlist.images.firstOrNull()?.url,
                title = playlist.name,
                type = playlist.type.replaceFirstChar { it.uppercase() },
                names = listOf(playlist.owner.displayName)
            )
        )
    }
}