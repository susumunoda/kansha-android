package com.susumunoda.kansha

import androidx.compose.animation.AnimatedContentTransitionScope
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
                    animationSpec = tween(durationMillis = 500)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Down,
                    animationSpec = tween(durationMillis = 500)
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