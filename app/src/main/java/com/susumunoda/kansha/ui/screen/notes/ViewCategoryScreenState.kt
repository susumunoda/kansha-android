package com.susumunoda.kansha.ui.screen.notes

import com.susumunoda.kansha.data.note.Note

data class ViewCategoryScreenState(
    val notes: List<Note> = emptyList()
)
