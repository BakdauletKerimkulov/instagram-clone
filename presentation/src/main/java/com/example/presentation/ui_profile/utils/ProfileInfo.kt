package com.example.presentation.ui_profile.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.presentation.R

@Composable
fun ProfileInfo(
    modifier: Modifier = Modifier,
    userName: String = "Bakdaulet",
    postCount: Int = 0,
    avatarUrl: String,
    followersCount: Int = 0,
    followingCount: Int = 0,
) {
    Column(modifier = modifier) {

        Row {
            Box(modifier = Modifier.size(80.dp)) {
                AsyncImage(
                    model = avatarUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.BottomEnd)
                        .background(
                            color = MaterialTheme.colorScheme.background,
                            shape = CircleShape
                        ),
                ) {
                    Icon(
                        painter = painterResource(R.drawable.plus_icon),
                        contentDescription = null,
                        tint = colorResource(R.color.dodger_blue),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(1.5.dp)
                            .align(Alignment.Center)
                    )
                }
            }

            Column {
                Text(userName, modifier = Modifier.padding(start = 28.dp, bottom = 8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    UserInfoItem("$postCount\nposts")
                    UserInfoItem("$followersCount\nfollowers")
                    UserInfoItem("$followingCount\nfollowing")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewProfileInfo() {
    ProfileInfo(
        avatarUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSkVLE78-GEWG7KXNaBe6wc7V2oyHgiEQYiPw&s"
    )
}

@Composable
private fun RowScope.UserInfoItem(
    userInfo: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .weight(1f)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(userInfo)
    }
}