package com.susumunoda.kansha.ui.mock

import com.susumunoda.kansha.repository.category.Category
import com.susumunoda.kansha.repository.category.CategoryRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

internal class MockCategoryRepository(
    categories: List<MockCategory> = emptyList(),
    private val mockLatency: Boolean = false,
    private val mockLatencyMillis: Long = 1000,
    private val errorOnAddCategory: Boolean = false
) : CategoryRepository {
    private val _categoriesStateFlow = MutableStateFlow(categories)
    override val categoriesStateFlow = _categoriesStateFlow.asStateFlow()

    override fun newInstance(name: String, photoUrl: String, order: Int) = MockCategory(
        name = name,
        photoUrl = photoUrl,
        order = order
    )

    override suspend fun addCategory(userId: String, category: Category): String {
        val mockCategory = category as MockCategory

        if (mockLatency) {
            delay(mockLatencyMillis)
        }

        if (errorOnAddCategory) {
            throw IllegalArgumentException("Could not add category $category for user $userId")
        }

        _categoriesStateFlow.update {
            listOf(*_categoriesStateFlow.value.toTypedArray(), mockCategory)
        }

        mockCategory.id = UUID.randomUUID().toString()
        return mockCategory.id
    }
}
