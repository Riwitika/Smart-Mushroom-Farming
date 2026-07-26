package com.smart.mushroomfarming.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.Filled.Temperature: ImageVector
    get() = TemperatureIcon

val Icons.Filled.Humidity: ImageVector
    get() = HumidityIcon

val Icons.Filled.Ventilation: ImageVector
    get() = VentilationIcon

val Icons.Filled.LightIntensity: ImageVector
    get() = LightIntensityIcon

val Icons.Filled.Ph: ImageVector
    get() = PhIcon

val Icons.Filled.History: ImageVector
    get() = HistoryIcon

val Icons.Filled.Prediction: ImageVector
    get() = PredictionIcon

private val TemperatureIcon: ImageVector by lazy {
    ImageVector.Builder(
        name = "Temperature",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).path(
        fill = SolidColor(Color.Black)
    ) {
        moveTo(15f, 13f)
        verticalLineTo(5f)
        curveTo(15f, 3.34f, 13.66f, 2f, 12f, 2f)
        reflectiveCurveTo(9f, 3.34f, 9f, 5f)
        verticalLineTo(13f)
        curveTo(7.79f, 13.91f, 7f, 15.36f, 7f, 17f)
        curveTo(7f, 19.76f, 9.24f, 22f, 12f, 22f)
        reflectiveCurveTo(17f, 19.76f, 17f, 17f)
        curveTo(17f, 15.36f, 16.21f, 13.91f, 15f, 13f)
        close()
        
        moveTo(12f, 20f)
        curveTo(10.34f, 20f, 9f, 18.66f, 9f, 17f)
        curveTo(9f, 15.9f, 9.58f, 14.93f, 10.5f, 14.39f)
        lineTo(11f, 14.1f)
        verticalLineTo(5f)
        curveTo(11f, 4.45f, 11.45f, 4f, 12f, 4f)
        reflectiveCurveTo(13f, 4.45f, 13f, 5f)
        verticalLineTo(14.1f)
        lineTo(13.5f, 14.39f)
        curveTo(14.42f, 14.93f, 15f, 15.9f, 15f, 17f)
        curveTo(15f, 18.66f, 13.66f, 20f, 12f, 20f)
        close()
    }.build()
}

private val HumidityIcon: ImageVector by lazy {
    ImageVector.Builder(
        name = "Humidity",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).path(
        fill = SolidColor(Color.Black)
    ) {
        moveTo(12f, 2.69f)
        curveTo(11.64f, 2.3f, 10.96f, 2.3f, 10.6f, 2.69f)
        lineTo(5.08f, 8.78f)
        curveTo(2.97f, 11.11f, 2.97f, 14.67f, 5.08f, 17f)
        curveTo(7.19f, 19.33f, 10.61f, 19.33f, 12.72f, 17f)
        curveTo(14.83f, 14.67f, 14.83f, 11.11f, 12.72f, 8.78f)
        lineTo(12f, 2.69f)
        close()
        
        moveTo(12f, 15.5f)
        curveTo(10.62f, 15.5f, 9.5f, 14.38f, 9.5f, 13f)
        curveTo(9.5f, 12.45f, 9.95f, 12f, 10.5f, 12f)
        reflectiveCurveTo(11.5f, 12.45f, 11.5f, 13f)
        curveTo(11.5f, 13.28f, 11.72f, 13.5f, 12f, 13.5f)
        reflectiveCurveTo(12.5f, 13.28f, 12.5f, 13f)
        curveTo(12.5f, 11.62f, 11.38f, 10.5f, 10f, 10.5f)
        curveTo(9.45f, 10.5f, 9f, 10.05f, 9f, 9.5f)
        reflectiveCurveTo(9.45f, 9f, 10f, 9f)
        curveTo(12.21f, 9f, 14f, 10.79f, 14f, 13f)
        curveTo(14f, 14.38f, 12.88f, 15.5f, 12f, 15.5f)
        close()
    }.build()
}

