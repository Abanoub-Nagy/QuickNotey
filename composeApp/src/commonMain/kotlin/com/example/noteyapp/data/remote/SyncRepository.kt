package com.example.noteyapp.data.remote

import com.example.noteyapp.data.db.NoteDao
import com.example.noteyapp.data.db.SyncDataDao
import com.example.noteyapp.model.Note
import com.example.noteyapp.model.NoteChange
import com.example.noteyapp.model.SyncMetadata
import com.example.noteyapp.model.SyncRequest
import com.example.noteyapp.model.SyncResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

class SyncRepository(
    private val userID: String,
    private val noteDao: NoteDao,
    private val syncDataDao: SyncDataDao,
    private val apiService: ApiService
) {

    // Internal state for observing Sync status
    private val _syncState = MutableStateFlow<SyncState>(SyncState.Idle)
    val syncState = _syncState.asStateFlow()

    // ------------------ New: Flow to observe SyncMetadata from DB ------------------
    val syncMetadataFlow: Flow<SyncMetadata?> =
        syncDataDao.observeSyncMetadata()

    // ------------------ Perform Sync ------------------
    suspend fun performSync() = withContext(Dispatchers.IO) {

        val metadata = syncDataDao.getSyncMetadata()
        if (metadata?.isSyncing == true) return@withContext

        _syncState.value = SyncState.Syncing
        syncDataDao.updateSyncingStatus(true)

        try {
            val dirtyNotes = noteDao.getDirtyNotes()
            val syncRequest = SyncRequest(
                since = metadata?.lastSyncTimestamp, changes = dirtyNotes.map { note ->
                    NoteChange(
                        id = note.id,
                        title = note.title,
                        body = note.description,
                        isDeleted = note.isDeleted,
                        updatedAt = note.updatedAt
                    )
                })

            val result = apiService.sync(syncRequest)
            result.fold(onSuccess = { response ->
                processSyncResponse(response)
                syncDataDao.updateLastSyncTimestamp(response.nextSince)
                _syncState.value = SyncState.Success(response)
            }, onFailure = { error ->
                val errorMessage = when (error) {
                    is ApiError.Network -> "Network error. Please try again."
                    is ApiError.Unauthorized -> "Unauthorized. Please login again."
                    is ApiError.Server -> "Server error: ${error.code}"
                    is ApiError.Unknown -> error.message ?: "Unknown error"
                    else -> error.message ?: "Sync failed"
                }
                _syncState.value = SyncState.Error(errorMessage)
            })

        } finally {
            syncDataDao.updateSyncingStatus(false)
        }
    }

    // ------------------ Process Sync Response ------------------
    suspend fun processSyncResponse(response: SyncResponse) = withContext(Dispatchers.IO) {
        // Mark applied notes as synced
        if (response.applied.isNotEmpty()) {
            noteDao.markAsSynced(response.applied)
        }

        // Insert/Update conflict notes from server
        if (response.conflicts.isNotEmpty()) {
            val conflictNotes = response.conflicts.map { noteChange ->
                Note(
                    id = noteChange.id,
                    title = noteChange.title,
                    description = noteChange.body,
                    isDeleted = noteChange.isDeleted,
                    updatedAt = noteChange.updatedAt,
                    isDirty = false,
                    userId = userID
                )
            }
            noteDao.insertNotes(conflictNotes)
        }

        // Insert/Update general changes from server
        if (response.changes.isNotEmpty()) {
            val serverNotes = response.changes.map { noteChange ->
                Note(
                    id = noteChange.id,
                    title = noteChange.title,
                    description = noteChange.body,
                    isDeleted = noteChange.isDeleted,
                    updatedAt = noteChange.updatedAt,
                    isDirty = false,
                    userId = userID
                )
            }
            noteDao.insertNotes(serverNotes)
        }
    }
}

// ------------------ Sync State ------------------
sealed class SyncState {
    object Idle : SyncState()
    object Syncing : SyncState()
    data class Success(val data: SyncResponse) : SyncState()
    data class Error(val error: String) : SyncState()
}
