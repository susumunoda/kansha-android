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
) {
    // Cannot be defined in primary constructor, as then the same initial value ("") will get
    // copied on all state updates, as we never directly modify this value in the ViewModel.
    val trimmedDisplayName = displayName.trim()
}