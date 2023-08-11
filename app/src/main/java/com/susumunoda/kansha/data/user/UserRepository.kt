package com.susumunoda.kansha.data.user


interface UserRepository {
    sealed class GetUserDataResult {
        class Success(val userData: UserData) : GetUserDataResult()
        class Failure(val exception: Exception) : GetUserDataResult()
    }

    suspend fun getUserData(id: String): GetUserDataResult

    fun saveUserData(
        id: String,
        userData: UserData,
        onSuccess: () -> Unit,
        onError: (Exception?) -> Unit
    )
}