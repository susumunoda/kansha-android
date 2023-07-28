package com.susumunoda.kansha.ui.screen.newmessage

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.susumunoda.kansha.R
import com.susumunoda.kansha.data.User
import com.susumunoda.kansha.ui.CircularUserPhoto

private const val TAG = "NewMessageScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewMessageScreen(
    newMessageViewModel: NewMessageViewModel = viewModel(),
    navController: NavHostController
) {
    val uiState by newMessageViewModel.uiState.collectAsState()
    var isSearchActive by remember { mutableStateOf(false) }
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { contentPadding ->
        Column(modifier = Modifier.padding(top = contentPadding.calculateTopPadding())) {
            Column {
                if (uiState.recipient == User.NONE) {
                    SearchBar(
                        modifier = Modifier.fillMaxWidth(),
                        query = uiState.searchTerm,
                        onQueryChange = { newMessageViewModel.setSearchTerm(it) },
                        active = isSearchActive,
                        onActiveChange = { isSearchActive = it },
                        onSearch = { Log.i(TAG, "Searching for ${uiState.searchTerm}") },
                        placeholder = { Text("Search recipients") },
                        leadingIcon = {
                            Icon(
                                Icons.Rounded.Search,
                                contentDescription = "Search recipients"
                            )
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    newMessageViewModel.setSearchTerm("")
                                    isSearchActive = false
                                }
                            ) {
                                Icon(Icons.Rounded.Close, contentDescription = "Clear search")
                            }
                        },
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
                                        newMessageViewModel.setRecipient(
                                            searchResult
                                        )
                                        isSearchActive = false
                                    }
                                )
                            }
                        }
                    }
                } else {
                    InputChip(
                        selected = true,
                        onClick = {},
                        avatar = {
                            CircularUserPhoto(
                                user = uiState.recipient,
                                size = dimensionResource(R.dimen.profile_photo_size_small)
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { newMessageViewModel.clearRecipient() }) {
                                Icon(Icons.Rounded.Close, contentDescription = "Remove recipient")
                            }
                        },
                        label = { Text(uiState.recipient.name) })
                }
                TextField(
                    value = uiState.message,
                    onValueChange = { newMessageViewModel.setMessage(it) },
                    minLines = 5,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        errorIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                    )
                )
            }
        }
    }
}