package com.susumunoda.kansha.data.note

import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class FirebaseNoteRepository @Inject constructor() : NoteRepository {
    private val db = Firebase.firestore

    override fun newInstance(message: String, category: String) =
        FirebaseNote(message = message, category = category)

    override fun notesFlow(userId: String) =
        db.collection("notes/$userId/self")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .snapshots()
            .map { snapshot -> snapshot.documents.mapNotNull { it.toObject<FirebaseNote>() } }

    override suspend fun addNote(userId: String, note: Note) = suspendCoroutine { cont ->
        db.collection("notes/$userId/self")
            .add(note)
            .addOnSuccessListener { cont.resume(Unit) }
            .addOnFailureListener { cont.resumeWithException(it) }
    }
}