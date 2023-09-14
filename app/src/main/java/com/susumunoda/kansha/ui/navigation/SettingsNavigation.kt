package com.susumunoda.kansha.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.susumunoda.kansha.ui.animation.enterSlidingLeft
import com.susumunoda.kansha.ui.animation.exitSlidingRight
import com.susumunoda.kansha.ui.screen.profile.ProfileScreen
import com.susumunoda.kansha.ui.screen.settings.SettingsScreen

fun NavGraphBuilder.settingsNavigation(navController: NavHostController) {
    navigation(
        route = Destination.SETTINGS.route,
        startDestination = Destination.SETTINGS_ROOT.route
    ) {
        composableWithoutTransitions(Destination.SETTINGS_ROOT.route) {
            SettingsScreen(navController)
        }

        composableWithConditionalTransitions(
            route = Destination.PROFILE.route,
            enterTransition = { enterSlidingLeft() },
            enterTransitionFrom = Destination.SETTINGS_ROOT.route,
            exitTransition = { exitSlidingRight() },
            exitTransitionTo = Destination.SETTINGS_ROOT.route
        ) {
            ProfileScreen(navController)
        }
    }
}