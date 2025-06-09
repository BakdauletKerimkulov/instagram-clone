package com.example.presentation.ui_profile.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.presentation.ui_home.story.StoryItem
import com.example.presentation.R

@Composable
fun Highlights(
    modifier: Modifier = Modifier,
    imageUrl: String,
    name: String,
    onEditClick: () -> Unit,
    onStoryClick: () -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 4.dp )
        ) {
            Text(stringResource(R.string.highlights))
            Spacer(modifier = Modifier.weight(1f))
            Text("Edit", color = colorResource(R.color.dodger_blue), modifier = Modifier.clickable(onClick = onEditClick))
        }

        LazyRow(
            modifier = Modifier.padding(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp)
        ) {
            items(10) {
                StoryItem(
                    imageUrl = imageUrl,
                    name = name,
                    onClick = onStoryClick
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HighlightsPreview() {
    Highlights(
        imageUrl = "",
        name = "Name",
        onEditClick = {},
        onStoryClick = {}
    )
}