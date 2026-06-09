package com.chotu.notes.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chotu.notes.data.local.NoteEntity
import com.chotu.notes.repository.NoteRepository
import kotlinx.coroutines.launch

class NotesViewModel(
    private val repository: NoteRepository
) : ViewModel() {
    var notes by mutableStateOf<List<NoteEntity>>(emptyList())
        private set

    fun insertNote(title: String) {
        viewModelScope.launch {
            repository.insertNote(
                NoteEntity(
                    title = title
                )
            )
            loadNotes()
        }
    }

    fun deleteNote(note: NoteEntity){
        viewModelScope.launch {
            repository.deleteNote(note)
            loadNotes()
        }
    }

    fun loadNotes(){
        viewModelScope.launch {
            notes = repository.getAllNotes()
        }
    }
}