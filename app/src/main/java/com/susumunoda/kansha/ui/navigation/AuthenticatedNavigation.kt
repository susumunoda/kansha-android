package com.susumunoda.kansha.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.susumunoda.kansha.ui.screen.explore.ExploreScreen
import com.susumunoda.kansha.ui.screen.reminders.RemindersScreen

enum class Destination(val route: String) {
    EXPLORE("explore"),
    NOTES("notes"),
    REMINDERS("reminders"),
    SETTINGS("settings"),
}

@Composable
fun AuthenticatedNavigation() {
    val navController = rememberNavController()

    Column {
        Box(
            Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            NavHost(navController = navController, startDestination = Destination.EXPLORE.route) {
                composableWithoutTransitions(Destination.EXPLORE.route) {
                    ExploreScreen()
                }

                composableWithoutTransitions(Destination.REMINDERS.route) {
                    RemindersScreen()
                }

                notesNavigation(navController)

                settingsNavigation(navController)
            }
        }

        BottomNavigation(navController)
    }
}
