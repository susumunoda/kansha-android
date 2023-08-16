package com.susumunoda.kansha.data.note

class MockSuccessNoteRepository(private val notes: List<Note>) : NoteRepository {
    override suspend fun getNotes(userId: String): List<Note> = notes
}