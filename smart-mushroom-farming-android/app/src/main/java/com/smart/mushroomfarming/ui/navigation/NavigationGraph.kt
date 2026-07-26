package com.smart.mushroomfarming.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.smart.mushroomfarming.ui.screens.auth.ForgotPasswordScreen
import com.smart.mushroomfarming.ui.screens.auth.LoginScreen
import com.smart.mushroomfarming.ui.screens.auth.RegisterScreen
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
            SplashScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                onNavigateToForgotPassword = {
                    navController.navigate(Screen.ForgotPassword.route)
                }
            )
        }
        
        composable(Screen.Register.route) {
            RegisterScreen(
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(Screen.ForgotPassword.route) {
            ForgotPasswordScreen(
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }
    }
}
