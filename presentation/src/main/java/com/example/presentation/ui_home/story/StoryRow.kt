package com.example.presentation.ui_home.story

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StoryRow(
    modifier: Modifier = Modifier,
    avatarUrl: String,
    userName: String,
    onStoryClick: () -> Unit,
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(10) {
            StoryItem(
                imageUrl = avatarUrl,
                name = userName,
                onClick = onStoryClick
            )
        }
    }
}