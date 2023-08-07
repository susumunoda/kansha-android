package com.susumunoda.kansha.auth

import kotlinx.coroutines.flow.StateFlow

interface AuthController {
    val sessionFlow: StateFlow<Session>
    fun createUser(email: String, password: String, onResult: (Throwable?) -> Unit)
    fun login(email: String, password: String, onResult: (Throwable?) -> Unit)
    fun logout()
}
