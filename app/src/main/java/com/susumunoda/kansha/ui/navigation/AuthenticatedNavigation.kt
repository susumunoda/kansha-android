package com.susumunoda.kansha.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.susumunoda.compose.navigation.composableWithoutTransitions
import com.susumunoda.kansha.ui.screen.profile.ProfileScreen
import com.susumunoda.kansha.ui.screen.reminders.RemindersScreen

enum class Destination(val route: String) {
    NOTES("notes"),
    VIEW_CATEGORIES("view_categories"),
    VIEW_CATEGORY("view_category/{${Params.CATEGORY_ID}}/{${Params.CATEGORY_NAME}}"),
    ADD_CATEGORY("add_category"),
    ADD_NOTE("add_note"),
    ADD_NOTE_WITH_CATEGORY("add_note/{${Params.CATEGORY_ID}}"),
    REMINDERS("reminders"),
    SETTINGS("settings"),
    SETTINGS_ROOT("settings_root"),
    PROFILE("profile");

    object Params {
        const val CATEGORY_ID = "categoryId"
        const val CATEGORY_NAME = "categoryName"
    }
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
            NavHost(navController = navController, startDestination = Destination.PROFILE.route) {
                composableWithoutTransitions(Destination.PROFILE.route) {
                    ProfileScreen(navController)
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
