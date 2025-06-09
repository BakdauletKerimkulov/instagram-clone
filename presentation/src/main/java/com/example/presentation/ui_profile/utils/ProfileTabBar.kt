package com.example.presentation.ui_profile.utils

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.presentation.ui_profile.TabBarItem
import kotlin.enums.EnumEntries

@Composable
fun ProfileTabBar(
    modifier: Modifier = Modifier,
    selectedTab: TabBarItem,
    onTabSelected: (TabBarItem) -> Unit,
    tabBarItems: EnumEntries<TabBarItem>
) {
    val tabWidth = remember { mutableIntStateOf(0) }
    val activeColor = MaterialTheme.colorScheme.onBackground
    val inactiveColor = Color(0xFF999999)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .onGloballyPositioned {
                    tabWidth.intValue = if (tabBarItems.isNotEmpty()) {
                        it.size.width / tabBarItems.size
                    } else {
                        0 // Защита от пустого списка
                    }
                },
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabBarItems.forEach { tabItem ->
                IconButton(
                    onClick = { onTabSelected(tabItem) },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    Icon(
                        painter = painterResource(id = tabItem.icon),
                        contentDescription = tabItem.title,
                        tint = if (tabItem == selectedTab) activeColor else inactiveColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        val selectedTabIndex = tabBarItems.indexOf(selectedTab).coerceAtLeast(0)
        val offset by animateDpAsState(
            targetValue = with(LocalDensity.current) {
                (tabWidth.intValue * selectedTabIndex).toDp()
            },
            animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
            label = "UnderlineOffset"
        )

        if (tabBarItems.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .offset(offset)
                    .align(Alignment.BottomStart)
                    .width(with(LocalDensity.current) { tabWidth.intValue.toDp() })
                    .height(2.dp)
                    .background(activeColor, RoundedCornerShape(1.dp))
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileTabBar(
        selectedTab = TabBarItem.Grid,
        tabBarItems = TabBarItem.entries,
        onTabSelected = {}
    )
}