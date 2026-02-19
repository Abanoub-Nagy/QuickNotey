package com.example.noteyapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.noteyapp.model.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM note WHERE isDeleted = 0 ORDER BY updatedAt DESC")
    fun getAllNotes(): Flow<List<Note>>

    @Query("SELECT * FROM note WHERE id = :id")
    suspend fun getNoteById(id: String): Note?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotes(notes: List<Note>)

    @Update
    suspend fun updateNote(note: Note)

    @Query("DELETE FROM note WHERE id = :id")
    suspend fun deleteNote(id: String)

    @Query("UPDATE note SET isDeleted = 1, isDirty = 1, updatedAt = :updatedAt WHERE id = :id")
    suspend fun softDeleteNote(id: String, updatedAt: String)

    @Query("SELECT * FROM note WHERE isDirty = 1")
    suspend fun getDirtyNotes(): List<Note>

    @Query("UPDATE note SET isDirty = 0 WHERE id IN (:noteIds)")
    suspend fun markAsSynced(noteIds: List<String>)

    @Query("DELETE FROM note")
    suspend fun clearAll()
}