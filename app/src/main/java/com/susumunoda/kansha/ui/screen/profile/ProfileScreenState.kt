package com.susumunoda.kansha.ui.screen.profile

data class ProfileScreenState(
    val isLoading: Boolean = true,
    val displayName: String = "",
    val profilePhotoUrl: String = "",
    val backgroundPhotoUrl: String = ""
)