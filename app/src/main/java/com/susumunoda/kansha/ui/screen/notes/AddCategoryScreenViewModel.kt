package com.susumunoda.kansha.ui.screen.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.susumunoda.kansha.auth.AuthController
import com.susumunoda.kansha.data.category.CategoryRepository
import com.susumunoda.kansha.ui.validation.StringValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddCategoryScreenViewModel @Inject constructor(
    private val authController: AuthController,
    private val categoryRepository: CategoryRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(AddCategoryScreenState())
    val uiState = _uiState.asStateFlow()

    init {
        val userId = authController.sessionFlow.value.user.id
        viewModelScope.launch {
            categoryRepository.categoriesFlow(userId).collect { categories ->
                _uiState.update { it.copy(categories = categories) }
            }
        }
    }

    fun setName(name: String) {
        _uiState.update {
            it.copy(
                name = name,
                nameValidation = null
            )
        }
    }

    fun setPhotoUrl(photoUrl: String) {
        _uiState.update {
            it.copy(
                photoUrl = photoUrl,
                photoUrlValidation = null
            )
        }
    }

    fun validateName(validator: StringValidator) {
        _uiState.update { it.copy(nameValidation = validator.validate(it.trimmedName)) }
    }

    fun validatePhotoUrl(validator: StringValidator) {
        if (_uiState.value.trimmedPhotoUrl.isNotBlank()) {
            _uiState.update { it.copy(photoUrlValidation = validator.validate(it.trimmedPhotoUrl)) }
        }
    }

    suspend fun submit(onSuccess: () -> Unit) {
        if (_uiState.value.nameValidation == null && _uiState.value.photoUrlValidation == null) {
            _uiState.update { it.copy(requestInFlight = true) }

            val userId = authController.sessionFlow.value.user.id
            val category = categoryRepository.newInstance(
                name = uiState.value.trimmedName,
                photoUrl = uiState.value.trimmedPhotoUrl,
                // For now, manually increment the order field so that new categories appear at the
                // end of the categories list
                order = _uiState.value.categories.maxOf { it.order } + 1
            )
            try {
                categoryRepository.addCategory(userId, category)
                onSuccess()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        requestInFlight = false,
                        errorMessage = e.message
                    )
                }
            }
        }
    }
}