package com.dicoding.picodiploma.loginwithanimation.view.detail

import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.loginwithanimation.data.StoryRepository

class DetailViewModel(private val repository: StoryRepository)
    : ViewModel() {
    fun getDetailStory(id: String) = repository.getDetailStory(id)
}