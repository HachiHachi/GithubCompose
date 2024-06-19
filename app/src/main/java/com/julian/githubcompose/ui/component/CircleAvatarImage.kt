package com.julian.githubcompose.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation


@Composable
fun CircleAvatarImage(avatarUrl: String, @DrawableRes default: Int, modifier: Modifier) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(avatarUrl)
            .placeholder(default)
            .error(default)
            .transformations(CircleCropTransformation())
            .crossfade(true)
            .build(),
        contentDescription = "User Avatar",
        modifier = modifier
    )
}