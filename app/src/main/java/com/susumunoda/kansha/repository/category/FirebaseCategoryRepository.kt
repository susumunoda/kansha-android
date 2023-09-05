package com.susumunoda.kansha.repository.category

import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.susumunoda.kansha.repository.SessionAwareRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

// Singleton so that all requests for the FirebaseCategoryRepository type will receive the same
// instance — e.g. RepositoryModule#provideCategoryRepository and #provideSessionAwareRepositories
@Singleton
class FirebaseCategoryRepository @Inject constructor() : CategoryRepository,
    SessionAwareRepository {
    private val db = Firebase.firestore
    private val _categoriesStateFlow = MutableStateFlow<List<FirebaseCategory>>(emptyList())
    override val categoriesStateFlow = _categoriesStateFlow.asStateFlow()
    private var listenerRegistration: ListenerRegistration? = null

    override fun onLogin(userId: String) {
        listenerRegistration = db.collection(collectionPath(userId))
            .orderBy("order")
            .addSnapshotListener(this::handleCategoriesSnapshot)
    }

    private fun handleCategoriesSnapshot(
        snapshot: QuerySnapshot?,
        exception: FirebaseFirestoreException?
    ) {
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

    override fun onLogout() {
        _categoriesStateFlow.update { emptyList() }
        listenerRegistration?.remove()
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