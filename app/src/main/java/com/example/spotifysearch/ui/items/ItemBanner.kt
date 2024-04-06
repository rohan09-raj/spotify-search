package com.example.spotifysearch.ui.items

import com.bumptech.glide.Glide
import com.example.spotifysearch.R
import com.example.spotifysearch.databinding.ItemBannerBinding
import com.example.spotifysearch.ui.groupie.DataBindingListItem

class ItemBanner(
    val title: String,
    val image: String,
) : DataBindingListItem<ItemBannerBinding>(R.layout.item_banner) {

    override fun ItemBannerBinding.onBind(position: Int) {
        Glide.with(context)
            .load(image)
            .into(ivAlbum)
        name = title
    }
}