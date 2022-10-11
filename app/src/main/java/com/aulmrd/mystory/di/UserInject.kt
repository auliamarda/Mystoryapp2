package com.aulmrd.mystory.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.aulmrd.mystory.data.api.ApiConfig
import com.aulmrd.mystory.data.repository.UserRepository

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

object UserInject {
    fun provideRepository(context: Context): UserRepository {
        val apiServices = ApiConfig.getApiService()
        return UserRepository.getInstance(context.dataStore, apiServices)
    }
}