package com.susumunoda.kansha.ui.screen.login

import android.content.Context
import android.util.Log
import android.util.Patterns
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.navigation.NavHostController
import com.susumunoda.kansha.BuildConfig
import com.susumunoda.kansha.R
import com.susumunoda.kansha.auth.AuthController
import com.susumunoda.kansha.ui.component.BackButton
import com.susumunoda.kansha.ui.navigation.UnauthenticatedScreen
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavHostController, authController: AuthController) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    UserCredentialsForm(
        title = stringResource(R.string.login_top_bar_text),
        submitButtonLabel = stringResource(R.string.login_button_text),
        snackbarHostState = snackbarHostState,
        onSubmit = { email, password ->
            authController.login(email, password) { exception ->
                if (exception != null) {
                    Log.e("LoginScreen", "Login failed with exception: ${exception.message}")
                    scope.launch {
                        snackbarHostState.showSnackbar(context.getString(R.string.login_failed_message))
                    }
                }
            }
        }
    ) {
        if (BuildConfig.DEBUG && BuildConfig.SHOW_TEST_LOGIN.toBoolean()) {
            val testEmail = BuildConfig.TEST_EMAIL
            val testPassword = BuildConfig.TEST_PASSWORD
            Button(
                onClick = { authController.login(testEmail, testPassword, {}) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text("Log in as $testEmail")
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

@Composable
fun SignupScreen(navController: NavHostController, authController: AuthController) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    UserCredentialsForm(
        title = stringResource(R.string.create_account_top_bar_text),
        submitButtonLabel = stringResource(R.string.signup_button_text),
        snackbarHostState = snackbarHostState,
        onSubmit = { email, password ->
            authController.createUser(email, password) { exception ->
                if (exception != null) {
                    Log.e(
                        "SignupScreen",
                        "User creation failed with exception: ${exception.message}"
                    )
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            context.getString(
                                R.string.user_creation_failed_message,
                                exception.message
                            )
                        )
                    }
                }
            }
        },
        navigationIcon = { BackButton { navController.popBackStack() } }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserCredentialsForm(
    title: String,
    submitButtonLabel: String,
    onSubmit: (String, String) -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    navigationIcon: @Composable () -> Unit = {},
    additionalSteps: @Composable ColumnScope.() -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var emailValidation: String? by remember { mutableStateOf(null) }
    var passwordValidation: String? by remember { mutableStateOf(null) }

    val loginEnabled = email.isNotEmpty() && password.isNotEmpty()

    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(title) },
                navigationIcon = navigationIcon
            )
        }
    ) { paddingValues ->
        Surface(modifier = Modifier.padding(paddingValues)) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(dimensionResource(R.dimen.padding_large))
            ) {
                Column {
                    OutlinedTextField(
                        label = { Text(stringResource(R.string.email_label_text)) },
                        singleLine = true,
                        value = email,
                        onValueChange = {
                            email = it
                            emailValidation = null
                        },
                        modifier = Modifier.fillMaxWidth(),
                        isError = emailValidation != null,
                        supportingText = { if (emailValidation != null) Text(emailValidation!!) },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email
                        )
                    )
                    OutlinedTextField(
                        label = { Text(stringResource(R.string.password_label_text)) },
                        singleLine = true,
                        value = password,
                        onValueChange = {
                            password = it
                            passwordValidation = null
                        },
                        modifier = Modifier.fillMaxWidth(),
                        isError = passwordValidation != null,
                        supportingText = { if (passwordValidation != null) Text(passwordValidation!!) },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password
                        ),
                        visualTransformation = PasswordVisualTransformation()
                    )
                    Button(
                        enabled = loginEnabled,
                        onClick = {
                            emailValidation = validateEmail(email, context)
                            passwordValidation = validatePassword(password, context)

                            if (emailValidation == null && passwordValidation == null) {
                                // Remove focus from text fields so that snackbar is visible at bottom of screen
                                focusManager.clearFocus()
                                onSubmit(email, password)
                            }
                        }, modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(submitButtonLabel)
                    }
                }

                additionalSteps()
            }
        }
    }
}

private fun validateEmail(email: String, context: Context) =
    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        context.getString(R.string.email_format_validation)
    } else null

private const val MIN_PASSWORD_LENGTH = 6
private fun validatePassword(password: String, context: Context) =
    if (password.length < MIN_PASSWORD_LENGTH) {
        context.getString(R.string.password_length_validation, MIN_PASSWORD_LENGTH)
    } else null
