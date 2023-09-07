package com.susumunoda.kansha.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.susumunoda.kansha.ui.animation.enterSlidingLeft
import com.susumunoda.kansha.ui.animation.exitSlidingRight
import com.susumunoda.kansha.ui.screen.profile.ProfileScreen
import com.susumunoda.kansha.ui.screen.settings.SettingsScreen

enum class SettingsScreen {
    SETTINGS_ROOT,
    PROFILE
}

fun NavGraphBuilder.settingsNavigation(navController: NavHostController) {
    navigation(
        route = Destination.SETTINGS.name,
        startDestination = SettingsScreen.SETTINGS_ROOT.name
    ) {
        composableWithoutTransitions(SettingsScreen.SETTINGS_ROOT.name) {
            SettingsScreen(navController)
        }

        composableWithConditionalTransitions(
            route = SettingsScreen.PROFILE.name,
            enterTransition = { enterSlidingLeft() },
            enterTransitionFrom = SettingsScreen.SETTINGS_ROOT.name,
            exitTransition = { exitSlidingRight() },
            exitTransitionTo = SettingsScreen.SETTINGS_ROOT.name
        ) {
            ProfileScreen(navController)
        }
    }
}