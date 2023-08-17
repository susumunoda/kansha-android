package com.susumunoda.kansha.data.note

class MockNoteRepository(private val database: MutableMap<String, MutableList<MockNote>> = mutableMapOf()) :
    NoteRepository {
    override fun newInstance(message: String, labels: List<String>) =
        MockNote(message = message, labels = labels)

    override suspend fun getNotes(userId: String): List<Note> {
        if (database[userId] == null) {
            database[userId] = mutableListOf()
        }
        return database[userId]!!
    }

    override suspend fun addNote(userId: String, note: Note) {
        if (database[userId] == null) {
            database[userId] = mutableListOf()
        }
        database[userId]!!.add(note as MockNote)
    }
}