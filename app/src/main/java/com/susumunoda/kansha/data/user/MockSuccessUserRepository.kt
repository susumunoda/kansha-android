package com.susumunoda.kansha.data.user


internal class MockSuccessUserRepository(private val userData: UserData) : UserRepository {
    override suspend fun getUserData(id: String) = userData

    override fun saveUserData(
        id: String,
        userData: UserData,
        onSuccess: () -> Unit,
        onError: (Exception?) -> Unit
    ) {
    }
}