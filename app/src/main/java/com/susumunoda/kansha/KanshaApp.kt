package com.susumunoda.kansha

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.susumunoda.kansha.data.DataSource
import com.susumunoda.kansha.data.Message

@Composable
fun KanshaApp(messages: List<Message> = DataSource.allMessages()) {
    Scaffold(
        topBar = {}
    ) { contentPadding ->
        Column(modifier = Modifier.padding(contentPadding)) {
            LazyColumn {
                items(messages) { message ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row {
                            Image(
                                painterResource(message.senderPhotoId),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(100.dp)
                            )
                            Column {
                                Text("From ${message.sender} to ${message.recipient}")
                                Text(message.message)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun KanshaAppPreview() {
    KanshaApp()
}