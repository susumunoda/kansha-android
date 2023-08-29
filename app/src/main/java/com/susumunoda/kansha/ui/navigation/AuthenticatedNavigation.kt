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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
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
    SETTINGS(R.string.settings_destination, R.drawable.settings_icon);
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
            NavHost(navController = navController, startDestination = Destination.EXPLORE.name) {
                composable(
                    route = Destination.EXPLORE.name,
                    enterTransition = { EnterTransition.None },
                    exitTransition = { ExitTransition.None }
                ) {
                    ExploreScreen()
                }

                composable(
                    route = Destination.REMINDERS.name,
                    enterTransition = { EnterTransition.None },
                    exitTransition = { ExitTransition.None }
                ) {
                    RemindersScreen()
                }

                notesNavigation(navController)
                settingsNavigation(navController)
            }
        }

        BottomNavigation(navController)
    }
}

@Composable
fun BottomNavigation(navController: NavHostController, modifier: Modifier = Modifier) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()

    NavigationBar(
        modifier = modifier,
        windowInsets = WindowInsets(
            left = 0.dp,
            top = 0.dp,
            right = 0.dp,
            bottom = dimensionResource(R.dimen.padding_small)
        ),
    ) {
        Destination.values().forEach() { destination ->
            val selected =
                currentBackStackEntry?.destination?.hierarchy?.any { it.route == destination.name } == true

            NavigationBarItem(
                label = { Text(stringResource(destination.titleId)) },
                icon = {
                    Icon(
                        painterResource(destination.iconId),
                        contentDescription = stringResource(destination.titleId),
                        modifier = Modifier.size(dimensionResource(R.dimen.icon))
                    )
                },
                selected = selected,
                onClick = {
                    navController.navigate(destination.name) {
                        // Pop up to the main navigation root to avoid creating a large backstack
                        popUpTo(navController.graph.findStartDestination().id) {
                            // If navigating to a different top-level destination, save the state
                            // (including backstack) of the current destination and restore it for
                            // the destination being navigated to.
                            // Otherwise, navigate to the root of the selected destination.
                            if (!selected) {
                                saveState = true
                            }
                        }
                        if (!selected) {
                            restoreState = true
                        }
                        // Avoid multiple copies of the destination at the top of the backstack
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}