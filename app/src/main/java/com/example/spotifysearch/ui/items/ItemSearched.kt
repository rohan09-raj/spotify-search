package com.example.spotifysearch.ui.items

import android.view.View
import com.bumptech.glide.Glide
import com.example.spotifysearch.R
import com.example.spotifysearch.databinding.ItemSearchBinding
import com.example.spotifysearch.model.SearchItem
import com.example.spotifysearch.ui.groupie.DataBindingListItem

class ItemSearched(
    val item: SearchItem,
    val onClick: () -> Unit
) : DataBindingListItem<ItemSearchBinding>(R.layout.item_search) {

    override fun ItemSearchBinding.onBind(position: Int) {
        Glide.with(context)
            .load(item.image)
            .placeholder(R.drawable.ic_spotify)
            .into(ivSearch)
        title = item.title
        type = item.type
        if (item.names?.isEmpty() != true) {
            names = item.names?.joinToString(", ")
            tvSeparator.visibility = View.VISIBLE
        } else {
            names = ""
            tvSeparator.visibility = View.GONE
        }

        root.setOnClickListener {
            onClick()
        }
    }
}