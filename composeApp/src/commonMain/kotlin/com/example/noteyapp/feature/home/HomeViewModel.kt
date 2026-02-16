package com.example.noteyapp.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteyapp.data.datastore.DataStoreManager
import com.example.noteyapp.data.db.NoteDatabase
import com.example.noteyapp.data.remote.ApiService
import com.example.noteyapp.data.remote.HttpClientFactory
import com.example.noteyapp.data.remote.SyncRepository
import com.example.noteyapp.data.remote.SyncState
import com.example.noteyapp.model.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.time.Clock

class HomeViewModel(
    private val noteDatabase: NoteDatabase,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val dao = noteDatabase.noteDao()

    // Notes Flow
    val notes = dao.getAllNotes()

    // User email
    private val _userEmail = MutableStateFlow("")
    val userEmail: StateFlow<String> = _userEmail

    // User ID Flow
    private val _userId = MutableStateFlow("")
    val userId: StateFlow<String> = _userId

    // Sync repository
    private var syncRepository: SyncRepository? = null

    // Helper: check if logged in
    fun isLoggedIn(): Boolean = _userId.value.isNotEmpty()

    init {
        viewModelScope.launch {
            // Load user info
            _userEmail.value = dataStoreManager.getEmail() ?: ""
            _userId.value = dataStoreManager.getUserId() ?: ""

            // Initialize SyncRepository if userId exists
            val userID = _userId.value
            if (userID.isNotEmpty()) {
                val apiService = ApiService(HttpClientFactory.getHttpClient(), dataStoreManager)
                syncRepository = SyncRepository(
                    userID, dao, noteDatabase.syncDataDao(), apiService
                )

                // Collect sync state
                syncRepository?.syncState?.collectLatest { state ->
                    when (state) {
                        is SyncState.Idle -> {}
                        is SyncState.Syncing -> { /* show loading */ }
                        is SyncState.Success -> { /* handle success */ }
                        is SyncState.Error -> { /* handle error */ }
                    }
                }

                // Perform initial sync
                performSync()
            }
        }
    }

    // Perform sync via SyncRepository
    fun performSync() {
        viewModelScope.launch {
            syncRepository?.performSync()
        }
    }

    // Add or update a note, then sync
    fun addNote(note: Note) {
        viewModelScope.launch {
            dao.insertNote(note)
            performSync()
        }
    }
}
