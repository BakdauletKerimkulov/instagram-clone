package com.example.presentation.ui_home.post_item

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.media3.exoplayer.ExoPlayer
import com.example.presentation.R
import com.example.presentation.ui_home.Post
import com.example.presentation.ui_home.PostContent

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PostItem(
    modifier: Modifier = Modifier,
    post: Post,
    isPlaying: Boolean,
    exoPlayer: ExoPlayer,
    onLikeClick: (String, Boolean) -> Unit,
    onCommentClick: (String) -> Unit,
    onShareClick: (String) -> Unit,
    onBookmarkClick: (String) -> Unit,
    onMoreClick: () -> Unit,
    onUserClick: (String) -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        PostHeader(
            avatarUrl = post.avatarUrl,
            userName = post.userName,
            onMoreClick = onMoreClick
        )

        PostContent(post = post, exoPlayer = exoPlayer, isPlaying = isPlaying)

        PostActions(
            post = post,
            onLikeClick = onLikeClick,
            onCommentClick = onCommentClick,
            onShareClick = onShareClick,
            onBookmarkClick = onBookmarkClick
        )

        Text(
            text = stringResource(R.string.likedCount, post.likesCount),
            modifier = Modifier.padding(start = 16.dp, top = 4.dp, bottom = 8.dp),
            textAlign = TextAlign.Center
        )

        val text = LoremIpsum(30).values.joinToString(" ")

        PostDescriptionText(
            userName = post.userName,
            contentDescription = text,
            timestamp = post.timestamp,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
            onUserClick = { onUserClick(post.userId) }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PreviewPostItem() {
    PostItem(
        post = Post(
            id = "1",
            userId = "123",
            userName = "Bakdaulet",
            avatarUrl = "https://www.google.com/url?sa=i&url=https%3A%2F%2Fftp.brokenbowcabinlife.com%2Fnetworth%2Funveiling-the-charismatic-world-of-chico-lachowski.html&psig=AOvVaw164dXdPW_ZOhEyuqZKFZw-&ust=1747388220640000&source=images&cd=vfe&opi=89978449&ved=0CBQQjRxqFwoTCOCBssaWpY0DFQAAAAAdAAAAABAE",
            content = PostContent.Image(
                imageUrl = "https://picsum.photos/200/300"
            ),
            caption = null,
            timestamp = System.currentTimeMillis()
        ),
        isPlaying = false,
        onLikeClick = { _, _ -> },
        onCommentClick = {},
        onShareClick = {},
        onBookmarkClick = {},
        onMoreClick = {},
        onUserClick = {},
        exoPlayer = ExoPlayer.Builder(LocalContext.current).build()
    )
}