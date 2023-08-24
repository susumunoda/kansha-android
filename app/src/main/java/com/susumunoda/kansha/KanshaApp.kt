package com.susumunoda.kansha

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.susumunoda.kansha.auth.AuthController
import com.susumunoda.kansha.auth.Session
import com.susumunoda.kansha.data.user.UserRepository
import com.susumunoda.kansha.ui.navigation.AuthenticatedNavigation
import com.susumunoda.kansha.ui.navigation.UnauthenticatedNavigation

@Composable
fun KanshaApp(authController: AuthController, userRepository: UserRepository) {
    val session by authController.sessionFlow.collectAsState()

    if (session != Session.UNKNOWN) {
        if (session == Session.LOGGED_OUT) {
            UnauthenticatedNavigation()
        } else {
            AuthenticatedNavigation()
        }
    }
}
