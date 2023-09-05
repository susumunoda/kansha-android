package com.susumunoda.kansha.repository

interface SessionAwareRepository {
    fun onLogin(userId: String)
    fun onLogout()
}
