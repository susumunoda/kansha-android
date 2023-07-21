package com.susumunoda.kansha.ui.screen.listview

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.susumunoda.kansha.Screen
import com.susumunoda.kansha.data.Message

@Composable
fun ListViewScreen(
    listViewViewModel: ListViewViewModel = viewModel(),
    navController: NavHostController
) {
    val uiState by listViewViewModel.uiState.collectAsState()
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
        /**
         * It looks like [androidx.compose.foundation.layout.PaddingValues.calculateTopPadding]
         * includes the status bar padding of [androidx.compose.foundation.layout.WindowInsets]
         * if topBar does not have an explicit size of 0. This is an issue because the status bar
         * inset is already consumed at a higher level so that this scaffold composable is displayed
         * below the status bar. This is needed so that animating the scaffold (e.g. when composing
         * a new message) does not include the status bar area.
         */
        topBar = { Spacer(Modifier.size(0.dp)) }
    ) { contentPadding ->
        Column(modifier = Modifier.padding(top = contentPadding.calculateTopPadding())) {
            Surface(shadowElevation = 10.dp, modifier = Modifier.zIndex(1f)) {
                FilterOptions(
                    viewModel = listViewViewModel,
                    uiState = uiState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
            MessagesList(
                messages = uiState.entries,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterOptions(
    viewModel: ListViewViewModel,
    uiState: ListViewState,
    modifier: Modifier = Modifier
) {
    Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = modifier) {
        ListViewViewModel.FilterType.values().forEach { filterType ->
            FilterChip(
                selected = filterType == uiState.filterType,
                onClick = { viewModel.setFilter(filterType) },
                label = { Text(filterType.label, fontSize = 16.sp, fontWeight = FontWeight.Bold) },
                modifier = Modifier.height(40.dp)
            )
        }
    }
}

@Composable
fun MessagesList(messages: List<Message>, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
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