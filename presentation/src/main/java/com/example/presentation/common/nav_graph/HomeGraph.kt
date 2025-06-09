package com.example.presentation.common.nav_graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.presentation.common.Home
import com.example.presentation.ui_home.HomeScreen

fun NavGraphBuilder.homeGraph(navController: NavController) {
    navigation(
        route = Home.HOME_GRAPH,
        startDestination = Home.HOME_SCREEN
    ) {
        composable(
            route = Home.HOME_SCREEN
        ) {
            HomeScreen(onDirectMessageClick = {}, onNotificationClick = {})
        }
    }
}