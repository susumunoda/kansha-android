package com.susumunoda.kansha.ui.screen.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material.icons.rounded.ExitToApp
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
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
import com.susumunoda.kansha.auth.NoOpAuthController
import com.susumunoda.kansha.auth.User
import com.susumunoda.kansha.data.note.MockSuccessNoteRepository
import com.susumunoda.kansha.data.note.NoteData
import com.susumunoda.kansha.data.user.MockSuccessUserRepository
import com.susumunoda.kansha.data.user.UserData
import com.susumunoda.kansha.ui.component.DefaultUserPhoto
import com.susumunoda.kansha.ui.component.UserPhoto
import com.susumunoda.kansha.ui.navigation.AuthenticatedScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavHostController,
    viewModel: ProfileScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val fetchInProgress = uiState.userDataFetchInProgress || uiState.notesDataFetchInProgress

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
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(stringResource(R.string.profile_screen_top_bar_text)) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Rounded.Menu, stringResource(R.string.open_menu_drawer))
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navController.navigate(AuthenticatedScreen.ADD_NOTE.name) }
                ) {
                    Icon(
                        Icons.Rounded.Create,
                        stringResource(R.string.add_note_fab_description)
                    )
                }
            }
        ) { contentPadding ->
            Column(Modifier.padding(top = contentPadding.calculateTopPadding())) {
                if (fetchInProgress) {
                    LinearProgressIndicator(Modifier.fillMaxWidth())
                }
                AnimatedVisibility(
                    visible = !fetchInProgress,
                    enter = slideInVertically(initialOffsetY = { fullHeight -> fullHeight / 4 })
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ProfileSection(uiState.userData)
                        NotesSection(uiState.notesData)
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileSection(userData: UserData) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = userData.backgroundPhotoUrl,
            contentDescription = stringResource(R.string.profile_background_photo_description),
            modifier = Modifier.height(dimensionResource(R.dimen.profile_background_photo_height)),
            contentScale = ContentScale.Crop,
            // Only for displaying in an @Preview
            placeholder = if (LocalInspectionMode.current) {
                painterResource(R.drawable.preview_background_photo)
            } else null
        )
        if (userData.profilePhotoUrl.isBlank()) {
            DefaultUserPhoto(
                size = dimensionResource(R.dimen.profile_photo_size_large)
            )
        } else {
            UserPhoto(
                url = userData.profilePhotoUrl,
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
                userData.displayName,
                style = MaterialTheme.typography.titleLarge,
                color = Color.White
            )
        }
    }
}

@Composable
private fun NotesSection(notesData: List<NoteData>) {
    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
        items(notesData) { noteData ->
            Box(
                modifier = Modifier
                    .height(dimensionResource(R.dimen.notes_grid_cell_height))
                    .background(MaterialTheme.colorScheme.tertiaryContainer)
            ) {
                Text(noteData.message, color = MaterialTheme.colorScheme.onTertiaryContainer)
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

    val navHostController = rememberNavController()
    val viewModel = ProfileScreenViewModel(authController, userRepository, noteRepository)
    ProfileScreen(navHostController, viewModel)
}