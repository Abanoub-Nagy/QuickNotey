package com.example.noteyapp

import androidx.compose.ui.window.ComposeUIViewController
import com.example.noteyapp.data.createDataStore
import com.example.noteyapp.data.datastore.DataStoreManager
import com.example.noteyapp.data.db.getNoteDatabase

fun MainViewController() = ComposeUIViewController {
    App(
        getNoteDatabase(
            getDatabaseBuilder()
        ),   DataStoreManager(
            createDataStore()
        )
    )
}