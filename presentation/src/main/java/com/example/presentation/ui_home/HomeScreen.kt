package com.example.presentation.ui_home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.presentation.R
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.media3.exoplayer.ExoPlayer
import com.example.presentation.common.utils.InstagramLogo
import com.example.presentation.ui_home.post_item.PostItem
import com.example.presentation.ui_home.story.StoryRow

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    onDirectMessageClick: () -> Unit,
    onNotificationClick: () -> Unit,
) {
    val context = LocalContext.current
    val posts by viewModel.posts.collectAsState()

    val currentlyPlayingIndex = viewModel.currentlyPlayingIndex.collectAsState()
    val listState = rememberLazyListState()
    val playingItemIndex = remember { mutableStateOf<Int?>(null) }

    val exoPlayer = remember(context) { ExoPlayer.Builder(context).build() }

    val postList = List(10) { videoPost.copy(id = generateUniqueId().toString()) }

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo }
            .collect { visibleItems ->
                val fullyVisible = visibleItems.firstOrNull {
                    val viewportHeight =
                        listState.layoutInfo.viewportEndOffset + listState.layoutInfo.viewportStartOffset
                    it.offset >= 0 && it.offset <= viewportHeight
                }
                playingItemIndex.value = fullyVisible?.index
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { InstagramLogo() },
                actions = {
                    TopBarButton(
                        painter = R.drawable.favorite_border,
                        contentDescription = stringResource(R.string.notificationButton),
                        onClick = onNotificationClick,
                        iconSize = 28.dp
                    )

                    TopBarButton(
                        painter = R.drawable.instagram_dm_direct_message_icon,
                        contentDescription = stringResource(R.string.directMessageButton),
                        onClick = onDirectMessageClick
                    )
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            state = listState
        ) {
            item {
                StoryRow(
                    avatarUrl = videoPost.avatarUrl,
                    userName = videoPost.userName,
                    onStoryClick = { }
                )
            }

            itemsIndexed(items = postList, key = { _, post -> post.id }) { index, post ->
                PostItem(
                    post = post,
                    exoPlayer = exoPlayer,
                    isPlaying = index == currentlyPlayingIndex.value,
                    onLikeClick = { _, _ -> },
                    onCommentClick = {},
                    onShareClick = {},
                    onBookmarkClick = {},
                    onUserClick = {},
                    onMoreClick = {}
                )
            }
        }
    }
}

@Composable
fun TopBarButton(
    modifier: Modifier = Modifier,
    painter: Int? = null,
    contentDescription: String? = null,
    onClick: () -> Unit,
    iconSize: Dp = 24.dp,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.size(48.dp)
    ) {
        painter?.let {
            Icon(
                painter = painterResource(it),
                contentDescription = contentDescription,
                modifier = Modifier.size(iconSize)
            )
        }
    }
}

private var currentId = 0

private fun generateUniqueId(): Int {
    return currentId++
}

val videoPost = Post(
    id = generateUniqueId().toString(),
    userId = "123",
    userName = "Bakdaulet",
    avatarUrl = "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcRM3rIT1BpMYyAQpZn4Ys7rlCOgCaeSTJBSvPBq2Pa12QDEJCghYg9z916MsXuL4u43WqipkiDtapi41jyP7-xz1w",
    content = PostContent.Video(
        videoUrl = "https://videos.pexels.com/video-files/7657449/7657449-sd_640_360_25fps.mp4"
    ),
    contentDescription = "Some content",
    caption = null,
    timestamp = System.currentTimeMillis()
)

val imagePost = Post(
    id = generateUniqueId().toString(),
    userId = "456",
    userName = "Baga",
    avatarUrl = "https://images.steamusercontent.com/ugc/2335748378538013291/A7D0B9BCBDEEEC1ACA6A81042FC17B813B02BB83/?imw=637&imh=358&ima=fit&impolicy=Letterbox&imcolor=%23000000&letterbox=true",
    content = PostContent.Image(
        imageUrl = "https://static.tvtropes.org/pmwiki/pub/images/abcb6534_7913_4eb1_a7a5_62b081ebc628.png"
    ),
    contentDescription = "Some content",
    caption = null,
    timestamp = System.currentTimeMillis()
)

val listOfVideos = List(10) { videoPost }
val listOfImages = List(10) { imagePost }
val listOfPosts = listOfVideos + listOfImages

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(onDirectMessageClick = {}, onNotificationClick = {})
}