package com.susumunoda.kansha.data.user

import com.susumunoda.kansha.auth.User

interface UserRepository {
    fun getBasicUserInformation(
        user: User,
        onSuccess: (Map<String, String>) -> Unit,
        onError: (Exception?) -> Unit
    )
}