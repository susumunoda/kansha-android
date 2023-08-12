package com.susumunoda.kansha.auth

import kotlinx.coroutines.flow.StateFlow

interface AuthController {
    val sessionFlow: StateFlow<Session>
    suspend fun createUser(email: String, password: String): User
    suspend fun login(email: String, password: String): User
    fun logout()
}
