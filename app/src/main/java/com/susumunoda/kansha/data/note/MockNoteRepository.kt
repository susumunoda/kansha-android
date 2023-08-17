package com.susumunoda.kansha.data.note

import kotlinx.coroutines.flow.flow

class MockNoteRepository(private val database: MutableMap<String, MutableList<MockNote>> = mutableMapOf()) :
    NoteRepository {
    override fun newInstance(message: String, labels: List<String>) =
        MockNote(message = message, labels = labels)

    override fun notesFlow(userId: String) = flow {
        if (database[userId] == null) {
            database[userId] = mutableListOf()
        }
        emit(database[userId]!!)
    }

    override suspend fun addNote(userId: String, note: Note) {
        if (database[userId] == null) {
            database[userId] = mutableListOf()
        }
        database[userId]!!.add(note as MockNote)
    }
}