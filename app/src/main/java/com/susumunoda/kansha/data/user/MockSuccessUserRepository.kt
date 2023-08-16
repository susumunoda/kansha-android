package com.susumunoda.kansha.data.user


internal class MockSuccessUserRepository(private val user: User) : UserRepository {
    override suspend fun getUser(id: String) = user
    override suspend fun setUser(id: String, user: User) {}
}