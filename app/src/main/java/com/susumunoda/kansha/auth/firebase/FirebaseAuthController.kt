package com.susumunoda.kansha.auth.firebase

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.susumunoda.kansha.auth.AuthController
import com.susumunoda.kansha.auth.Session
import com.susumunoda.kansha.auth.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object FirebaseAuthController : AuthController {
    private val auth = Firebase.auth
    private var _sessionFlow = MutableStateFlow(null as Session?)
    override val sessionFlow = _sessionFlow.asStateFlow()

    init {
        auth.addAuthStateListener { updatedAuth ->
            synchronized(this) {
                val currentSession = sessionFlow.value
                val updatedUser = updatedAuth.currentUser
                if (updatedUser == null) {
                    _sessionFlow.update { null }
                } else if (updatedUser.uid != currentSession?.getCurrentUser()?.id) {
                    val user = User(updatedUser.uid, updatedUser.displayName ?: "")
                    _sessionFlow.update { Session(user) }
                }
            }
        }
    }

    override fun createUser(email: String, password: String, onResult: (Throwable?) -> Unit) {
        assert(_sessionFlow.value == null) { "Cannot create user while already logged in" }
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task -> onResult(task.exception) }
    }

    override fun login(email: String, password: String, onResult: (Throwable?) -> Unit) {
        assert(_sessionFlow.value == null) { "Cannot login user while already logged in" }
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task -> onResult(task.exception) }
    }

    override fun logout() {
        auth.signOut()
    }
}