package com.susumunoda.kansha.ui.screen.notes

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.susumunoda.kansha.R
import com.susumunoda.kansha.repository.note.Note
import com.susumunoda.kansha.ui.component.ScaffoldWithStatusBarInsets
import com.susumunoda.kansha.ui.mock.MockCategory
import com.susumunoda.kansha.ui.mock.MockNote
import com.susumunoda.kansha.ui.mock.MockProvider

private const val TRANSPARENT_BACKGROUND_ALPHA = 0.75f
private val BACKGROUND_BLUR_RADIUS = 16.dp

@Composable
fun ViewCategoryScreen(
    navController: NavHostController,
    categoryId: String,
    categoryName: String,
    viewModel: ViewCategoryScreenViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.fetchNotes(categoryId)
    }

    val uiState by viewModel.uiState.collectAsState()
    val category = viewModel.getCategory(categoryId)
    val hasBackgroundPhoto = category?.photoUrl?.isNotBlank() == true
    val painter = if (hasBackgroundPhoto) {
        rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(category?.photoUrl)
                .size(Size.ORIGINAL)
                .build()
        )
    } else null
    val transparentBackgroundColor = MaterialTheme.colorScheme.surface.copy(
        alpha = TRANSPARENT_BACKGROUND_ALPHA
    )

    Box(Modifier.fillMaxSize()) {
        if (painter != null) {
            Image(
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .blur(BACKGROUND_BLUR_RADIUS)
                    .fillMaxSize()
            )
        }
        ScaffoldWithStatusBarInsets(
            containerColor = Color.Transparent,
            topBar = {
                TopBar(
                    title = categoryName,
                    backgroundColor = transparentBackgroundColor,
                    onBackPressed = { navController.popBackStack() }
                )
            }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(transparentBackgroundColor)
            ) {
                val imageLoadingSucceeded = painter?.state is AsyncImagePainter.State.Success
                val imageLoadingFailed = painter?.state is AsyncImagePainter.State.Error
                if (!hasBackgroundPhoto || imageLoadingSucceeded || imageLoadingFailed) {
                    NotesList(uiState.notes)
                } else {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun TopBar(
    title: String,
    backgroundColor: Color,
    onBackPressed: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = { Text(title) },
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(
                    Icons.Rounded.ArrowBack,
                    contentDescription = stringResource(R.string.back_button_description)
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = backgroundColor
        )
    )
}

@Composable
private fun NotesList(notes: List<Note>, modifier: Modifier = Modifier) {
    val padding = dimensionResource(R.dimen.padding_medium)
    LazyColumn(
        contentPadding = PaddingValues(padding),
        verticalArrangement = Arrangement.spacedBy(padding),
        modifier = modifier
    ) {
        items(notes) { note ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(R.dimen.card_height))
            ) {
                Box(Modifier.padding(padding)) {
                    Text(note.message)
                }
            }
        }
    }
}

@Preview
@Composable
fun ViewCategoryScreenPreview() {
    val categoryId = "category_123"
    val categoryName = "My Category"
    val navController = rememberNavController()
    val mockProvider = MockProvider().apply {
        noteRepositoryDatabase[sessionUserId] = mutableListOf(
            MockNote.Builder().shortMessage().categoryId(categoryId).build(),
            MockNote.Builder().shortMessage().categoryId("other_category_1").build(),
            MockNote.Builder().mediumMessage().categoryId(categoryId).build(),
            MockNote.Builder().mediumMessage().categoryId("other_category_2").build(),
            MockNote.Builder().longMessage().categoryId(categoryId).build(),
            MockNote.Builder().longMessage().categoryId("other_category_3").build()
        )
        categoryRepositoryCategories = mutableListOf(
            MockCategory(id = categoryId, name = categoryName)
        )
    }
    val viewModel =
        ViewCategoryScreenViewModel(
            authController = mockProvider.authController,
            noteRepository = mockProvider.noteRepository,
            categoryRepository = mockProvider.categoryRepository
        )
    ViewCategoryScreen(
        navController = navController,
        categoryId = categoryId,
        categoryName = categoryName,
        viewModel = viewModel
    )
}