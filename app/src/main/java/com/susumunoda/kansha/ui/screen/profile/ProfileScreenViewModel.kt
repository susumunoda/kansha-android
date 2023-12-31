package com.susumunoda.kansha.ui.screen.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.susumunoda.auth.AuthController
import com.susumunoda.kansha.repository.note.NoteRepository
import com.susumunoda.kansha.repository.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    authController: AuthController,
    userRepository: UserRepository,
    noteRepository: NoteRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileScreenState())
    val uiState = _uiState.asStateFlow()

    companion object {
        private const val TAG = "ProfileScreenViewModel"
    }

    init {
        val currentUser = authController.sessionFlow.value.user

        viewModelScope.launch {
            try {
                val user = userRepository.getUser(currentUser.id)

                Log.d(TAG, "User fetch succeeded: $user")

                _uiState.update {
                    it.copy(user = user)
                }
            } catch (e: Exception) {
                Log.d(TAG, "User fetch failed: ${e.message}")

                _uiState.update {
                    it.copy(error = e)
                }
            }
        }

        viewModelScope.launch {
            noteRepository.notesFlow(currentUser.id).collect { notes ->
                _uiState.update { it.copy(notes = notes) }
            }
        }
    }
}