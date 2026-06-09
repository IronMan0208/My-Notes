package com.chotu.notes.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.chotu.notes.data.dao.NoteDao
import com.chotu.notes.data.local.NoteEntity

@Database(
    entities = [NoteEntity::class],
    version = 1
)

abstract class NotesDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}