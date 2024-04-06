package com.example.spotifysearch.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.spotifysearch.databinding.FragmentAlbumBinding
import com.example.spotifysearch.model.Album
import com.example.spotifysearch.model.SearchItem
import com.example.spotifysearch.network.models.Resource
import com.example.spotifysearch.ui.SearchViewModel
import com.example.spotifysearch.ui.items.ItemBanner
import com.example.spotifysearch.ui.items.ItemDetail
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Section
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlbumFragment : Fragment() {

    private lateinit var binding: FragmentAlbumBinding
    private val viewModel: SearchViewModel by activityViewModels()
    private var detailsAdapter = GroupAdapter<GroupieViewHolder>()
    private var detailsSection = Section()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = arguments?.getString("id")
        if (id != null) {
            viewModel.getAlbum(id)
            observeAlbum()
        } else {
            Toast.makeText(requireContext(), "Something went wrong!", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }

        binding.ibBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun observeAlbum() {
        viewModel.albumResource.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    val album = resource.data
                    setAlbumData(album)
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

    private fun setAlbumData(album: Album) {
        binding.rvAlbum.adapter = detailsAdapter
        detailsAdapter.replaceAll(
            listOf(detailsSection)
        )
        detailsSection.replaceAll(
            listOf(
                ItemBanner(
                    title = album.name,
                    image = album.images.firstOrNull()?.url ?: ""
                ),
                ItemDetail(
                    metric = "${album.totalTracks} Tracks",
                    items = album.artists.map { it.name }
                )
            )
        )
        viewModel.insertLastSearch(
            SearchItem(
                id = album.id,
                image = album.images.firstOrNull()?.url,
                title = album.name,
                type = album.type.replaceFirstChar { it.uppercase() },
                names = album.artists.map { it.name }
            )
        )
    }
}