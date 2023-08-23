package com.susumunoda.kansha.data.category

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FirebaseCategoryRepository @Inject constructor() : CategoryRepository {
    private val db = Firebase.firestore

    override fun categoriesFlow(userId: String) =
        db.collection("categories/$userId/all")
            .orderBy("order")
            .snapshots()
            .map { snapshot -> snapshot.documents.mapNotNull { it.toObject<Category>() } }
}