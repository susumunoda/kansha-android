package com.susumunoda.kansha.ui.screen.notes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.susumunoda.kansha.R
import com.susumunoda.kansha.ui.component.ScaffoldWithStatusBarInsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewCategoryScreen(navController: NavHostController, categoryId: String, categoryName: String) {
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

    }
}