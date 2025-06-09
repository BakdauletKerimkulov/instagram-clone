package com.example.presentation.common.nav_graph

import android.app.Activity
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.presentation.common.Auth
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.presentation.common.Home
import com.example.presentation.ui_auth.AuthMode
import com.example.presentation.ui_auth.VerificationScreen
import com.example.presentation.ui_auth.AuthViewModel
import com.example.presentation.ui_auth.FillEmailScreen
import com.example.presentation.ui_auth.FillNumberScreen
import com.example.presentation.ui_auth.BirthDateScreen
import com.example.presentation.ui_auth.InputType
import com.example.presentation.ui_auth.LoginScreen
import com.example.presentation.ui_auth.PasswordScreen
import com.example.presentation.ui_auth.RecoverPasswordScreen
import com.example.presentation.ui_auth.UsernameScreen
import kotlinx.coroutines.delay

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.authGraph(navController: NavHostController) {
    val tweenDuration = 500

    navigation(
        route = Auth.AUTH_GRAPH,
        startDestination = Auth.LOGIN_SCREEN
    ) {

        composable(
            route = Auth.LOGIN_SCREEN
        ) {
            var backPressedOnce by remember { mutableStateOf(false) }
            val context = LocalContext.current

            LaunchedEffect(backPressedOnce == true) {
                delay(2000)
                backPressedOnce = false
            }

            BackHandler {
                if (backPressedOnce) {
                    (context as? Activity)?.finish()
                } else {
                    backPressedOnce = true
                    Toast.makeText(context, "Tap one more time to exit", Toast.LENGTH_SHORT).show()
                }
            }

            LoginScreen(
                onLoginClick = {
                    if (it == InputType.Phone.name) {
                        navController.navigate(Auth.VERIFICATION_SCREEN)
                    } else {
                        navController.navigate(Home.HOME_SCREEN)
                    }
                },
                onCreateAccClick = {
                    navController.navigate(Auth.FILL_NUMBER_SCREEN)
                },
                onForgotPasswordClick = {

                }
            )
        }

        phoneRegistration(navController, tweenDuration)

        emailRegistration(navController, tweenDuration)

        composable(
            route = Auth.RECOVER_PASSWORD_SCREEN,
            enterTransition = {
                slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(tweenDuration))
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(tweenDuration))
            },
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(tweenDuration))
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(tweenDuration))
            }
        ) {
            val viewModel: AuthViewModel = hiltViewModel()

            RecoverPasswordScreen(
                onBackClick = {
                    navController.popBackStack(Auth.LOGIN_SCREEN, inclusive = false)
                },
                onContinueClick = {

                },
                viewModel = viewModel
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.phoneRegistration(navController: NavHostController, tweenDuration: Int = 500) {
    navigation(
        route = Auth.PHONE_REGISTRATION_GRAPH,
        startDestination = Auth.FILL_NUMBER_SCREEN
    ) {
        composable(
            route = Auth.FILL_NUMBER_SCREEN,
            enterTransition = {
                slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(tweenDuration))
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(tweenDuration))
            },
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(tweenDuration))
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(tweenDuration))
            }
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Auth.PHONE_REGISTRATION_GRAPH)
            }

            val viewModel: AuthViewModel = hiltViewModel(parentEntry)

            FillNumberScreen(
                onBackClick = {
                    navController.popBackStack(Auth.LOGIN_SCREEN, inclusive = false)
                },
                onAuthEmailClick = {
                    navController.navigate(Auth.FILL_EMAIL_SCREEN)
                },
                onAutoVerified = {
                    navController.navigate(Auth.BIRTH_DATE_SCREEN)
                },
                onCodeSent = {
                    navController.navigate(Auth.VERIFICATION_SCREEN)
                },
                viewModel = viewModel
            )
        }

        composable(
            route = Auth.VERIFICATION_SCREEN,
            enterTransition = {
                slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(tweenDuration))
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(tweenDuration))
            },
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(tweenDuration))
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(tweenDuration))
            }
        ) { backStackEntry ->

            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Auth.PHONE_REGISTRATION_GRAPH)
            }

            val viewModel: AuthViewModel = hiltViewModel(parentEntry)

            VerificationScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onNextClick = {
                    if (it == AuthMode.Register) {
                        navController.navigate(Auth.BIRTH_DATE_SCREEN)
                    } else {
                        navController.navigate(Home.HOME_SCREEN)
                    }
                },
                viewModel = viewModel
            )
        }

        composable(
            route = Auth.BIRTH_DATE_SCREEN,
            enterTransition = {
                slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(tweenDuration))
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(tweenDuration))
            },
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(tweenDuration))
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(tweenDuration))
            }
        ) { backStackEntry ->

            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Auth.PHONE_REGISTRATION_GRAPH)
            }

            val viewModel: AuthViewModel = hiltViewModel(parentEntry)

            BirthDateScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onNextClick = {
                    navController.navigate(Auth.USERNAME_SCREEN)
                    Log.d("VerificationScreen", "onNextClick called. CurrentBackStack route: ${backStackEntry.destination.route}")
                },
                viewModel = viewModel
            )
        }

        composable(
            route = Auth.USERNAME_SCREEN,
            enterTransition = {
                slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(tweenDuration))
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(tweenDuration))
            },
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(tweenDuration))
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(tweenDuration))
            }
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Auth.PHONE_REGISTRATION_GRAPH)
            }

            val viewModel: AuthViewModel = hiltViewModel(parentEntry)

            UsernameScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onNextClick = {
                    navController.navigate(Home.HOME_SCREEN) {
                        popUpTo(Auth.AUTH_GRAPH) { inclusive = true }
                    }
                    Log.d("UsernameScreen", "onNextClick called. CurrentBackStack route: ${backStackEntry.destination.route}")
                },
                viewModel = viewModel
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.emailRegistration(navController: NavController, tweenDuration: Int = 500) {
    navigation(
        route = Auth.EMAIL_REGISTRATION_GRAPH,
        startDestination = Auth.FILL_EMAIL_SCREEN
    ) {
        composable(
            route = Auth.FILL_EMAIL_SCREEN,
            enterTransition = {
                slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(tweenDuration))
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(tweenDuration))
            },
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(tweenDuration))
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(tweenDuration))
            }
        ) { backStackEntry ->

            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Auth.EMAIL_REGISTRATION_GRAPH)
            }

            val viewModel: AuthViewModel = hiltViewModel(parentEntry)

            FillEmailScreen(
                onBackClick = {
                    navController.popBackStack(Auth.FILL_NUMBER_SCREEN, inclusive = false)
                },
                onAuthMobileClick = {
                    navController.popBackStack(Auth.FILL_NUMBER_SCREEN, inclusive = false)
                },
                onNextClick = {
                    navController.navigate(Auth.PASSWORD_SCREEN)
                    Log.d("VerificationScreen", "onNextClick called. CurrentBackStack route: ${backStackEntry.destination.route}")
                },
                viewModel = viewModel
            )
        }

        composable(
            route = Auth.PASSWORD_SCREEN,
            enterTransition = {
                slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(tweenDuration))
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(tweenDuration))
            },
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(tweenDuration))
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(tweenDuration))
            }
        ) { backStackEntry ->

            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Auth.EMAIL_REGISTRATION_GRAPH)
            }

            val viewModel: AuthViewModel = hiltViewModel(parentEntry)

            PasswordScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onNextClick = {
                    navController.navigate(Auth.BIRTH_DATE_SCREEN)
                },
                viewModel = viewModel
            )
        }

        composable(
            route = Auth.BIRTH_DATE_SCREEN,
            enterTransition = {
                slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(tweenDuration))
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(tweenDuration))
            },
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(tweenDuration))
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(tweenDuration))
            }
        ) { backStackEntry ->

            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Auth.EMAIL_REGISTRATION_GRAPH)
            }

            val viewModel: AuthViewModel = hiltViewModel(parentEntry)

            BirthDateScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onNextClick = {
                    navController.navigate(Auth.USERNAME_SCREEN)
                    Log.d("VerificationScreen", "onNextClick called. CurrentBackStack route: ${backStackEntry.destination.route}")
                },
                viewModel = viewModel
            )
        }

        composable(
            route = Auth.USERNAME_SCREEN,
            enterTransition = {
                slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(tweenDuration))
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(tweenDuration))
            },
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(tweenDuration))
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(tweenDuration))
            }
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Auth.EMAIL_REGISTRATION_GRAPH)
            }

            val viewModel: AuthViewModel = hiltViewModel(parentEntry)

            UsernameScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onNextClick = {
                    navController.navigate(Home.HOME_SCREEN) {
                        popUpTo(Auth.AUTH_GRAPH) { inclusive = true }
                    }
                    Log.d("UsernameScreen", "onNextClick called. CurrentBackStack route: ${backStackEntry.destination.route}")
                },
                viewModel = viewModel
            )
        }
    }
}