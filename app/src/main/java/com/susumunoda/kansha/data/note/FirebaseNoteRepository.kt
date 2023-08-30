package com.susumunoda.kansha.data.note

import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FirebaseNoteRepository @Inject constructor() : NoteRepository {
    private val db = Firebase.firestore

    override fun newInstance(message: String, categoryId: String?) =
        FirebaseNote(message = message, categoryId = categoryId)

    override fun notesFlow(userId: String) =
        notesFlowInternal(userId, false, null)

    override fun notesFlow(userId: String, categoryId: String?) =
        notesFlowInternal(userId, true, categoryId)

    private fun notesFlowInternal(
        userId: String,
        filterByCategory: Boolean,
        categoryId: String?
    ): Flow<List<Note>> {
        val collectionRef = db.collection(collectionPath(userId))

        val query = if (filterByCategory) {
            collectionRef.whereEqualTo("categoryId", categoryId)
        } else {
            collectionRef
        }

        return query.orderBy("createdAt", Query.Direction.DESCENDING)
            .snapshots()
            .map { snapshot -> snapshot.documents.mapNotNull { it.toObject<FirebaseNote>() } }
    }

    override suspend fun addNote(userId: String, note: Note) = suspendCoroutine { cont ->
        db.collection(collectionPath(userId))
            .add(note)
            .addOnSuccessListener { cont.resume(Unit) }
            .addOnFailureListener { cont.resumeWithException(it) }
    }

    private fun collectionPath(userId: String) = "notes/$userId/all"
}