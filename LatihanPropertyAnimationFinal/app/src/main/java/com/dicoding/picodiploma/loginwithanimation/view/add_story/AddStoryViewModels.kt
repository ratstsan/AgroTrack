package com.dicoding.picodiploma.loginwithanimation.view.add_story

import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.loginwithanimation.data.StoryRepository
import java.io.File

class AddStoryViewModels (private val repository: StoryRepository) : ViewModel() {
    fun uploadImage(file: File, description: String) = repository.uploadImage(file, description)
}