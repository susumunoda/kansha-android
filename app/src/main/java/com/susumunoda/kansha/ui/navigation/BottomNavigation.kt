package com.susumunoda.kansha.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.susumunoda.compose.navigation.BottomNavBar
import com.susumunoda.compose.navigation.BottomNavDestination
import com.susumunoda.kansha.R

private val BOTTOM_NAV_DESTINATIONS = listOf(
    BottomNavDestination(
        route = Destination.PROFILE.route,
        titleResId = R.string.profile_destination,
        iconResId = R.drawable.profile_icon
    ),
    BottomNavDestination(
        route = Destination.NOTES.route,
        titleResId = R.string.notes_destination,
        iconResId = R.drawable.notes_icon
    ),
    BottomNavDestination(
        route = Destination.REMINDERS.route,
        titleResId = R.string.reminders_destination,
        iconResId = R.drawable.calendar_icon
    ),
    BottomNavDestination(
        route = Destination.SETTINGS.route,
        titleResId = R.string.settings_destination,
        iconResId = R.drawable.settings_icon
    )
)

@Composable
fun BottomNavigation(navController: NavHostController) {
    BottomNavBar(destinations = BOTTOM_NAV_DESTINATIONS, navController = navController)
}