package com.aulmrd.mystory.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.room.Dao
import com.aulmrd.mystory.data.response.StoriesResponse
import com.aulmrd.mystory.data.response.UploadResponse
import com.aulmrd.mystory.data.api.ApiServices
import com.aulmrd.mystory.data.entity.StoryEntity
import com.aulmrd.mystory.data.result.Result
import com.aulmrd.mystory.data.room.StoryDatabase
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository(private val apiServices: ApiServices,
                      private val dao: Dao,
                      private val storyDatabase: StoryDatabase
) {

    fun getStories(token: String): LiveData<Result<StoriesResponse>> = liveData{
        emit(Result.Loading)
        try {
            val client = apiServices.getStories("Bearer $token")
            emit(Result.Success(client))
        }catch (e: Exception)
        {
            Log.d( "StoryRepository", "getStories: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getStoryFromDatabase() : LiveData<List<StoryEntity>> {
        return dao.getStoryFromDatabase()
    }

    fun uploadStory(token: String,
                    imageMultipart: MultipartBody.Part,
                    lat: RequestBody?,
                    lon: RequestBody?,
                    desc: RequestBody): LiveData<Result<UploadResponse>> = liveData {
        emit(Result.Loading)
        try {
            val client = apiServices.uploadStory("Bearer $token", imageMultipart, desc)
            emit (Result.Success(client))

        }catch (e: Exception){
            e.printStackTrace()
            Log.d("StoryRepository", "UploadStory: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            apiServices: ApiServices,
            dao: Dao,
            userDatabase: StoryDatabase
        ): StoryRepository =
            instance ?: synchronized(this){
                instance ?: StoryRepository(apiServices,dao, userDatabase)
            }.also { instance = it }
    }
}