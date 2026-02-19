package com.example.noteyapp.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteyapp.data.datastore.DataStoreManager
import com.example.noteyapp.data.remote.SyncRepository
import com.example.noteyapp.data.remote.SyncState
import com.example.noteyapp.model.Note
import com.example.noteyapp.data.db.NoteDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class HomeViewModel(
    private val noteDao: NoteDao,
    private val dataStoreManager: DataStoreManager,
    private val syncRepository: SyncRepository?  // null when logged out
) : ViewModel() {

    // region Notes

    val notes = noteDao.getAllNotes()

    // endregion

    // region Auth state

    val authState = dataStoreManager.authStateFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = DataStoreManager.AuthState()
        )

    val isLoggedIn: Boolean get() = authState.value.isLoggedIn

    // endregion

    // region Sync state

    private val _syncState = MutableStateFlow<SyncState>(SyncState.Idle)
    val syncState: StateFlow<SyncState> = _syncState.asStateFlow()

    // endregion

    init {
        observeSyncState()
        if (isLoggedIn) performSync()
    }

    // region Sync

    private fun observeSyncState() {
        syncRepository?.syncState
            ?.onEach { _syncState.value = it }
            ?.launchIn(viewModelScope)
    }

    fun performSync() {
        viewModelScope.launch {
            syncRepository?.performSync()
        }
    }

    // endregion

    // region Note operations

    fun addNote(note: Note) {
        viewModelScope.launch {
            noteDao.insertNote(note)
            performSync()
        }
    }

    @OptIn(ExperimentalTime::class)
    fun deleteNote(note: Note) {
        viewModelScope.launch {
            noteDao.softDeleteNote(note.id, Clock.System.now().toString())
            performSync()
        }
    }

    // endregion
}