package com.susumunoda.kansha

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import com.susumunoda.kansha.auth.AuthController
import com.susumunoda.kansha.auth.Session
import com.susumunoda.kansha.ui.navigation.AuthenticatedNavigation
import com.susumunoda.kansha.ui.navigation.UnauthenticatedNavigation
import kotlinx.coroutines.delay

private const val LOADING_SCREEN_MILLIS = 500L

@Composable
fun KanshaApp(authController: AuthController) {
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(LOADING_SCREEN_MILLIS)
        loading = false
    }

    if (loading) {
        Box(contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        val session by authController.sessionFlow.collectAsState()
        if (session == Session.LOGGED_OUT) {
            UnauthenticatedNavigation(authController = authController)
        } else {
            AuthenticatedNavigation(authController = authController, session = session)
        }
    }
}
