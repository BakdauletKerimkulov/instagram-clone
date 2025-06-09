package com.example.presentation.common.nav_graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.presentation.common.Notification
import com.example.presentation.ui_notification.NotificationScreen

fun NavGraphBuilder.notificationGraph(navController: NavController) {
    navigation(
        route = Notification.NOTIFICATION_GRAPH,
        startDestination = Notification.NOTIFICATION_SCREEN
    ) {
        composable(
            route = Notification.NOTIFICATION_SCREEN
        ) {
            NotificationScreen()
        }

    }
}