package com.susumunoda.kansha.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.susumunoda.kansha.ui.screen.profile.ProfileScreen

enum class AuthenticatedScreen { PROFILE }

@Composable
fun AuthenticatedNavigation(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = AuthenticatedScreen.PROFILE.name) {
        composable(AuthenticatedScreen.PROFILE.name) {
            ProfileScreen()
        }
    }
}
