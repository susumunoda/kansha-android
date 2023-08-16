package com.susumunoda.kansha.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.susumunoda.kansha.ui.animation.enterSlidingUp
import com.susumunoda.kansha.ui.animation.exitSlidingDown
import com.susumunoda.kansha.ui.screen.addnote.AddNoteScreen
import com.susumunoda.kansha.ui.screen.profile.ProfileScreen

enum class AuthenticatedScreen {
    PROFILE,
    ADD_NOTE
}

@Composable
fun AuthenticatedNavigation(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = AuthenticatedScreen.PROFILE.name) {
        composable(AuthenticatedScreen.PROFILE.name) {
            ProfileScreen(navController)
        }

        composable(
            route = AuthenticatedScreen.ADD_NOTE.name,
            enterTransition = { enterSlidingUp() },
            exitTransition = { exitSlidingDown() }

        ) {
            AddNoteScreen(navController)
        }
    }
}
