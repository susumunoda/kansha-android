package com.susumunoda.kansha.ui.screen.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import com.susumunoda.kansha.auth.AuthController
import com.susumunoda.kansha.auth.User
import com.susumunoda.kansha.data.user.UserData
import com.susumunoda.kansha.data.user.UserRepository
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

    fun validateDisplayName(validator: FieldValidator) {
        // Ensure trimmed display name is still valid
        _uiState.update { it.copy(displayNameValidation = validator.validate(it.trimmedDisplayName)) }
    }

    fun validateEmail(validator: FieldValidator) {
        _uiState.update { it.copy(emailValidation = validator.validate(it.email)) }
    }

    fun validatePassword(validator: FieldValidator) {
        _uiState.update { it.copy(passwordValidation = validator.validate(it.password)) }
    }

    fun logInUser() {
        if (_uiState.value.emailValidation == null && _uiState.value.passwordValidation == null) {
            _uiState.update { it.copy(requestInFlight = true) }

            authController.login(
                email = _uiState.value.email,
                password = _uiState.value.password,
                onSuccess = {},
                onFailure = { exception ->
                    _uiState.update {
                        it.copy(
                            errorResponse = exception.message,
                            requestInFlight = false
                        )
                    }
                }
            )
        }
    }

    fun createUser() {
        if (_uiState.value.displayNameValidation == null &&
            _uiState.value.emailValidation == null &&
            _uiState.value.passwordValidation == null
        ) {
            _uiState.update { it.copy(requestInFlight = true) }

            authController.createUser(
                email = _uiState.value.email,
                password = _uiState.value.password,
                onSuccess = { user: User ->
                    userRepository.saveUserData(
                        id = user.id,
                        // Important to use trimmed display name as that is what we validated against
                        userData = UserData(_uiState.value.trimmedDisplayName),
                        onSuccess = { Log.d(TAG, "User data creation succeeded") },
                        onError = { Log.e(TAG, "User data creation failed") }
                    )
                },
                onFailure = { exception ->
                    _uiState.update {
                        it.copy(
                            errorResponse = exception.message,
                            requestInFlight = false
                        )
                    }
                }
            )
        }
    }
}

abstract class FieldValidator() {
    fun validate(value: String) = if (isValid(value)) null else validationMessage()
    abstract fun isValid(value: String): Boolean
    abstract fun validationMessage(): String
}