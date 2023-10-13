package com.susumunoda.kansha.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.susumunoda.kansha.ui.screen.settings.SettingsScreen

fun NavGraphBuilder.settingsNavigation(navController: NavHostController) {
    navigation(
        route = Destination.SETTINGS.route,
        startDestination = Destination.SETTINGS_ROOT.route
    ) {
        composableWithoutTransitions(Destination.SETTINGS_ROOT.route) {
            SettingsScreen(navController)
        }
    }
}