package com.aulmrd.mystory.di

import android.content.Context
import com.aulmrd.mystory.data.api.ApiConfig
import com.aulmrd.mystory.data.repository.StoryRepository
import com.aulmrd.mystory.data.room.StoryDatabase

object StoryInject {
    fun provideRepository(context: Context): StoryRepository {
        val apiServices = ApiConfig.getApiService()
        val database = StoryDatabase.getInstance(context)
        val dao = database.dao()
        return StoryRepository.getInstance(apiServices, dao, database)
    }
}