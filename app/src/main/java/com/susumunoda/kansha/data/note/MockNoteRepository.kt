package com.susumunoda.kansha.data.note

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MockNoteRepository(
    private val notes: MutableMap<String, MutableList<MockNote>> = mutableMapOf(),
    private val labels: MutableMap<String, MutableList<Label>> = mutableMapOf()
) : NoteRepository {
    override fun newInstance(message: String, labels: List<Label>) =
        MockNote(message = message, labels = labels)

    override fun notesFlow(userId: String) = flow {
        if (notes[userId] == null) {
            notes[userId] = mutableListOf()
        }
        emit(notes[userId]!!)
    }

    override suspend fun addNote(userId: String, note: Note) {
        if (notes[userId] == null) {
            notes[userId] = mutableListOf()
        }
        notes[userId]!!.add(note as MockNote)
    }

    override fun labelsFlow(userId: String) = flow {
        if (labels[userId] == null) {
            labels[userId] = mutableListOf()
        }
        emit(labels[userId]!!)
    }
}