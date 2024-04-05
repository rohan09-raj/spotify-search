package com.example.spotifysearch.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.spotifysearch.databinding.FragmentSearchBinding
import com.example.spotifysearch.model.SearchItem
import com.example.spotifysearch.preferences.SharedPreference
import com.example.spotifysearch.ui.SearchViewModel
import com.example.spotifysearch.ui.adapters.SearchAdapter
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
    private val viewModel by viewModels<SearchViewModel>()
    private var lastQuerySearchAt: Long = 0L
    private var queryJob: Job? = null

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
        setRecyclerView()

        val queryListener = object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    viewModel.getSearchResults(query, listOf("album", "artist", "playlist", "track"))
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
        viewModel.searchResults.observe(viewLifecycleOwner) { data ->
            if (data != null) {
                val searchItems = data.albums.items.map { album ->
                    SearchItem(
                        id = album.id,
                        image = album.images[0].url,
                        title = album.name,
                        type = album.type.replaceFirstChar { it.uppercase() },
                        names = album.artists.map { it.name }
                    )
                }
                val adapter = SearchAdapter(requireContext()) {
                    Toast.makeText(requireContext(), "Item clicked", Toast.LENGTH_SHORT).show()
                }
                binding.rvSearch.adapter = adapter
                adapter.submitList(searchItems)
                binding.rvSearch.isVisible = true
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
                    viewModel.getSearchResults(query, listOf("album", "artist", "playlist", "track"))
                    queryJob?.cancel()
                }
            }
        }
    }
}