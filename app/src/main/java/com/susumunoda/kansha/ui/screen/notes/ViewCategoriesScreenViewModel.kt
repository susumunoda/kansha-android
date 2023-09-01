package com.susumunoda.kansha.ui.screen.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.susumunoda.kansha.auth.AuthController
import com.susumunoda.kansha.repository.category.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewCategoriesScreenViewModel @Inject constructor (
    authController: AuthController,
    categoryRepository: CategoryRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ViewCategoriesScreenState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val currentUser = authController.sessionFlow.value.user
            categoryRepository.categoriesFlow(currentUser.id).collect { categories ->
                _uiState.update {
                    it.copy(
                        categories = categories,
                        requestInFlight = false
                    )
                }
            }
        }
    }
}