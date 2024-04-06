package com.example.spotifysearch.ui.items

import com.example.spotifysearch.R
import com.example.spotifysearch.databinding.ItemSearchSubHeaderBinding
import com.example.spotifysearch.databinding.ItemSubHeaderBinding
import com.example.spotifysearch.ui.groupie.DataBindingListItem

class ItemSearchSubHeader(
    val title: String
) :
    DataBindingListItem<ItemSearchSubHeaderBinding>(R.layout.item_search_sub_header) {

    override fun ItemSearchSubHeaderBinding.onBind(position: Int) {
        headerTitle = title
    }
}