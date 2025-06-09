package com.example.presentation.common.nav_graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.presentation.common.Profile
import com.example.presentation.common.Settings
import com.example.presentation.ui_profile.ProfileScreen

fun NavGraphBuilder.profileGraph(navController: NavController) {
    navigation(
        route = Profile.PROFILE_GRAPH,
        startDestination = Profile.PROFILE_SCREEN
    ) {
        composable(
            route = Profile.PROFILE_SCREEN
        ) {
            ProfileScreen(
                onSettingsClick = {
                    navController.navigate(Settings.SETTINGS_SCREEN)
                },
                onCreateClick = {

                }
            )
        }
    }
}