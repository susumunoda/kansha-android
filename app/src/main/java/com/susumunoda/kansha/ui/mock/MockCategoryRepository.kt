package com.susumunoda.kansha.ui.mock

import com.susumunoda.kansha.data.category.CategoryRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

internal class MockCategoryRepository(
    private val database: MutableMap<String, MutableList<MockCategory>> = mutableMapOf(),
    private val mockLatency: Boolean = false,
    private val mockLatencyMillis: Long = 1000,
    private val errorOnCategoriesFlow: Boolean = false
) : CategoryRepository {
    override fun newInstance() = MockCategory()

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
}
