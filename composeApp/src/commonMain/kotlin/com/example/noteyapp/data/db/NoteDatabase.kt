package com.example.noteyapp.data.db

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.example.noteyapp.model.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

@Database(entities = [Note::class], version = 1, exportSchema = true)
@ConstructedBy(NoteDatabaseConstructor::class)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}


@Suppress("KotlinNoActualForExpect")
expect object NoteDatabaseConstructor : RoomDatabaseConstructor<NoteDatabase> {
    override fun initialize(): NoteDatabase
}

fun getNoteDatabase(builder: RoomDatabase.Builder<NoteDatabase>): NoteDatabase {
    return builder.setDriver(BundledSQLiteDriver()).setQueryCoroutineContext(Dispatchers.IO).build()
}