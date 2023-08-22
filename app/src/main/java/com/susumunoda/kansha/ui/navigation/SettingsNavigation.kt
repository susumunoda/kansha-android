package com.susumunoda.kansha.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.susumunoda.kansha.ui.screen.profile.ProfileScreen
import com.susumunoda.kansha.ui.screen.settings.SettingsScreen

enum class SettingsScreen {
    MAIN,
    PROFILE
}

@Composable
fun SettingsNavigation(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = SettingsScreen.MAIN.name) {
        composable(SettingsScreen.MAIN.name) {
            SettingsScreen(navController)
        }

        composable(SettingsScreen.PROFILE.name) {
            ProfileScreen()
        }
    }
}