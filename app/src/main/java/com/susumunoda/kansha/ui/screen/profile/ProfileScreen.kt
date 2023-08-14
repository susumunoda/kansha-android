package com.susumunoda.kansha.ui.screen.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.susumunoda.kansha.auth.NoOpAuthController
import com.susumunoda.kansha.auth.User
import com.susumunoda.kansha.data.user.MockSuccessUserRepository
import com.susumunoda.kansha.data.user.UserData
import com.susumunoda.kansha.ui.component.DefaultUserPhoto
import com.susumunoda.kansha.ui.component.LogoutButton

@Composable
fun ProfileScreen(viewModel: ProfileScreenViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

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
                DefaultUserPhoto()
                Text(uiState.displayName, style = MaterialTheme.typography.titleLarge)
                LogoutButton { viewModel.logout() }
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