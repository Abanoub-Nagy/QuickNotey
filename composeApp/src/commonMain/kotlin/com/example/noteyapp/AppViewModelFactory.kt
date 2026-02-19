package com.example.noteyapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.noteyapp.data.datastore.DataStoreManager
import com.example.noteyapp.data.db.NoteDatabase
import com.example.noteyapp.data.remote.ApiService
import com.example.noteyapp.data.remote.HttpClientFactory
import com.example.noteyapp.data.remote.SyncRepository
import com.example.noteyapp.feature.home.HomeViewModel
import com.example.noteyapp.feature.profile.ProfileViewModel
import kotlin.reflect.KClass

class AppViewModelFactory(
    private val database: NoteDatabase,
    private val dataStoreManager: DataStoreManager
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        return when (modelClass) {
            HomeViewModel::class -> {
                val dao = database.noteDao()
                val syncDataDao = database.syncDataDao()
                val apiService = ApiService(HttpClientFactory.getHttpClient(), dataStoreManager)
                val syncRepository = SyncRepository(dao, syncDataDao, apiService, dataStoreManager)
                HomeViewModel(dao, dataStoreManager, syncRepository) as T
            }
            ProfileViewModel::class -> {
                ProfileViewModel(dataStoreManager) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel: ${modelClass.simpleName}")
        }
    }
}