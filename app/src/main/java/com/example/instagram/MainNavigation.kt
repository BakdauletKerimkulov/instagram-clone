package com.example.instagram

import android.app.Activity
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.domain.UserStateUseCase
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
import com.example.presentation.common.utils.AuthUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import kotlin.collections.contains

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainNavigation() {
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
                SplashScreen(
                    onAuthenticated = {
                        navController.navigate(Home.HOME_GRAPH) {
                            popUpTo(Root.SPLASH_SCREEN) { inclusive = true }
                        }
                    },
                    onUnauthenticated = {
                        navController.navigate(Auth.AUTH_GRAPH) {
                            popUpTo(Root.SPLASH_SCREEN) { inclusive = true }
                        }
                    }
                )
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

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    viewModel: SplashScreenViewModel = hiltViewModel(),
    onAuthenticated: () -> Unit,
    onUnauthenticated: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState) {
        when (uiState) {
            AuthUiState.Authenticated -> {
                onAuthenticated()
            }
            AuthUiState.Unauthenticated -> {
                onUnauthenticated()
            }
            AuthUiState.Loading -> {

            }
        }
    }

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    userStateUseCase: UserStateUseCase
) : ViewModel() {

    val uiState: StateFlow<AuthUiState> = userStateUseCase.userState
        .map { user ->
            when {
                user == null -> AuthUiState.Unauthenticated
                else -> AuthUiState.Authenticated
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = AuthUiState.Loading
        )
}