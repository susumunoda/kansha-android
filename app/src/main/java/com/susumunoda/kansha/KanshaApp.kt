package com.susumunoda.kansha

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.susumunoda.kansha.ui.screen.listview.ListViewScreen

enum class Screen {
    LIST_VIEW
}

@Composable
fun KanshaApp(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Screen.LIST_VIEW.name) {
        composable(Screen.LIST_VIEW.name) {
            ListViewScreen()
        }
    }
}

@Preview
@Composable
private fun KanshaAppPreview() {
    KanshaApp()
}