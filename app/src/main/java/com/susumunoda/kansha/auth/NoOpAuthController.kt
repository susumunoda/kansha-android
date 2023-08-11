package com.susumunoda.kansha.auth

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class NoOpAuthController(session: Session = Session.LOGGED_OUT) : AuthController {
    override val sessionFlow: StateFlow<Session> = MutableStateFlow(session).asStateFlow()
    override fun createUser(
        email: String,
        password: String,
        onSuccess: (User) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
    }

    override fun login(
        email: String,
        password: String,
        onSuccess: (User) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
    }

    override fun logout() {}
}