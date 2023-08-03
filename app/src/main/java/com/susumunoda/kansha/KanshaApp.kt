package com.susumunoda.kansha

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.susumunoda.kansha.auth.AuthController
import com.susumunoda.kansha.ui.screen.auth.AuthNavigation
import com.susumunoda.kansha.ui.screen.listview.ListViewScreen
import com.susumunoda.kansha.ui.screen.newmessage.NewMessageScreen

enum class Screen {
    LIST_VIEW,
    NEW_MESSAGE,
    AUTH
}

private const val DIALOG_EASING_MILLIS = 350

@Composable
fun KanshaApp(
    navController: NavHostController = rememberNavController(),
    authController: AuthController
) {
    val session by authController.sessionFlow.collectAsState()
    val startScreen = if (session == null) Screen.AUTH else Screen.LIST_VIEW
    NavHost(navController = navController, startDestination = startScreen.name) {
        composable(Screen.AUTH.name) {
            AuthNavigation(authController = authController)
        }
        composable(Screen.LIST_VIEW.name) {
            ListViewScreen(navController = navController, authController = authController)
        }
        composable(
            route = Screen.NEW_MESSAGE.name,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = tween(
                        durationMillis = DIALOG_EASING_MILLIS,
                        easing = FastOutSlowInEasing
                    )
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Down,
                    animationSpec = tween(
                        durationMillis = DIALOG_EASING_MILLIS,
                        easing = FastOutSlowInEasing
                    )
                )
            }
        ) {
            NewMessageScreen(navController = navController)
        }
    }
}
