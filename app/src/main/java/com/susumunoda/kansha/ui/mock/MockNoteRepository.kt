package com.susumunoda.kansha.ui.mock

import com.susumunoda.kansha.data.note.Note
import com.susumunoda.kansha.data.note.NoteRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class MockNoteRepository(
    private val database: MutableMap<String, MutableList<MockNote>> = mutableMapOf(),
    private val mockLatency: Boolean = false,
    private val mockLatencyMillis: Long = 1000,
    private val errorOnAddNote: Boolean = false,
    private val errorOnNotesFlow: Boolean = false,
) : NoteRepository {
    override fun newInstance(message: String, categoryId: String?) =
        MockNote(message = message, categoryId = categoryId)

    override fun notesFlow(userId: String): Flow<List<Note>> =
        notesFlowInternal(userId, false, null)

    override fun notesFlow(userId: String, categoryId: String?) =
        notesFlowInternal(userId, true, categoryId)

    private fun notesFlowInternal(userId: String, filterByCategory: Boolean, categoryId: String?) =
        flow {
            if (mockLatency) {
                delay(mockLatencyMillis)
            }

            if (errorOnNotesFlow) {
                throw IllegalArgumentException("Could not get notes for user $userId")
            }

            if (database[userId] == null) {
                database[userId] = mutableListOf()
            }

            val notes = database[userId]!!
            if (filterByCategory) {
                emit(notes.filter { it.categoryId == categoryId })
            } else {
                emit(notes)
            }
        }

    override suspend fun addNote(userId: String, note: Note) {
        if (mockLatency) {
            delay(mockLatencyMillis)
        }

        if (errorOnAddNote) {
            throw IllegalArgumentException("Could not add note $note for user $userId")
        }

        if (database[userId] == null) {
            database[userId] = mutableListOf()
        }

        database[userId]!!.add(note as MockNote)
    }
}
