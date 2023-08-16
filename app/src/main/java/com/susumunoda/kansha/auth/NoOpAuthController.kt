package com.susumunoda.kansha.auth

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class NoOpAuthController(private val user: Session.User) : AuthController {
    override val sessionFlow: StateFlow<Session> = MutableStateFlow(Session(user)).asStateFlow()
    override suspend fun createUser(email: String, password: String) = user
    override suspend fun login(email: String, password: String) = user
    override fun logout() {}
}