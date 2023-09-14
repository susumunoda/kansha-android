package com.susumunoda.kansha.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.susumunoda.compose.animation.enterSlidingLeft
import com.susumunoda.compose.animation.exitSlidingRight
import com.susumunoda.kansha.ui.screen.auth.LoginScreen
import com.susumunoda.kansha.ui.screen.auth.SignupScreen

enum class UnauthenticatedScreen { LOGIN, SIGNUP }

@Composable
fun UnauthenticatedNavigation(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = UnauthenticatedScreen.LOGIN.name) {
        composable(UnauthenticatedScreen.LOGIN.name) {
            LoginScreen(navController)
        }
        composable(
            route = UnauthenticatedScreen.SIGNUP.name,
            enterTransition = { enterSlidingLeft() },
            exitTransition = { exitSlidingRight() }
        ) {
            SignupScreen(navController)
        }
    }
}

@Preview
@Composable
fun UnauthenticatedNavigationPreview() {
    UnauthenticatedNavigation()
}