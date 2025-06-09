package com.example.presentation.ui_home.post_item

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.media3.exoplayer.ExoPlayer
import coil.compose.AsyncImage
import com.example.presentation.ui_home.Post
import com.example.presentation.ui_home.PostContent
import com.example.presentation.ui_home.VideoPlayer

@Composable
fun PostContent(
    modifier: Modifier = Modifier,
    post: Post,
    exoPlayer: ExoPlayer,
    isPlaying: Boolean,
) {
    when (val content = post.content) {
        is PostContent.Image -> {
            PostAsyncImage(
                imageUrl = content.imageUrl,
                modifier = modifier
            )
        }

        is PostContent.Video -> {
            VideoPlayer(
                modifier = modifier.fillMaxWidth().aspectRatio(0.8f),
                isVisible = false,
                isPlaying = isPlaying,
                videoUrl = content.videoUrl,
                exoPlayer = exoPlayer
            )
        }

        is PostContent.CarouselContent -> {
            val state = rememberPagerState(pageCount = { content.imageUrls.size })
            HorizontalPager(state = state) { page ->
                PostAsyncImage(
                    modifier = modifier,
                    imageUrl = content.imageUrls[page],
                )
            }
        }
    }
}

@Composable
fun PostAsyncImage(
    modifier: Modifier = Modifier,
    imageUrl: String,
    contentScale: ContentScale = ContentScale.Crop,
    contentDescription: String? = null,
) {
    AsyncImage(
        model = imageUrl,
        contentDescription = contentDescription,
        contentScale = contentScale,
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(0.8f)
    )
}