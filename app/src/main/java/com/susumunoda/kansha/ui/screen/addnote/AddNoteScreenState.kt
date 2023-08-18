package com.susumunoda.kansha.ui.screen.addnote

import com.susumunoda.kansha.data.note.Label

data class AddNoteScreenState(
    val message: String = "",
    val labels: List<Label> = emptyList(),
    val validationMessage: String? = null,
    val errorMessage: String? = null,
    val requestInFlight: Boolean = false
) {
    val trimmedMessage = message.trim()
}
