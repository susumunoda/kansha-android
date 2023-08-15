package com.susumunoda.kansha.ui.screen.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExitToApp
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.susumunoda.kansha.R
import com.susumunoda.kansha.auth.NoOpAuthController
import com.susumunoda.kansha.auth.User
import com.susumunoda.kansha.data.note.MockSuccessNoteRepository
import com.susumunoda.kansha.data.note.NoteData
import com.susumunoda.kansha.data.user.MockSuccessUserRepository
import com.susumunoda.kansha.data.user.UserData
import com.susumunoda.kansha.ui.component.DefaultUserPhoto
import com.susumunoda.kansha.ui.component.UserPhoto
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(viewModel: ProfileScreenViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    AnimatedVisibility(
        visible = !uiState.userDataFetchInProgress && !uiState.notesDataFetchInProgress,
        enter = slideInVertically(initialOffsetY = { fullHeight -> fullHeight / 4 })
    ) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet {
                    NavigationDrawerItem(
                        label = { Text(stringResource(R.string.sign_out_menu_item)) },
                        icon = {
                            Icon(
                                Icons.Rounded.ExitToApp,
                                stringResource(R.string.sign_out_menu_item)
                            )
                        },
                        selected = false,
                        onClick = { viewModel.logout() }
                    )
                }
            }
        ) {
            Surface {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .windowInsetsPadding(WindowInsets.statusBars),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(
                        onClick = { scope.launch { drawerState.open() } },
                        modifier = Modifier.align(Alignment.Start)
                    ) {
                        Icon(Icons.Rounded.Menu, stringResource(R.string.open_menu_drawer))
                    }

                    PhotosSection(
                        backgroundPhotoUrl = uiState.userData.backgroundPhotoUrl,
                        profilePhotoUrl = uiState.userData.profilePhotoUrl
                    )

                    Text(uiState.userData.displayName, style = MaterialTheme.typography.titleLarge)

                    NotesSection(uiState.notesData)
                }
            }
        }
    }
}

@Composable
private fun PhotosSection(backgroundPhotoUrl: String, profilePhotoUrl: String) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = backgroundPhotoUrl,
            contentDescription = stringResource(R.string.profile_background_photo_description),
            modifier = Modifier.height(dimensionResource(R.dimen.profile_background_photo_height)),
            contentScale = ContentScale.FillWidth,
            // Only for displaying in an @Preview
            placeholder = if (LocalInspectionMode.current) {
                painterResource(R.drawable.preview_background_photo)
            } else null
        )
        if (profilePhotoUrl.isBlank()) {
            DefaultUserPhoto(
                size = dimensionResource(R.dimen.profile_photo_size_large)
            )
        } else {
            UserPhoto(
                url = profilePhotoUrl,
                size = dimensionResource(R.dimen.profile_photo_size_large),
                // Only for displaying in an @Preview
                placeholder = if (LocalInspectionMode.current) {
                    painterResource(R.drawable.preview_profile_photo)
                } else null
            )
        }
    }
}

@Composable
private fun NotesSection(notesData: List<NoteData>) {
    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
        items(notesData) { noteData ->
            Box(Modifier.height(dimensionResource(R.dimen.notes_grid_cell_height))) {
                Text(noteData.message)
            }
        }
    }
}

@Preview
@Composable
private fun ProfileScreenPreview() {
    val user = User("1")
    val userData = UserData("John Smith", "photo.jpg")
    val notesData = mutableListOf<NoteData>()
    notesData.add(NoteData("Grateful to be alive", listOf("Mindfulness")))
    notesData.add(NoteData("Thank you", listOf("Friends", "Family")))

    val authController = NoOpAuthController(user)
    val userRepository = MockSuccessUserRepository(userData)
    val noteRepository = MockSuccessNoteRepository(notesData)

    val viewModel = ProfileScreenViewModel(authController, userRepository, noteRepository)
    ProfileScreen(viewModel)
}