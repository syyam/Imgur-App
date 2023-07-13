package com.syyamnoor.imgurapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.syyamnoor.imgurapp.R
import com.syyamnoor.imgurapp.databinding.ItemListContentBinding
import com.syyamnoor.imgurapp.domain.models.Image

class ImageAdapter(val onItemClickListener: (Int, Image) -> Unit) :
    ListAdapter<Image, ImageAdapter.NewsViewHolder>(object :
        DiffUtil.ItemCallback<Image>() {
        override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean {
            return oldItem == newItem
        }
    }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder(
            ItemListContentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class NewsViewHolder(private val itemListContentBinding: ItemListContentBinding) :
        RecyclerView.ViewHolder(itemListContentBinding.root) {
        init {
            itemView.setOnClickListener {
                onItemClickListener(bindingAdapterPosition, getItem(bindingAdapterPosition))
            }
        }

        fun bind(position: Int) {
            val image = getItem(position)
            if (image.link.isNotEmpty()) {
                Glide.with(itemView.context)
                    .load(image.images)
                    .placeholder(R.drawable.ic_dummy_icon)
                    .error(R.drawable.ic_error)
                    .into(itemListContentBinding.imageViewFeature)
                itemListContentBinding.textViewTitle.text = image.title
                itemListContentBinding.textViewDesc.text = image.description
                itemListContentBinding.textViewTopic.text = image.topic
            }



        }
    }


}