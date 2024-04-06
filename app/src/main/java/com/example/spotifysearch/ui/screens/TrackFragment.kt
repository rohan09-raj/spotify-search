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
import com.example.spotifysearch.databinding.FragmentTrackBinding
import com.example.spotifysearch.model.SearchItem
import com.example.spotifysearch.model.Track
import com.example.spotifysearch.network.models.Resource
import com.example.spotifysearch.ui.SearchViewModel
import com.example.spotifysearch.ui.items.ItemDetail
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Section
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrackFragment : Fragment() {

    private lateinit var binding: FragmentTrackBinding
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
        binding = FragmentTrackBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = arguments?.getString("id")
        if (id != null) {
            viewModel.getTrack(id)
            observeTrack()
        } else {
            Toast.makeText(requireContext(), "Something went wrong!", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }

        binding.ibBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun observeTrack() {
        viewModel.trackResource.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    val track = resource.data
                    setTrackData(track)
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

    private fun setTrackData(track: Track) {
        binding.name = track.name
        Glide.with(this)
            .load(track.album.images.firstOrNull()?.url)
            .into(binding.ivTrack)

        binding.rvTrack.adapter = detailsAdapter
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
                    metric = "${track.durationMs / 1000 / 60} min :${track.durationMs / 1000 % 60} sec",
                    items = track.artists.map { it.name }
                )
            )
        )
        viewModel.insertLastSearch(
            SearchItem(
                id = track.id,
                image = track.album.images.firstOrNull()?.url,
                title = track.name,
                type = track.type.replaceFirstChar { it.uppercase() },
                names = track.artists.map { it.name }
            )
        )
    }
}