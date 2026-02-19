package com.example.noteyapp

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.noteyapp.data.datastore.createDataStore
import com.example.noteyapp.data.datastore.dataStoreFileName


fun createDataStore(context: Context): DataStore<Preferences> = createDataStore(
    producePath = { context.filesDir.resolve(dataStoreFileName).absolutePath })