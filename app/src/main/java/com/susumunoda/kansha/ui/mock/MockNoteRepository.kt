package com.susumunoda.kansha.ui.mock

import com.susumunoda.kansha.data.note.Label
import com.susumunoda.kansha.data.note.Note
import com.susumunoda.kansha.data.note.NoteRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

internal class MockNoteRepository(
    private val notes: MutableMap<String, MutableList<MockNote>> = mutableMapOf(),
    private val labels: MutableMap<String, MutableList<Label>> = mutableMapOf(),
    private val mockLatency: Boolean = false,
    private val mockLatencyMillis: Long = 1000,
    private val errorOnAddNote: Boolean = false,
    private val errorOnNotesFlow: Boolean = false,
    private val errorOnLabelsFlow: Boolean = false
) : NoteRepository {
    companion object {
        val LABEL_NATURE = Label(0, "Nature", "🌲")
        val LABEL_MUSIC = Label(1, "Music", "🎶")
        val LABEL_FRIENDS = Label(2, "Friends", "😊")
        val LABEL_FAMILY = Label(3, "Family", "👨‍👩‍👧‍👦")
        val LABEL_MEMORIES = Label(4, "Memories")
        val LABEL_FOOD = Label(5, "Food", "🌮")
        val LABEL_TRAVEL = Label(6, "Travel", "🛫")
        val LABEL_ADVENTURES = Label(7, "Adventure")
        val LABEL_ART = Label(8, "Art", "🎨")
        val LABEL_PETS = Label(9, "Pets", "🐈")
        val LABEL_HEALTH = Label(10, "Health", "🏥")
        val LABEL_LAUGHTER = Label(11, "Laughter", "🤣")

        val DEFAULT_LABELS
            // Return new mutable list for every caller
            get() = mutableListOf(
                LABEL_NATURE,
                LABEL_MUSIC,
                LABEL_FRIENDS,
                LABEL_FAMILY,
                LABEL_MEMORIES,
                LABEL_FOOD,
                LABEL_TRAVEL,
                LABEL_ADVENTURES,
                LABEL_ART,
                LABEL_PETS,
                LABEL_HEALTH,
                LABEL_LAUGHTER
            )
    }

    override fun newInstance(message: String, labels: List<Label>) =
        MockNote(message = message, labels = labels)

    override fun notesFlow(userId: String) = flow {
        if (mockLatency) {
            delay(mockLatencyMillis)
        }

        if (errorOnNotesFlow) {
            throw IllegalArgumentException("Could not get notes for user $userId")
        }

        if (notes[userId] == null) {
            notes[userId] = mutableListOf()
        }

        emit(notes[userId]!!)
    }

    override suspend fun addNote(userId: String, note: Note) {
        if (mockLatency) {
            delay(mockLatencyMillis)
        }

        if (errorOnAddNote) {
            throw IllegalArgumentException("Could not add note $note for user $userId")
        }

        if (notes[userId] == null) {
            notes[userId] = mutableListOf()
        }

        notes[userId]!!.add(note as MockNote)
    }

    override fun labelsFlow(userId: String) = flow {
        if (mockLatency) {
            delay(mockLatencyMillis)
        }

        if (errorOnLabelsFlow) {
            throw IllegalArgumentException("Could not get labels for user $userId")
        }

        if (labels[userId] == null) {
            labels[userId] = mutableListOf()
        }

        emit(labels[userId]!!)
    }
}
