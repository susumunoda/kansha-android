package com.susumunoda.kansha.ui.mock

import com.susumunoda.kansha.data.category.Category
import com.susumunoda.kansha.data.category.CategoryRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

internal class MockCategoryRepository(
    private val database: MutableMap<String, MutableList<MockCategory>> = mutableMapOf(),
    private val mockLatency: Boolean = false,
    private val mockLatencyMillis: Long = 1000,
    private val errorOnCategoriesFlow: Boolean = false,
    private val errorOnAddCategory: Boolean = false
) : CategoryRepository {
    override fun newInstance() = MockCategory()

    override fun newInstance(name: String, photoUrl: String, order: Int) = MockCategory(
        name = name,
        photoUrl = photoUrl,
        order = order
    )

    override fun categoriesFlow(userId: String) = flow {
        if (mockLatency) {
            delay(mockLatencyMillis)
        }

        if (errorOnCategoriesFlow) {
            throw IllegalArgumentException("Could not get categories for user $userId")
        }

        if (database[userId] == null) {
            database[userId] = mutableListOf()
        }

        emit(database[userId]!!)
    }

    override suspend fun addCategory(userId: String, category: Category) {
        if (mockLatency) {
            delay(mockLatencyMillis)
        }

        if (errorOnAddCategory) {
            throw IllegalArgumentException("Could not add category $category for user $userId")
        }

        if (database[userId] == null) {
            database[userId] = mutableListOf()
        }

        database[userId]!!.add(category as MockCategory)
    }
}
