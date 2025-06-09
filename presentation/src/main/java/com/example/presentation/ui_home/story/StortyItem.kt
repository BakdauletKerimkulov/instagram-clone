package com.example.presentation.ui_home.story

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.presentation.R

@Composable
fun StoryItem(
    modifier: Modifier = Modifier,
    imageUrl: String,
    name: String,
    onClick: () -> Unit,
    contentScale: ContentScale = ContentScale.Crop,
    contentDescription: String? = null
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AvatarImage(
            imageUrl = imageUrl,
            modifier = Modifier,
            contentDescription = contentDescription,
            contentScale = contentScale,
            onClick = onClick
        )

        Text(text = name, fontSize = 12.sp)
    }
}

@Composable
fun AvatarImage(
    modifier: Modifier = Modifier,
    imageUrl: String,
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.Crop,
    size: Int = 90,
    onClick: () -> Unit
) {
    AsyncImage(
        model = imageUrl,
        contentDescription = contentDescription,
        modifier = modifier
            .size(size.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick),
        contentScale = contentScale,
        placeholder = painterResource(R.drawable.unknown_person),
        error = painterResource(R.drawable.unknown_person),
        fallback = painterResource(R.drawable.unknown_person)
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewStoryItem() {
    StoryItem(
        imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSkVLE78-GEWG7KXNaBe6wc7V2oyHgiEQYiPw&s",
        name = "",
        onClick = { }
    )
}