package com.susumunoda.kansha.data.user


interface UserRepository {
    fun getUser(
        id: String,
        onSuccess: (UserData) -> Unit,
        onError: (Exception?) -> Unit
    )
}