package com.susumunoda.kansha.repository.category

import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun newInstance(): Category
    fun newInstance(name: String, photoUrl: String, order: Int): Category
    fun categoriesFlow(userId: String): Flow<List<Category>>
    suspend fun addCategory(userId: String, category: Category)
}