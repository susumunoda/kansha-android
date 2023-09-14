package com.susumunoda.kansha.ui.screen.auth

import android.content.Context
import android.util.Patterns
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.susumunoda.compose.material3.BackButton
import com.susumunoda.compose.material3.ScaffoldWithStatusBarInsets
import com.susumunoda.kansha.BuildConfig
import com.susumunoda.kansha.R
import com.susumunoda.kansha.ui.navigation.UnauthenticatedScreen
import com.susumunoda.kansha.ui.validation.StringValidator
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: AuthScreenViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    val buttonColors = ButtonDefaults.buttonColors()

    ScaffoldWithStatusBarInsets(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.login_top_bar_text)) },
                actions = {
                    // Only for development - convenience for faster login
                    if (BuildConfig.DEBUG && BuildConfig.SHOW_TEST_LOGIN.toBoolean()) {
                        IconButton(
                            onClick = {
                                viewModel.setEmail(BuildConfig.TEST_EMAIL)
                                viewModel.setPassword(BuildConfig.TEST_PASSWORD)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Star,
                                contentDescription = null,
                                modifier = Modifier.size(dimensionResource(R.dimen.icon))
                            )
                        }
                    }
                }
            )
        }
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.padding_large))
        ) {
            Column {
                EmailField(viewModel, uiState)
                PasswordField(viewModel, uiState)
                if (uiState.requestInFlight) {
                    SubmissionInProgressButton(
                        colors = buttonColors,
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    SubmitButton(
                        label = stringResource(R.string.login_button_text),
                        enabled = uiState.email.isNotEmpty() && uiState.password.isNotEmpty(),
                        validateForm = {
                            viewModel.validateEmail(EmailValidator(context))
                            viewModel.validatePassword(PasswordValidator(context))
                        },
                        submitForm = {
                            scope.launch { viewModel.logInUser() }
                        },
                        colors = buttonColors,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                if (uiState.errorResponse != null) {
                    Text(
                        stringResource(R.string.login_failed_message),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(stringResource(R.string.no_account_question_text))
                Spacer(Modifier.size(dimensionResource(R.dimen.padding_small)))
                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { navController.navigate(UnauthenticatedScreen.SIGNUP.name) }
                ) {
                    Text(stringResource(R.string.create_account_button_text))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(
    navController: NavHostController,
    viewModel: AuthScreenViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    val buttonColors = ButtonDefaults.buttonColors()

    ScaffoldWithStatusBarInsets(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.create_account_top_bar_text)) },
                navigationIcon = { BackButton(onClick = { navController.popBackStack() }) }
            )
        }
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.padding_large))
        ) {
            Column {
                DisplayNameField(viewModel, uiState)
                EmailField(viewModel, uiState)
                PasswordField(viewModel, uiState)
                if (uiState.requestInFlight) {
                    SubmissionInProgressButton(
                        colors = buttonColors,
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    SubmitButton(
                        label = stringResource(R.string.signup_button_text),
                        enabled = uiState.displayName.isNotEmpty() && uiState.email.isNotEmpty() && uiState.password.isNotEmpty(),
                        validateForm = {
                            viewModel.validateDisplayName(DisplayNameValidator(context))
                            viewModel.validateEmail(EmailValidator(context))
                            viewModel.validatePassword(PasswordValidator(context))
                        },
                        submitForm = {
                            scope.launch { viewModel.createUser() }
                        },
                        colors = buttonColors,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                if (uiState.errorResponse != null) {
                    Text(
                        stringResource(
                            R.string.user_creation_failed_message,
                            uiState.errorResponse!!
                        ),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
private fun DisplayNameField(viewModel: AuthScreenViewModel, uiState: AuthScreenState) {
    OutlinedTextField(
        label = { Text(stringResource(R.string.display_name_label)) },
        placeholder = { Text(stringResource(R.string.display_name_placeholder)) },
        singleLine = true,
        value = uiState.displayName,
        onValueChange = { viewModel.setDisplayName(it) },
        modifier = Modifier.fillMaxWidth(),
        isError = uiState.displayNameValidation != null,
        supportingText = { if (uiState.displayNameValidation != null) Text(uiState.displayNameValidation) }
    )
}

@Composable
private fun EmailField(viewModel: AuthScreenViewModel, uiState: AuthScreenState) {
    OutlinedTextField(
        label = { Text(stringResource(R.string.email_label)) },
        placeholder = { Text(stringResource(R.string.email_placeholder)) },
        singleLine = true,
        value = uiState.email,
        onValueChange = { viewModel.setEmail(it) },
        modifier = Modifier.fillMaxWidth(),
        isError = uiState.emailValidation != null,
        supportingText = { if (uiState.emailValidation != null) Text(uiState.emailValidation) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
    )
}

@Composable
private fun PasswordField(viewModel: AuthScreenViewModel, uiState: AuthScreenState) {
    OutlinedTextField(
        label = { Text(stringResource(R.string.password_label)) },
        singleLine = true,
        value = uiState.password,
        onValueChange = { viewModel.setPassword(it) },
        modifier = Modifier.fillMaxWidth(),
        isError = uiState.passwordValidation != null,
        supportingText = { if (uiState.passwordValidation != null) Text(uiState.passwordValidation) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = PasswordVisualTransformation()
    )
}

@Composable
private fun SubmitButton(
    label: String,
    enabled: Boolean,
    validateForm: () -> Unit,
    submitForm: () -> Unit,
    colors: ButtonColors,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    Button(
        enabled = enabled,
        onClick = {
            // Remove focus from text fields to close software keyboard
            focusManager.clearFocus()
            validateForm()
            submitForm()
        },
        modifier = modifier,
        colors = colors
    ) {
        Text(label)
    }
}

@Composable
private fun SubmissionInProgressButton(colors: ButtonColors, modifier: Modifier = Modifier) {
    Button(
        onClick = {},
        enabled = false,
        modifier = modifier,
        colors = colors
    ) {
        CircularProgressIndicator(
            color = colors.disabledContentColor,
            modifier = Modifier.size(dimensionResource(R.dimen.icon))
        )
    }
}

private class DisplayNameValidator(context: Context) :
    StringValidator(context.getString(R.string.display_name_validation)) {
    override fun isValid(value: String) = value.isNotEmpty()
}

private class EmailValidator(context: Context) :
    StringValidator(context.getString(R.string.email_format_validation)) {
    override fun isValid(value: String) = Patterns.EMAIL_ADDRESS.matcher(value).matches()
}

private class PasswordValidator(context: Context) :
    StringValidator(context.getString(R.string.password_length_validation, MIN_LENGTH)) {
    companion object {
        const val MIN_LENGTH = 6
    }

    override fun isValid(value: String) = value.length >= MIN_LENGTH
}
