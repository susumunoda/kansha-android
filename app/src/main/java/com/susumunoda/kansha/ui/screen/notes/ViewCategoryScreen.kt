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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.susumunoda.kansha.R
import com.susumunoda.kansha.ui.component.ScaffoldWithStatusBarInsets

@OptIn(ExperimentalMaterial3Api::class)
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