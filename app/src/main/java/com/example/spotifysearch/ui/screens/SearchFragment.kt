package com.example.spotifysearch.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.spotifysearch.databinding.FragmentSearchBinding
import com.example.spotifysearch.model.SearchItem
import com.example.spotifysearch.ui.SearchViewModel
import com.example.spotifysearch.ui.adapters.SearchAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel by viewModels<SearchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAccessToken()
        setRecyclerView()
    }

    private fun setRecyclerView() {
        val data = listOf(SearchItem(
            id = "1",
            image = "https://i.scdn.co/image/ab67616d0000b273f4b3b3b3b3b3b3b3b3b3b3b3",
            title = "Sun Raha Hai Na Tu",
            type = "Album",
            names = listOf("Ankit Tiwari")
        ), SearchItem(
            id = "2",
            image = "https://i.scdn.co/image/ab67616d0000b273f4b3b3b3b3b3b3b3b3b3b3",
            title = "Bekhayali",
            type = "Album",
            names = listOf("Sachet Tandon")
        ), SearchItem(
            id = "3",
            image = "https://i.scdn.co/image/ab67616d0000b273f4b3b3b3b3b3b3b3b3b3b3",
            title = "Tum Hi Ho",
            type = "Album",
            names = listOf("Arijit Singh")
        ))
        val adapter = SearchAdapter(requireContext()) {
            Toast.makeText(requireContext(), "Item clicked", Toast.LENGTH_SHORT).show()
        }
        binding.rvSearch.adapter = adapter
        adapter.submitList(data)
        binding.rvSearch.isVisible = true
    }
}