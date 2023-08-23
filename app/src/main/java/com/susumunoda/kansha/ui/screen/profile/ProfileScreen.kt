package com.susumunoda.kansha.ui.screen.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.susumunoda.kansha.R
import com.susumunoda.kansha.ui.component.DefaultUserPhoto
import com.susumunoda.kansha.ui.component.ScaffoldWithStatusBarInsets
import com.susumunoda.kansha.ui.component.UserPhoto
import com.susumunoda.kansha.ui.mock.MockProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavHostController,
    viewModel: ProfileScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val user = uiState.user
    val error = uiState.error

    ScaffoldWithStatusBarInsets(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.profile_title)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_button_description)
                        )
                    }
                }
            )
        }
    ) {
        Box(contentAlignment = Alignment.Center) {
            if (user != null) {
                if (user.backgroundPhotoUrl.isNotBlank()) {
                    AsyncImage(
                        model = user.backgroundPhotoUrl,
                        contentDescription = stringResource(R.string.profile_background_photo_description),
                        modifier = Modifier.height(dimensionResource(R.dimen.profile_background_photo_height)),
                        contentScale = ContentScale.Crop,
                        // Only for displaying in an @Preview
                        placeholder = if (LocalInspectionMode.current) {
                            painterResource(R.drawable.preview_background_photo)
                        } else null
                    )
                } else {
                    Spacer(Modifier.height(dimensionResource(R.dimen.profile_background_photo_height)))
                }
                if (user.profilePhotoUrl.isBlank()) {
                    DefaultUserPhoto(
                        size = dimensionResource(R.dimen.profile_photo_size_large)
                    )
                } else {
                    UserPhoto(
                        url = user.profilePhotoUrl,
                        size = dimensionResource(R.dimen.profile_photo_size_large),
                        // Only for displaying in an @Preview
                        placeholder = if (LocalInspectionMode.current) {
                            painterResource(R.drawable.preview_profile_photo)
                        } else null
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .background(Color.Black.copy(alpha = 0.2f))
                        .height(dimensionResource(R.dimen.profile_name_background_height))
                        .fillMaxWidth()
                ) {
                    Text(
                        user.displayName,
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White
                    )
                }
            } else if (error != null) {
                Box(Modifier.padding(dimensionResource(R.dimen.padding_medium))) {
                    Text(
                        stringResource(R.string.profile_fetch_error, error.message ?: ""),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun ProfileScreenPreview() {
    val navController = rememberNavController()
    val mockProvider = MockProvider().apply { userRepositoryDatabase[sessionUserId] = user }
    val authController = mockProvider.authController
    val userRepository = mockProvider.userRepository
    val viewModel = ProfileScreenViewModel(authController, userRepository)
    ProfileScreen(navController, viewModel)
}

@Preview
@Composable
fun ProfileScreenErrorPreview() {
    val navController = rememberNavController()
    val mockProvider = MockProvider().apply { userRepositoryErrorOnGetUser = true }
    val authController = mockProvider.authController
    val userRepository = mockProvider.userRepository
    val viewModel = ProfileScreenViewModel(authController, userRepository)
    ProfileScreen(navController, viewModel)
}
