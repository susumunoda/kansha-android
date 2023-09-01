package com.susumunoda.kansha.repository.user


interface UserRepository {
    fun newInstance(displayName: String): User
    suspend fun getUser(id: String): User
    suspend fun setUser(id: String, user: User)
}