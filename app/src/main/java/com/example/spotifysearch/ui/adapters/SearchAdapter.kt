package com.example.spotifysearch.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spotifysearch.R
import com.example.spotifysearch.databinding.ItemSearchBinding
import com.example.spotifysearch.model.SearchItem

class SearchAdapter(private val context: Context, private val onItemClick: () -> Unit) :
    ListAdapter<SearchItem, SearchAdapter.ViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<SearchItem>() {
        override fun areItemsTheSame(oldItem: SearchItem, newItem: SearchItem): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: SearchItem, newItem: SearchItem): Boolean {
            return oldItem.id == newItem.id
        }
    }

    inner class ViewHolder(var binding: ItemSearchBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SearchItem) {
            Glide.with(context)
                .load(item.image)
                .placeholder(R.drawable.ic_spotify)
                .into(binding.ivSearch)
            binding.title = item.title
            binding.type = item.type
            if (item.names?.isEmpty() != true) {
                binding.names = item.names?.joinToString(", ")
                binding.tvSeparator.visibility = View.VISIBLE
            } else {
                binding.names = ""
                binding.tvSeparator.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val repository = getItem(position)
        holder.bind(repository)
        holder.binding.root.setOnClickListener {
            onItemClick()
        }
    }
}