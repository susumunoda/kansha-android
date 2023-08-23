package com.susumunoda.kansha.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.susumunoda.kansha.R
import com.susumunoda.kansha.ui.screen.explore.ExploreScreen
import com.susumunoda.kansha.ui.screen.reminders.RemindersScreen

enum class Destination(
    @StringRes val titleId: Int,
    @DrawableRes val iconId: Int,
    private val provideNavController: Boolean
) {
    EXPLORE(R.string.explore_destination, R.drawable.explore_icon, false),
    NOTES(R.string.notes_destination, R.drawable.notes_icon, false),
    REMINDERS(R.string.reminders_destination, R.drawable.calendar_icon, false),
    SETTINGS(R.string.settings_destination, R.drawable.settings_icon, true);

    // Destination NavHostController is managed at the top-level — e.g. so that the user is taken
    // back to the previous screen they were looking at before navigating away, or to take the user
    // back to the navigation root if the bottom navigation icon is tapped for a second time.
    private lateinit var _navController: NavHostController
    val navController: NavHostController?
        @Composable
        get() {
            if (provideNavController) {
                if (!this::_navController.isInitialized) {
                    _navController = rememberNavController()
                }
                return _navController
            }
            return null
        }
}

@Composable
fun AuthenticatedNavigation() {
    var selectedDestination by remember { mutableStateOf(Destination.EXPLORE) }

    Column {
        Box(
            Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            // Explicitly not using NavHost because we don't need any of the navigation behavior
            // between top-level destinations (e.g. pushing onto or popping the back stack).
            // The desired behavior is for all top-level destinations to be at the root of the app —
            // that is, the back button/gesture does the same thing across destinations.
            when (selectedDestination) {
                Destination.EXPLORE -> {
                    ExploreScreen()
                }

                Destination.NOTES -> {
                    NotesNavigation()
                }

                Destination.REMINDERS -> {
                    RemindersScreen()
                }

                Destination.SETTINGS -> {
                    SettingsNavigation(Destination.SETTINGS.navController!!)
                }
            }
        }

        BottomNavigation(
            selectedDestination = selectedDestination,
            onSelectDestination = { selectedDestination = it }
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
            val selected = destination == selectedDestination
            val navController = destination.navController

            NavigationBarItem(
                label = { Text(stringResource(destination.titleId)) },
                icon = {
                    Icon(
                        painterResource(destination.iconId),
                        contentDescription = stringResource(destination.titleId),
                        modifier = Modifier.size(24.dp)
                    )
                },
                selected = selected,
                onClick = {
                    if (!selected) {
                        onSelectDestination(destination)
                    } else if (navController?.previousBackStackEntry != null) {
                        // If the user had previously visited a destination which itself had
                        // navigation (e.g. settings page) and the last visited screen there was not
                        // the root, then tapping the bottom nav for a second time should take the
                        // user to the root of that top-level destination.
                        while (navController.previousBackStackEntry != null) {
                            navController.popBackStack()
                        }
                    }
                }
            )
        }
    }
}