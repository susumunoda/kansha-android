package com.susumunoda.kansha.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import com.susumunoda.kansha.data.User

@Composable
fun UserPhoto(user: User, size: Dp) {
    Image(
        painterResource(user.profilePhotoId),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .clip(CircleShape)
            .size(size)
    )
}