private val VentilationIcon: ImageVector by lazy {
    ImageVector.Builder(
        name = "Ventilation",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).path(
        fill = SolidColor(Color.Black)
    ) {
        moveTo(12f, 12f)
        curveTo(13.1f, 12f, 14f, 11.1f, 14f, 10f)
        curveTo(14f, 8.9f, 13.1f, 8f, 12f, 8f)
        reflectiveCurveTo(10f, 8.9f, 10f, 10f)
        curveTo(10f, 11.1f, 10.9f, 12f, 12f, 12f)
        close()
        
        moveTo(12f, 2f)
        curveTo(6.48f, 2f, 2f, 6.48f, 2f, 12f)
        reflectiveCurveTo(6.48f, 22f, 12f, 22f)
        reflectiveCurveTo(22f, 17.52f, 22f, 12f)
        reflectiveCurveTo(17.52f, 2f, 12f, 2f)
        close()
        
        moveTo(12f, 20f)
        curveTo(7.59f, 20f, 4f, 16.41f, 4f, 12f)
        reflectiveCurveTo(7.59f, 4f, 12f, 4f)
        reflectiveCurveTo(20f, 16.41f, 20f, 12f)
        reflectiveCurveTo(16.41f, 20f, 12f, 20f)
        close()
        
        moveTo(12f, 6f)
        curveTo(12.55f, 6f, 13f, 6.45f, 13f, 7f)
        verticalLineTo(9.1f)
        curveTo(13.5f, 9.35f, 13.85f, 9.8f, 13.97f, 10.3f)
        lineTo(16f, 9.5f)
        curveTo(16.5f, 9.3f, 17f, 9.7f, 17f, 10.25f)
        curveTo(17f, 10.8f, 16.5f, 11.2f, 16f, 11f)
        lineTo(13.97f, 10.3f)
        curveTo(13.85f, 10.8f, 13.5f, 11.25f, 13f, 11.5f)
        verticalLineTo(13f)
        curveTo(13.55f, 13f, 14f, 13.45f, 14f, 14f)
        reflectiveCurveTo(13.55f, 15f, 13f, 15f)
        horizontalLineTo(11f)
        curveTo(10.45f, 15f, 10f, 14.55f, 10f, 14f)
        reflectiveCurveTo(10.45f, 13f, 11f, 13f)
        verticalLineTo(11.5f)
        curveTo(10.5f, 11.25f, 10.15f, 10.8f, 10.03f, 10.3f)
        lineTo(8f, 11.1f)
        curveTo(7.5f, 11.3f, 7f, 10.9f, 7f, 10.35f)
        curveTo(7f, 9.8f, 7.5f, 9.4f, 8f, 9.6f)
        lineTo(10.03f, 10.3f)
        curveTo(10.15f, 9.8f, 10.5f, 9.35f, 11f, 9.1f)
        verticalLineTo(7f)
        curveTo(11f, 6.45f, 11.45f, 6f, 12f, 6f)
        close()
    }.build()
}

private val LightIntensityIcon: ImageVector by lazy {
    ImageVector.Builder(
        name = "LightIntensity",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).path(
        fill = SolidColor(Color.Black)
    ) {
        moveTo(12f, 7f)
        curveTo(9.24f, 7f, 7f, 9.24f, 7f, 12f)
        reflectiveCurveTo(9.24f, 17f, 12f, 17f)
        reflectiveCurveTo(17f, 14.76f, 17f, 12f)
        reflectiveCurveTo(14.76f, 7f, 12f, 7f)
        close()
        
        moveTo(12f, 5f)
        curveTo(12.55f, 5f, 13f, 4.55f, 13f, 4f)
        verticalLineTo(2f)
        curveTo(13f, 1.45f, 12.55f, 1f, 12f, 1f)
        reflectiveCurveTo(11f, 1.45f, 11f, 2f)
        verticalLineTo(4f)
        curveTo(11f, 4.55f, 11.45f, 5f, 12f, 5f)
        close()
        
        moveTo(12f, 19f)
        curveTo(11.45f, 19f, 11f, 19.45f, 11f, 20f)
        verticalLineTo(22f)
        curveTo(11f, 22.55f, 11.45f, 23f, 12f, 23f)
        reflectiveCurveTo(13f, 22.55f, 13f, 22f)
        verticalLineTo(20f)
        curveTo(13f, 19.45f, 12.55f, 19f, 12f, 19f)
        close()
        
        moveTo(5f, 12f)
        curveTo(5f, 11.45f, 4.55f, 11f, 4f, 11f)
        horizontalLineTo(2f)
        curveTo(1.45f, 11f, 1f, 11.45f, 1f, 12f)
        reflectiveCurveTo(1.45f, 13f, 2f, 13f)
        horizontalLineTo(4f)
        curveTo(4.55f, 13f, 5f, 12.55f, 5f, 12f)
        close()
        
        moveTo(19f, 12f)
        curveTo(19f, 12.55f, 19.45f, 13f, 20f, 13f)
        horizontalLineTo(22f)
        curveTo(22.55f, 13f, 23f, 12.55f, 23f, 12f)
        reflectiveCurveTo(23f, 11f, 22f, 11f)
        horizontalLineTo(20f)
        curveTo(19.45f, 11f, 19f, 11.45f, 19f, 12f)
        close()
    }.build()
}

