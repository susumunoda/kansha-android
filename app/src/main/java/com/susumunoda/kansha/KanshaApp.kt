package com.susumunoda.kansha

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.susumunoda.auth.AuthController
import com.susumunoda.auth.Session
import com.susumunoda.kansha.ui.navigation.AuthenticatedNavigation
import com.susumunoda.kansha.ui.navigation.UnauthenticatedNavigation

@Composable
fun KanshaApp(authController: AuthController) {
    val session by authController.sessionFlow.collectAsState()

    if (session != Session.UNKNOWN) {
        if (session == Session.LOGGED_OUT) {
            UnauthenticatedNavigation()
        } else {
            AuthenticatedNavigation()
        }
    }
}
