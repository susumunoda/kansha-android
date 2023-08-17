package com.susumunoda.kansha.data.note

import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class FirebaseNoteRepository @Inject constructor() : NoteRepository {
    private val db = Firebase.firestore

    override fun newInstance(message: String, labels: List<String>) =
        FirebaseNote(message = message, labels = labels)

    override suspend fun getNotes(userId: String): List<Note> = suspendCoroutine { cont ->
        db.collection("notes/$userId/self")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                val notes = mutableListOf<FirebaseNote>()
                for (document in result) {
                    notes.add(document.toObject<FirebaseNote>())
                }
                cont.resume(notes)
            }
            .addOnFailureListener { cont.resumeWithException(it) }
    }

    override suspend fun addNote(userId: String, note: Note) = suspendCoroutine { cont ->
        db.collection("notes/$userId/self")
            .add(note)
            .addOnSuccessListener { cont.resume(Unit) }
            .addOnFailureListener { cont.resumeWithException(it) }
    }
}