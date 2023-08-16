package com.susumunoda.kansha.ui.screen.addnote

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.susumunoda.kansha.R
import com.susumunoda.kansha.ui.screen.Validator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteScreen(
    navController: NavHostController,
    viewModel: AddNoteScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.add_note_top_bar_text)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Rounded.ArrowBack,
                            contentDescription = stringResource(R.string.back_button_description)
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.validateNote(NoteLengthValidator(context))
                            viewModel.saveNote()
                        },
                        enabled = uiState.message.isNotBlank()
                    ) {
                        Icon(Icons.Rounded.Done, stringResource(R.string.submit_button_description))
                    }
                }
            )
        }
    ) { contentPadding ->
        Box(Modifier.padding(contentPadding)) {
            TextField(
                value = uiState.message,
                onValueChange = viewModel::setMessage,
                isError = uiState.validationMessage != null,
                supportingText = if (uiState.validationMessage != null) {
                    { Text(uiState.validationMessage!!) }
                } else null,
                modifier = Modifier.fillMaxWidth(),
                minLines = 10
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