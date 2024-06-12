package com.dicoding.picodiploma.loginwithanimation.view.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.loginwithanimation.data.api.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityDetailBinding
import com.dicoding.picodiploma.loginwithanimation.view.StoryViewModelFactory
import com.google.gson.Gson

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val viewModel by viewModels<DetailViewModel> {
        StoryViewModelFactory.getInstance(this)
    }

    companion object {
        const val DATA_STORY = "extra_story"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
    }

    @Suppress("DEPRECATION")
    private fun setupView() {
        showLoading(true) // Show loading indicator

        intent.getStringExtra(DATA_STORY)?.let { data ->
            val storyItem = Gson().fromJson(data, ListStoryItem::class.java)
            binding.apply {
                tvAuthor.text = storyItem.name
                tvDescDetail.text = storyItem.description
                Glide.with(this@DetailActivity)
                    .load(storyItem.photoUrl)
                    .into(ivDetailStory)

                showLoading(false) // Hide loading indicator when data is set
            }
        } ?: run {
            showToast("Data doesn't exist")
            showLoading(false) // Hide loading indicator if there's an error
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
