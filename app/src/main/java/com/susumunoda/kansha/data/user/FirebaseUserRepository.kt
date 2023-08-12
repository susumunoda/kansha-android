package com.susumunoda.kansha.data.user

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FirebaseUserRepository @Inject constructor() : UserRepository {
    private val db = Firebase.firestore

    companion object {
        const val COLLECTION = "users"
    }

    // No need for withContext(Dispatchers.IO) because the Firebase API uses callbacks (i.e. the
    // code block provided here does not block the main thread).
    override suspend fun getUserData(id: String) = suspendCoroutine { cont ->
        db.collection(COLLECTION)
            .document(id)
            .get()
            .addOnSuccessListener { document ->
                val userData = document.toObject<UserData>()
                if (userData != null) {
                    cont.resume(userData)
                } else {
                    cont.resumeWithException(IllegalArgumentException("Could not find user with id $id"))
                }
            }
            .addOnFailureListener { cont.resumeWithException(it) }
    }

    override fun saveUserData(
        id: String,
        userData: UserData,
        onSuccess: () -> Unit,
        onError: (Exception?) -> Unit
    ) {
        db.collection(COLLECTION)
            .document(id)
            .set(userData)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener(onError)
    }
}