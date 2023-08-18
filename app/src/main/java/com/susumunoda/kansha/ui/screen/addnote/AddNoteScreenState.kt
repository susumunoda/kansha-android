package com.susumunoda.kansha.ui.screen.addnote

import com.susumunoda.kansha.data.note.Label

data class AddNoteScreenState(
    val message: String = "",
    val allLabels: List<Label> = emptyList(),
    val selectedLabels: List<Label> = emptyList(),
    val validationMessage: String? = null,
    val errorMessage: String? = null,
    // Initial fetch to retrieve labels list
    val requestInFlight: Boolean = true
) {
    val trimmedMessage = message.trim()
}
