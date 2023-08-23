package com.susumunoda.kansha.ui.screen.notes

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.susumunoda.kansha.R
import com.susumunoda.kansha.data.category.Category
import com.susumunoda.kansha.ui.component.ScaffoldWithStatusBarInsets
import com.susumunoda.kansha.ui.screen.Validator
import kotlinx.coroutines.launch

@Composable
fun AddNoteScreen(
    navController: NavHostController,
    viewModel: AddNoteScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    ScaffoldWithStatusBarInsets(
        topBar = {
            TopBar(
                saveEnabled = uiState.message.isNotBlank() && !uiState.requestInFlight,
                onSave = {
                    viewModel.validateNote(NoteLengthValidator(context))
                    scope.launch {
                        viewModel.saveNote { navController.popBackStack() }
                    }
                },
                onGoBack = { navController.popBackStack() }
            )
        }
    ) {
        Column {
            ProgressIndicator(
                requestInFlight = uiState.requestInFlight,
                modifier = Modifier.fillMaxWidth()
            )
            MessageSection(
                message = uiState.message,
                onMessageChange = viewModel::setMessage,
                validationMessage = uiState.validationMessage,
                modifier = Modifier.fillMaxWidth()
            )
            CategorySection(
                allCategories = uiState.allCategories,
                selectedCategory = uiState.selectedCategory,
                onSelectCategory = viewModel::selectCategory,
                onDeselectCategory = viewModel::deselectCategory,
                modifier = Modifier
                    // Only horizontal needed because the chips themselves provide a minimum height
                    // for accessibility purposes
                    .padding(PaddingValues(horizontal = dimensionResource(R.dimen.padding_medium)))
                    // Allow error section to appear below when present
                    .weight(1f)
            )
            ErrorSection(
                errorMessage = uiState.errorMessage,
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    saveEnabled: Boolean,
    onSave: () -> Unit,
    onGoBack: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = { Text(stringResource(R.string.add_note_top_bar_text)) },
        navigationIcon = {
            IconButton(onClick = onGoBack) {
                Icon(
                    Icons.Rounded.ArrowBack,
                    contentDescription = stringResource(R.string.back_button_description)
                )
            }
        },
        actions = {
            IconButton(
                onClick = onSave,
                enabled = saveEnabled
            ) {
                Icon(Icons.Rounded.Done, stringResource(R.string.add_note_save_button))
            }
        }
    )
}

@Composable
private fun ProgressIndicator(requestInFlight: Boolean, modifier: Modifier = Modifier) {
    val height = dimensionResource(R.dimen.linear_progress_indicator_height)
    if (requestInFlight) {
        LinearProgressIndicator(modifier.height(height))
    } else {
        Spacer(modifier.height(height))
    }
}

@Composable
private fun MessageSection(
    message: String,
    onMessageChange: (String) -> Unit,
    validationMessage: String?,
    modifier: Modifier = Modifier
) {
    TextField(
        value = message,
        onValueChange = onMessageChange,
        isError = validationMessage != null,
        supportingText = if (validationMessage != null) {
            { Text(validationMessage) }
        } else null,
        modifier = modifier,
        minLines = 10
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
private fun CategorySection(
    allCategories: List<Category>,
    selectedCategory: Category?,
    onSelectCategory: (Category) -> Unit,
    onDeselectCategory: () -> Unit,
    modifier: Modifier = Modifier
) {
    FlowRow(
        modifier = modifier.verticalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
    ) {
        allCategories.forEach { category ->
            val selected = selectedCategory == category
            FilterChip(
                selected = selected,
                onClick = {
                    if (selected) {
                        onDeselectCategory()
                    } else {
                        onSelectCategory(category)
                    }
                },
                label = { Text(category.name) }
            )
        }
    }
}

@Composable
private fun ErrorSection(errorMessage: String?, modifier: Modifier = Modifier) {
    if (errorMessage != null) {
        Box(modifier = modifier) {
            Text(
                stringResource(R.string.add_note_error_message, errorMessage),
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

private class NoteLengthValidator(private val context: Context) : Validator() {
    companion object {
        private const val MAX_LENGTH = 250
    }

    override fun isValid(value: String) = value.length <= MAX_LENGTH
    override fun validationMessage() =
        context.getString(R.string.add_note_length_validation, MAX_LENGTH)
}

//@Preview
//@Composable
//fun AddNoteScreenPreview() {
//    val userId = "1"
//    val authController = MockAuthController(Session(userId))
//    val noteRepository = MockNoteRepository(
//        labels = mutableMapOf(userId to MockNoteRepository.DEFAULT_LABELS)
//    )
//    val navController = rememberNavController()
//    val viewModel = AddNoteScreenViewModel(authController, noteRepository)
//    AddNoteScreen(navController, viewModel)
//}
//
//@Composable
//fun AddNoteScreenPreviewWithNavigation(errorOnAddNote: Boolean) {
//    assert(LocalInspectionMode.current) { "This composable is only for use in an @Preview composable" }
//
//    val navController = rememberNavController()
//    val startDestination = "start"
//    val addNoteDestination = "addNote"
//
//    NavHost(navController, startDestination = startDestination) {
//        composable(startDestination) {
//            Surface(Modifier.fillMaxSize()) {
//                Box(contentAlignment = Alignment.Center) {
//                    Button(onClick = { navController.navigate(addNoteDestination) }) {
//                        Text("Add note")
//                    }
//                }
//            }
//        }
//        composable(addNoteDestination) {
//            val userId = "1"
//            val authController = MockAuthController(Session(userId))
//            val noteRepository = MockNoteRepository(
//                labels = mutableMapOf(userId to MockNoteRepository.DEFAULT_LABELS),
//                errorOnAddNote = errorOnAddNote
//            )
//            val viewModel = AddNoteScreenViewModel(authController, noteRepository)
//            AddNoteScreen(navController, viewModel)
//        }
//    }
//}
//
//@Preview
//@Composable
//fun AddNoteScreenSuccessPreview() {
//    AddNoteScreenPreviewWithNavigation(false)
//}
//
//@Preview
//@Composable
//fun AddNoteScreenErrorPreview() {
//    AddNoteScreenPreviewWithNavigation(true)
//}