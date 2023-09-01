package com.susumunoda.kansha.repository.category

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.susumunoda.kansha.auth.AuthController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FirebaseCategoryRepository @Inject constructor(
    authController: AuthController
) : CategoryRepository {
    private val db = Firebase.firestore
    private val _categoriesStateFlow = MutableStateFlow<List<FirebaseCategory>>(emptyList())
    override val categoriesStateFlow = _categoriesStateFlow.asStateFlow()

    init {
        // Assume that the user is already logged by the time this instance is initialized
        db.collection(collectionPath(authController.sessionFlow.value.user.id))
            .orderBy("order")
            .addSnapshotListener { snapshot, exception ->
                if (exception == null) {
                    _categoriesStateFlow.update {
                        snapshot!!.documents.mapNotNull {
                            it.toObject<FirebaseCategory>()?.apply {
                                // ID is not a persisted field, so add it here dynamically
                                id = it.id
                            }
                        }
                    }
                }
            }
    }

    override fun newInstance(name: String, photoUrl: String, order: Int) = FirebaseCategory(
        name = name,
        photoUrl = photoUrl,
        order = order
    )

    override suspend fun addCategory(userId: String, category: Category) =
        suspendCoroutine { cont ->
            db.collection(collectionPath(userId))
                .add(category)
                .addOnSuccessListener { cont.resume(Unit) }
                .addOnFailureListener { cont.resumeWithException(it) }
        }

    private fun collectionPath(userId: String) = "categories/$userId/all"
}