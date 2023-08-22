package com.susumunoda.kansha.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ScaffoldWithStatusBarInsets(
    topBar: @Composable () -> Unit = {},
    content: @Composable () -> Unit
) {
    Scaffold(topBar = topBar) { contentPadding ->
        Box(
            modifier = Modifier.padding(top = contentPadding.calculateTopPadding())
        ) {
            content()
        }
    }
}