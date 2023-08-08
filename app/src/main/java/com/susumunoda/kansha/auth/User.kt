package com.susumunoda.kansha.auth

class User(val id: String) {
    companion object {
        val UNAUTHENTICATED = User("")
    }
}