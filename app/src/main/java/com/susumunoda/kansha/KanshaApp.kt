package com.susumunoda.kansha

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.susumunoda.kansha.ui.screen.listview.MessagesListView

@Composable
fun KanshaApp() {
    MessagesListView()
}

@Preview
@Composable
private fun KanshaAppPreview() {
    KanshaApp()
}