package com.susumunoda.kansha.auth

import kotlinx.coroutines.flow.StateFlow

interface AuthController {
    val sessionFlow: StateFlow<Session>
    fun createUser(
        email: String,
        password: String,
        onSuccess: (User) -> Unit,
        onFailure: (Exception) -> Unit
    )

    fun login(
        email: String,
        password: String,
        onSuccess: (User) -> Unit,
        onFailure: (Exception) -> Unit
    )

    fun logout()
}
