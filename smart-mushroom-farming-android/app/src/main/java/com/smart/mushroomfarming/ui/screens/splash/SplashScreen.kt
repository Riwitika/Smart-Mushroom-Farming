package com.smart.mushroomfarming.ui.screens.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.smart.mushroomfarming.ui.screens.auth.AuthViewModel
import com.smart.mushroomfarming.ui.theme.spacing
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onNavigateToDashboard: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val scale = remember { Animatable(0f) }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(key1 = true) {
        // Run animations in parallel
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            )
        )
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 800
            )
        )
        // Keep splash screen visible for 1.5 seconds
        delay(1500)
        
        // Route according to active session
        if (viewModel.isUserLoggedIn()) {
            onNavigateToDashboard()
        } else {
            onNavigateToLogin()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0C160C)), // Always deep forest green for splash start
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Elegant Canvas-drawn Geometric Minimalist Mushroom Logo
            Canvas(
                modifier = Modifier
                    .size(120.dp)
                    .scale(scale.value)
                    .alpha(alpha.value)
            ) {
                val width = size.width
                val height = size.height
                
                // Draw stem (rounded rect in middle bottom)
                val stemWidth = width * 0.25f
                val stemHeight = height * 0.45f
                val stemX = (width - stemWidth) / 2
                val stemY = height * 0.5f
                drawRoundRect(
                    color = Color(0xFFC8E6C9), // Soft green-white stem
                    topLeft = Offset(stemX, stemY),
                    size = Size(stemWidth, stemHeight),
                    cornerRadius = CornerRadius(20f, 20f)
                )

                // Draw cap (custom curved path for top dome)
                val capPath = Path().apply {
                    val capStartY = height * 0.55f
                    moveTo(width * 0.1f, capStartY)
                    // Control points for organic but neat mushroom cap dome
                    cubicTo(
                        width * 0.1f, height * 0.1f,
                        width * 0.9f, height * 0.1f,
                        width * 0.9f, capStartY
                    )
                    lineTo(width * 0.1f, capStartY)
                    close()
                }
                drawPath(
                    path = capPath,
                    color = Color(0xFF81C784), // Earthy light green cap
                    style = Fill
                )
                
                // Draw decorative dots on mushroom cap
                drawCircle(
                    color = Color(0xFF0C160C),
                    radius = width * 0.04f,
                    center = Offset(width * 0.35f, height * 0.35f)
                )
                drawCircle(
                    color = Color(0xFF0C160C),
                    radius = width * 0.05f,
                    center = Offset(width * 0.5f, height * 0.25f)
                )
                drawCircle(
                    color = Color(0xFF0C160C),
                    radius = width * 0.04f,
                    center = Offset(width * 0.65f, height * 0.38f)
                )
            }
            
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
            
            Text(
                text = "SMART MUSHROOM",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 4.sp
                ),
                color = Color(0xFFE2E3DE),
                modifier = Modifier.alpha(alpha.value)
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = "FARMING AI",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Normal,
                    letterSpacing = 6.sp
                ),
                color = Color(0xFF81C784).copy(alpha = 0.8f),
                modifier = Modifier.alpha(alpha.value)
            )
        }
    }
}
