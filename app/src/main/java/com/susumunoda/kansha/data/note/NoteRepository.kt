package com.susumunoda.kansha.data.note

import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun newInstance(message: String, labels: List<Label>): Note
    fun notesFlow(userId: String): Flow<List<Note>>
    suspend fun addNote(userId: String, note: Note)
    fun labelsFlow(userId: String): Flow<List<Label>>
}