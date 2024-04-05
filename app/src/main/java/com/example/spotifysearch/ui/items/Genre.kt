package com.example.spotifysearch.ui.items

import com.example.spotifysearch.R
import com.example.spotifysearch.databinding.ItemGenreBinding
import com.example.spotifysearch.ui.groupie.DataBindingListItem

class Genre(
    val text: String
) : DataBindingListItem<ItemGenreBinding>(R.layout.item_genre) {

    override fun ItemGenreBinding.onBind(position: Int) {
        genre = text
    }
}