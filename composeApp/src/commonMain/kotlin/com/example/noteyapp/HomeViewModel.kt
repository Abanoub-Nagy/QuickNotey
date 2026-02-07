package com.example.noteyapp

import androidx.lifecycle.ViewModel
import com.example.noteyapp.model.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlin.collections.emptyList

class HomeViewModel : ViewModel() {

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes


    fun addNotes(note: Note) {
        _notes.update {
            it + note
        }
    }

}