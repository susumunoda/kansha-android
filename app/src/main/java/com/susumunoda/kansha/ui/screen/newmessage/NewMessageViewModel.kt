package com.susumunoda.kansha.ui.screen.newmessage

import androidx.lifecycle.ViewModel
import com.susumunoda.kansha.data.DataSource
import com.susumunoda.kansha.data.User
import com.susumunoda.kansha.data.filterByNameStartsWith
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

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
        _uiState.update { current -> current.copy(message = message) }
    }

    fun setRecipient(recipient: User) {
        _uiState.update { current -> current.copy(recipient = recipient) }
    }

    fun clearRecipient() {
        setRecipient(User.NONE)
    }
}