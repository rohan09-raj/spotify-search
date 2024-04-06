package com.example.spotifysearch.ui.items

import com.example.spotifysearch.R
import com.example.spotifysearch.databinding.ItemSubHeaderBinding
import com.example.spotifysearch.ui.groupie.DataBindingListItem

class ItemSubHeader(
    val title: String
) :
    DataBindingListItem<ItemSubHeaderBinding>(R.layout.item_sub_header) {

    override fun ItemSubHeaderBinding.onBind(position: Int) {
        headerTitle = title
    }
}