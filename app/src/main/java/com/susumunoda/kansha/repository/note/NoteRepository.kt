package com.susumunoda.kansha.repository.note

import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun newInstance(message: String, categoryId: String?): Note
    fun notesFlow(userId: String): Flow<List<Note>>
    fun notesFlow(userId: String, categoryId: String?): Flow<List<Note>>
    suspend fun addNote(userId: String, note: Note)
}