package com.example.presentation.ui_profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.presentation.R
import com.example.presentation.ui_home.Post
import com.example.presentation.ui_home.PostContent
import com.example.presentation.ui_home.TopBarButton
import com.example.presentation.ui_profile.utils.Highlights
import com.example.presentation.ui_profile.utils.ProfileButton
import com.example.presentation.ui_profile.utils.ProfileInfo
import com.example.presentation.ui_profile.utils.ProfileTabBar
import com.example.presentation.ui_profile.utils.TextWithIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onCreateClick: () -> Unit,
    onSettingsClick: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    var email by remember { mutableStateOf("kerimkulov_b") }

    val content by viewModel.content.collectAsState()
    val selectedTab by viewModel.selectedTab.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    TextWithIcon(
                        email = email,
                        icon = R.drawable.keyboard_arrow_down_24px
                    )
                },
                actions = {
                    TopBarButton(
                        painter = R.drawable.instagram_add_new_post_icon,
                        contentDescription = stringResource(R.string.createContent),
                        onClick = onCreateClick
                    )

                    TopBarButton(
                        painter = R.drawable.menu_24px,
                        contentDescription = stringResource(R.string.moreHorizontal),
                        onClick = onSettingsClick,
                        iconSize = 32.dp
                    )
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            item {
                ProfileInfo(
                    modifier = Modifier.padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 16.dp,
                        bottom = 16.dp
                    ),
                    avatarUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSkVLE78-GEWG7KXNaBe6wc7V2oyHgiEQYiPw&s"
                )
            }

            item {
                Row(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                        .fillMaxWidth()
                ) {
                    ProfileButton(
                        text = "Edit profile",
                        onClick = {},
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .weight(1f)
                    )

                    ProfileButton(
                        text = "Share Profile",
                        onClick = {},
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                Highlights(
                    imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSkVLE78-GEWG7KXNaBe6wc7V2oyHgiEQYiPw&s",
                    name = "Bakdaulet",
                    onEditClick = {},
                    onStoryClick = {},
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            item {
                ProfileTabBar(
                    selectedTab = selectedTab,
                    tabBarItems = TabBarItem.entries,
                    onTabSelected = { viewModel.setTabBarState(it) }
                )
            }

            item {
                UserContentGrid(
                    posts = content
                )
            }

            item {
                Spacer(modifier = Modifier.height(58.dp))
            }
        }
    }
}

@Composable
fun calculateGridHeight(): Dp {
    val screenWidth = LocalWindowInfo.current.containerSize.width.dp
    return (screenWidth / 3 * 1.2f)
}

@Composable
fun UserContentGrid(
    modifier: Modifier = Modifier,
    posts: List<Post>,
) {
    val rows = (posts.size + 1) / 3
    val gridHeight = (calculateGridHeight() * rows) + ((rows - 1).coerceAtLeast(0)).dp

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        verticalArrangement = Arrangement.spacedBy(1.dp),
        horizontalArrangement = Arrangement.spacedBy(1.dp),
        modifier = modifier
            .height(gridHeight)
            .fillMaxWidth(),
        userScrollEnabled = false
    ) {
        items(posts) { post ->
            ProfilePost(
                previewUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQczw5NblVSMOzJx0jujbn07S69a2TkJm6kOw&s",
                postType = post.content
            )
        }
    }
}

@Composable
fun ProfilePost(
    modifier: Modifier = Modifier,
    previewUrl: String,
    postType: PostContent,
) {
    val iconColor = Color.White
    Box(
        modifier = modifier.aspectRatio(0.8f)
    ) {
        AsyncImage(
            model = previewUrl,
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )

        when (postType) {
            is PostContent.Image -> { }
            is PostContent.Video -> {
                Icon(
                    painter = painterResource(R.drawable.instagram_reels_icon_full),
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(24.dp)
                        .align(Alignment.TopEnd)
                )
            }
            is PostContent.CarouselContent -> {
                Icon(
                    painter = painterResource(R.drawable.carousel_icon),
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(24.dp)
                        .align(Alignment.TopEnd)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewProfileScreen() {
    ProfileScreen(
        onCreateClick = {},
        onSettingsClick = {}
    )
}