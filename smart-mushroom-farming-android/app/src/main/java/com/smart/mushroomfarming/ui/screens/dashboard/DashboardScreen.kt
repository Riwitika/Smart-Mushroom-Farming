package com.smart.mushroomfarming.ui.screens.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import com.smart.mushroomfarming.ui.components.History
import com.smart.mushroomfarming.ui.components.Prediction
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.smart.mushroomfarming.domain.model.FarmingTelemetry
import com.smart.mushroomfarming.ui.components.Humidity
import com.smart.mushroomfarming.ui.components.LightIntensity
import com.smart.mushroomfarming.ui.components.MushroomCard
import com.smart.mushroomfarming.ui.components.Ph
import com.smart.mushroomfarming.ui.components.Prediction
import com.smart.mushroomfarming.ui.components.Temperature
import com.smart.mushroomfarming.ui.components.Ventilation
import com.smart.mushroomfarming.ui.screens.auth.AuthViewModel
import com.smart.mushroomfarming.ui.theme.spacing
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToPrediction: () -> Unit,
    onNavigateToPredictionHistory: () -> Unit,
    onNavigateToRecommendations: () -> Unit,
    onNavigateToSettings: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val dashboardState by viewModel.uiState.collectAsState()
    val user = authViewModel.getCurrentUser()
    var selectedTab by remember { mutableStateOf(0) }
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Smart Mushroom Farming",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                actions = {
                    IconButton(
                        onClick = {
                            authViewModel.logout {
                                onNavigateToLogin()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Logout",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Tab Header Selection
            TabRow(selectedTabIndex = selectedTab) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Overview", fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Analytics & Trends", fontWeight = FontWeight.Bold) }
                )
            }

            if (dashboardState.isLoading) {
                // Skeleton loading interface
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(MaterialTheme.spacing.medium),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
                ) {
                    ShimmerPlaceholder(modifier = Modifier.fillMaxWidth().height(120.dp))
                    ShimmerPlaceholder(modifier = Modifier.fillMaxWidth().height(200.dp))
                    ShimmerPlaceholder(modifier = Modifier.fillMaxWidth().height(160.dp))
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                ) {
                    // TAB 0: OVERVIEW
                    if (selectedTab == 0) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(scrollState)
                                .padding(MaterialTheme.spacing.medium),
                            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
                        ) {
                            // Profile Greeting Card
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer
                                ),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(MaterialTheme.spacing.medium)
                                ) {
                                    Text(
                                        text = "Hello, ${user?.displayName ?: "Mushroom Farmer"}",
                                        style = MaterialTheme.typography.headlineSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                    user?.email?.let { email ->
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = email,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault()).format(Date()),
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                                    )
                                }
                            }

                            // Telemetry Grid
                            Text(
                                text = "Environment Telemetry Monitoring",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )

                            val telemetry = dashboardState.currentTelemetry
                            telemetry?.let {
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
                                    ) {
                                        SensorCard(
                                            modifier = Modifier.weight(1f),
                                            title = "Temperature",
                                            value = "${it.temperature} °C",
                                            label = if (it.temperature in 22.0..26.0) "Optimal" else "Spike",
                                            color = if (it.temperature in 22.0..26.0) Color(0xFF4CAF50) else Color(0xFFF44336),
                                            icon = Icons.Filled.Temperature
                                        )
                                        SensorCard(
                                            modifier = Modifier.weight(1f),
                                            title = "Humidity",
                                            value = "${it.humidity} %",
                                            label = if (it.humidity in 80.0..90.0) "Optimal" else "Alert",
                                            color = if (it.humidity in 80.0..90.0) Color(0xFF2196F3) else Color(0xFFFF9800),
                                            icon = Icons.Filled.Humidity
                                        )
                                    }
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
                                    ) {
                                        SensorCard(
                                            modifier = Modifier.weight(1f),
                                            title = "Ventilation",
                                            value = it.ventilation,
                                            label = if (it.ventilation == "High") "Good Air Flow" else "Stagnant",
                                            color = if (it.ventilation == "High") Color(0xFF009688) else Color(0xFFFF9800),
                                            icon = Icons.Filled.Ventilation
                                        )
                                        SensorCard(
                                            modifier = Modifier.weight(1f),
                                            title = "Light Intensity",
                                            value = it.lightIntensity,
                                            label = "Suitable",
                                            color = Color(0xFFFFC107),
                                            icon = Icons.Filled.LightIntensity
                                        )
                                    }
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
                                    ) {
                                        SensorCard(
                                            modifier = Modifier.weight(1f),
                                            title = "pH Level",
                                            value = "${it.ph}",
                                            label = if (it.ph in 6.0..7.0) "Optimal" else "Basic/Acidic",
                                            color = if (it.ph in 6.0..7.0) Color(0xFF9C27B0) else Color(0xFFFF5722),
                                            icon = Icons.Filled.Ph
                                        )
                                    }
                                }
                            }

                            // Quick Actions
                            Text(
                                text = "Quick Actions",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )

                            MushroomCard(modifier = Modifier.fillMaxWidth()) {
                                Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
                                    ) {
                                        ActionCard(
                                            modifier = Modifier.weight(1f),
                                            title = "AI Predict",
                                            icon = Icons.Filled.Prediction,
                                            onClick = onNavigateToPrediction
                                        )
                                        ActionCard(
                                            modifier = Modifier.weight(1f),
                                            title = "History Log",
                                            icon = Icons.Filled.History,
                                            onClick = onNavigateToPredictionHistory
                                        )
                                    }
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
                                    ) {
                                        ActionCard(
                                            modifier = Modifier.weight(1f),
                                            title = "Cultivation Guide",
                                            icon = Icons.Default.Star,
                                            onClick = onNavigateToRecommendations
                                        )
                                        ActionCard(
                                            modifier = Modifier.weight(1f),
                                            title = "Preferences",
                                            icon = Icons.Default.Settings,
                                            onClick = onNavigateToSettings
                                        )
                                    }
                                }
                            }

                            // Farm Insights
                            Text(
                                text = "AI Cultivation Insights",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )

                            dashboardState.insights.forEach { insight ->
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(MaterialTheme.spacing.medium),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Warning,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Text(
                                            text = insight,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // TAB 1: ANALYTICS & TRENDS
                    if (selectedTab == 1) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(scrollState)
                                .padding(MaterialTheme.spacing.medium),
                            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
                        ) {
                            // Metrics Grid M3 Cards
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                MetricCard(
                                    modifier = Modifier.weight(1f),
                                    title = "Total runs",
                                    value = "${dashboardState.totalPredictions}",
                                    color = MaterialTheme.colorScheme.primary
                                )
                                MetricCard(
                                    modifier = Modifier.weight(1f),
                                    title = "Last run",
                                    value = dashboardState.lastPredictionDate.split(" - ").firstOrNull() ?: "N/A",
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                MetricCard(
                                    modifier = Modifier.weight(1f),
                                    title = "Healthy",
                                    value = "${dashboardState.healthyPredictions}",
                                    color = Color(0xFF4CAF50)
                                )
                                MetricCard(
                                    modifier = Modifier.weight(1f),
                                    title = "Moderate",
                                    value = "${dashboardState.moderatePredictions}",
                                    color = Color(0xFFFF9800)
                                )
                                MetricCard(
                                    modifier = Modifier.weight(1f),
                                    title = "High Risk",
                                    value = "${dashboardState.highRiskPredictions}",
                                    color = Color(0xFFF44336)
                                )
                            }

                            // 1. Pie Chart Card
                            Text(
                                text = "Possibility breakdown (Pie chart)",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                border = CardDefaults.outlinedCardBorder()
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    CustomPieChart(
                                        healthy = dashboardState.healthyPredictions,
                                        moderate = dashboardState.moderatePredictions,
                                        highRisk = dashboardState.highRiskPredictions,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }

                            // 2. Line Chart Card
                            Text(
                                text = "Risk profile trend (Line chart)",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                border = CardDefaults.outlinedCardBorder()
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    CustomLineChart(
                                        predictions = dashboardState.recentPredictions,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }

                            // 3. Activity Timeline
                            Text(
                                text = "Recent Activity Timeline",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            ActivityTimeline(
                                items = dashboardState.recentPredictions
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ShimmerPlaceholder(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition()
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.LightGray.copy(alpha = alpha))
    )
}

@Composable
fun SensorCard(
    title: String,
    value: String,
    label: String,
    color: Color,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(
            modifier = Modifier.padding(MaterialTheme.spacing.medium),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(color.copy(alpha = 0.15f))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = color,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun ActionCard(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        )
    ) {
        Row(
            modifier = Modifier.padding(MaterialTheme.spacing.medium),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(MaterialTheme.colorScheme.primary, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun MetricCard(
    title: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.12f)),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = color,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun CustomPieChart(
    healthy: Int,
    moderate: Int,
    highRisk: Int,
    modifier: Modifier = Modifier
) {
    val total = healthy + moderate + highRisk
    if (total == 0) {
        Box(
            modifier = modifier.fillMaxWidth().height(100.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No metrics logged. Run predictions first.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
        return
    }

    val healthyAngle = 360f * (healthy.toFloat() / total)
    val moderateAngle = 360f * (moderate.toFloat() / total)
    val highRiskAngle = 360f * (highRisk.toFloat() / total)

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Canvas(
            modifier = Modifier.size(100.dp)
        ) {
            var startAngle = 0f
            if (highRiskAngle > 0f) {
                drawArc(
                    color = Color(0xFFF44336),
                    startAngle = startAngle,
                    sweepAngle = highRiskAngle,
                    useCenter = true
                )
                startAngle += highRiskAngle
            }
            if (moderateAngle > 0f) {
                drawArc(
                    color = Color(0xFFFF9800),
                    startAngle = startAngle,
                    sweepAngle = moderateAngle,
                    useCenter = true
                )
                startAngle += moderateAngle
            }
            if (healthyAngle > 0f) {
                drawArc(
                    color = Color(0xFF4CAF50),
                    startAngle = startAngle,
                    sweepAngle = healthyAngle,
                    useCenter = true
                )
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            LegendItem(color = Color(0xFF4CAF50), label = "Healthy: $healthy (${(healthy.toFloat()/total*100).toInt()}%)")
            LegendItem(color = Color(0xFFFF9800), label = "Moderate: $moderate (${(moderate.toFloat()/total*100).toInt()}%)")
            LegendItem(color = Color(0xFFF44336), label = "High Risk: $highRisk (${(highRisk.toFloat()/total*100).toInt()}%)")
        }
    }
}

@Composable
fun LegendItem(color: Color, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color, shape = RoundedCornerShape(2.dp))
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun CustomLineChart(
    predictions: List<FarmingTelemetry>,
    modifier: Modifier = Modifier
) {
    if (predictions.isEmpty()) {
        Box(
            modifier = modifier.fillMaxWidth().height(100.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No analytics data available.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
        return
    }

    val points = predictions.take(7).reversed()
    val values = points.map { item ->
        when (item.diseaseGrowthPossibility.lowercase()) {
            "high" -> 2f
            "moderate" -> 1f
            else -> 0f
        }
    }

    val primaryColor = MaterialTheme.colorScheme.primary
    val gridColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(130.dp)
    ) {
        val width = size.width
        val height = size.height
        val padding = 20f

        val chartWidth = width - (padding * 2)
        val chartHeight = height - (padding * 2)

        val ySteps = 3
        for (i in 0 until ySteps) {
            val yVal = padding + chartHeight * (1f - i.toFloat() / (ySteps - 1))
            drawLine(
                color = gridColor,
                start = androidx.compose.ui.geometry.Offset(padding, yVal),
                end = androidx.compose.ui.geometry.Offset(width - padding, yVal),
                strokeWidth = 2f
            )
        }

        if (values.size > 1) {
            val xStep = chartWidth / (values.size - 1)
            val path = androidx.compose.ui.graphics.Path()

            for (i in values.indices) {
                val x = padding + i * xStep
                val normalizedY = values[i] / 2f
                val y = padding + chartHeight * (1f - normalizedY)

                if (i == 0) {
                    path.moveTo(x, y)
                } else {
                    path.lineTo(x, y)
                }

                drawCircle(
                    color = when (points[i].diseaseGrowthPossibility.lowercase()) {
                        "high" -> Color(0xFFF44336)
                        "moderate" -> Color(0xFFFF9800)
                        else -> Color(0xFF4CAF50)
                    },
                    radius = 8f,
                    center = androidx.compose.ui.geometry.Offset(x, y)
                )
            }

            drawPath(
                path = path,
                color = primaryColor,
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4f)
            )
        }
    }
}

@Composable
fun ActivityTimeline(
    items: List<FarmingTelemetry>,
    modifier: Modifier = Modifier
) {
    if (items.isEmpty()) {
        Text(
            text = "No recent operations.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )
        return
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items.take(10).forEach { item ->
            val dateString = SimpleDateFormat("MMM dd, hh:mm a", Locale.getDefault()).format(Date(item.timestamp))
            val badgeColor = when (item.diseaseGrowthPossibility.lowercase()) {
                "high" -> Color(0xFFF44336)
                "moderate" -> Color(0xFFFF9800)
                else -> Color(0xFF4CAF50)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Vertical Timeline Node
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(badgeColor, shape = CircleShape)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Disease Risk: ${item.diseaseGrowthPossibility}",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Temp: ${item.temperature}°C, Humidity: ${item.humidity}%, Date: $dateString",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
}
