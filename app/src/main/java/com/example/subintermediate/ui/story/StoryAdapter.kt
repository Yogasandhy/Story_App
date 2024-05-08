package com.example.subintermediate.ui.story

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.subintermediate.R
import com.example.subintermediate.data.api.ListStoryItem
import com.example.subintermediate.ui.detail.DetailActivity

class StoryAdapter(diffCallback: DiffUtil.ItemCallback<ListStoryItem>) :
    PagingDataAdapter<ListStoryItem, StoryAdapter.StoryViewHolder>(diffCallback) {

    inner class StoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(story: ListStoryItem?) {
            story?.let {
                val storyImage = itemView.findViewById<ImageView>(R.id.storyImage)
                val storyTitle = itemView.findViewById<TextView>(R.id.storyTitle)
                val storydesc = itemView.findViewById<TextView>(R.id.desc)

                Glide.with(itemView.context).load(story.photoUrl).into(storyImage)

                storyTitle.text = story.name
                storydesc.text = story.description

                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, DetailActivity::class.java)
                    intent.putExtra("EXTRA_DATA", story.id)

                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            itemView.context as Activity,
                            Pair(storyImage, "profile"),
                            Pair(storyTitle, "name"),
                            Pair(storydesc, "description"),
                        )
                    itemView.context.startActivity(intent, optionsCompat.toBundle())
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_story, parent, false)
        return StoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val storyItem = getItem(position)
        holder.bind(storyItem)
    }

    companion object {
        val STORY_COMPARATOR = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean =
                oldItem == newItem
        }
    }
}




