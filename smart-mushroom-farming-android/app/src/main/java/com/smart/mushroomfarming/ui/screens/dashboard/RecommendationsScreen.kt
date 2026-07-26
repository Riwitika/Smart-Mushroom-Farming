package com.smart.mushroomfarming.ui.screens.dashboard

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.smart.mushroomfarming.ui.components.Humidity
import com.smart.mushroomfarming.ui.components.LightIntensity
import com.smart.mushroomfarming.ui.components.MushroomCard
import com.smart.mushroomfarming.ui.components.Ph
import com.smart.mushroomfarming.ui.components.Prediction
import com.smart.mushroomfarming.ui.components.Temperature
import com.smart.mushroomfarming.ui.components.Ventilation
import com.smart.mushroomfarming.ui.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecommendationsScreen(
    onNavigateBack: () -> Unit
) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "AI Cultivation Guide",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
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
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(MaterialTheme.spacing.medium)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
        ) {
            // 1. Introduction Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Smart Mushroom Farming & AI",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Mushroom cultivation requires precise environmental control. Mushrooms lack a protective cuticle and are highly sensitive to shifts in telemetry parameters. Our predictive AI model evaluates temperature, humidity, ventilation, light, and pH to calculate disease growth probability levels, helping you prevent mold outbreaks and optimize crop yields in real time.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                    )
                }
            }

            // 2. Ideal Growing Conditions Cards
            Text(
                text = "Ideal Growing Conditions",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                ConditionRowCard(
                    title = "Temperature",
                    range = "20.0°C – 28.0°C",
                    description = "Optimal range for fast mycelial growth. Cold slows growth, while excessive heat (>30°C) triggers green mold (Trichoderma).",
                    icon = Icons.Filled.Temperature,
                    color = Color(0xFF4CAF50)
                )
                ConditionRowCard(
                    title = "Humidity",
                    range = "80.0% – 95.0%",
                    description = "Crucial for primordia pinheads pinning and fruiting. Low humidity dries out pins, while over-watering pools and rot pins.",
                    icon = Icons.Filled.Humidity,
                    color = Color(0xFF2196F3)
                )
                ConditionRowCard(
                    title = "Ventilation",
                    range = "Medium to High",
                    description = "Clears toxic CO2 buildup. Low airflow leads to long leggy stems and tiny caps, while high air speeds dry out substrate.",
                    icon = Icons.Filled.Ventilation,
                    color = Color(0xFF009688)
                )
                ConditionRowCard(
                    title = "Light Intensity",
                    range = "Low / Diffused",
                    description = "Mushrooms need no direct light for photosynthesis. Keep lights low to guide cap growth direction without drying pinning pins.",
                    icon = Icons.Filled.LightIntensity,
                    color = Color(0xFFFFC107)
                )
                ConditionRowCard(
                    title = "Substrate pH",
                    range = "6.0 – 7.5 pH",
                    description = "Optimal nutrient absorption is obtained near neutral pH. Acidic conditions increase competitor mold infestation rates.",
                    icon = Icons.Filled.Ph,
                    color = Color(0xFF9C27B0)
                )
            }

            // 3. Mushroom Growth Stages
            Text(
                text = "Mushroom Growth Stages",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            MushroomCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    StageItem(
                        stage = "1. Substrate Preparation",
                        description = "Sterilizing bulk substrate (sawdust/straw) to clear competing fungi spores before inoculation.",
                        icon = Icons.Default.Info
                    )
                    StageItem(
                        stage = "2. Spawning (Inoculation)",
                        description = "Mixing mushroom grain spawn thoroughly into cooled substrate inside a clean, sterile workspace.",
                        icon = Icons.Default.PlayArrow
                    )
                    StageItem(
                        stage = "3. Incubation (Colonization)",
                        description = "Placing spawn runs in dark, warm rooms. Mycelium colonizes substrate, turning it completely white.",
                        icon = Icons.Default.Star
                    )
                    StageItem(
                        stage = "4. Pinning (Primordia)",
                        description = "Inducing pinning by introducing fresh air flow, lowering temperature, and boosting relative humidity.",
                        icon = Icons.Filled.Prediction
                    )
                    StageItem(
                        stage = "5. Fruiting (Growth)",
                        description = "Rapid expansion of mushrooms. Maintain high humidity and ventilation for clean, healthy caps.",
                        icon = Icons.Default.Star
                    )
                    StageItem(
                        stage = "6. Harvesting",
                        description = "Picking caps right before the veils break, ensuring multiple flush cycles are preserved.",
                        icon = Icons.Default.Check
                    )
                }
            }

            // 4. Common Problems
            Text(
                text = "Common Problems & Solutions",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                ProblemCard(
                    problem = "Dry substrate (Low Humidity)",
                    cause = "Insufficent misting or excessive direct ventilation drafts.",
                    solution = "Turn on humidifier systems, decrease airflow speed, and spray casing soil with fine mist."
                )
                ProblemCard(
                    problem = "Mushroom heat stress (High Temp)",
                    cause = "High ambient temperature or heat released during mycelial metabolism.",
                    solution = "Turn on auxiliary coolers, move spawn bags farther apart, and increase ventilation rates."
                )
                ProblemCard(
                    problem = "Green Mold contamination (Trichoderma)",
                    cause = "Poor sterilization practices, high humidity/heat, or acidic substrate.",
                    solution = "Discard contaminated blocks immediately, sterilize tools, and apply lime to adjust soil pH to 7.2."
                )
                ProblemCard(
                    problem = "Thin leggy stems / small caps",
                    cause = "Poor air ventilation causing elevated carbon dioxide accumulation.",
                    solution = "Increase fresh air exchanges (FAE), open ventilation slots, and turn on circulation fans."
                )
            }

            // 5. AI Recommendation Guide
            Text(
                text = "AI Prediction Risk levels",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    RiskGuideItem(level = "🟢 LOW RISK", desc = "Environmental conditions are optimal. No action required. Continue standard monitoring protocols.")
                    RiskGuideItem(level = "🟡 MODERATE RISK", desc = "Minor parameter shifts detected. Check telemetry logs (e.g. low humidity/high temp). Adjust venting or misting cycles.")
                    RiskGuideItem(level = "🔴 HIGH RISK", desc = "CRITICAL telemetry thresholds breached. High probability of crop failure or green mold contamination. Inspect IoT nodes immediately!")
                }
            }

            // 6. Best Practices Checklist
            Text(
                text = "Cultivation Best Practices",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            MushroomCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    ChecklistItem("Keep workspaces, tools, and hands thoroughly sanitized with 70% isopropyl alcohol.")
                    ChecklistItem("Maintain isolated incubation rooms to prevent crossover mold contamination.")
                    ChecklistItem("Discard contaminated substrate blocks far from the grow facility immediately.")
                    ChecklistItem("Label spawn bags with dates and strains to track incubation cycles.")
                }
            }

            // 7. Daily Monitoring Checklist
            Text(
                text = "Daily Monitoring Checklist",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            MushroomCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    ChecklistItem("Review Dashboard telemetry parameters twice daily for spikes.")
                    ChecklistItem("Inspect substrate bags for healthy white mycelium growth.")
                    ChecklistItem("Confirm misting nozzle networks are clear of blockages.")
                    ChecklistItem("Check exhaust fan inlets for proper carbon dioxide extraction.")
                }
            }

            // 8. Helpful Tips Cards
            Text(
                text = "Pro Cultivator Tips",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Tip: When pinning begins, shock colonization runs by dropping the room temp by 3–5°C. This temperature shock triggers pinning pins to break substrate surfaces simultaneously.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun ConditionRowCard(
    title: String,
    range: String,
    description: String,
    icon: ImageVector,
    color: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(color.copy(alpha = 0.15f), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = range,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelMedium,
                        color = color
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                )
            }
        }
    }
}

@Composable
fun StageItem(
    stage: String,
    description: String,
    icon: ImageVector
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(MaterialTheme.colorScheme.primaryContainer, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(16.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = stage,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun ProblemCard(
    problem: String,
    cause: String,
    solution: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = problem,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Possible Cause: $cause",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "AI Resolution: $solution",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun RiskGuideItem(level: String, desc: String) {
    Column {
        Text(
            text = level,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.labelMedium
        )
        Text(
            text = desc,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun ChecklistItem(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
        )
    }
}
