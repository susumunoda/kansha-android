package com.susumunoda.kansha.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Popup

private const val DEFAULT_ALPHA = 0.6f

@Composable
fun LoadingIndicatorOverlay(
    showLoadingIndicator: Boolean,
    modifier: Modifier = Modifier,
    background: Color = MaterialTheme.colorScheme.surface,
    alpha: Float = DEFAULT_ALPHA
) {
    if (showLoadingIndicator) {
        Popup {
            Box(
                contentAlignment = Alignment.Center,
                modifier = modifier
                    .fillMaxSize()
                    .background(background.copy(alpha = alpha))
            ) {
                CircularProgressIndicator()
            }
        }
    }
}