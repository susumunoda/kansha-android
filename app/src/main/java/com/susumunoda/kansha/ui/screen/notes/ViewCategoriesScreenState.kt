package com.susumunoda.kansha.ui.screen.notes

import com.susumunoda.kansha.repository.category.Category

data class ViewCategoriesScreenState(
    val categories: List<Category> = emptyList(),
    val requestInFlight: Boolean = true
)
