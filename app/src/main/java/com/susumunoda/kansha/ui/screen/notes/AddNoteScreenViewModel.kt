package com.susumunoda.kansha.ui.screen.notes

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.susumunoda.auth.AuthController
import com.susumunoda.kansha.repository.category.CategoryRepository
import com.susumunoda.kansha.repository.note.NoteRepository
import com.susumunoda.kansha.ui.validation.StringValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddNoteScreenViewModel @Inject constructor(
    private val authController: AuthController,
    private val noteRepository: NoteRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {
    companion object {
        private const val TAG = "AddNoteScreenViewModel"
    }

    private val _uiState = MutableStateFlow(AddNoteScreenState())
    val uiState = _uiState.asStateFlow()

    val categories get() = categoryRepository.categoriesStateFlow.value

    fun setMessage(message: String) {
        _uiState.update {
            it.copy(
                message = message,
                validationMessage = null,
                errorMessage = null
            )
        }
    }

    fun selectCategory(categoryId: String) {
        val category = categories.find { it.id == categoryId }
        if (_uiState.value.selectedCategory != category) {
            _uiState.update { it.copy(selectedCategory = category) }
        }
    }

    fun deselectCategory() {
        if (_uiState.value.selectedCategory != null) {
            _uiState.update { it.copy(selectedCategory = null) }
        }
    }

    fun validateNote(validator: StringValidator) {
        _uiState.update { it.copy(validationMessage = validator.validate(it.trimmedMessage)) }
    }

    fun saveNote(onSuccess: () -> Unit) {
        if (_uiState.value.validationMessage == null) {
            _uiState.update {
                it.copy(
                    errorMessage = null,
                    requestInFlight = true
                )
            }

            val currentUser = authController.sessionFlow.value.user
            val note = noteRepository.newInstance(
                message = _uiState.value.trimmedMessage,
                categoryId = _uiState.value.selectedCategory?.id
            )
            viewModelScope.launch {
                try {
                    noteRepository.addNote(currentUser.id, note)

                    Log.d(TAG, "Adding note succeeded")

                    onSuccess()
                } catch (e: Exception) {
                    Log.d(TAG, "Adding note failed: ${e.message}")

                    _uiState.update {
                        it.copy(
                            errorMessage = e.message,
                            requestInFlight = false
                        )
                    }
                }
            }
        }
    }
}