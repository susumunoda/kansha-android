package com.susumunoda.kansha.ui.screen.addnote

import android.util.Log
import androidx.lifecycle.ViewModel
import com.susumunoda.kansha.auth.AuthController
import com.susumunoda.kansha.data.note.NoteRepository
import com.susumunoda.kansha.ui.screen.Validator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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

    suspend fun saveNote(onSuccess: () -> Unit) {
        if (_uiState.value.validationMessage == null) {
            _uiState.update { it.copy(requestInFlight = true) }

            val currentUser = authController.sessionFlow.value.user
            val note = noteRepository.newInstance(
                message = _uiState.value.trimmedMessage,
                labels = _uiState.value.labels
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