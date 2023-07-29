package com.susumunoda.kansha.ui.screen.newmessage

import android.content.Context
import androidx.annotation.StringRes
import android.util.Log
import androidx.lifecycle.ViewModel
import com.susumunoda.kansha.R
import com.susumunoda.kansha.data.DataSource
import com.susumunoda.kansha.data.User
import com.susumunoda.kansha.data.filterByNameStartsWith
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

private const val TAG = "NewMessageViewModel"

private object Constants {
    const val MAX_MESSAGE_LENGTH = 250
}

class NewMessageViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(NewMessageState())
    val uiState = _uiState.asStateFlow()

    fun setSearchTerm(searchTerm: String) {
        _uiState.update { current ->
            current.copy(
                searchTerm = searchTerm,
                searchResults = DataSource.allUsers().filterByNameStartsWith(searchTerm)
            )
        }
    }

    fun setMessage(message: String) {
        _uiState.update { current ->
            current.copy(
                message = message,
                validationErrors = validateMessage(message)
            )
        }
    }

    private fun validateMessage(message: String): List<ValidationError> {
        val errors: MutableList<ValidationError> = mutableListOf()
        if (message.length > Constants.MAX_MESSAGE_LENGTH) {
            errors.add(
                ValidationError(
                    R.string.message_validation_length_exceeded,
                    Constants.MAX_MESSAGE_LENGTH
                )
            )
        }
        return errors
    }

    fun setRecipient(recipient: User) {
        _uiState.update { current -> current.copy(recipient = recipient) }
    }

    fun clearRecipient() {
        setRecipient(User.NONE)
    }

    fun sendMessage() {
        Log.d(TAG, "Sending to ${_uiState.value.recipient}: ${_uiState.value.message}")
    }
}

class ValidationError(@StringRes private val messageId: Int, private vararg val formatArgs: Any) {
    fun toLocalizedString(context: Context) = context.getString(messageId, *formatArgs)
}

fun NewMessageState.isSubmittable() =
    recipient != User.NONE && message.isNotEmpty() && !hasValidationErrors