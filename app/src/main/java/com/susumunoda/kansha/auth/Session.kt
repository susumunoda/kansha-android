package com.susumunoda.kansha.auth

class Session(private val currentUser: User) {
    companion object {
        val LOGGED_OUT = Session(User.UNAUTHENTICATED)
    }

    fun getCurrentUser() = currentUser
}
