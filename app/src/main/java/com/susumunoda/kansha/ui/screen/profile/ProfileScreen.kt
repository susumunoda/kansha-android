package com.susumunoda.kansha.ui.screen.profile

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.susumunoda.kansha.auth.AuthController
import com.susumunoda.kansha.auth.Session
import com.susumunoda.kansha.data.user.UserRepository
import com.susumunoda.kansha.ui.component.LoadingIndicatorOverlay
import com.susumunoda.kansha.ui.component.LogoutButton

const val TAG = "ProfileScreen"

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

    Scaffold { contentPadding ->
        Column(modifier = Modifier.padding(contentPadding)) {
            Text(basicUserInfo.toString())
            LogoutButton { authController.logout() }
        }
    }

    LoadingIndicatorOverlay(showLoadingIndicator = basicUserInfo == null)
}