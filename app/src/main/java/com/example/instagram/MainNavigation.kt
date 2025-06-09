package com.example.instagram

import android.app.Activity
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.domain.AuthRepository
import com.example.presentation.common.Auth
import com.example.presentation.common.Create
import com.example.presentation.common.Home
import com.example.presentation.common.Notification
import com.example.presentation.common.Profile
import com.example.presentation.common.Root
import com.example.presentation.common.Search
import com.example.presentation.common.nav_graph.authGraph
import com.example.presentation.common.nav_graph.createGraph
import com.example.presentation.common.nav_graph.homeGraph
import com.example.presentation.common.nav_graph.notificationGraph
import com.example.presentation.common.nav_graph.profileGraph
import com.example.presentation.common.nav_graph.searchGraph
import com.example.presentation.common.nav_graph.settingsGraph
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.collections.contains

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainNavigation(
    authRepository: AuthRepository = hiltViewModel<MainNavigationViewModel>().authRepository,
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val mainScreens = listOf(
        Home.HOME_SCREEN,
        Search.SEARCH_SCREEN,
        Profile.PROFILE_SCREEN,
        Create.CREATE_SCREEN,
        Notification.NOTIFICATION_SCREEN,
    )

    val showBottomBar = currentRoute in mainScreens

    val context = LocalContext.current

    BackHandler(enabled = true) {
        val currentGraphRoute = navController.currentDestination?.parent?.route
        if (currentGraphRoute != Home.HOME_GRAPH) {
            navController.navigate(Home.HOME_GRAPH) {
                popUpTo(navController.graph.startDestinationId) { inclusive = false }
                launchSingleTop = true
            }
        } else {
            (context as? Activity)?.finish()
        }
    }

    val startDestination = Root.SPLASH_SCREEN

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavBar(navController = navController)
            }
        }
    ) { innerPadding ->
        val mod = Modifier.padding(innerPadding)

        NavHost(
            navController = navController,
            startDestination = startDestination
        ) {

            composable(
                route = Root.SPLASH_SCREEN
            ) {
                val user by authRepository.userState.collectAsState()

                LaunchedEffect(user) {
                    delay(300)
                    if (user != null) {
                        navController.navigate(Home.HOME_GRAPH) {
                            popUpTo(Root.SPLASH_SCREEN) { inclusive = true }
                        }
                    } else {
                        navController.navigate(Auth.AUTH_GRAPH) {
                            popUpTo(Root.SPLASH_SCREEN) { inclusive = true }
                        }
                    }
                }
                SplashScreen()
            }

            homeGraph(navController)
            searchGraph(navController)
            createGraph(navController)
            notificationGraph(navController)
            profileGraph(navController)

            settingsGraph(navController)

            authGraph(navController)
        }
    }
}

@HiltViewModel
class MainNavigationViewModel @Inject constructor(
    internal val authRepository: AuthRepository
) : ViewModel()

@Composable
fun SplashScreen() {

}