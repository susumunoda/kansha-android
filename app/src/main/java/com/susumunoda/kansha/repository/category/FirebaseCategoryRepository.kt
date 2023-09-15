package com.susumunoda.kansha.repository.category

import com.susumunoda.android.firebase.firestore.FirestoreService
import com.susumunoda.android.firebase.firestore.Order
import com.susumunoda.kansha.repository.SessionAwareRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

// Singleton so that all requests for the FirebaseCategoryRepository type will receive the same
// instance — e.g. RepositoryModule#provideCategoryRepository and #provideSessionAwareRepositories
@Singleton
class FirebaseCategoryRepository @Inject constructor(
    private val firestoreService: FirestoreService,
    private val coroutineScope: CoroutineScope
) : CategoryRepository, SessionAwareRepository {
    private val _categoriesStateFlow = MutableStateFlow<List<FirebaseCategory>>(emptyList())
    override val categoriesStateFlow = _categoriesStateFlow.asStateFlow()
    private var job: Job? = null

    private enum class Field(val fieldName: String) {
        ORDER("order")
    }

    override fun onLogin(userId: String) {
        job = coroutineScope.launch {
            firestoreService.getDocumentsFlow<FirebaseCategory>(
                path = collectionPath(userId),
                order = Order.ascending(Field.ORDER.fieldName)
            ) { document ->
                id = document.id
            }.collect { categories ->
                _categoriesStateFlow.update { categories }
            }
        }
    }

    override fun onLogout() {
        _categoriesStateFlow.update { emptyList() }
        job?.cancel()
    }

    override fun newInstance(name: String, photoUrl: String, order: Int) = FirebaseCategory(
        name = name,
        photoUrl = photoUrl,
        order = order
    )

    override suspend fun addCategory(userId: String, category: Category) =
        firestoreService.addDocument(collectionPath(userId), category)


    private fun collectionPath(userId: String) = "categories/$userId/all"
}