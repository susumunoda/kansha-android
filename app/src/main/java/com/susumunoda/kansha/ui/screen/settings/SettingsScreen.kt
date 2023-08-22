package com.susumunoda.kansha.ui.screen.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExitToApp
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.susumunoda.kansha.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text(stringResource(R.string.settings_title)) })
        }
    ) { contentPadding ->
        Column(modifier = Modifier.padding(contentPadding)) {
            ListItem(
                leadingContent = {
                    Icon(
                        Icons.Rounded.ExitToApp,
                        contentDescription = stringResource(R.string.settings_sign_out)
                    )
                },
                headlineContent = { Text(stringResource(R.string.settings_sign_out)) }
            )
        }
    }
}