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
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.susumunoda.kansha.data.user.UserData
import com.susumunoda.kansha.data.user.UserRepository
import com.susumunoda.kansha.ui.component.HorizontalPagerWithIndicator
import com.susumunoda.kansha.ui.component.LoadingIndicatorOverlay
import com.susumunoda.kansha.ui.component.PagerController
import com.susumunoda.kansha.ui.component.rememberPagerController

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
    var userData: UserData? by remember { mutableStateOf(null) }

    LaunchedEffect(Unit) {
        userRepository.getUserData(
            id = currentUser.id,
            onSuccess = { userData = it },
            onError = { Log.e(TAG, it?.message ?: "") }
        )
    }

    val pagerController = rememberPagerController(SetupSteps.values().size)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(stringResource(R.string.account_setup_top_bar_text))
                }
            )
        }
    ) { contentPadding ->
        HorizontalPagerWithIndicator(
            pagerState = pagerController.pagerState,
            userScrollEnabled = false,
            modifier = Modifier.padding(contentPadding)
        ) { page ->
            when (SetupSteps.values()[page]) {
                SetupSteps.NAME -> {
                    NameStep(pagerController, authController)
                }

                SetupSteps.PROFILE_PHOTO -> {
                    ProfilePhotoStep(pagerController)
                }
            }
        }
    }

    LoadingIndicatorOverlay(showLoadingIndicator = userData == null)
}

@Composable
private fun NameStep(pagerController: PagerController, authController: AuthController) {
    SetupStep(
        primaryAction = { pagerController.goToNext() },
        primaryActionLabel = stringResource(R.string.account_setup_next_button),
        secondaryAction = { authController.logout() },
        secondaryActionLabel = stringResource(R.string.account_setup_sign_out_button)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {
            Text(stringResource(R.string.account_setup_name_prompt))
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

@Composable
private fun ProfilePhotoStep(pagerController: PagerController) {
    SetupStep(
        primaryAction = { pagerController.goToNext() },
        primaryActionLabel = stringResource(R.string.account_setup_next_button),
        secondaryAction = { pagerController.goToPrevious() },
        secondaryActionLabel = stringResource(R.string.account_setup_back_button)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {
            Text("Pick a profile photo or enter a URL")
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
            override fun getUserData(
                id: String,
                onSuccess: (UserData) -> Unit,
                onError: (Exception?) -> Unit
            ) {
                onSuccess(
                    UserData(
                        id = "1",
                        displayName = "Snoopie"
                    )
                )
            }

            override fun saveUserData(
                userData: UserData,
                onSuccess: () -> Unit,
                onError: (Exception?) -> Unit
            ) {
            }
        },
        session = session
    )
}