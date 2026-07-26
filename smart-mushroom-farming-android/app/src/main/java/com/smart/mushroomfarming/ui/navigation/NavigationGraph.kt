package com.smart.mushroomfarming.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.smart.mushroomfarming.ui.screens.auth.AuthViewModel
import com.smart.mushroomfarming.ui.screens.auth.ForgotPasswordScreen
import com.smart.mushroomfarming.ui.screens.auth.LoginScreen
import com.smart.mushroomfarming.ui.screens.auth.RegisterScreen
import com.smart.mushroomfarming.ui.screens.dashboard.DashboardScreen
import com.smart.mushroomfarming.ui.screens.dashboard.RecommendationsScreen
import com.smart.mushroomfarming.ui.screens.history.PredictionDetailScreen
import com.smart.mushroomfarming.ui.screens.history.PredictionHistoryScreen
import com.smart.mushroomfarming.ui.screens.prediction.PredictionScreen
import com.smart.mushroomfarming.ui.screens.settings.SettingsScreen
import com.smart.mushroomfarming.ui.screens.splash.SplashScreen

@Composable
fun NavigationGraph() {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        enterTransition = {
            fadeIn(animationSpec = tween(400)) + slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(400)
            )
        },
        exitTransition = {
            fadeOut(animationSpec = tween(400)) + slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(400)
            )
        },
        popEnterTransition = {
            fadeIn(animationSpec = tween(400)) + slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(400)
            )
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(400)) + slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(400)
            )
        }
    ) {
        composable(Screen.Splash.route) {
            val splashViewModel = hiltViewModel<AuthViewModel>()
            SplashScreen(
                onNavigateToDashboard = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                viewModel = splashViewModel
            )
        }
        
        composable(Screen.Login.route) {
            val loginViewModel = hiltViewModel<AuthViewModel>()
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                onNavigateToForgotPassword = {
                    navController.navigate(Screen.ForgotPassword.route)
                },
                onNavigateToDashboard = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                viewModel = loginViewModel
            )
        }
        
        composable(Screen.Register.route) {
            val registerViewModel = hiltViewModel<AuthViewModel>()
            RegisterScreen(
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                onNavigateToDashboard = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                viewModel = registerViewModel
            )
        }
        
        composable(Screen.ForgotPassword.route) {
            val forgotPasswordViewModel = hiltViewModel<AuthViewModel>()
            ForgotPasswordScreen(
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                viewModel = forgotPasswordViewModel
            )
        }

        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Dashboard.route) { inclusive = true }
                    }
                },
                onNavigateToPrediction = {
                    navController.navigate(Screen.Prediction.route)
                },
                onNavigateToPredictionHistory = {
                    navController.navigate(Screen.PredictionHistory.route)
                },
                onNavigateToRecommendations = {
                    navController.navigate(Screen.Recommendations.route)
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }

        composable(Screen.Prediction.route) {
            PredictionScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.PredictionHistory.route) {
            PredictionHistoryScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToDetail = { id ->
                    navController.navigate(Screen.PredictionDetail.createRoute(id))
                }
            )
        }

        composable(
            route = Screen.PredictionDetail.route,
            arguments = listOf(
                navArgument("predictionId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val predictionId = backStackEntry.arguments?.getString("predictionId").orEmpty()
            PredictionDetailScreen(
                predictionId = predictionId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Recommendations.route) {
            RecommendationsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Dashboard.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
