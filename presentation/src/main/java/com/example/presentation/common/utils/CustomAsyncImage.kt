package com.example.presentation.common.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.example.presentation.R

@Composable
fun CustomAsyncImage(
    modifier: Modifier = Modifier,
    imageUrl: String,
    contentScale: ContentScale = ContentScale.Crop,
    contentDescription: String? = null,
) {
    AsyncImage(
        model = imageUrl,
        modifier = modifier,
        contentDescription = contentDescription,
        contentScale = contentScale,
        placeholder = painterResource(R.drawable.unknown_person),
        error = painterResource(R.drawable.unknown_person),
        fallback = painterResource(R.drawable.unknown_person)
    )
}