package com.susumunoda.kansha.ui.screen.notes

import android.content.Context
import android.util.Patterns
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.susumunoda.kansha.R
import com.susumunoda.kansha.repository.category.Category
import com.susumunoda.kansha.ui.component.ProgressIndicator
import com.susumunoda.kansha.ui.component.ScaffoldWithStatusBarInsets
import com.susumunoda.kansha.ui.mock.MockCategory
import com.susumunoda.kansha.ui.mock.MockProvider
import com.susumunoda.kansha.ui.validation.StringValidator
import kotlinx.coroutines.launch

@Composable
fun AddCategoryScreen(
    navController: NavHostController,
    viewModel: AddCategoryScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    ScaffoldWithStatusBarInsets(
        topBar = {
            TopBar(
                saveEnabled = uiState.name.isNotBlank(),
                onSave = {
                    viewModel.validateName(NameValidator(viewModel.categories, context))
                    viewModel.validatePhotoUrl(PhotoUrlValidator(context))
                    scope.launch {
                        viewModel.submit {
                            navController.popBackStack()
                        }
                    }
                }
            )
        }
    ) {
        Column {
            ProgressIndicator(
                showIndicator = uiState.requestInFlight,
                modifier = Modifier.fillMaxWidth()
            )
            NameField(
                name = uiState.name,
                onNameChange = viewModel::setName,
                nameValidation = uiState.nameValidation,
                modifier = Modifier.fillMaxWidth()
            )
            PhotoUrlField(
                photoUrl = uiState.photoUrl,
                onPhotoUrlChange = viewModel::setPhotoUrl,
                photoUrlValidation = uiState.photoUrlValidation,
                modifier = Modifier.fillMaxWidth()
            )
            CategoryPreview(
                name = uiState.name,
                photoUrl = uiState.photoUrl,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun TopBar(saveEnabled: Boolean, onSave: () -> Unit, modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        title = { Text(stringResource(R.string.new_category_title)) },
        actions = {
            IconButton(
                enabled = saveEnabled,
                onClick = onSave
            ) {
                Icon(
                    Icons.Filled.Done,
                    stringResource(R.string.create_category_description)
                )
            }
        },
        modifier = modifier
    )
}

@Composable
private fun NameField(
    name: String,
    onNameChange: (String) -> Unit,
    nameValidation: String?,
    modifier: Modifier = Modifier
) {
    TextField(
        label = { Text(stringResource(R.string.category_name)) },
        value = name,
        onValueChange = onNameChange,
        modifier = modifier,
        isError = nameValidation != null,
        supportingText = if (nameValidation != null) {
            { Text(nameValidation) }
        } else null
    )
}

@Composable
private fun PhotoUrlField(
    photoUrl: String,
    onPhotoUrlChange: (String) -> Unit,
    photoUrlValidation: String?,
    modifier: Modifier = Modifier
) {
    TextField(
        label = { Text(stringResource(R.string.category_photo_url)) },
        value = photoUrl,
        onValueChange = onPhotoUrlChange,
        modifier = modifier,
        isError = photoUrlValidation != null,
        supportingText = if (photoUrlValidation != null) {
            { Text(photoUrlValidation) }
        } else null
    )
}

@Composable
private fun CategoryPreview(name: String, photoUrl: String, modifier: Modifier = Modifier) {
    val padding = dimensionResource(R.dimen.padding_medium)
    BoxWithConstraints(
        contentAlignment = Alignment.Center,
        modifier = modifier.padding(padding)
    ) {
        Text(
            text = stringResource(R.string.category_preview),
            modifier = Modifier.align(Alignment.TopStart)
        )

        val cardHeight = dimensionResource(R.dimen.card_height)
        // Show the card as it would appear in the categories grid — i.e. in a 2-column grid
        // with padding of padding_medium on each side. Since this BoxWithConstraints already
        // applies padding, we only need to subtract the padding between the columns.
        val cardWidth = (maxWidth - padding) / 2
        ElevatedCard(
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .height(cardHeight)
                .width(cardWidth)
        ) {
            AsyncImage(
                model = photoUrl,
                contentDescription = stringResource(R.string.category_preview_description),
                contentScale = ContentScale.Crop,
                modifier = Modifier.height(dimensionResource(R.dimen.card_image_height))
            )
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(name)
            }
        }
    }
}

private class NameValidator(private val existingCategories: List<Category>, context: Context) :
    StringValidator(context.getString(R.string.category_name_validation)) {
    override fun isValid(value: String) = existingCategories.none { it.name == value }
}

private class PhotoUrlValidator(context: Context) :
    StringValidator(context.getString(R.string.category_photo_url_validation)) {
    override fun isValid(value: String) = Patterns.WEB_URL.matcher(value).matches()
}

@Preview
@Composable
fun AddCategoryScreenPreview() {
    val navController = rememberNavController()
    val mockProvider = MockProvider().apply {
        categoryRepositoryCategories = mutableListOf(
            MockCategory(name = "cat_1"),
            MockCategory(name = "cat_2"),
            MockCategory(name = "cat_3")
        )
    }
    val viewModel =
        AddCategoryScreenViewModel(mockProvider.authController, mockProvider.categoryRepository)
    AddCategoryScreen(navController, viewModel)
}