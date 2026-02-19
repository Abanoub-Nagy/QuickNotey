package com.example.noteyapp.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteyapp.data.datastore.DataStoreManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    val authState = dataStoreManager.authStateFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = DataStoreManager.AuthState()
        )

    fun logout(onComplete: () -> Unit) {
        viewModelScope.launch {
            dataStoreManager.clearAll()
            onComplete()
        }
    }
}