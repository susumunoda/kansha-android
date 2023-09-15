package com.susumunoda.kansha.repository.note

import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.Query
import com.susumunoda.kansha.repository.FirebaseFirestoreService
import javax.inject.Inject

class FirebaseNoteRepository @Inject constructor(
    private val firestoreService: FirebaseFirestoreService
) : NoteRepository {
    companion object {
        private val DESCENDING_BY_CREATED_AT =
            FirebaseFirestoreService.Order("createdAt", Query.Direction.DESCENDING)
    }

    override fun newInstance(message: String, categoryId: String?) =
        FirebaseNote(message = message, categoryId = categoryId)

    override fun notesFlow(userId: String) =
        firestoreService.getDocumentsFlow<FirebaseNote>(
            path = collectionPath(userId),
            order = DESCENDING_BY_CREATED_AT
        )

    override fun notesFlow(userId: String, categoryId: String?) =
        firestoreService.getDocumentsFlow<FirebaseNote>(
            path = collectionPath(userId),
            filters = listOf(Filter.equalTo("categoryId", categoryId)),
            order = DESCENDING_BY_CREATED_AT
        )

    override suspend fun addNote(userId: String, note: Note) =
        firestoreService.addDocument(collectionPath(userId), note)

    private fun collectionPath(userId: String) = "notes/$userId/all"
}