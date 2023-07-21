package com.susumunoda.kansha

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.susumunoda.kansha.ui.screen.listview.ListViewScreen
import com.susumunoda.kansha.ui.screen.newmessage.NewMessageScreen

enum class Screen {
    LIST_VIEW,
    NEW_MESSAGE
}

private const val DIALOG_EASING_MILLIS = 350

@Composable
fun KanshaApp(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Screen.LIST_VIEW.name) {
        composable(Screen.LIST_VIEW.name) {
            ListViewScreen(navController = navController)
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
            NewMessageScreen()
        }
    }
}

@Preview
@Composable
private fun KanshaAppPreview() {
    KanshaApp()
}