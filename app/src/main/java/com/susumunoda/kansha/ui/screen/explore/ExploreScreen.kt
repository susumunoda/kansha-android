package com.susumunoda.kansha.ui.screen.explore

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.susumunoda.kansha.R
import com.susumunoda.kansha.ui.component.ScaffoldWithStatusBarInsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen() {
    ScaffoldWithStatusBarInsets(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Explore") })
        }
    ) {
        val gridPadding = dimensionResource(R.dimen.padding_medium)
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(gridPadding),
            verticalArrangement = Arrangement.spacedBy(gridPadding),
            horizontalArrangement = Arrangement.spacedBy(gridPadding)
        ) {
            item {
                GridCard(
                    titleId = R.string.explore_card_add_note,
                    descriptionId = R.string.explore_card_add_note_description
                )
            }
            item {
                GridCard(
                    titleId = R.string.explore_card_set_reminder,
                    descriptionId = R.string.explore_card_set_reminder_description
                )
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GridCard(@StringRes titleId: Int, @StringRes descriptionId: Int) {
    ElevatedCard(onClick = {}) {
        Column(modifier = Modifier.height(dimensionResource(R.dimen.card_height))) {
            Text(stringResource(titleId))
            Text(stringResource(descriptionId))
        }
    }
}

@Preview
@Composable
fun ExploreScreenPreview() {
    ExploreScreen()
}