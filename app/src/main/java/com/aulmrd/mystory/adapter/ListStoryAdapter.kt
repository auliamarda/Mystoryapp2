package com.aulmrd.mystory.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aulmrd.mystory.data.response.ListStoryItem
import com.aulmrd.mystory.databinding.ItemStoryBinding
import com.bumptech.glide.Glide

class ListStoryAdapter(private val listStory: ArrayList<ListStoryItem>) : RecyclerView.Adapter<ListStoryAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback
    inner class ListViewHolder(var binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root)

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val story = listStory[position]
        holder.binding.apply{
           tvUsername.text = story.name
            tvDescription.text = story.description
        }
        Glide.with(holder.itemView.context)
            .load(story.photoUrl)
            .into(holder.binding.imgAvatar)
        holder.itemView.setOnClickListener{
            onItemClickCallback.onItemCLicked(listStory[holder.adapterPosition])
        }

    }

    override fun getItemCount(): Int = listStory.size

    interface OnItemClickCallback {
        fun onItemCLicked(data: ListStoryItem)
    }
}