package com.susumunoda.kansha.ui.screen.notes

import com.susumunoda.kansha.data.category.Category
import com.susumunoda.kansha.data.note.Note

data class ViewCategoryScreenState(
    val category: Category,
    val notes: List<Note> = emptyList(),
)
