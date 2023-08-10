package com.susumunoda.kansha.data.user

internal class NoOpUserRepository : UserRepository {
    override fun getUser(
        id: String,
        onSuccess: (UserData) -> Unit,
        onError: (Exception?) -> Unit
    ) {
    }
}