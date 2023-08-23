package com.susumunoda.kansha.ui.screen.notes

import com.susumunoda.kansha.data.category.Category

data class ViewCategoriesScreenState(
    val categories: List<Category> = emptyList(),
    val requestInFlight: Boolean = true
)
