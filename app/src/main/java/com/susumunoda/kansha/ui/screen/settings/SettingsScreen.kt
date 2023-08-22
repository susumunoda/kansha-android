package com.susumunoda.kansha.ui.screen.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExitToApp
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.susumunoda.kansha.R
import com.susumunoda.kansha.auth.MockAuthController
import com.susumunoda.kansha.ui.component.ScaffoldWithStatusBarInsets
import com.susumunoda.kansha.ui.navigation.SettingsScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavHostController,
    viewModel: SettingsScreenViewModel = hiltViewModel()
) {
    ScaffoldWithStatusBarInsets(
        topBar = {
            CenterAlignedTopAppBar(title = { Text(stringResource(R.string.settings_title)) })
        }
    ) {
        LazyColumn {
            item {
                val profileText = stringResource(R.string.settings_item_profile)
                ListItem(
                    leadingContent = { Icon(Icons.Rounded.Person, profileText) },
                    headlineContent = { Text(profileText) },
                    modifier = Modifier.clickable { navController.navigate(SettingsScreen.PROFILE.name) }
                )
            }
            item {
                val signOutText = stringResource(R.string.settings_item_sign_out)
                ListItem(
                    leadingContent = { Icon(Icons.Rounded.ExitToApp, signOutText) },
                    headlineContent = { Text(signOutText) },
                    modifier = Modifier.clickable { viewModel.logout() }
                )
            }
        }
    }
}

@Preview
@Composable
fun SettingsScreenPreview() {
    val navController = rememberNavController()
    val viewModel = SettingsScreenViewModel(MockAuthController())
    SettingsScreen(navController, viewModel)
}
