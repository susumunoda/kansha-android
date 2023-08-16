package com.susumunoda.kansha.auth

class Session(val user: User) {
    constructor(userId: String) : this(User(userId))

    companion object {
        val LOGGED_OUT = Session(User.UNAUTHENTICATED)
    }

    class User(val id: String) {
        companion object {
            val UNAUTHENTICATED = User("")
        }
    }
}
