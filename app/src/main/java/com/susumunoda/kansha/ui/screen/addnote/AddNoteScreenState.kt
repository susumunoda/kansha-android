package com.susumunoda.kansha.ui.screen.addnote

data class AddNoteScreenState(
    val message: String = "",
    val labels: List<String> = emptyList(),
    val validationMessage: String? = null
) {
    val trimmedMessage = message.trim()
}
