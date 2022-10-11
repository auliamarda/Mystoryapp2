package com.aulmrd.mystory.di

import com.aulmrd.mystory.data.api.ApiConfig
import com.aulmrd.mystory.data.repository.StoryRepository

object StoryInject {
    fun provideRepository(): StoryRepository {
        val apiServices = ApiConfig.getApiService()
        return StoryRepository.getInstance(apiServices)
    }
}