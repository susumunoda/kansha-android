package com.susumunoda.kansha.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.susumunoda.kansha.auth.AuthController
import com.susumunoda.kansha.auth.NoOpAuthController
import com.susumunoda.kansha.auth.Session
import com.susumunoda.kansha.auth.User
import com.susumunoda.kansha.ui.screen.profile.ProfileScreen

enum class AuthenticatedScreen { PROFILE }

@Composable
fun AuthenticatedNavigation(
    navController: NavHostController = rememberNavController(),
    authController: AuthController,
    session: Session
) {
    NavHost(navController = navController, startDestination = AuthenticatedScreen.PROFILE.name) {
        composable(AuthenticatedScreen.PROFILE.name) {
            ProfileScreen(authController = authController, session = session)
        }
    }
}

@Preview
@Composable
fun AuthenticatedNavigationPreview() {
    val session = Session(User("1", "John Doe"))
    AuthenticatedNavigation(
        authController = NoOpAuthController(session),
        session = session
    )
}