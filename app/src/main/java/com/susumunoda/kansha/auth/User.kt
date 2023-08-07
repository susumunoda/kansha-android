package com.susumunoda.kansha.auth

class User(val id: String, val displayName: String) {
    companion object {
        val UNAUTHENTICATED = User("", "")
    }
}