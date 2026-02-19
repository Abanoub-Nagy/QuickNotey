package com.example.noteyapp

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.noteyapp.data.db.NoteDatabase


fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<NoteDatabase> {

    val appContext = context.applicationContext
    val dbPath = appContext.getDatabasePath("note_database.db")
    return Room.databaseBuilder(appContext, NoteDatabase::class.java, dbPath.path)

}