package com.susumunoda.kansha.data.note

interface NoteRepository {
    suspend fun getNotes(userId: String): List<Note>
}