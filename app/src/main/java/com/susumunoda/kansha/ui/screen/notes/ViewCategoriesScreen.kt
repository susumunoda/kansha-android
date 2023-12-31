package com.susumunoda.kansha.ui.screen.notes


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.susumunoda.compose.material3.ScaffoldWithStatusBarInsets
import com.susumunoda.kansha.R
import com.susumunoda.kansha.repository.category.Category
import com.susumunoda.kansha.ui.navigation.Destination
import com.susumunoda.kansha.ui.navigation.categoryDestination

const val CATEGORY_ALL = "all"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewCategoriesScreen(
    navController: NavHostController,
    viewModel: ViewCategoriesScreenViewModel = hiltViewModel()
) {
    ScaffoldWithStatusBarInsets(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(stringResource(R.string.view_categories_title))
                },
                actions = {
                    IconButton(
                        onClick = {
                            navController.navigate(Destination.ADD_CATEGORY.route) {
                                restoreState = true
                            }
                        }
                    ) {
                        Icon(
                            Icons.Filled.Add,
                            contentDescription = stringResource(R.string.notes_new_category)
                        )
                    }
                }
            )
        }
    ) {
        // This is the top-level entry point for viewing categories. All subsequent reads from the
        // categories repo will not need to observe the state for changes in this way; they can
        // simply read the latest value from it. Here, however, we must observe the state until the
        // repo's StateFlow has been updated with the fetched categories.
        val categories by viewModel.categoriesStateFlow.collectAsState()
        CategoriesGrid(navController, categories)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesGrid(
    navController: NavHostController,
    categories: List<Category>,
    modifier: Modifier = Modifier
) {
    val gridPadding = dimensionResource(R.dimen.padding_medium)

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(gridPadding),
        verticalArrangement = Arrangement.spacedBy(gridPadding),
        contentPadding = PaddingValues(gridPadding),
        modifier = modifier
    ) {
        item {
            val categoryAllText = stringResource(R.string.notes_all_category)
            ElevatedCard(
                onClick = {
                    navController.navigate(
                        categoryDestination(
                            categoryId = CATEGORY_ALL,
                            categoryName = categoryAllText
                        )
                    )
                }
            ) {
                Box(
                    modifier = Modifier
                        .height(dimensionResource(R.dimen.card_height))
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = categoryAllText,
                        modifier = Modifier.padding(top = dimensionResource(R.dimen.card_image_height))
                    )
                }
            }
        }
        items(categories) { category ->
            ElevatedCard(
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.height(dimensionResource(R.dimen.card_height)),
                onClick = {
                    navController.navigate(
                        categoryDestination(
                            categoryId = category.id,
                            categoryName = category.name
                        )
                    )
                }
            ) {
                AsyncImage(
                    model = category.photoUrl,
                    contentDescription = category.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.height(dimensionResource(R.dimen.card_image_height))
                )
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(category.name)
                }
            }
        }
    }
}

@Preview
@Composable
private fun NotesScreenPreview() {
    ViewCategoriesScreen(rememberNavController())
}
