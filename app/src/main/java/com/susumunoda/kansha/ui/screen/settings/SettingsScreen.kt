package com.susumunoda.kansha.ui.screen.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExitToApp
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.susumunoda.kansha.R
import com.susumunoda.kansha.ui.component.ScaffoldWithStatusBarInsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: SettingsScreenViewModel = hiltViewModel()) {
    ScaffoldWithStatusBarInsets(
        topBar = {
            CenterAlignedTopAppBar(title = { Text(stringResource(R.string.settings_title)) })
        }
    ) {
        LazyColumn {
            item {
                ListItem(
                    leadingContent = {
                        Icon(
                            Icons.Rounded.ExitToApp,
                            contentDescription = stringResource(R.string.settings_sign_out)
                        )
                    },
                    headlineContent = { Text(stringResource(R.string.settings_sign_out)) },
                    modifier = Modifier.clickable { viewModel.logout() }
                )
            }
        }
    }
}