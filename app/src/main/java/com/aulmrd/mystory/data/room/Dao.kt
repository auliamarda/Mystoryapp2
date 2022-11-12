package com.aulmrd.mystory.data.room

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.aulmrd.mystory.data.entity.StoryEntity
import retrofit2.http.Query

@Dao
interface Dao {

    @androidx.room.Query("SELECT * FROM story_entity ORDER BY createdAt DESC")
    fun getStories(): PagingSource<Int, StoryEntity>

    @androidx.room.Query("SELECT * FROM story_entity")
    fun getStoryFromDatabase(): LiveData<List<StoryEntity>>

    @androidx.room.Query("SELECT * FROM story_entity ORDER BY createdAt DESC")
    suspend fun getStoriesForWidget(): List<StoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(storyEntity: List<StoryEntity>)

    @androidx.room.Query("DELETE FROM story_entity")
    suspend fun deleteAll()

}