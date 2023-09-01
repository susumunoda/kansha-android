package com.susumunoda.kansha.ui.screen.profile

import com.susumunoda.kansha.repository.user.User

data class ProfileScreenState(
    val user: User? = null,
    val error: Exception? = null
)