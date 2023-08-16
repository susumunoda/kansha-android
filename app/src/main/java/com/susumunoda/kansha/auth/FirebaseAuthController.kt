package com.susumunoda.kansha.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FirebaseAuthController @Inject constructor() : AuthController {
    private val auth = Firebase.auth
    private var _sessionFlow = MutableStateFlow(Session.LOGGED_OUT)
    override val sessionFlow = _sessionFlow.asStateFlow()

    // This was initially implemented as a lambda, but it seems that calls to removeAuthStateListener
    // below do *not* actually remove a lambda-defined listener from the auth object.
    // An anonymous inner class — i.e. object: FirebaseAuth.AuthStateListener { ... } — behaved
    // as expected, but this caused a persistent IDE suggestion to convert it to a lambda; hence
    // a proper class was created for clarity and cleanliness.
    private inner class UpdateSessionListener : FirebaseAuth.AuthStateListener {
        override fun onAuthStateChanged(newAuth: FirebaseAuth) {
            if (newAuth.currentUser != null) {
                updateSession(newAuth.currentUser!!)
            }
        }
    }

    private val automaticLoginListener = UpdateSessionListener()

    init {
        // The Firebase SDK may automatically re-authenticate the user on app start if they had
        // previously been logged in. In that case, and only in that case, it is necessary to
        // run this callback to update the session state to reflect the Firebase auth state.
        // While we could rely on the same mechanism to update the session state on *any* auth
        // state change, including those initiated by the user, it is preferable to set the state
        // explicitly in each method (createUser, login, etc) to make it clearer when the state
        // change is actually happening.
        auth.addAuthStateListener(automaticLoginListener)
    }

    override suspend fun createUser(email: String, password: String) = suspendCoroutine { cont ->
        // Session state is set manually below
        auth.removeAuthStateListener(automaticLoginListener)

        assert(_sessionFlow.value == Session.LOGGED_OUT) { "Cannot create user while already logged in" }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                updateSession(it.user!!)
                cont.resume(_sessionFlow.value.user)
            }
            .addOnFailureListener { cont.resumeWithException(it) }
    }

    override suspend fun login(email: String, password: String) = suspendCoroutine { cont ->
        // Session state is set manually below
        auth.removeAuthStateListener(automaticLoginListener)

        assert(_sessionFlow.value == Session.LOGGED_OUT) { "Cannot login user while already logged in" }

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                updateSession(it.user!!)
                cont.resume(_sessionFlow.value.user)
            }
            .addOnFailureListener { cont.resumeWithException(it) }
    }

    override fun logout() {
        // Session state is set manually below
        auth.removeAuthStateListener(automaticLoginListener)

        auth.signOut()

        _sessionFlow.update { Session.LOGGED_OUT }
    }

    private fun updateSession(currentUser: FirebaseUser) =
        _sessionFlow.update { Session(currentUser.uid) }
}