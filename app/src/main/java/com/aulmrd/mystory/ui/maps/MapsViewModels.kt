package com.aulmrd.mystory.ui.maps

import androidx.lifecycle.ViewModel
import com.aulmrd.mystory.data.repository.StoryRepository

class MapsViewModels(private val dataRepository: StoryRepository): ViewModel() {
    fun getStoryLocation() = dataRepository.getStoryFromDatabase()
}