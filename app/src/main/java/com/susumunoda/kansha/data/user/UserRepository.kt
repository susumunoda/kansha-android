package com.susumunoda.kansha.data.user


interface UserRepository {
    fun getUserData(
        id: String,
        onSuccess: (UserData) -> Unit,
        onError: (Exception?) -> Unit
    )

    fun saveUserData(
        id: String,
        userData: UserData,
        onSuccess: () -> Unit,
        onError: (Exception?) -> Unit
    )
}