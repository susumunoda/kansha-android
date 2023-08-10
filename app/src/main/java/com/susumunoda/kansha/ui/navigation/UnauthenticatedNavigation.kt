package com.susumunoda.kansha.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.susumunoda.kansha.auth.AuthController
import com.susumunoda.kansha.auth.NoOpAuthController
import com.susumunoda.kansha.ui.screen.login.LoginScreen
import com.susumunoda.kansha.ui.screen.login.SignupScreen

enum class UnauthenticatedScreen { LOGIN, SIGNUP }

private const val SCREEN_TRANSITION_DURATION = 350

@Composable
fun UnauthenticatedNavigation(
    navController: NavHostController = rememberNavController(),
    authController: AuthController
) {
    NavHost(navController = navController, startDestination = UnauthenticatedScreen.LOGIN.name) {
        composable(UnauthenticatedScreen.LOGIN.name) {
            LoginScreen(navController)
        }
        composable(
            route = UnauthenticatedScreen.SIGNUP.name,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(
                        durationMillis = SCREEN_TRANSITION_DURATION,
                        easing = FastOutSlowInEasing
                    )
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(
                        durationMillis = SCREEN_TRANSITION_DURATION,
                        easing = FastOutSlowInEasing
                    )
                )
            }
        ) {
            SignupScreen(navController)
        }
    }
}

@Preview
@Composable
fun UnauthenticatedNavigationPreview() {
    UnauthenticatedNavigation(authController = NoOpAuthController())
}