package com.susumunoda.kansha.ui.screen.addnote

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.susumunoda.kansha.auth.AuthController
import com.susumunoda.kansha.data.note.Label
import com.susumunoda.kansha.data.note.NoteRepository
import com.susumunoda.kansha.ui.screen.Validator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddNoteScreenViewModel @Inject constructor(
    private val authController: AuthController,
    private val noteRepository: NoteRepository
) : ViewModel() {
    companion object {
        private const val TAG = "AddNoteScreenViewModel"
    }

    private val _uiState = MutableStateFlow(AddNoteScreenState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val currentUser = authController.sessionFlow.value.user
            noteRepository.labelsFlow(currentUser.id).collect { allLabels ->
                _uiState.update {
                    it.copy(
                        allLabels = allLabels,
                        requestInFlight = false
                    )
                }
            }
        }
    }

    fun setMessage(message: String) {
        _uiState.update {
            it.copy(
                message = message,
                validationMessage = null,
                errorMessage = null
            )
        }
    }

    fun validateNote(validator: Validator) {
        _uiState.update { it.copy(validationMessage = validator.validate(it.trimmedMessage)) }
    }

    fun addSelectedLabel(label: Label) {
        if (!_uiState.value.selectedLabels.contains(label)) {
            _uiState.update {
                val selectedLabels = listOf(
                    *it.selectedLabels.toTypedArray(),
                    label
                )
                it.copy(selectedLabels = selectedLabels)
            }
        }
    }

    fun removedSelectedLabel(label: Label) {
        if (_uiState.value.selectedLabels.contains(label)) {
            _uiState.update {
                val selectedLabels =
                    it.selectedLabels.filter { selectedLabel -> selectedLabel != label }
                it.copy(selectedLabels = selectedLabels)
            }
        }
    }

    suspend fun saveNote(onSuccess: () -> Unit) {
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
                labels = _uiState.value.selectedLabels
            )
            try {
                noteRepository.addNote(currentUser.id, note)

                Log.d(TAG, "Add note succeeded")

                onSuccess()
            } catch (e: Exception) {
                Log.e(TAG, "Add note failed: ${e.message}")

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