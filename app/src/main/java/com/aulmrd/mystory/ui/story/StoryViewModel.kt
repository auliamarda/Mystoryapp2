package com.aulmrd.mystory.ui.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.aulmrd.mystory.data.repository.StoryRepository
import com.aulmrd.mystory.data.repository.UserRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryViewModel(private val userRepository: UserRepository, private val storyRepository: StoryRepository) : ViewModel() {
    fun getToken(): LiveData<String> {
        return userRepository.getToken().asLiveData()
    }
    fun uploadStory(token: String, imageMultipart: MultipartBody.Part, desc: RequestBody) = storyRepository.uploadStory(token, imageMultipart, desc)
}