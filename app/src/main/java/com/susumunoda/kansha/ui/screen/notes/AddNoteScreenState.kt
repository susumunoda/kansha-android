package com.susumunoda.kansha.ui.screen.notes

import com.susumunoda.kansha.repository.category.Category

data class AddNoteScreenState(
    val message: String = "",
    val selectedCategory: Category? = null,
    val validationMessage: String? = null,
    val errorMessage: String? = null,
    val requestInFlight: Boolean = false
) {
    val trimmedMessage = message.trim()
}
