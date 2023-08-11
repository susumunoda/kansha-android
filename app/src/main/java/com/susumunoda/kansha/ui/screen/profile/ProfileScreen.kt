package com.susumunoda.kansha.ui.screen.profile

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.susumunoda.kansha.R
import com.susumunoda.kansha.auth.AuthController
import com.susumunoda.kansha.auth.NoOpAuthController
import com.susumunoda.kansha.auth.Session
import com.susumunoda.kansha.auth.User
import com.susumunoda.kansha.data.user.UserData
import com.susumunoda.kansha.data.user.UserRepository
import com.susumunoda.kansha.data.user.UserRepository.GetUserDataResult.Failure
import com.susumunoda.kansha.data.user.UserRepository.GetUserDataResult.Success
import com.susumunoda.kansha.ui.component.LoadingIndicatorOverlay
import com.susumunoda.kansha.ui.component.LogoutButton

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
        when (val result = userRepository.getUserData(currentUser.id)) {
            is Success -> {
                userData = result.userData
            }

            is Failure -> {
                Log.e(TAG, "User data fetch failed: ${result.exception.message}")
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(stringResource(R.string.account_setup_top_bar_text))
                }
            )
        }
    ) { contentPadding ->
        Column(modifier = Modifier.padding(contentPadding)) {
            Text("id = ${currentUser.id}")
            Text("displayName = ${userData?.displayName}")
            LogoutButton { authController.logout() }
        }
    }

    LoadingIndicatorOverlay(showLoadingIndicator = userData == null)
}

@Preview
@Composable
private fun ProfileScreenPreview() {
    val user = User("1")
    val session = Session(user)
    ProfileScreen(
        authController = NoOpAuthController(),
        userRepository = object : UserRepository {
            override suspend fun getUserData(id: String): UserRepository.GetUserDataResult {
                return Success(UserData("Snoopie"))
            }

            override fun saveUserData(
                id: String,
                userData: UserData,
                onSuccess: () -> Unit,
                onError: (Exception?) -> Unit
            ) {
            }
        },
        session = session
    )
}