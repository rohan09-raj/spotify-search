package com.example.spotifysearch.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.spotifysearch.databinding.FragmentTrackBinding
import com.example.spotifysearch.model.Track
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
        val trackDetail = viewModel.searchResults.value?.tracks?.items?.find { it.id == id }

        binding.ibBack.setOnClickListener {
            findNavController().popBackStack()
        }

        if (trackDetail != null) {
            setUpRecyclerView(trackDetail)
        }
    }

    private fun setUpRecyclerView(trackDetail: Track) {
        binding.name = trackDetail.name
        Glide.with(this)
            .load(trackDetail.album.images.firstOrNull()?.url)
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
                    followers = 1000,
                    genres = listOf("Pop", "Rock", "Jazz")
                )
            )
        )
    }
}