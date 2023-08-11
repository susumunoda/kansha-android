package com.susumunoda.kansha.data.user


interface UserRepository {
    fun getUserData(
        id: String,
        onSuccess: (UserData) -> Unit,
        onError: (Exception?) -> Unit
    )

    fun saveUserData(
        userData: UserData,
        onSuccess: () -> Unit,
        onError: (Exception?) -> Unit
    )
}