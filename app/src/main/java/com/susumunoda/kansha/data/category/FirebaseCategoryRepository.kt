package com.susumunoda.kansha.data.category

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FirebaseCategoryRepository @Inject constructor() : CategoryRepository {
    private val db = Firebase.firestore

    override fun newInstance() = FirebaseCategory()

    override fun newInstance(name: String, photoUrl: String, order: Int) = FirebaseCategory(
        name = name,
        photoUrl = photoUrl,
        order = order
    )

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

    override suspend fun addCategory(userId: String, category: Category) =
        suspendCoroutine { cont ->
            db.collection(collectionPath(userId))
                .add(category)
                .addOnSuccessListener { cont.resume(Unit) }
                .addOnFailureListener { cont.resumeWithException(it) }
        }

    private fun collectionPath(userId: String) = "categories/$userId/all"
}