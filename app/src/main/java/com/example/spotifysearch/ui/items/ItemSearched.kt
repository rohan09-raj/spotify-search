package com.example.spotifysearch.ui.items

import android.view.View
import com.bumptech.glide.Glide
import com.example.spotifysearch.R
import com.example.spotifysearch.databinding.ItemSearchBinding
import com.example.spotifysearch.model.SearchItem
import com.example.spotifysearch.ui.groupie.DataBindingListItem

class ItemSearched(
    val item: SearchItem,
    val isLastSearchItem: Boolean = false,
    val onClick: () -> Unit,
    val onDeleteClick: () -> Unit = {}
) : DataBindingListItem<ItemSearchBinding>(R.layout.item_search) {

    override fun ItemSearchBinding.onBind(position: Int) {
        Glide.with(context)
            .load(item.image)
            .into(ivSearch)
        title = item.title
        type = item.type
        if (item.names.isNullOrEmpty()) {
            names = ""
            tvSeparator.visibility = View.GONE
        } else {
            names = item.names.joinToString(", ")
            tvSeparator.visibility = View.VISIBLE
        }

        if (isLastSearchItem) ibDelete.visibility = View.VISIBLE
        else ibDelete.visibility = View.GONE

        ibDelete.setOnClickListener {
            onDeleteClick()
        }

        root.setOnClickListener {
            onClick()
        }
    }
}