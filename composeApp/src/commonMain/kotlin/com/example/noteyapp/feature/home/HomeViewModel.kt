package com.example.noteyapp.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteyapp.data.db.NoteDatabase
import com.example.noteyapp.model.Note
import kotlinx.coroutines.launch

class HomeViewModel(
    val noteDatabase: NoteDatabase,
) : ViewModel() {

    private val dao = noteDatabase.noteDao()
    private val _notes = dao.getAllNotes()
    val notes = _notes


    fun addNotes(note: Note) {
        viewModelScope.launch {
            dao.insertNote(note)
        }
    }

}