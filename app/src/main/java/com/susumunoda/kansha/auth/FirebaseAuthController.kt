package com.susumunoda.kansha.auth

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object FirebaseAuthController : AuthController {
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
                } else if (updatedUser.uid != currentSession.getCurrentUser().id) {
                    val user = User(updatedUser.uid, updatedUser.displayName ?: "")
                    _sessionFlow.update { Session(user) }
                }
            }
        }
    }

    override fun createUser(email: String, password: String, onResult: (Throwable?) -> Unit) {
        assert(_sessionFlow.value == Session.LOGGED_OUT) { "Cannot create user while already logged in" }
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task -> onResult(task.exception) }
    }

    override fun login(email: String, password: String, onResult: (Throwable?) -> Unit) {
        assert(_sessionFlow.value == Session.LOGGED_OUT) { "Cannot login user while already logged in" }
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task -> onResult(task.exception) }
    }

    override fun logout() {
        auth.signOut()
    }
}