package com.susumunoda.kansha.ui.screen.notes

import com.susumunoda.kansha.repository.category.Category
import com.susumunoda.kansha.repository.note.Note

data class ViewCategoryScreenState(
    val category: Category,
    val notes: List<Note> = emptyList(),
)
