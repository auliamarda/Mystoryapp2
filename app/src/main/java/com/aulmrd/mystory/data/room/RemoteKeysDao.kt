package com.aulmrd.mystory.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.aulmrd.mystory.data.entity.RemoteKeys

@Dao
interface RemoteKeysDao {

    @Insert(onConflict = onConflictStrategy.REPLACE)
    suspend fun fun insertAll(remoteKey: List<RemoteKeys>)

    @Query("SELECT * FROM remote_keys WHERE id = :id")
    suspend fun getRemoteKeysId(id: String): RemoteKeys?

    @Query("DELETE FROM remote_keys")
    suspend fun deleteRemoteKeys
}