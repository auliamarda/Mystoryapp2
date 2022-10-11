package com.aulmrd.mystory.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aulmrd.mystory.data.response.ListStoryItem
import com.aulmrd.mystory.databinding.ActivityDetailBinding
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "Story Detail"

        val story = intent.getParcelableExtra<ListStoryItem>(EXTRA_STORY)
        binding.apply {
            tvUsername.text = story?.name
            tvDescription.text = story?.description
        }
        Glide.with(this)
            .load(story?.photoUrl)
            .into(binding.imgAvatar)
    }

    companion object{
        const val EXTRA_STORY = "extra_story"
    }

}