package com.susumunoda.kansha.ui.screen.addnote

import androidx.lifecycle.ViewModel
import com.susumunoda.kansha.ui.screen.Validator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AddNoteScreenViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(AddNoteScreenState())
    val uiState = _uiState.asStateFlow()

    fun setMessage(message: String) {
        _uiState.update {
            it.copy(
                message = message,
                validationMessage = null
            )
        }
    }

    fun validateNote(validator: Validator) {
        _uiState.update { it.copy(validationMessage = validator.validate(it.trimmedMessage)) }
    }

    fun saveNote() {
        if (_uiState.value.validationMessage == null) {
            // submit trimmedMessage
        }
    }
}