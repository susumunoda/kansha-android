package com.susumunoda.kansha.auth

class Session(val user: User) {
    constructor(userId: String) : this(User(userId))

    companion object {
        val LOGGED_OUT = Session(User.ANONYMOUS)
        val UNKNOWN = Session(User.ANONYMOUS)
    }

    class User(val id: String) {
        companion object {
            val ANONYMOUS = User("")
        }
    }
}
