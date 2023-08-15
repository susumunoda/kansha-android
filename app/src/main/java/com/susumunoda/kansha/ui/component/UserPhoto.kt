package com.susumunoda.kansha.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import coil.compose.AsyncImage
import com.susumunoda.kansha.R

@Composable
fun DefaultUserPhoto(size: Dp = dimensionResource(R.dimen.profile_photo_size_medium)) {
    Image(
        painterResource(R.drawable.blank_profile_picture),
        contentDescription = stringResource(R.string.default_profile_photo_description),
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .clip(CircleShape)
            .size(size)
    )
}

@Composable
fun UserPhoto(
    url: String,
    size: Dp = dimensionResource(R.dimen.profile_photo_size_medium),
    placeholder: Painter? = null
) {
    AsyncImage(
        model = url,
        contentDescription = stringResource(R.string.profile_photo_description),
        contentScale = ContentScale.Crop,
        placeholder = placeholder,
        modifier = Modifier
            .clip(CircleShape)
            .size(size)
    )
}