package com.example.noteyapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.noteyapp.model.SyncMetadata
import kotlinx.coroutines.flow.Flow

@Dao
interface SyncDataDao {

    @Query("SELECT * FROM syncmetadata WHERE id = 1")
    suspend fun getSyncMetadata(): SyncMetadata?

    @Query("UPDATE syncmetadata SET lastSyncTimestamp = :timestamp WHERE id = 1")
    suspend fun updateLastSyncTimestamp(timestamp: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSyncMetadata(metadata: SyncMetadata)

    @Query("UPDATE syncmetadata SET isSyncing = :isSyncing WHERE id = 1")
    suspend fun updateSyncingStatus(isSyncing: Boolean)

    @Query("SELECT * FROM syncmetadata WHERE id = 1")
    fun observeSyncMetadata(): Flow<SyncMetadata?>

}