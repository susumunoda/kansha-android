package com.susumunoda.kansha.ui.screen.newmessage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.InputChip
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.susumunoda.kansha.R
import com.susumunoda.kansha.data.User
import com.susumunoda.kansha.ui.CircularUserPhoto

private const val TAG = "NewMessageScreen"

private val SURFACE_ELEVATION = 6.dp
private val SEARCH_INPUT_MIN_HEIGHT = 56.dp
private val RECIPIENT_CHIP_CONTAINER_PADDING = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
private const val MIN_MESSAGE_LINES = 5

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewMessageScreen(
    newMessageViewModel: NewMessageViewModel = viewModel(),
    navController: NavHostController
) {
    val uiState by newMessageViewModel.uiState.collectAsState()
    var isSearchActive by remember { mutableStateOf(false) }
    val backgroundColor = MaterialTheme.colorScheme.surfaceColorAtElevation(SURFACE_ELEVATION)
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        stringResource(R.string.new_message_top_bar_text),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = stringResource(R.string.back_button_description),
                            modifier = Modifier.size(dimensionResource(R.dimen.top_bar_icon_size))
                        )
                    }
                },
                actions = {
                    IconButton(
                        enabled = uiState.isSubmittable(),
                        onClick = { newMessageViewModel.sendMessage() },
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Icon(
                            Icons.Rounded.Send,
                            contentDescription = stringResource(R.string.send_message_description)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(top = contentPadding.calculateTopPadding())
                .background(backgroundColor)
        ) {
            if (uiState.recipient == User.NONE) {
                SearchBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = SEARCH_INPUT_MIN_HEIGHT),
                    tonalElevation = SURFACE_ELEVATION,
                    query = uiState.searchTerm,
                    onQueryChange = { newMessageViewModel.setSearchTerm(it) },
                    active = isSearchActive,
                    onActiveChange = { isSearchActive = it },
                    // required param, but no need to explicitly click search button since we are filtering on value change
                    onSearch = { },
                    placeholder = { Text(stringResource(R.string.search_recipients_place_holder_text)) },
                    leadingIcon = {
                        Icon(
                            Icons.Rounded.Search,
                            contentDescription = stringResource(R.string.search_recipients_description)
                        )
                    },
                    trailingIcon = {
                        if (isSearchActive) {
                            IconButton(
                                onClick = {
                                    newMessageViewModel.setSearchTerm("")
                                    isSearchActive = false
                                }
                            ) {
                                Icon(
                                    Icons.Rounded.Close,
                                    contentDescription = stringResource(R.string.clear_recipient_search_description)
                                )
                            }
                        }
                    },
                    // If not explicitly passed, SearchBar will accommodate for the status bar inset,
                    // even though this has already been accounted for by the top bar of the scaffold
                    windowInsets = WindowInsets(top = 0.dp)
                ) {
                    LazyColumn {
                        itemsIndexed(uiState.searchResults) { index, searchResult ->
                            if (index != 0) {
                                Divider()
                            }
                            ListItem(
                                headlineContent = { Text(searchResult.name) },
                                leadingContent = {
                                    CircularUserPhoto(
                                        user = searchResult,
                                        size = dimensionResource(R.dimen.profile_photo_size_small)
                                    )
                                },
                                modifier = Modifier.clickable {
                                    newMessageViewModel.setRecipient(searchResult)
                                    isSearchActive = false
                                }
                            )
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .heightIn(min = SEARCH_INPUT_MIN_HEIGHT)
                        .padding(RECIPIENT_CHIP_CONTAINER_PADDING)
                ) {
                    InputChip(
                        selected = true,
                        // required param, but no behavior other than the remove icon
                        onClick = {},
                        avatar = {
                            CircularUserPhoto(
                                user = uiState.recipient,
                                size = dimensionResource(R.dimen.profile_photo_size_small)
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { newMessageViewModel.clearRecipient() }) {
                                Icon(
                                    Icons.Rounded.Close,
                                    contentDescription = stringResource(R.string.remove_recipient_description)
                                )
                            }
                        },
                        label = { Text(uiState.recipient.name) }
                    )
                }
            }
            Divider()
            TextField(
                label = { Text(stringResource(R.string.new_message_field_label_text)) },
                value = uiState.message,
                onValueChange = newMessageViewModel::setMessage,
                modifier = Modifier.fillMaxWidth(),
                minLines = MIN_MESSAGE_LINES,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(
                    onSend = {
                        if (uiState.isSubmittable()) {
                            newMessageViewModel.sendMessage()
                        }
                    }
                ),
                isError = uiState.hasValidationErrors,
                supportingText = if (uiState.hasValidationErrors) {
                    {
                        val context = LocalContext.current
                        // There may be multiple validation messages — show just the first one
                        val validation = uiState.validationErrors[0].toLocalizedString(context)
                        Text(validation)
                    }
                } else null,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                )
            )
        }
    }
}