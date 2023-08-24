package com.susumunoda.kansha.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.susumunoda.kansha.ui.animation.enterSlidingUp
import com.susumunoda.kansha.ui.animation.exitSlidingDown
import com.susumunoda.kansha.ui.screen.profile.ProfileScreen
import com.susumunoda.kansha.ui.screen.settings.SettingsScreen

enum class SettingsScreen {
    MAIN,
    PROFILE
}

@Composable
fun SettingsNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = SettingsScreen.MAIN.name) {
        composable(
            route = SettingsScreen.MAIN.name,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            SettingsScreen(navController)
        }

        composable(
            route = SettingsScreen.PROFILE.name,
            enterTransition = { enterSlidingUp() },
            exitTransition = { exitSlidingDown() }
        ) {
            ProfileScreen(navController)
        }
    }
}