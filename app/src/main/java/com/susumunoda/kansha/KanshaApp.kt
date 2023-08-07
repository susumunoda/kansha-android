package com.susumunoda.kansha

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.susumunoda.kansha.auth.AuthController
import com.susumunoda.kansha.ui.screen.auth.AuthNavigation

enum class Screen { AUTH }

@Composable
fun KanshaApp(
    navController: NavHostController = rememberNavController(),
    authController: AuthController
) {
    NavHost(navController = navController, startDestination = Screen.AUTH.name) {
        composable(Screen.AUTH.name) {
            AuthNavigation(authController = authController)
        }
    }
}
