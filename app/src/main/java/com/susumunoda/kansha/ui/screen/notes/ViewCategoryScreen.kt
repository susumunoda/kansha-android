package com.susumunoda.kansha.ui.screen.notes

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.susumunoda.kansha.R
import com.susumunoda.kansha.ui.component.ScaffoldWithStatusBarInsets
import com.susumunoda.kansha.ui.mock.MockCategory
import com.susumunoda.kansha.ui.mock.MockNote
import com.susumunoda.kansha.ui.mock.MockProvider

private const val TRANSPARENT_BACKGROUND_ALPHA = 0.75f
private val BACKGROUND_BLUR_RADIUS = 16.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewCategoryScreen(
    navController: NavHostController,
    categoryId: String,
    categoryName: String,
    viewModel: ViewCategoryScreenViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.fetchCategory(categoryId)
    }
    LaunchedEffect(Unit) {
        viewModel.fetchNotes(categoryId)
    }

    Box(Modifier.fillMaxSize()) {
        val uiState by viewModel.uiState.collectAsState()

        AsyncImage(
            model = uiState.category.photoUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .blur(BACKGROUND_BLUR_RADIUS)
                .fillMaxSize()
        )

        val transparentBackgroundColor =
            MaterialTheme.colorScheme.surface.copy(alpha = TRANSPARENT_BACKGROUND_ALPHA)
        ScaffoldWithStatusBarInsets(
            containerColor = Color.Transparent,
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(categoryName) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                Icons.Rounded.ArrowBack,
                                contentDescription = stringResource(R.string.back_button_description)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = transparentBackgroundColor
                    )
                )
            }
        ) {
            val padding = dimensionResource(R.dimen.padding_medium)
            LazyColumn(
                contentPadding = PaddingValues(padding),
                verticalArrangement = Arrangement.spacedBy(padding),
                modifier = Modifier
                    .fillMaxSize()
                    .background(transparentBackgroundColor)
            ) {
                items(uiState.notes) { note ->
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
        categoryRepositoryDatabase[sessionUserId] = mutableListOf(
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