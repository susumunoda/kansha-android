package com.susumunoda.kansha.auth

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class FirebaseAuthController @Inject constructor() : AuthController {
    private val auth = Firebase.auth
    private var _sessionFlow = MutableStateFlow(Session.LOGGED_OUT)
    override val sessionFlow = _sessionFlow.asStateFlow()

    init {
        auth.addAuthStateListener { updatedAuth ->
            synchronized(this) {
                val currentSession = sessionFlow.value
                val updatedUser = updatedAuth.currentUser
                if (updatedUser == null) {
                    _sessionFlow.update { Session.LOGGED_OUT }
                } else if (updatedUser.uid != currentSession.currentUser.id) {
                    _sessionFlow.update { Session(User(updatedUser.uid)) }
                }
            }
        }
    }

    override fun createUser(
        email: String,
        password: String,
        onSuccess: (User) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        assert(_sessionFlow.value == Session.LOGGED_OUT) { "Cannot create user while already logged in" }
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { onSuccess(User(it.user!!.uid)) }
            .addOnFailureListener(onFailure)
    }

    override fun login(
        email: String,
        password: String,
        onSuccess: (User) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        assert(_sessionFlow.value == Session.LOGGED_OUT) { "Cannot login user while already logged in" }
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { onSuccess(User(it.user!!.uid)) }
            .addOnFailureListener(onFailure)
    }

    override fun logout() {
        auth.signOut()
    }
}