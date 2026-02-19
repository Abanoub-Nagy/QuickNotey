package com.example.noteyapp.data.remote

import com.example.noteyapp.data.datastore.DataStoreManager
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
    private val noteDao: NoteDao,
    private val syncDataDao: SyncDataDao,
    private val apiService: ApiService,
    private val dataStoreManager: DataStoreManager
) {

    // region Sync state

    private val _syncState = MutableStateFlow<SyncState>(SyncState.Idle)
    val syncState = _syncState.asStateFlow()

    val syncMetadataFlow: Flow<SyncMetadata?> = syncDataDao.observeSyncMetadata()

    // endregion

    // region Public API

    suspend fun performSync() = withContext(Dispatchers.IO) {
        val userId = dataStoreManager.getUserId() ?: return@withContext

        val metadata = syncDataDao.getSyncMetadata()
        if (metadata?.isSyncing == true) return@withContext

        _syncState.value = SyncState.Syncing
        syncDataDao.updateSyncingStatus(true)

        try {
            val dirtyNotes = noteDao.getDirtyNotes()
            val syncRequest = SyncRequest(
                since = metadata?.lastSyncTimestamp, changes = dirtyNotes.map { it.toNoteChange() })

            apiService.sync(syncRequest).fold(onSuccess = { response ->
                processSyncResponse(response, userId)
                syncDataDao.updateLastSyncTimestamp(response.nextSince)
                _syncState.value = SyncState.Success(response)
            }, onFailure = { error ->
                _syncState.value =
                    SyncState.Error(error as? ApiError ?: ApiError.Unknown(error.message))
            })
        } finally {
            syncDataDao.updateSyncingStatus(false)
        }
    }

    // endregion

    // region Private helpers

    private suspend fun processSyncResponse(
        response: SyncResponse, userId: String
    ) = withContext(Dispatchers.IO) {
        if (response.applied.isNotEmpty()) {
            noteDao.markAsSynced(response.applied)
        }
        val incomingNotes = (response.conflicts + response.changes)
        if (incomingNotes.isNotEmpty()) {
            noteDao.insertNotes(incomingNotes.map { it.toNote(userId) })
        }
    }

    private fun Note.toNoteChange() = NoteChange(
        id = id, title = title, body = description, isDeleted = isDeleted, updatedAt = updatedAt
    )

    private fun NoteChange.toNote(userId: String) = Note(
        id = id,
        title = title,
        description = body,
        isDeleted = isDeleted,
        updatedAt = updatedAt,
        isDirty = false,
        userId = userId
    )

    // endregion
}

// region Sync state

sealed class SyncState {
    data object Idle : SyncState()
    data object Syncing : SyncState()
    data class Success(val data: SyncResponse) : SyncState()
    data class Error(val error: ApiError) : SyncState()
}

// endregion