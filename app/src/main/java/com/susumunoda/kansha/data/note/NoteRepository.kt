package com.susumunoda.kansha.data.note

interface NoteRepository {
    suspend fun getNotesData(userId: String): List<NoteData>
}