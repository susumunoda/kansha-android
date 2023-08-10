package com.susumunoda.kansha.ui.screen.auth

import android.util.Patterns
import androidx.lifecycle.ViewModel
import com.susumunoda.kansha.auth.AuthController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    private val authController: AuthController
) : ViewModel() {
    private val _uiState = MutableStateFlow(AuthScreenState())
    val uiState = _uiState.asStateFlow()

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

    fun validateAndLogInUser(emailValidation: String, passwordValidation: String) {
        validate(emailValidation, passwordValidation)
        execute { errorHandler ->
            authController.login(_uiState.value.email, _uiState.value.password, errorHandler)
        }
    }

    fun validateAndCreateUser(emailValidation: String, passwordValidation: String) {
        validate(emailValidation, passwordValidation)
        execute { errorHandler ->
            authController.createUser(_uiState.value.email, _uiState.value.password, errorHandler)
        }
    }

    private fun validate(emailValidation: String, passwordValidation: String) {
        _uiState.update {
            it.copy(
                emailValidation = if (isValidEmail(it.email)) null else emailValidation,
                passwordValidation = if (isValidPassword(it.password)) null else passwordValidation
            )
        }
    }

    private fun execute(errorHandler: ((Throwable?) -> Unit) -> Unit) {
        if (_uiState.value.emailValidation == null && _uiState.value.passwordValidation == null) {
            _uiState.update { it.copy(requestInFlight = true) }
            errorHandler { throwable ->
                if (throwable != null) {
                    _uiState.update {
                        it.copy(
                            errorResponse = throwable.message,
                            requestInFlight = false
                        )
                    }
                }
            }
        }
    }
}

const val MIN_PASSWORD_LENGTH = 6
private fun isValidEmail(email: String) = Patterns.EMAIL_ADDRESS.matcher(email).matches()
private fun isValidPassword(password: String) = password.length >= MIN_PASSWORD_LENGTH