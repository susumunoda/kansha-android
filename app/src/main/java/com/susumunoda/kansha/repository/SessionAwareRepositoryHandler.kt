package com.susumunoda.kansha.repository

import com.susumunoda.kansha.auth.AuthController
import com.susumunoda.kansha.auth.Session
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

// Singleton to ensure that only one instance is reacting to changes to the session
@Singleton
class SessionAwareRepositoryHandler @Inject constructor(
    repositories: List<@JvmSuppressWildcards SessionAwareRepository>,
    authController: AuthController,
    coroutineScope: CoroutineScope
) {
    init {
        coroutineScope.launch {
            authController.sessionFlow.collect { session ->
                if (session != Session.UNKNOWN) {
                    if (session == Session.LOGGED_OUT) {
                        repositories.forEach { it.onLogout() }
                    } else {
                        repositories.forEach { it.onLogin(session.user.id) }
                    }
                }
            }
        }
    }
}

interface SessionAwareRepository {
    fun onLogin(userId: String)
    fun onLogout()
}
