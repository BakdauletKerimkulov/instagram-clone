package com.example.presentation.common.nav_graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.presentation.common.Auth
import com.example.presentation.common.Settings
import com.example.presentation.ui_settings.SettingsScreen

fun NavGraphBuilder.settingsGraph(navController: NavController) {
    navigation(
        route = Settings.SETTINGS_GRAPH,
        startDestination = Settings.SETTINGS_SCREEN
    ) {
        composable(
            route = Settings.SETTINGS_SCREEN
        ) {
            SettingsScreen(
                onBackClick = { navController.popBackStack() },
                onSignOut = {
                    navController.navigate(Auth.AUTH_GRAPH) {
                        popUpTo(Settings.SETTINGS_GRAPH) { inclusive = true }
                    }
                }
            )
        }
    }
}