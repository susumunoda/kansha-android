package com.susumunoda.kansha.ui.screen.listview

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.susumunoda.kansha.R
import com.susumunoda.kansha.Screen
import com.susumunoda.kansha.data.Message

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListViewScreen(
    listViewViewModel: ListViewViewModel = viewModel(),
    navController: NavHostController
) {
    val uiState by listViewViewModel.uiState.collectAsState()
    var isFiltersPopupVisible by remember { mutableStateOf(false) }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.NEW_MESSAGE.name) },
                shape = CircleShape,
                modifier = Modifier.size(80.dp)
            ) {
                Text("🙏", fontSize = 30.sp)
            }
        },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Kansha", color = MaterialTheme.colorScheme.onPrimary) },
                actions = {
                    FilterAction(
                        onClick = { isFiltersPopupVisible = true },
                        onDismiss = { isFiltersPopupVisible = false },
                        isExpanded = isFiltersPopupVisible,
                        listViewViewModel = listViewViewModel,
                        uiState = uiState
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { contentPadding ->
        Column(modifier = Modifier.padding(top = contentPadding.calculateTopPadding())) {
            MessagesList(
                messages = uiState.entries,
                modifier = Modifier.padding(start = 8.dp, end = 8.dp)
            )
        }
    }
}

@Composable
private fun FilterAction(
    onClick: () -> Unit,
    onDismiss: () -> Unit,
    isExpanded: Boolean,
    listViewViewModel: ListViewViewModel,
    uiState: ListViewState,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        IconButton(
            onClick = onClick,
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Icon(
                painter = painterResource(R.drawable.filter_list),
                contentDescription = "Filter list",
                modifier = Modifier.size(24.dp)
            )
        }
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = onDismiss
        ) {
            ListViewViewModel.FilterType.values().forEach { filterType ->
                DropdownMenuItem(
                    text = { Text(filterType.label, fontSize = 16.sp) },
                    onClick = { listViewViewModel.setFilter(filterType) },
                    leadingIcon = {
                        if (filterType == uiState.filterType) {
                            Icon(
                                imageVector = Icons.Rounded.Check,
                                contentDescription = "Filtered by ${filterType.label}"
                            )
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun MessagesList(messages: List<Message>, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            item {
                // Empty spacer to provide even padding after list content
                Spacer(Modifier)
            }
            items(messages) { message ->
                MessageCard(message)
            }
            item {
                // Empty spacer to provide even padding after list content
                Spacer(Modifier)
            }
        }
    }
}

@Composable
fun MessageCard(message: Message, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .height(100.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Image(
                painterResource(message.senderPhotoId),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(80.dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 16.dp)
            ) {
                Text("From ${message.sender} to ${message.recipient}")
                Text(message.message)
            }
        }
    }
}