package com.smart.mushroomfarming.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// Custom shapes inspired by premium Airbnb and Samsung Health style rounding
val Shapes = Shapes(
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(16.dp), // Extra soft corners for cards and inputs
    large = RoundedCornerShape(24.dp), // Smooth panels
    extraLarge = RoundedCornerShape(32.dp)
)
