package com.susumunoda.kansha.data.category

import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun newInstance(): Category
    fun categoriesFlow(userId: String): Flow<List<Category>>
}