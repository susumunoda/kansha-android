package com.susumunoda.kansha.data.note

class MockSuccessNoteRepository(private val notesData: List<NoteData>) : NoteRepository {
    override suspend fun getNotesData(userId: String): List<NoteData> = notesData
}