package com.susumunoda.kansha.ui.screen.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.susumunoda.kansha.auth.AuthController
import com.susumunoda.kansha.data.note.NoteRepository
import com.susumunoda.kansha.data.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val authController: AuthController,
    private val userRepository: UserRepository,
    private val noteRepository: NoteRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileScreenState())
    val uiState = _uiState.asStateFlow()

    companion object {
        private const val TAG = "ProfileScreenViewModel"
    }

    init {
        val currentUser = authController.sessionFlow.value.user

        viewModelScope.launch {
            Log.d(TAG, "Launched coroutine for getUser")
            try {
                val user = userRepository.getUser(currentUser.id)

                Log.d(TAG, "User fetch succeeded: $user")

                _uiState.update {
                    it.copy(
                        user = user,
                        userFetchInProgress = false
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "User fetch failed: ${e.message}")

                _uiState.update {
                    it.copy(
                        userFetchFailed = true,
                        userFetchInProgress = false
                    )
                }
            }
        }

        viewModelScope.launch {
            Log.d(TAG, "Launched coroutine for getNotes")
            try {
                val notes = noteRepository.getNotes(currentUser.id)

                Log.d(TAG, "Notes fetch succeeded: $notes")

                _uiState.update {
                    it.copy(
                        notes = notes,
                        notesFetchInProgress = false
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "Notes fetch failed: ${e.message}")

                _uiState.update {
                    it.copy(
                        notesFetchFailed = true,
                        notesFetchInProgress = false
                    )
                }
            }
        }
    }

    fun logout() {
        authController.logout()
    }
}