package com.susumunoda.kansha.data.user


internal class MockUserRepository(private val database: MutableMap<String, MockUser> = mutableMapOf()) :
    UserRepository {
    override fun newInstance() = MockUser()
    override fun newInstance(displayName: String) = MockUser(displayName = displayName)
    override suspend fun getUser(id: String) = database[id]!!
    override suspend fun setUser(id: String, user: User) {
        database[id] = user as MockUser
    }
}