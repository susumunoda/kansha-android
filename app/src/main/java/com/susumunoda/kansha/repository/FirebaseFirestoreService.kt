package com.susumunoda.kansha.repository

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Query.Direction
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FirebaseFirestoreService @Inject constructor() {
    class Order(val field: String, val direction: Direction)

    suspend inline fun <reified T : Any> getDocument(
        path: String,
        id: String,
        crossinline transform: T.(DocumentSnapshot) -> Unit = { }
    ) = suspendCoroutine { cont ->
        Firebase.firestore.collection(path)
            .document(id)
            .get()
            .addOnSuccessListener { document ->
                val result = document.toObject<T>()
                if (result != null) {
                    cont.resume(result.apply { transform(document) })
                } else {
                    cont.resumeWithException(IllegalArgumentException("Could not get document at path '$path'"))
                }
            }
            .addOnFailureListener { cont.resumeWithException(it) }
    }

    inline fun <reified T : Any> getDocumentsFlow(
        path: String,
        filters: List<Filter> = emptyList(),
        order: Order? = null,
        crossinline transform: T.(DocumentSnapshot) -> Unit = { }
    ): Flow<List<T>> {
        var query: Query = Firebase.firestore.collection(path)
        for (filter in filters) {
            query = query.where(filter)
        }
        if (order != null) {
            query = query.orderBy(order.field, order.direction)
        }
        return query
            .snapshots()
            .map { snapshot ->
                snapshot.documents.mapNotNull { document ->
                    document.toObject<T>()?.apply { transform(document) }
                }
            }
    }

    suspend fun <T : Any> addDocument(path: String, value: T) =
        suspendCoroutine { cont ->
            Firebase.firestore.collection(path)
                .add(value)
                .addOnSuccessListener { cont.resume(Unit) }
                .addOnFailureListener { cont.resumeWithException(it) }
        }

    suspend fun <T : Any> setDocument(path: String, id: String, value: T) =
        suspendCoroutine { cont ->
            Firebase.firestore.collection(path)
                .document(id)
                .set(value)
                .addOnSuccessListener { cont.resume(Unit) }
                .addOnFailureListener { cont.resumeWithException(it) }
        }
}