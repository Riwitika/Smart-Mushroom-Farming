package com.smart.mushroomfarming.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.Filled.Visibility: ImageVector
    get() = VisibilityIcon

val Icons.Filled.VisibilityOff: ImageVector
    get() = VisibilityOffIcon

private val VisibilityIcon: ImageVector by lazy {
    ImageVector.Builder(
        name = "Visibility",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).path(
        fill = SolidColor(Color.Black)
    ) {
        moveTo(12f, 4.5f)
        curveTo(7f, 4.5f, 2.73f, 7.61f, 1f, 12f)
        curveTo(2.73f, 16.39f, 7f, 19.5f, 12f, 19.5f)
        reflectiveCurveTo(21.27f, 16.39f, 23f, 12f)
        curveTo(21.27f, 7.61f, 17f, 4.5f, 12f, 4.5f)
        close()
        
        moveTo(12f, 17f)
        curveTo(9.24f, 17f, 7f, 14.76f, 7f, 12f)
        reflectiveCurveTo(9.24f, 7f, 12f, 7f)
        reflectiveCurveTo(17f, 9.24f, 17f, 12f)
        reflectiveCurveTo(14.76f, 17f, 12f, 17f)
        close()
        
        moveTo(12f, 9f)
        curveTo(10.34f, 9f, 9f, 10.34f, 9f, 12f)
        reflectiveCurveTo(10.34f, 15f, 12f, 15f)
        reflectiveCurveTo(15f, 13.66f, 15f, 12f)
        reflectiveCurveTo(13.66f, 9f, 12f, 9f)
        close()
    }.build()
}

private val VisibilityOffIcon: ImageVector by lazy {
    ImageVector.Builder(
        name = "VisibilityOff",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).path(
        fill = SolidColor(Color.Black)
    ) {
        moveTo(12f, 7f)
        curveTo(14.76f, 7f, 17f, 9.24f, 17f, 12f)
        curveTo(17f, 12.64f, 16.87f, 13.25f, 16.65f, 13.82f)
        lineTo(19.57f, 16.74f)
        curveTo(21.2f, 14.93f, 22.37f, 12.7f, 23f, 10.22f)
        curveTo(21.27f, 5.83f, 17f, 2.72f, 12f, 2.72f)
        curveTo(10.08f, 2.72f, 8.26f, 3.17f, 6.62f, 3.95f)
        lineTo(9.18f, 6.51f)
        curveTo(10.04f, 6.03f, 11.04f, 5.78f, 12f, 5.78f)
        close()
        
        moveTo(1.41f, 1.27f)
        lineTo(0f, 2.69f)
        lineTo(3.52f, 6.21f)
        curveTo(1.91f, 7.96f, 0.73f, 10.18f, 0.1f, 12.65f)
        curveTo(1.83f, 17.04f, 6.1f, 20.15f, 11.1f, 20.15f)
        curveTo(12.65f, 20.15f, 14.13f, 19.85f, 15.48f, 19.31f)
        lineTo(19.73f, 23.56f)
        lineTo(21.14f, 22.15f)
        lineTo(1.41f, 1.27f)
        close()
        
        moveTo(6.9f, 9.6f)
        lineTo(9.02f, 11.72f)
        curveTo(9.01f, 11.81f, 9f, 11.9f, 9f, 12f)
        curveTo(9f, 13.66f, 10.34f, 15f, 12f, 15f)
        curveTo(12.1f, 15f, 12.19f, 14.99f, 12.28f, 14.99f)
        lineTo(14.4f, 17.1f)
        curveTo(13.43f, 17.58f, 12.74f, 17f, 12f, 17f)
        curveTo(9.24f, 17f, 7f, 14.76f, 7f, 12f)
        curveTo(7f, 11.26f, 7.58f, 10.57f, 8.1f, 10.1f)
        lineTo(6.9f, 9.6f)
        close()
        
        moveTo(11.9f, 5f)
        curveTo(11.83f, 5f, 11.76f, 5f, 11.7f, 5.01f)
        lineTo(14.9f, 8.2f)
        curveTo(14.9f, 8.13f, 15f, 8.06f, 15f, 8f)
        curveTo(15f, 6.34f, 13.66f, 5f, 12f, 5f)
        close()
    }.build()
}
