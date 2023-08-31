package com.susumunoda.kansha.ui.screen.notes

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.susumunoda.kansha.R
import com.susumunoda.kansha.ui.component.ScaffoldWithStatusBarInsets
import com.susumunoda.kansha.ui.mock.MockCategory
import com.susumunoda.kansha.ui.mock.MockNote
import com.susumunoda.kansha.ui.mock.MockProvider

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

    val uiState by viewModel.uiState.collectAsState()

    ScaffoldWithStatusBarInsets(
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
                }
            )
        }
    ) {
        LazyColumn {
            item {
                Text(uiState.category.photoUrl ?: "")
            }
            items(uiState.notes) { note ->
                ListItem(
                    headlineContent = {
                        Text(note.message)
                        Text(note.categoryId ?: "")
                    }
                )
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