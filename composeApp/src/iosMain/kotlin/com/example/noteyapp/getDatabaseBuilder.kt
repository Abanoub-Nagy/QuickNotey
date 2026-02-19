package com.example.noteyapp

import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.noteyapp.data.db.NoteDatabase
import com.example.noteyapp.data.getDocumentPath
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask


fun getDatabaseBuilder(): RoomDatabase.Builder<NoteDatabase> {
    val dbPath = getDocumentPath() + "/note_database.db"
    return Room.databaseBuilder(name = dbPath)
}

@OptIn(ExperimentalForeignApi::class)
fun getDocumentPath(): String {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null
    )
    return requireNotNull(documentDirectory?.path)
}