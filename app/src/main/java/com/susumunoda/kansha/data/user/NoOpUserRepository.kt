package com.susumunoda.kansha.data.user

internal class NoOpUserRepository : UserRepository {
    override fun getUserData(
        id: String,
        onSuccess: (UserData) -> Unit,
        onError: (Exception?) -> Unit
    ) {
    }

    override fun saveUserData(
        id: String,
        userData: UserData,
        onSuccess: () -> Unit,
        onError: (Exception?) -> Unit
    ) {
    }
}