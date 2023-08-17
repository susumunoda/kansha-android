package com.susumunoda.kansha.data.note

interface NoteRepository {
    fun newInstance(message: String, labels: List<String>): Note
    suspend fun getNotes(userId: String): List<Note>
    suspend fun addNote(userId: String, note: Note)
}