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
        val currentUser = authController.sessionFlow.value.currentUser

        viewModelScope.launch {
            try {
                val userData = userRepository.getUserData(currentUser.id)

                Log.d(TAG, "User data fetch succeeded: $userData")

                _uiState.update {
                    it.copy(
                        userData = userData,
                        userDataFetchInProgress = false
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "User data fetch failed: ${e.message}")

                _uiState.update {
                    it.copy(
                        userDataFetchFailed = true,
                        userDataFetchInProgress = false
                    )
                }
            }
        }

        viewModelScope.launch {
            try {
                val notesData = noteRepository.getNotesData(currentUser.id)

                Log.d(TAG, "Notes data fetch succeeded: $notesData")

                _uiState.update {
                    it.copy(
                        notesData = notesData,
                        notesDataFetchInProgress = false
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "Notes data fetch failed: ${e.message}")

                _uiState.update {
                    it.copy(
                        notesDataFetchFailed = true,
                        notesDataFetchInProgress = false
                    )
                }
            }
        }
    }

    fun logout() {
        authController.logout()
    }
}