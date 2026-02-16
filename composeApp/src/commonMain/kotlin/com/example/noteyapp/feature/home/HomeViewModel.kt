package com.example.noteyapp.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteyapp.data.datastore.DataStoreManager
import com.example.noteyapp.data.db.NoteDatabase
import com.example.noteyapp.model.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    val noteDatabase: NoteDatabase, dataStoreManager: DataStoreManager
) : ViewModel() {

    private val dao = noteDatabase.noteDao()
    private val _notes = dao.getAllNotes()
    val notes = _notes

    val userEmail = MutableStateFlow<String>("")

    init {
        viewModelScope.launch {
            val email = dataStoreManager.getEmail()
            userEmail.value = email ?: ""
        }
    }

    fun addNotes(note: Note) {
        viewModelScope.launch {
            dao.insertNote(note)
        }
    }

}