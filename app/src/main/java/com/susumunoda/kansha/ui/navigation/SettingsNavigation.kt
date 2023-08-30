package com.susumunoda.kansha.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.susumunoda.kansha.ui.animation.enterSlidingLeft
import com.susumunoda.kansha.ui.animation.exitSlidingRight
import com.susumunoda.kansha.ui.screen.profile.ProfileScreen
import com.susumunoda.kansha.ui.screen.settings.SettingsScreen

enum class SettingsScreen {
    MAIN,
    PROFILE
}

fun NavGraphBuilder.settingsNavigation(navController: NavHostController) {
    navigation(route = Destination.SETTINGS.name, startDestination = SettingsScreen.MAIN.name) {
        composableWithoutTransitions(SettingsScreen.MAIN.name) {
            SettingsScreen(navController)
        }

        composable(
            route = SettingsScreen.PROFILE.name,
            enterTransition = { enterSlidingLeft() },
            exitTransition = { exitSlidingRight() }
        ) {
            ProfileScreen(navController)
        }
    }
}