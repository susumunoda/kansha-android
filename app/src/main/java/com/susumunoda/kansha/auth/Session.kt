package com.susumunoda.kansha.auth

class Session(val user: User, private val label: String = "") {
    companion object {
        val LOGGED_OUT = Session(User.ANONYMOUS, "LOGGED_OUT")
        val UNKNOWN = Session(User.ANONYMOUS, "UNKNOWN")
    }

    class User(val id: String) {
        companion object {
            val ANONYMOUS = User("")
        }

        override fun toString() = "User(id=$id)"
    }

    override fun toString() = "Session(user=$user, label=$label)"
}
