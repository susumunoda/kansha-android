package com.susumunoda.kansha.auth

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FirebaseAuthController @Inject constructor() : AuthController {
    private val auth = Firebase.auth
    private var _sessionFlow = MutableStateFlow(Session.LOGGED_OUT)
    override val sessionFlow = _sessionFlow.asStateFlow()

    override suspend fun createUser(email: String, password: String) = suspendCoroutine { cont ->
        assert(_sessionFlow.value == Session.LOGGED_OUT) { "Cannot create user while already logged in" }
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { updateSessionAndResume(it.user!!, cont) }
            .addOnFailureListener { cont.resumeWithException(it) }
    }

    override suspend fun login(email: String, password: String) = suspendCoroutine { cont ->
        assert(_sessionFlow.value == Session.LOGGED_OUT) { "Cannot login user while already logged in" }
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { updateSessionAndResume(it.user!!, cont) }
            .addOnFailureListener { cont.resumeWithException(it) }
    }

    override fun logout() {
        auth.signOut()
        _sessionFlow.update { Session.LOGGED_OUT }
    }

    private fun updateSessionAndResume(
        currentUser: FirebaseUser,
        continuation: Continuation<User>
    ) {
        val user = User(currentUser.uid)
        _sessionFlow.update { Session(user) }
        continuation.resume(user)
    }
}