package com.aulmrd.mystory.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.aulmrd.mystory.data.api.ApiServices
import com.aulmrd.mystory.data.entity.RemoteKeys
import com.aulmrd.mystory.data.entity.StoryEntity
import com.aulmrd.mystory.data.room.StoryDatabase
import com.aulmrd.mystory.utils.INITIAL_PAGE_INDEX
import com.aulmrd.mystory.utils.LOCATION

@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator (
    private val database: StoryDatabase,
    private val apiServices: ApiServices
    ): RemoteMediator<Int, StoryEntity>(){

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, page>
    ): MediatorResult {
        val page = when(loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosetsToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1)?: INITIAL_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        try {
            val responseData = apiServices.getStories(page, state.config.pageSize, LOCATION)
            val endOfPaginationReached = responseData.listStory.isEmpty()

            database.withTransaction {
                if (loadType == LoadType.REFRESH){
                    database.remoteKeyDao().deleteRemoteKeys()
                    database.dao().deleteAll()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = responseData.listStory.map {
                    RemoteKeys(id = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                database.remoteKeyDao().insertAll(keys)
                val newStory = ArrayList<StoryEntity>()
                responseData.listStory.forEach {
                    val storyEntity = StoryEntity(
                        it.photoUrl,
                        it.name,
                        it.description,
                        it.lon,
                        it.id,
                        it.lat)
                    newStory.add(storyEntity)
                }
               database.dao().insertStory(newStory)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, StoryEntity>): RemoteKeys? {
      return state.pages.lastOrNull{ it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
          database.remoteKeyDao().getRemoteKeysId(data.id)
      }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, StoryEntity>): RemoteKeys? {
      return state.pages.firstOrNull(){ it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
        database.remoteKeyDao().getRemoteKeysId(data.id)
      }
    }

    private suspend fun getRemoteKeyClosetsToCurrentPosition(state: PagingState<Int, StoryEntity>): RemoteKeys? {
       return state.anchorPosition?.let { position ->
           state.closestItemToPosition(position)?.id?.let {id ->
               database.remoteKeyDao().getRemoteKeysId(id)
           }
       }
    }
}