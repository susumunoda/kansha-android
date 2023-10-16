package com.susumunoda.kansha.ui.screen.notes

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
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
import com.susumunoda.compose.material3.BackButton
import com.susumunoda.compose.material3.DoneButton
import com.susumunoda.compose.material3.ProgressIndicator
import com.susumunoda.compose.material3.ScaffoldWithStatusBarInsets
import com.susumunoda.kansha.R
import com.susumunoda.kansha.repository.category.Category
import com.susumunoda.kansha.ui.validation.StringValidator
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteScreen(
    navController: NavHostController,
    viewModel: AddNoteScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val isSaveEnabled =
        uiState.message.isNotBlank() && uiState.selectedCategory != null && !uiState.requestInFlight

    ScaffoldWithStatusBarInsets(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.add_note_top_bar_text)) },
                navigationIcon = { BackButton(onClick = { navController.popBackStack() }) },
                actions = {
                    DoneButton(
                        enabled = isSaveEnabled,
                        onClick = {
                            viewModel.validateNote(NoteLengthValidator(context))
                            scope.launch {
                                viewModel.saveNote { navController.popBackStack() }
                            }
                        }
                    )
                }
            )
        }
    ) {
        Column {
            ProgressIndicator(
                showIndicator = uiState.requestInFlight,
                modifier = Modifier.fillMaxWidth()
            )
            MessageSection(
                message = uiState.message,
                onMessageChange = viewModel::setMessage,
                validationMessage = uiState.validationMessage,
                modifier = Modifier.fillMaxWidth()
            )
            CategorySection(
                allCategories = viewModel.categories,
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

private class NoteLengthValidator(context: Context) :
    StringValidator(context.getString(R.string.add_note_length_validation, MAX_LENGTH)) {
    companion object {
        private const val MAX_LENGTH = 250
    }

    override fun isValid(value: String) = value.length <= MAX_LENGTH
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