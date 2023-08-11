package com.susumunoda.kansha.ui.screen.auth

data class AuthScreenState(
    val displayName: String = "",
    val displayNameValidation: String? = null,
    val email: String = "",
    val emailValidation: String? = null,
    val password: String = "",
    val passwordValidation: String? = null,
    val errorResponse: String? = null,
    val requestInFlight: Boolean = false
)