package com.susumunoda.kansha.ui.screen.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import com.susumunoda.kansha.auth.AuthController
import com.susumunoda.kansha.data.user.UserRepository
import com.susumunoda.kansha.ui.screen.Validator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AuthScreenViewModel @Inject constructor(
    private val authController: AuthController,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(AuthScreenState())
    val uiState = _uiState.asStateFlow()

    companion object {
        private const val TAG = "AuthScreenViewModel"
    }

    fun setDisplayName(displayName: String) {
        _uiState.update {
            it.copy(
                displayName = displayName,
                displayNameValidation = null,
                errorResponse = null
            )
        }
    }

    fun setEmail(email: String) {
        _uiState.update {
            it.copy(
                email = email,
                emailValidation = null,
                errorResponse = null
            )
        }
    }

    fun setPassword(password: String) {
        _uiState.update {
            it.copy(
                password = password,
                passwordValidation = null,
                errorResponse = null
            )
        }
    }

    fun validateDisplayName(validator: Validator) {
        // Ensure trimmed display name is still valid
        _uiState.update { it.copy(displayNameValidation = validator.validate(it.trimmedDisplayName)) }
    }

    fun validateEmail(validator: Validator) {
        _uiState.update { it.copy(emailValidation = validator.validate(it.email)) }
    }

    fun validatePassword(validator: Validator) {
        _uiState.update { it.copy(passwordValidation = validator.validate(it.password)) }
    }

    suspend fun logInUser() {
        if (_uiState.value.emailValidation == null && _uiState.value.passwordValidation == null) {
            _uiState.update { it.copy(requestInFlight = true) }

            try {
                authController.login(_uiState.value.email, _uiState.value.password)
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        errorResponse = e.message,
                        requestInFlight = false
                    )
                }
            }
        }
    }

    suspend fun createUser() {
        if (_uiState.value.displayNameValidation == null &&
            _uiState.value.emailValidation == null &&
            _uiState.value.passwordValidation == null
        ) {
            _uiState.update { it.copy(requestInFlight = true) }

            try {
                val sessionUser =
                    authController.createUser(_uiState.value.email, _uiState.value.password)
                try {
                    // Important to use trimmed display name as that is what we validated against
                    val user = userRepository.newInstance(_uiState.value.trimmedDisplayName)
                    userRepository.setUser(sessionUser.id, user)
                    Log.d(TAG, "User data creation succeeded")
                } catch (e: Exception) {
                    Log.e(TAG, "User data creation failed: ${e.message}")
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        errorResponse = e.message,
                        requestInFlight = false
                    )
                }
            }
        }
    }
}
