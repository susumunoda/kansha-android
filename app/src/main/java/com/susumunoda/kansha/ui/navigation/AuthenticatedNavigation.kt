package com.susumunoda.kansha.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.susumunoda.kansha.R
import com.susumunoda.kansha.ui.screen.explore.ExploreScreen
import com.susumunoda.kansha.ui.screen.reminders.RemindersScreen

enum class Destination(
    @StringRes val titleId: Int,
    @DrawableRes val iconId: Int
) {
    EXPLORE(R.string.explore_destination, R.drawable.explore_icon),
    NOTES(R.string.notes_destination, R.drawable.notes_icon),
    REMINDERS(R.string.reminders_destination, R.drawable.calendar_icon),
    SETTINGS(R.string.settings_destination, R.drawable.settings_icon)
}

@Composable
fun AuthenticatedNavigation(navController: NavHostController = rememberNavController()) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    var selectedDestination by remember { mutableStateOf(Destination.EXPLORE) }

    // The bottom navigation's state needs to remain in sync with the current destination,
    // specifically when the user navigates via the device's back button/gesture.
    LaunchedEffect(currentBackStackEntry) {
        val route = currentBackStackEntry?.destination?.route
        if (route != null) {
            selectedDestination = Destination.valueOf(route)
        }
    }

    Column {
        Box(
            Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            NavHost(
                navController = navController,
                startDestination = Destination.EXPLORE.name,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None }
            ) {
                composable(Destination.EXPLORE.name) {
                    ExploreScreen()
                }
                composable(Destination.NOTES.name) {
                    NotesNavigation()
                }
                composable(Destination.REMINDERS.name) {
                    RemindersScreen()
                }
                composable(Destination.SETTINGS.name) {
                    SettingsNavigation()
                }
            }
        }

        BottomNavigation(
            selectedDestination = selectedDestination,
            onSelectDestination = {
                selectedDestination = it
                navController.navigate(it.name)
            }
        )
    }
}

@Composable
fun BottomNavigation(
    selectedDestination: Destination,
    onSelectDestination: (Destination) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
        windowInsets = WindowInsets(left = 0.dp, top = 0.dp, right = 0.dp, bottom = 8.dp),
    ) {
        Destination.values().forEach() { destination ->
            NavigationBarItem(
                label = { Text(stringResource(destination.titleId)) },
                icon = {
                    Icon(
                        painterResource(destination.iconId),
                        contentDescription = stringResource(destination.titleId),
                        modifier = Modifier.size(24.dp)
                    )
                },
                selected = destination == selectedDestination,
                onClick = { onSelectDestination(destination) },
            )
        }
    }
}