package com.susumunoda.kansha.ui.mock

import com.susumunoda.kansha.data.user.User
import com.susumunoda.kansha.data.user.UserRepository
import kotlinx.coroutines.delay


internal class MockUserRepository(
    private val database: MutableMap<String, MockUser> = mutableMapOf(),
    private val mockLatency: Boolean = false,
    private val mockLatencyMillis: Long = 1000,
    private val errorOnGetUser: Boolean = false,
    private val errorOnSetUser: Boolean = false
) : UserRepository {
    override fun newInstance(displayName: String) = MockUser(displayName = displayName)
    override suspend fun getUser(id: String): User {
        if (mockLatency) {
            delay(mockLatencyMillis)
        }

        if (errorOnGetUser) {
            throw IllegalArgumentException("Could not get data for user $id")
        }

        return database[id]!!
    }

    override suspend fun setUser(id: String, user: User) {
        if (mockLatency) {
            delay(mockLatencyMillis)
        }

        if (errorOnSetUser) {
            throw IllegalArgumentException("Could set data $user for user $id")
        }

        database[id] = user as MockUser
    }
}