package com.hkweather.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.hkweather.app.ui.theme.GrokColors

@Composable
fun TechBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(GrokColors.Canvas),
        content = content,
    )
}

@Composable
fun HudPanel(
    modifier: Modifier = Modifier,
    accent: Color = GrokColors.Divider,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(GrokColors.Surface1)
            .border(1.dp, accent, RoundedCornerShape(16.dp)),
        content = content,
    )
}

fun formatReadingValue(value: Int, unit: String?): String = when (unit?.lowercase()) {
    "percent" -> "$value%"
    "c" -> "$value°C"
    else -> if (unit.isNullOrBlank()) "$value" else "$value $unit"
}

fun formatUpdateTime(raw: String?): String {
    if (raw.isNullOrBlank()) return "--"
    return raw.replace("T", " · ").replace("+08:00", " HKT")
}
