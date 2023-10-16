package com.susumunoda.kansha.ui.screen.notes

import androidx.lifecycle.ViewModel
import com.susumunoda.auth.AuthController
import com.susumunoda.kansha.repository.category.CategoryRepository
import com.susumunoda.kansha.ui.validation.StringValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AddCategoryScreenViewModel @Inject constructor(
    private val authController: AuthController,
    private val categoryRepository: CategoryRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(AddCategoryScreenState())
    val uiState = _uiState.asStateFlow()

    val categories get() = categoryRepository.categoriesStateFlow.value

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
                order = if (categories.isEmpty()) 0 else categories.maxOf { it.order } + 1
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