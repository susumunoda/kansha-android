package com.susumunoda.kansha.data.user


interface UserRepository {
    suspend fun getUser(id: String): User
    suspend fun setUser(id: String, user: User)
}