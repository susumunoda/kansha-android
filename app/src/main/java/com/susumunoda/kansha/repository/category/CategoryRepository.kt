package com.susumunoda.kansha.repository.category

import kotlinx.coroutines.flow.StateFlow

interface CategoryRepository {
    fun newInstance(name: String, photoUrl: String, order: Int): Category
    val categoriesStateFlow: StateFlow<List<Category>>
    suspend fun addCategory(userId: String, category: Category): String
}