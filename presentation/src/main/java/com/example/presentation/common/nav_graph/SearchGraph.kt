package com.example.presentation.common.nav_graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.presentation.common.Search
import com.example.presentation.ui_search.SearchScreen

fun NavGraphBuilder.searchGraph(navController: NavController) {
    navigation(
        route = Search.SEARCH_GRAPH,
        startDestination = Search.SEARCH_SCREEN
    ) {
        composable(
            route = Search.SEARCH_SCREEN
        ) {
            SearchScreen()
        }
    }
}