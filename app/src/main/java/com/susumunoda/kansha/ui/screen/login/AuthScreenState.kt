package com.susumunoda.kansha.ui.screen.login

data class AuthScreenState(
    val email: String = "",
    val password: String = "",
    val emailValidation: String? = null,
    val passwordValidation: String? = null,
    val errorResponse: String? = null,
    val requestInFlight: Boolean = false
)