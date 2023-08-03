package com.susumunoda.kansha.auth

class Session(private val currentUser: User) {
    fun getCurrentUser() = currentUser
}