package com.susumunoda.kansha.ui.screen.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.susumunoda.kansha.auth.AuthController
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
    private val userRepository: UserRepository
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
                        isLoading = false,
                        displayName = userData.displayName,
                        profilePhotoUrl = userData.profilePhotoUrl,
                        backgroundPhotoUrl = userData.backgroundPhotoUrl
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "User data fetch failed: ${e.message}")
            }
        }
    }

    fun logout() {
        authController.logout()
    }
}