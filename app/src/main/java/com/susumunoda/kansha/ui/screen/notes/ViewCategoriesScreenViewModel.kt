package com.susumunoda.kansha.ui.screen.notes

import androidx.lifecycle.ViewModel
import com.susumunoda.kansha.repository.category.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ViewCategoriesScreenViewModel @Inject constructor(
    categoryRepository: CategoryRepository
) : ViewModel() {
    val categoriesStateFlow = categoryRepository.categoriesStateFlow
}