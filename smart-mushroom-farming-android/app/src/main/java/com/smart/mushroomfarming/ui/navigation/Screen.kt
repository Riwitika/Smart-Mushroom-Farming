package com.smart.mushroomfarming.ui.navigation

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Login : Screen("login")
    data object Register : Screen("register")
    data object ForgotPassword : Screen("forgot_password")
    data object Dashboard : Screen("dashboard")
    data object Prediction : Screen("prediction")
    data object PredictionHistory : Screen("prediction_history")
    data object PredictionDetail : Screen("prediction_detail/{predictionId}") {
        fun createRoute(predictionId: String): String = "prediction_detail/$predictionId"
    }
    data object Recommendations : Screen("recommendations")
    data object Settings : Screen("settings")
}
