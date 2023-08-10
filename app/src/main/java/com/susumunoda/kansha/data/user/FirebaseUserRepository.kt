package com.susumunoda.kansha.data.user

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class FirebaseUserRepository @Inject constructor() : UserRepository {
    private val db = Firebase.firestore

    companion object {
        const val COLLECTION = "users"
    }

    override fun getUser(
        id: String,
        onSuccess: (UserData) -> Unit,
        onError: (Exception?) -> Unit
    ) {
        db.collection(COLLECTION)
            .document(id)
            .get()
            .addOnSuccessListener { document ->
                val userData = document.toObject<UserData>()
                if (userData != null) {
                    onSuccess(userData)
                } else {
                    onError(IllegalArgumentException("Could not find user with id $id"))
                }
            }.addOnFailureListener(onError)
    }
}