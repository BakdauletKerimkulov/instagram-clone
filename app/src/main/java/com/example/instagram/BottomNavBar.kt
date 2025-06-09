package com.example.instagram

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.presentation.common.Create
import com.example.presentation.common.Home
import com.example.presentation.common.Notification
import com.example.presentation.common.Profile
import com.example.presentation.common.Search
import com.example.presentation.R
import com.example.presentation.common.utils.CustomAsyncImage

@Composable
fun BottomNavBar(
    navController: NavHostController,
) {
    val bottomScreens = listOf(
        NavBarItem(title = "Home", icon = painterResource(R.drawable.instagram_home_feed_icon), route = Home.HOME_SCREEN),
        NavBarItem(title = "Search", icon = painterResource(R.drawable.instagram_search_icon), route = Search.SEARCH_SCREEN),
        NavBarItem(title = "AddNewPost", icon = painterResource(R.drawable.instagram_add_new_post_icon), route = Create.CREATE_SCREEN),
        NavBarItem(title = "Reels", icon = painterResource(R.drawable.instagram_reels_icon), selectedIcon = painterResource(R.drawable.instagram_reels_icon_full), route = Notification.NOTIFICATION_SCREEN),
        NavBarItem(title = "Profile", imageUrl = "https://picsum.photos/200/300", route = Profile.PROFILE_SCREEN)
    )

    CustomNavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentGraphRoute = navBackStackEntry?.destination?.route

        bottomScreens.forEach { item ->
            CustomNavBarItem(
                icon = if (currentGraphRoute == item.route) item.selectedIcon ?: item.icon else item.icon,
                imageUrl = item.imageUrl,
                isSelected = currentGraphRoute == item.route,
                title = item.title,
                onClick = {
                    navController.navigate(item.route) {
                        launchSingleTop = true

                        popUpTo(navController.currentDestination?.parent?.id ?: 0) {
                            inclusive = false
                            saveState = true
                        }
                    }
                },
                showLabel = false //Не показываем название
            )
        }
    }
}

@Preview
@Composable
fun BottomNavBarPreview() {
    BottomNavBar(navController = NavHostController(LocalContext.current))
}

data class NavBarItem(val title: String, val icon: Painter? = null, val selectedIcon: Painter? = null, val imageUrl: String? = null, val route: String)

@Composable
fun CustomNavigationBar(
    modifier: Modifier = Modifier,
    containerColor: Color = NavigationBarDefaults.containerColor,
    contentColor: Color = MaterialTheme.colorScheme.contentColorFor(containerColor),
    tonalElevation: Dp  = NavigationBarDefaults.Elevation,
    windowInsets: WindowInsets = NavigationBarDefaults.windowInsets,
    content: @Composable RowScope.() -> Unit
) {
    Surface(
        color = containerColor,
        contentColor = contentColor,
        tonalElevation = tonalElevation,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(windowInsets)
                .defaultMinSize(minHeight = 56.dp)
                .selectableGroup(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            content = content
        )
    }
}

@Composable
fun RowScope.CustomNavBarItem(
    icon: Painter? = null,
    imageUrl: String? = null,
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    selectedIconColor: Color = MaterialTheme.colorScheme.onBackground,
    unselectedIconColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    selectedTextColor: Color = MaterialTheme.colorScheme.onBackground,
    unselectedTextColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    showLabel: Boolean = false // Для Instagram-стиля можно отключить текст
) {
    // Анимация масштабирования иконки при выборе
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.1f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "IconScale"
    )

    Column(
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null // Убираем стандартную рябь для кастомного эффекта
            ) {
                onClick()
            }
        ,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .graphicsLayer { scaleX = scale; scaleY = scale },
            contentAlignment = Alignment.Center
        ) {
            if (imageUrl != null) {
                CustomAsyncImage(
                    imageUrl = "adf",
                    contentDescription = title,
                    modifier = Modifier
                        .size(26.dp)
                        .clip(CircleShape)
                        .border(
                            width = if (isSelected) 3.dp else 0.dp,
                            color = if (isSelected) selectedIconColor else Color.Transparent,
                            shape = CircleShape
                        )
                )
            } else if (icon != null) {
                Icon(
                    painter = icon,
                    contentDescription = title,
                    tint = if (isSelected) selectedIconColor else unselectedIconColor,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
        if (showLabel) {
            CompositionLocalProvider(LocalContentColor provides if (isSelected) selectedTextColor else unselectedTextColor) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isSelected) selectedTextColor else unselectedTextColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}