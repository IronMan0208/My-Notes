package com.chotu.notes.data.database

import android.content.Context
import androidx.room.Room

object DatabaseProvider {
    fun getDatabase(context: Context): NotesDatabase {
        return Room.databaseBuilder(
            context,
            NotesDatabase::class.java,
            "note_database"
        ).build()
    }
}