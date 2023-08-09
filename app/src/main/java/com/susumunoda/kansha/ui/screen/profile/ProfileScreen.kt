package com.susumunoda.kansha.ui.screen.profile

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import com.susumunoda.kansha.R
import com.susumunoda.kansha.auth.AuthController
import com.susumunoda.kansha.auth.NoOpAuthController
import com.susumunoda.kansha.auth.Session
import com.susumunoda.kansha.auth.User
import com.susumunoda.kansha.data.user.UserRepository
import com.susumunoda.kansha.ui.component.HorizontalPagerWithIndicator
import com.susumunoda.kansha.ui.component.LoadingIndicatorOverlay
import kotlinx.coroutines.launch

const val TAG = "ProfileScreen"

private enum class SetupSteps {
    // Order is significant: steps will be displayed in the order defined here
    NAME,
    PROFILE_PHOTO
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    authController: AuthController,
    userRepository: UserRepository,
    session: Session
) {
    val currentUser = session.currentUser
    var basicUserInfo: Map<String, String>? by remember { mutableStateOf(null) }

    LaunchedEffect(Unit) {
        userRepository.getBasicUserInformation(
            user = currentUser,
            onSuccess = { basicUserInfo = it },
            onError = { Log.e(TAG, "Failed to retrieve user info: ${it?.message}") }
        )
    }

    val totalPages = SetupSteps.values().size
    val pagerState = rememberPagerState { totalPages }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        stringResource(R.string.account_setup_top_bar_text),
                        style = MaterialTheme.typography.headlineLarge
                    )
                }
            )
        }
    ) { contentPadding ->
        HorizontalPagerWithIndicator(
            totalPages = totalPages,
            pagerState = pagerState,
            userScrollEnabled = false,
            modifier = Modifier.padding(contentPadding)
        ) { page ->
            when (SetupSteps.values()[page]) {
                SetupSteps.NAME -> {
                    NameStep(pagerState, authController)
                }

                SetupSteps.PROFILE_PHOTO -> {
                    ProfilePhotoStep(pagerState)
                }
            }
        }
    }

    LoadingIndicatorOverlay(showLoadingIndicator = basicUserInfo == null)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun NameStep(pagerState: PagerState, authController: AuthController) {
    val scope = rememberCoroutineScope()
    SetupStep(
        primaryAction = {
            scope.launch {
                pagerState.animateScrollToPage(pagerState.currentPage + 1)
            }
        },
        primaryActionLabel = stringResource(R.string.account_setup_next_button),
        secondaryAction = { authController.logout() },
        secondaryActionLabel = stringResource(R.string.account_setup_sign_out_button)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                stringResource(R.string.account_setup_name_prompt),
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.size(dimensionResource(R.dimen.padding_medium)))
            OutlinedTextField(
                placeholder = {
                    Text(
                        stringResource(R.string.account_setup_name_placeholder),
                        fontStyle = FontStyle.Italic
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                value = "",
                onValueChange = {}
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProfilePhotoStep(pagerState: PagerState) {
    val scope = rememberCoroutineScope()
    SetupStep(
        primaryAction = {
            scope.launch {
                pagerState.animateScrollToPage(pagerState.currentPage + 1)
            }
        },
        primaryActionLabel = stringResource(R.string.account_setup_next_button),
        secondaryAction = {
            scope.launch {
                pagerState.animateScrollToPage(pagerState.currentPage - 1)
            }
        },
        secondaryActionLabel = stringResource(R.string.account_setup_back_button)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                "Pick a profile photo or enter a URL",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.size(dimensionResource(R.dimen.padding_medium)))
            Text("TODO")
        }
    }
}

@Composable
private fun SetupStep(
    primaryAction: () -> Unit,
    primaryActionLabel: String,
    secondaryAction: (() -> Unit)? = null,
    secondaryActionLabel: String? = null,
    stepContent: @Composable ColumnScope.() -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.padding_large))
    ) {
        stepContent()
        if (secondaryAction != null && secondaryActionLabel != null) {
            OutlinedButton(
                onClick = secondaryAction,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(secondaryActionLabel)
            }
            Spacer(Modifier.size(dimensionResource(R.dimen.padding_small)))
        }
        Button(
            onClick = primaryAction,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(primaryActionLabel)
        }
    }
}

@Preview
@Composable
private fun ProfileScreenPreview() {
    val user = User("1")
    val session = Session(user)
    ProfileScreen(
        authController = NoOpAuthController(),
        userRepository = object : UserRepository {
            override fun getBasicUserInformation(
                user: User,
                onSuccess: (Map<String, String>) -> Unit,
                onError: (Exception?) -> Unit
            ) {
                onSuccess(
                    mapOf(
                        "nickname" to "Snoopie",
                        "profile_photo_url" to "https://www.example.com/snoopie.jpg"
                    )
                )
            }
        },
        session = session
    )
}