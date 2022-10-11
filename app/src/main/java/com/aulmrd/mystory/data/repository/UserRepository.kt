package com.aulmrd.mystory.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.aulmrd.mystory.data.response.LoginResponse
import com.aulmrd.mystory.data.response.RegisterResponse
import com.aulmrd.mystory.data.api.ApiServices
import com.aulmrd.mystory.data.result.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserRepository private constructor(
    private val dataStore: DataStore<Preferences>,
    private val apiServices: ApiServices
) {

    fun register(name: String, email:String, password: String): LiveData<Result<RegisterResponse>> = liveData {
       emit(Result.Loading)
       try {
           val result = apiServices.register(name, email, password)
           emit(Result.Success(result))
       } catch (e: Exception)
       {
           e.printStackTrace()
           emit(Result.Error(e.message.toString()))
       }
    }
    fun login(email: String, password: String) : LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val result = apiServices.login(email, password)
            emit(Result.Success(result))
        }catch (e: Exception)
        {
            e.printStackTrace()
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[TOKEN] ?: ""
        }
    }

    fun doLogin(): Flow<Boolean>{
        return dataStore.data.map { preferences ->
            preferences[STATE_KEY] ?: false
        }
    }

    suspend fun setToken(token: String, doLogin: Boolean){
        dataStore.edit { preferences ->
            preferences[TOKEN] = token
            preferences[STATE_KEY] = doLogin
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences[TOKEN] = ""
            preferences[STATE_KEY] = false
        }
    }

    private val USERNAME_KEY = stringPreferencesKey("user_name")

    fun getUsername(): Flow<String> = dataStore.data.map {
        it[USERNAME_KEY] ?: DEFAULT_VALUE
    }

    suspend fun saveUsername(name: String) {
        dataStore.edit {
            it[USERNAME_KEY] = name
        }
    }

    companion object{
        @Volatile
        private var  INSTANCE: UserRepository? = null
        const val DEFAULT_VALUE = "Coder"

        private val TOKEN = stringPreferencesKey("token")
        private val STATE_KEY = booleanPreferencesKey("state")

        fun getInstance(
            dataStore: DataStore<Preferences>,
            apiServices: ApiServices
        ): UserRepository {
            return INSTANCE ?: synchronized(this){
                val instance = UserRepository(dataStore, apiServices)
                INSTANCE = instance
                instance
            }
        }
    }
}