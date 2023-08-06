package com.susumunoda.kansha.auth

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

internal object NoOpAuthController : AuthController {
    override val sessionFlow: StateFlow<Session?> = MutableStateFlow(null).asStateFlow()
    override fun createUser(email: String, password: String, onResult: (Throwable?) -> Unit) {}
    override fun login(email: String, password: String, onResult: (Throwable?) -> Unit) {}
    override fun logout() {}
}