package com.susumunoda.kansha.ui.screen.notes

import com.susumunoda.kansha.repository.note.Note

data class ViewCategoryScreenState(
    val notes: List<Note> = emptyList(),
    // Default to true until fetch completion
    val notesFetchInProgress: Boolean = true
)
