package com.aulmrd.mystory.ui.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aulmrd.mystory.data.repository.StoryRepository
import com.aulmrd.mystory.data.repository.UserRepository
import com.aulmrd.mystory.di.StoryInject
import com.aulmrd.mystory.di.UserInject
import com.aulmrd.mystory.ui.home.HomeViewModel
import com.aulmrd.mystory.ui.story.StoryViewModel

class FactoryStoryViewModel private constructor(private val userRepository: UserRepository,
    private val storyRepository: StoryRepository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(userRepository, storyRepository) as T
            }
            modelClass.isAssignableFrom(StoryViewModel::class.java) -> {
                StoryViewModel(userRepository, storyRepository) as T
            }
            else -> {
                throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
            }
        }
    }

    companion object {
        @Volatile
        private var instance: FactoryStoryViewModel? = null
        fun getInstance(context: Context): FactoryStoryViewModel = instance ?: synchronized(this) {
            instance ?: FactoryStoryViewModel(UserInject.provideRepository(context), StoryInject.provideRepository())
        }.also { instance = it }
    }
}