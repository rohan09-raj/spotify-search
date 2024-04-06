package com.example.spotifysearch.ui.items

import com.bumptech.glide.Glide
import com.example.spotifysearch.R
import com.example.spotifysearch.databinding.ItemArtistDetailBinding
import com.example.spotifysearch.model.ArtistDetailItem
import com.example.spotifysearch.ui.groupie.DataBindingListItem

class ItemArtistDetail(
    val item: ArtistDetailItem
) : DataBindingListItem<ItemArtistDetailBinding>(R.layout.item_artist_detail) {

    override fun ItemArtistDetailBinding.onBind(position: Int) {
        Glide.with(context)
            .load(item.image)
            .placeholder(R.drawable.ic_spotify)
            .into(ivSearch)
        number = item.index.toString()
        title = item.title
        if (item.names != null && item.names.isNotEmpty()) {
            names = item.names.joinToString(", ")
        }
    }
}