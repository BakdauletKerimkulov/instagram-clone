package com.example.presentation.ui_home.post_item

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.presentation.R
import com.example.presentation.ui_home.Post
import com.example.presentation.ui_home.PostContent

@Composable
fun PostActions(
    modifier: Modifier = Modifier,
    post: Post,
    onLikeClick: (String, Boolean) -> Unit,
    onCommentClick: (String) -> Unit,
    onShareClick: (String) -> Unit,
    onBookmarkClick: (String) -> Unit,
) {
    Row(
        modifier = modifier.padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PostActionButton(
            icon = if (post.isLiked) painterResource(R.drawable.favorite_icon)
            else painterResource(R.drawable.favorite_border),
            onClick = { onLikeClick(post.id, !post.isLiked) },
            iconSize = 30
        )

        Spacer(Modifier.padding(horizontal = 4.dp))

        PostActionButton(
            icon = painterResource(R.drawable.instagram_comment_icon),
            onClick = { onCommentClick(post.id) }
        )

        if (post.commentsCount > 0)
            Text(text = post.commentsCount.toString())

        Spacer(Modifier.padding(horizontal = 4.dp))

        PostActionButton(
            icon = painterResource(R.drawable.instagram_share_icon),
            onClick = { onShareClick(post.id) }
        )

        Spacer(Modifier.weight(1f))

        PostActionButton(
            icon = painterResource(R.drawable.instagram_save_icon),
            onClick = { onBookmarkClick(post.id) }
        )
    }
}

@Composable
private fun PostActionButton(
    modifier: Modifier = Modifier,
    icon: Painter,
    onClick: () -> Unit,
    contentDescription: String? = null,
    iconSize: Int = 24,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .padding(vertical = 4.dp)
            .size(32.dp)
            .clip(CircleShape)
    ) {
        Icon(
            painter = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(iconSize.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPostActions() {
    PostActions(
        post = Post(
            id = "1",
            userId = "123",
            userName = "Bakdaulet",
            avatarUrl = "https://www.google.com/url?sa=i&url=https%3A%2F%2Fftp.brokenbowcabinlife.com%2Fnetworth%2Funveiling-the-charismatic-world-of-chico-lachowski.html&psig=AOvVaw164dXdPW_ZOhEyuqZKFZw-&ust=1747388220640000&source=images&cd=vfe&opi=89978449&ved=0CBQQjRxqFwoTCOCBssaWpY0DFQAAAAAdAAAAABAE",
            content = PostContent.Image(
                imageUrl = "https://picsum.photos/200/300"
            ),
            caption = null,
            timestamp = System.currentTimeMillis(),
            isLiked = false,
        ),
        onLikeClick = { _, _ -> },
        onCommentClick = {},
        onShareClick = {},
        onBookmarkClick = {}
    )
}