package com.susumunoda.kansha.data.user


interface UserRepository {
    fun newInstance(): User
    fun newInstance(displayName: String): User
    suspend fun getUser(id: String): User
    suspend fun setUser(id: String, user: User)
}