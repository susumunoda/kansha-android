package com.susumunoda.kansha.repository.note

import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.ktx.toObject
import com.susumunoda.android.firebase.firestore.FirestoreService
import com.susumunoda.android.firebase.firestore.Order
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FirebaseNoteRepository @Inject constructor(
    private val firestoreService: FirestoreService
) : NoteRepository {
    private enum class Field(val fieldName: String) {
        CREATED_AT("createdAt"),
        CATEGORY_ID("categoryId")
    }

    override fun newInstance(message: String, categoryId: String?) =
        FirebaseNote(message = message, categoryId = categoryId)

    override fun notesFlow(userId: String) =
        firestoreService.getDocumentsFlow(
            path = collectionPath(userId),
            order = Order.descending(Field.CREATED_AT.fieldName)
        ).map { documents ->
            documents.mapNotNull { document ->
                document.toObject<FirebaseNote>()
            }
        }

    override fun notesFlow(userId: String, categoryId: String?) =
        firestoreService.getDocumentsFlow(
            path = collectionPath(userId),
            filters = listOf(Filter.equalTo(Field.CATEGORY_ID.fieldName, categoryId)),
            order = Order.descending(Field.CREATED_AT.fieldName)
        ).map { documents ->
            documents.mapNotNull { document ->
                document.toObject<FirebaseNote>()
            }
        }

    override suspend fun addNote(userId: String, note: Note) =
        firestoreService.addDocument(collectionPath(userId), note)

    private fun collectionPath(userId: String) = "notes/$userId/all"
}