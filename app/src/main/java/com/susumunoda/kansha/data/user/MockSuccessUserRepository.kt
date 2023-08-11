package com.susumunoda.kansha.data.user

import com.susumunoda.kansha.data.user.UserRepository.GetUserDataResult
import com.susumunoda.kansha.data.user.UserRepository.GetUserDataResult.Success

internal class MockSuccessUserRepository(private val userData: UserData) : UserRepository {
    override suspend fun getUserData(id: String): GetUserDataResult = Success(userData)

    override fun saveUserData(
        id: String,
        userData: UserData,
        onSuccess: () -> Unit,
        onError: (Exception?) -> Unit
    ) {
    }
}