package com.example.storyappdicoding.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyappdicoding.databinding.ItemStoryBinding
import com.example.storyappdicoding.model.Story
import com.example.storyappdicoding.ui.DetailActivity

class ListStoriesAdapter(private val listStory: List<Story>) : RecyclerView.Adapter<ListStoriesAdapter.ListViewHolder>() {

    class ListViewHolder(val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.binding.tvItemName.text = listStory[position].name
        Glide.with(holder.itemView.context).load(listStory[position].photoUrl).into(holder.binding.ivItemPhoto)
        holder.binding.cardStory.setOnClickListener{
            val moveDetail = Intent(holder.itemView.context, DetailActivity::class.java)
            moveDetail.putExtra("id", listStory[position].id)
            holder.itemView.context.startActivity(moveDetail)
        }
    }

    override fun getItemCount(): Int = listStory.size
}