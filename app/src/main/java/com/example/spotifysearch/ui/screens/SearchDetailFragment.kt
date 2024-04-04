package com.example.spotifysearch.ui.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.spotifysearch.databinding.FragmentSearchDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchDetailFragment : Fragment() {

    private lateinit var binding: FragmentSearchDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchDetailBinding.inflate(inflater, container, false)
        return binding.root
    }
}