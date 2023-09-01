package com.susumunoda.kansha.ui.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import com.susumunoda.kansha.R

@Composable
fun ProgressIndicator(
    showIndicator: Boolean,
    modifier: Modifier = Modifier,
    indicatorHeight: Dp = dimensionResource(R.dimen.linear_progress_indicator_height)
) {
    if (showIndicator) {
        LinearProgressIndicator(modifier.height(indicatorHeight))
    } else {
        Spacer(modifier.height(indicatorHeight))
    }
}