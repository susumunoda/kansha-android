package com.susumunoda.kansha.ui.mock

import com.susumunoda.kansha.data.category.Category

data class MockCategory(
    override var id: String = "",
    override val order: Int = 0,
    override val name: String = "",
    override val photoUrl: String = ""
) : Category
