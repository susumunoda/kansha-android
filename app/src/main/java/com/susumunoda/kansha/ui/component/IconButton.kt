package com.susumunoda.kansha.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.ExitToApp
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import com.susumunoda.kansha.R

@Composable
fun BackButton(
    enabled: Boolean = true,
    size: Dp = dimensionResource(R.dimen.icon_button),
    contentDescription: String = stringResource(R.string.back_button_description),
    onClick: () -> Unit
) {
    VectorIconButton(Icons.Rounded.ArrowBack, enabled, size, contentDescription, onClick)
}

@Composable
fun FilterButton(
    enabled: Boolean = true,
    size: Dp = dimensionResource(R.dimen.icon_button),
    contentDescription: String = stringResource(R.string.filter_button_description),
    onClick: () -> Unit
) {
    PainterIconButton(R.drawable.filter_icon, enabled, size, contentDescription, onClick)
}

@Composable
fun LogoutButton(
    enabled: Boolean = true,
    size: Dp = dimensionResource(R.dimen.icon_button),
    contentDescription: String = stringResource(R.string.logout_button_description),
    onClick: () -> Unit
) {
    VectorIconButton(Icons.Rounded.ExitToApp, enabled, size, contentDescription, onClick)
}

@Composable
fun SendButton(
    enabled: Boolean = true,
    size: Dp = dimensionResource(R.dimen.icon_button),
    contentDescription: String = stringResource(R.string.send_button_description),
    onClick: () -> Unit
) {
    VectorIconButton(Icons.Rounded.Send, enabled, size, contentDescription, onClick)
}

@Composable
fun RemoveButton(
    enabled: Boolean = true,
    size: Dp = dimensionResource(R.dimen.icon_button),
    contentDescription: String = stringResource(R.string.remove_button_description),
    onClick: () -> Unit
) {
    VectorIconButton(Icons.Rounded.Close, enabled, size, contentDescription, onClick)
}

@Composable
private fun VectorIconButton(
    imageVector: ImageVector,
    enabled: Boolean,
    size: Dp,
    contentDescription: String,
    onClick: () -> Unit
) {
    IconButton(
        enabled = enabled,
        onClick = onClick
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            modifier = Modifier.size(size)
        )
    }
}

@Composable
private fun PainterIconButton(
    @DrawableRes drawable: Int,
    enabled: Boolean,
    size: Dp,
    contentDescription: String,
    onClick: () -> Unit
) {
    IconButton(
        enabled = enabled,
        onClick = onClick
    ) {
        Icon(
            painter = painterResource(drawable),
            contentDescription = contentDescription,
            modifier = Modifier.size(size)
        )
    }
}