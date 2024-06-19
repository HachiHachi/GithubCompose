package com.julian.githubcompose.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.julian.githubcompose.R

@Composable
fun BadgeTextView(text: String, modifier: Modifier) {
    Text(
        modifier = modifier,
        text = text,
        color = MaterialTheme.colorScheme.surface,
        textAlign = TextAlign.Start,
        overflow = TextOverflow.Ellipsis,
        style = MaterialTheme.typography.bodyMedium,
    )
}