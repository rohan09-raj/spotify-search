package com.example.spotifysearch.ui.items

import com.example.spotifysearch.R
import com.example.spotifysearch.databinding.ItemHeaderLastSearchBinding
import com.example.spotifysearch.ui.groupie.DataBindingListItem

class ItemHeaderLastSearch :
    DataBindingListItem<ItemHeaderLastSearchBinding>(R.layout.item_header_last_search) {

    override fun ItemHeaderLastSearchBinding.onBind(position: Int) {}
}