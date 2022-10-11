package com.aulmrd.mystory.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.aulmrd.mystory.data.repository.StoryRepository
import com.aulmrd.mystory.data.repository.UserRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val userRepository: UserRepository, private val storyRepository: StoryRepository) : ViewModel() {
    fun getToken() : LiveData<String> {
        return userRepository.getToken().asLiveData()
    }
    fun doLogin() : LiveData<Boolean> {
        return userRepository.doLogin().asLiveData()
    }
    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }

    fun getStories(token: String) = storyRepository.getStories(token)

    fun getName() : LiveData<String> = userRepository.getUsername().asLiveData()
    suspend fun saveUserToken(value: String) = userRepository.saveUsername(value)
}