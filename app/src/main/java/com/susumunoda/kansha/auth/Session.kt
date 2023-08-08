package com.susumunoda.kansha.auth

class Session(val currentUser: User) {
    companion object {
        val LOGGED_OUT = Session(User.UNAUTHENTICATED)
    }
}
