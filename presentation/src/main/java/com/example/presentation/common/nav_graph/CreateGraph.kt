package com.example.presentation.common.nav_graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.presentation.common.Create
import com.example.presentation.ui_create.CreateScreen

fun NavGraphBuilder.createGraph(navController: NavController) {
    navigation(
        route = Create.CREATE_GRAPH,
        startDestination = Create.CREATE_SCREEN
    ) {
        composable(
            route = Create.CREATE_SCREEN
        ) {
            CreateScreen()
        }
    }
}