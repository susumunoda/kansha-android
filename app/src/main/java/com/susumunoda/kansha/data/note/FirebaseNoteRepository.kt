package com.susumunoda.kansha.data.note

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class FirebaseNoteRepository @Inject constructor() : NoteRepository {
    private val db = Firebase.firestore

    override suspend fun getNotes(userId: String): List<Note> = suspendCoroutine { cont ->
        db.collection("notes/$userId/self")
            .get()
            .addOnSuccessListener { result ->
                val notes = mutableListOf<Note>()
                for (document in result) {
                    notes.add(document.toObject<Note>())
                }
                cont.resume(notes)
            }
            .addOnFailureListener { cont.resumeWithException(it) }
    }
}