private val PhIcon: ImageVector by lazy {
    ImageVector.Builder(
        name = "Ph",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).path(
        fill = SolidColor(Color.Black)
    ) {
        moveTo(19f, 3f)
        horizontalLineTo(5f)
        curveTo(3.9f, 3f, 3f, 3.9f, 3f, 5f)
        verticalLineTo(19f)
        curveTo(3f, 20.1f, 3.9f, 21f, 5f, 21f)
        horizontalLineTo(19f)
        curveTo(20.1f, 21f, 21f, 20.1f, 21f, 19f)
        verticalLineTo(5f)
        curveTo(21f, 3.9f, 20.1f, 3f, 19f, 3f)
        close()
        
        moveTo(11f, 17f)
        horizontalLineTo(9f)
        verticalLineTo(7f)
        horizontalLineTo(11f)
        verticalLineTo(17f)
        close()
        
        moveTo(15f, 17f)
        horizontalLineTo(13f)
        verticalLineTo(12f)
        horizontalLineTo(15f)
        verticalLineTo(17f)
        close()
        
        moveTo(15f, 10f)
        horizontalLineTo(13f)
        verticalLineTo(7f)
        horizontalLineTo(15f)
        verticalLineTo(10f)
        close()
    }.build()
}

private val HistoryIcon: ImageVector by lazy {
    ImageVector.Builder(
        name = "History",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).path(
        fill = SolidColor(Color.Black)
    ) {
        moveTo(13f, 3f)
        curveTo(8.03f, 3f, 4f, 7.03f, 4f, 12f)
        horizontalLineTo(1f)
        lineTo(4.89f, 15.89f)
        lineTo(5.1f, 16.1f)
        lineTo(9f, 12f)
        horizontalLineTo(6f)
        curveTo(6f, 8.69f, 8.69f, 6f, 12f, 6f)
        reflectiveCurveTo(18f, 8.69f, 18f, 12f)
        reflectiveCurveTo(15.31f, 18f, 12f, 18f)
        curveTo(10.38f, 18f, 8.93f, 17.36f, 7.86f, 16.32f)
        lineTo(6.42f, 17.76f)
        curveTo(7.85f, 19.16f, 9.83f, 20f, 12f, 20f)
        curveTo(16.97f, 20f, 21f, 15.97f, 21f, 12f)
        reflectiveCurveTo(16.97f, 3f, 13f, 3f)
        close()
        
        moveTo(12f, 8f)
        verticalLineTo(13f)
        lineTo(16.28f, 15.54f)
        lineTo(17f, 14.33f)
        lineTo(13.5f, 12.25f)
        verticalLineTo(8f)
        horizontalLineTo(12f)
        close()
    }.build()
}

private val PredictionIcon: ImageVector by lazy {
    ImageVector.Builder(
        name = "Prediction",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).path(
        fill = SolidColor(Color.Black)
    ) {
        moveTo(12f, 2f)
        curveTo(6.48f, 2f, 2f, 6.48f, 2f, 12f)
        reflectiveCurveTo(6.48f, 22f, 12f, 22f)
        reflectiveCurveTo(22f, 17.52f, 22f, 12f)
        reflectiveCurveTo(17.52f, 2f, 12f, 2f)
        close()
        
        moveTo(12f, 20f)
        curveTo(7.59f, 20f, 4f, 16.41f, 4f, 12f)
        reflectiveCurveTo(7.59f, 4f, 12f, 4f)
        reflectiveCurveTo(20f, 7.59f, 20f, 12f)
        reflectiveCurveTo(16.41f, 20f, 12f, 20f)
        close()
        
        moveTo(12f, 6f)
        verticalLineTo(10f)
        moveTo(12f, 14f)
        verticalLineTo(18f)
        moveTo(6f, 12f)
        horizontalLineTo(10f)
        moveTo(14f, 12f)
        horizontalLineTo(18f)
    }.build()
}
