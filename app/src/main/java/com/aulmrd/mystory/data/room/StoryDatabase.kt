package com.aulmrd.mystory.data.room

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.aulmrd.mystory.data.entity.RemoteKeys
import com.aulmrd.mystory.data.entity.StoryEntity
import com.aulmrd.mystory.utils.DATABASE_NAME
import com.aulmrd.mystory.utils.DATABASE_VERSION

@Database(
    entities = [StoryEntity::class, RemoteKeys::class],
    version = DATABASE_VERSION,
    exportSchema = true)
abstract class StoryDatabase: RoomDatabase() {

    abstract fun remoteKeyDao(): RemoteKeysDao
    abstract fun dao(): Dao

    companion object {
        @Volatile
        private var instance: StoryDatabase? = null
        fun getInstance(context: Context): StoryDatabase =
            instance?: Room.databaseBuilder(
                context.applicationContext,
                StoryDatabase::class.java, DATABASE_NAME).build()
    }
}