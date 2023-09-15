package com.susumunoda.kansha.auth

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.susumunoda.android.auth.AuthController
import com.susumunoda.android.auth.Session
import com.susumunoda.android.auth.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FirebaseAuthController @Inject constructor() : AuthController {
    private val auth = Firebase.auth
    private var _sessionFlow = MutableStateFlow(Session.UNKNOWN)
    override val sessionFlow = _sessionFlow.asStateFlow()

    init {
        auth.addAuthStateListener { newAuth ->
            if (newAuth.currentUser != null) {
                _sessionFlow.update { Session(User(newAuth.currentUser!!.uid)) }
            } else {
                _sessionFlow.update { Session.LOGGED_OUT }
            }
        }
    }

    override suspend fun createUser(email: String, password: String) = suspendCoroutine { cont ->
        assert(_sessionFlow.value == Session.LOGGED_OUT) { "Cannot create user while already logged in" }

        auth.createUserWithEmailAndPassword(email, password)
            // Session has been updated by auth state listener
            .addOnSuccessListener { cont.resume(_sessionFlow.value.user) }
            .addOnFailureListener { cont.resumeWithException(it) }
    }

    override suspend fun login(email: String, password: String) = suspendCoroutine { cont ->
        assert(_sessionFlow.value == Session.LOGGED_OUT) { "Cannot login user while already logged in" }

        auth.signInWithEmailAndPassword(email, password)
            // Session has been updated by auth state listener
            .addOnSuccessListener { cont.resume(_sessionFlow.value.user) }
            .addOnFailureListener { cont.resumeWithException(it) }
    }

    override fun logout() {
        // Session will be updated by auth state listener
        auth.signOut()
    }
}