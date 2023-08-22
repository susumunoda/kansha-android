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
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.susumunoda.kansha.R
import com.susumunoda.kansha.ui.component.ScaffoldWithStatusBarInsets
import com.susumunoda.kansha.ui.component.TabOption

data class Category(
    val name: String,
    val photoUrl: String
)

val categories = listOf(
    Category("All", "https://images.pexels.com/photos/2882660/pexels-photo-2882660.jpeg"),
    Category("Family", "https://images.pexels.com/photos/4452209/pexels-photo-4452209.jpeg"),
    Category("Friends", "https://images.pexels.com/photos/4834142/pexels-photo-4834142.jpeg"),
    Category("Nature", "https://images.pexels.com/photos/1496373/pexels-photo-1496373.jpeg"),
    Category("Music", "https://images.pexels.com/photos/1407322/pexels-photo-1407322.jpeg"),
    Category("Food", "https://images.pexels.com/photos/14013441/pexels-photo-14013441.jpeg"),
    Category(
        "Relaxation",
        "https://images.pexels.com/photos/7034022/pexels-photo-7034022.jpeg"
    ),
    Category("Travel", "https://images.pexels.com/photos/1285625/pexels-photo-1285625.jpeg"),
    Category(
        "Uncategorized",
        "https://images.pexels.com/photos/1486612/pexels-photo-1486612.jpeg"
    )
)

private val allTab = TabOption(R.string.notes_tab_all)
private val categoriesTab = TabOption(R.string.notes_tab_categories)
private val notesTabs = listOf(allTab, categoriesTab)

@Composable
fun NotesScreen() {
    ScaffoldWithStatusBarInsets {
        CategoriesGrid()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesGrid(modifier: Modifier = Modifier) {
    val gridPadding = dimensionResource(R.dimen.padding_medium)

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(gridPadding),
        verticalArrangement = Arrangement.spacedBy(gridPadding),
        contentPadding = PaddingValues(gridPadding),
        modifier = modifier
    ) {
        items(categories) { category ->
            ElevatedCard(
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.height(dimensionResource(R.dimen.card_height)),
                onClick = {}
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
        item {
            ElevatedCard(onClick = {}) {
                Box(
                    modifier = Modifier
                        .height(dimensionResource(R.dimen.card_height))
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Add, stringResource(R.string.notes_new_category))
                    Text(
                        text = stringResource(R.string.notes_new_category),
                        modifier = Modifier.padding(top = dimensionResource(R.dimen.card_image_height))
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun NotesScreenPreview() {
    NotesScreen()
}
