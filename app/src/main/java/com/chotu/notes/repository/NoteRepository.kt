package com.chotu.notes.repository

import androidx.room.Entity
import com.chotu.notes.data.dao.NoteDao
import com.chotu.notes.data.local.NoteEntity

data class NoteRepository(
    private val noteDao: NoteDao
) {
    suspend fun insertNote(note: NoteEntity) {
        noteDao.insertNote(note)
    }

    suspend fun deleteNote(note: NoteEntity) {
        noteDao.deleteNote(note)
    }

    suspend fun getAllNotes(): List<NoteEntity> {
        return noteDao.getAllNotes()
    }
}
