package com.susumunoda.kansha.repository.user

import com.google.firebase.firestore.ktx.toObject
import com.susumunoda.android.firebase.firestore.FirestoreService
import javax.inject.Inject

class FirebaseUserRepository @Inject constructor(
    private val firestoreService: FirestoreService
) : UserRepository {
    companion object {
        const val COLLECTION = "users"
    }

    override fun newInstance(displayName: String): User = FirebaseUser(displayName = displayName)

    override suspend fun getUser(id: String) =
        firestoreService.getDocument(COLLECTION, id).toObject<FirebaseUser>()!!

    override suspend fun setUser(id: String, user: User) =
        firestoreService.setDocument(COLLECTION, id, user)
}