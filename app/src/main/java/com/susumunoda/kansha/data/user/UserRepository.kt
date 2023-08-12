package com.susumunoda.kansha.data.user


interface UserRepository {
    suspend fun getUserData(id: String): UserData

    fun saveUserData(
        id: String,
        userData: UserData,
        onSuccess: () -> Unit,
        onError: (Exception?) -> Unit
    )
}