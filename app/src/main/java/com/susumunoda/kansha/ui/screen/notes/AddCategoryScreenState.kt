package com.susumunoda.kansha.ui.screen.notes

data class AddCategoryScreenState(
    val name: String = "",
    val nameValidation: String? = null,
    val photoUrl: String = "",
    val photoUrlValidation: String? = null,
    val requestInFlight: Boolean = false,
    val errorMessage: String? = null
) {
    val trimmedName = name.trim()
    val trimmedPhotoUrl = photoUrl.trim()
}
