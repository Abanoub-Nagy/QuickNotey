package com.example.noteyapp.model


import kotlinx.serialization.Serializable

@Serializable
data class SyncRequest(
    val since: String? = null,
    val changes: List<NoteChange>
)

@Serializable
data class NoteChange(
    val id: String,
    val title: String,
    val body: String,
    val isDeleted: Boolean,
    val updatedAt: String
)

@Serializable
data class SyncResponse(
    val now: String,
    val applied: List<String>,
    val conflicts: List<NoteChange>,
    val changes: List<NoteChange>,
    val nextSince: String
) {
    fun toNotes(userId: String): List<Note> =
        (conflicts + changes).map { noteChange ->
            Note(
                id = noteChange.id,
                title = noteChange.title,
                description = noteChange.body,
                isDeleted = noteChange.isDeleted,
                updatedAt = noteChange.updatedAt,
                isDirty = false,
                userId = userId
            )
        }
}

