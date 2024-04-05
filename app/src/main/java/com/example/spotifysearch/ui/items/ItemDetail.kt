package com.example.spotifysearch.ui.items

import com.example.spotifysearch.R
import com.example.spotifysearch.databinding.ItemDetailBinding
import com.example.spotifysearch.ui.groupie.DataBindingListItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Section

class ItemDetail(
    val followers: Int,
    val genres: List<String>
) : DataBindingListItem<ItemDetailBinding>(R.layout.item_detail) {

    private var genreAdapter = GroupAdapter<GroupieViewHolder>()
    private var genresSection = Section()

    override fun ItemDetailBinding.onBind(position: Int) {
        followersCount = "${followers} Followers"
        rvGenres.adapter = genreAdapter
        genreAdapter.replaceAll(
            listOf(genresSection)
        )
        genresSection.replaceAll(
            genres.map { genre -> Genre(genre) }
        )
    }
}