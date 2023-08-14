package com.susumunoda.kansha.ui.screen.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExitToApp
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.susumunoda.kansha.R
import com.susumunoda.kansha.auth.NoOpAuthController
import com.susumunoda.kansha.auth.User
import com.susumunoda.kansha.data.user.MockSuccessUserRepository
import com.susumunoda.kansha.data.user.UserData
import com.susumunoda.kansha.ui.component.DefaultUserPhoto

@Composable
fun ProfileScreen(viewModel: ProfileScreenViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    var isMenuExpanded by remember { mutableStateOf(false) }

    AnimatedVisibility(
        visible = !uiState.isLoading,
        enter = slideInVertically(
            initialOffsetY = { fullHeight -> fullHeight / 4 }
        )
    ) {
        Surface {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.statusBars),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    DefaultUserPhoto()
                    Box(modifier = Modifier.align(Alignment.CenterEnd)) {
                        IconButton(onClick = { isMenuExpanded = true }) {
                            Icon(
                                Icons.Rounded.MoreVert,
                                stringResource(R.string.expand_menu_description)
                            )
                        }
                        DropdownMenu(
                            expanded = isMenuExpanded,
                            onDismissRequest = { isMenuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = stringResource(R.string.sign_out_menu_item),
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                },
                                onClick = { viewModel.logout() },
                                trailingIcon = { Icon(Icons.Rounded.ExitToApp, null) }
                            )
                        }
                    }
                }

                Text(uiState.displayName, style = MaterialTheme.typography.titleLarge)
            }
        }
    }
}

@Preview
@Composable
private fun ProfileScreenPreview() {
    val authController = NoOpAuthController(User("1"))
    val userRepository = MockSuccessUserRepository(UserData("Pikachu"))
    val viewModel = ProfileScreenViewModel(authController, userRepository)
    ProfileScreen(viewModel)
}