package com.susumunoda.kansha.repository.user

import com.susumunoda.kansha.repository.FirebaseFirestoreService
import javax.inject.Inject

class FirebaseUserRepository @Inject constructor(
    private val firestoreService: FirebaseFirestoreService
) : UserRepository {
    companion object {
        const val COLLECTION = "users"
    }

    override fun newInstance(displayName: String): User = FirebaseUser(displayName = displayName)

    override suspend fun getUser(id: String): FirebaseUser =
        firestoreService.getDocument(COLLECTION, id)

    override suspend fun setUser(id: String, user: User) =
        firestoreService.setDocument(COLLECTION, id, user)
}