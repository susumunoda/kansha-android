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

    override suspend fun getNotesData(userId: String): List<NoteData> = suspendCoroutine { cont ->
        db.collection("notes/$userId/self")
            .get()
            .addOnSuccessListener { result ->
                val notesData = mutableListOf<NoteData>()
                for (document in result) {
                    notesData.add(document.toObject<NoteData>())
                }
                cont.resume(notesData)
            }
            .addOnFailureListener { cont.resumeWithException(it) }
    }
}