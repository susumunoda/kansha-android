package com.susumunoda.kansha.data.category

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FirebaseCategoryRepository @Inject constructor() : CategoryRepository {
    private val db = Firebase.firestore

    override fun newInstance() = FirebaseCategory()

    override fun categoriesFlow(userId: String) =
        db.collection(collectionPath(userId))
            .orderBy("order")
            .snapshots()
            .map { snapshot ->
                snapshot.documents.mapNotNull {
                    it.toObject<FirebaseCategory>()?.apply {
                        // ID is not a persisted field, so add it here dynamically
                        id = it.id
                    }
                }
            }

    private fun collectionPath(userId: String) = "categories/$userId/all"
}