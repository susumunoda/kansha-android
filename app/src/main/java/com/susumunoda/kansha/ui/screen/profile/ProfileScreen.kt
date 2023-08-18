package com.susumunoda.kansha.ui.screen.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material.icons.rounded.ExitToApp
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.susumunoda.kansha.R
import com.susumunoda.kansha.auth.MockAuthController
import com.susumunoda.kansha.auth.Session
import com.susumunoda.kansha.data.note.Label
import com.susumunoda.kansha.data.note.MockNote
import com.susumunoda.kansha.data.note.MockNoteRepository
import com.susumunoda.kansha.data.note.MockNoteRepository.Companion.DEFAULT_LABELS
import com.susumunoda.kansha.data.note.MockNoteRepository.Companion.LABEL_ADVENTURES
import com.susumunoda.kansha.data.note.MockNoteRepository.Companion.LABEL_FAMILY
import com.susumunoda.kansha.data.note.MockNoteRepository.Companion.LABEL_FOOD
import com.susumunoda.kansha.data.note.MockNoteRepository.Companion.LABEL_FRIENDS
import com.susumunoda.kansha.data.note.MockNoteRepository.Companion.LABEL_MUSIC
import com.susumunoda.kansha.data.note.MockNoteRepository.Companion.LABEL_PETS
import com.susumunoda.kansha.data.note.MockNoteRepository.Companion.LABEL_TRAVEL
import com.susumunoda.kansha.data.note.Note
import com.susumunoda.kansha.data.user.MockUser
import com.susumunoda.kansha.data.user.MockUserRepository
import com.susumunoda.kansha.data.user.User
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

    val fetchInProgress =
        uiState.userFetchInProgress || uiState.notesFetchInProgress || uiState.allLabelsFetchInProgress

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
                    LinearProgressIndicator(
                        Modifier
                            .fillMaxWidth()
                            .height(dimensionResource(R.dimen.linear_progress_indicator_height))
                    )
                }
                AnimatedVisibility(
                    visible = !fetchInProgress,
                    enter = slideInVertically(initialOffsetY = { fullHeight -> fullHeight / 4 })
                ) {
                    Column {
                        ProfileSection(uiState.user)
                        FiltersSection(
                            viewModel = viewModel,
                            allLabels = uiState.allLabels,
                            selectedLabels = uiState.selectedLabels
                        )
                        NotesSection(uiState.notes)
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileSection(user: User, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
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
    }
}

private val FILTER_DIVIDER_HEIGHT = 32.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FiltersSection(
    viewModel: ProfileScreenViewModel,
    allLabels: List<Label>,
    selectedLabels: List<Label>,
    modifier: Modifier = Modifier
) {
    // For consistency with the LazyVerticalGrid below
    val padding = dimensionResource(R.dimen.notes_grid_cell_padding)

    val lazyListState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    Row(
        modifier = modifier.padding(PaddingValues(horizontal = padding)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        LazyRow(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(padding),
            state = lazyListState,
            modifier = Modifier.weight(1f)
        ) {
            items(allLabels) {
                val selected = selectedLabels.contains(it)
                FilterChip(
                    selected = selected,
                    onClick = {
                        if (selected) {
                            viewModel.removedSelectedLabel(it)
                        } else {
                            viewModel.addSelectedLabel(it)
                        }
                    },
                    label = { Text(it.text) },
                    leadingIcon = { Text(it.icon) }
                )
            }
            // Show padding after the last element only when the scroll reaches it (i.e.
            // contentPadding won't work because that will add padding after the last
            // _visible_ element, even if the actual last element hasn't been reached)
            item {}
        }
        VerticalDivider(Modifier.height(FILTER_DIVIDER_HEIGHT))
        Surface(
            onClick = {
                viewModel.clearSelectedLabels()
                scope.launch { lazyListState.animateScrollToItem(0) }
            }
        ) {
            Text(
                stringResource(R.string.notes_filter_clear_text),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = padding)
            )
        }
    }
}

@Composable
private fun NotesSection(notes: List<Note>, modifier: Modifier = Modifier) {
    val cellPadding = dimensionResource(R.dimen.notes_grid_cell_padding)
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(horizontal = cellPadding),
        horizontalArrangement = Arrangement.spacedBy(cellPadding),
        verticalArrangement = Arrangement.spacedBy(cellPadding)
    ) {
        items(notes) { note ->
            Box(
                modifier = Modifier
                    .height(dimensionResource(R.dimen.notes_grid_cell_height))
                    .background(MaterialTheme.colorScheme.tertiaryContainer)
            ) {
                Text(
                    note.message,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )

                val numLabels = note.labels.size
                if (numLabels != 0) {
                    val labelText = if (numLabels == 1) {
                        note.labels[0].text
                    } else {
                        stringResource(
                            R.string.note_multiple_labels_text,
                            note.labels[0].text,
                            numLabels - 1
                        )
                    }
                    Text(labelText, modifier = Modifier.align(Alignment.BottomStart))
                }
            }
        }
    }
}

@Preview
@Composable
private fun ProfileScreenPreview() {
    val userId = "1"
    val authController = MockAuthController(Session(userId))
    val userRepository = MockUserRepository(
        mutableMapOf(
            userId to MockUser(
                displayName = "John Smith",
                profilePhotoUrl = "photo.jpg"
            )
        )
    )
    val notes = mutableMapOf(
        userId to mutableListOf(
            MockNote.Builder().message("Thanks!").build(),
            MockNote.Builder().shortMessage().labels(LABEL_FAMILY, LABEL_FRIENDS).build(),
            MockNote.Builder().mediumMessage().labels(LABEL_FOOD).build(),
            MockNote.Builder().longMessage().build(),
            MockNote.Builder().shortMessage().labels(LABEL_PETS).build(),
            MockNote.Builder().mediumMessage().labels(LABEL_TRAVEL).build(),
            MockNote.Builder().longMessage().labels(LABEL_MUSIC, LABEL_ADVENTURES).build(),
            MockNote.Builder().message("Thanks!").build(),
            MockNote.Builder().shortMessage().labels(LABEL_FAMILY, LABEL_FRIENDS).build(),
            MockNote.Builder().mediumMessage().labels(LABEL_FOOD).build(),
            MockNote.Builder().longMessage().build(),
            MockNote.Builder().shortMessage().labels(LABEL_PETS).build(),
            MockNote.Builder().mediumMessage().labels(LABEL_TRAVEL).build(),
            MockNote.Builder().longMessage().labels(LABEL_MUSIC, LABEL_ADVENTURES).build()
        )
    )
    val labels = mutableMapOf(userId to DEFAULT_LABELS)
    val noteRepository = MockNoteRepository(notes, labels)
    val navHostController = rememberNavController()
    val viewModel = ProfileScreenViewModel(authController, userRepository, noteRepository)
    ProfileScreen(navHostController, viewModel)
}