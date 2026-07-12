package com.hkweather.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import com.hkweather.app.ui.theme.WeatherAppColors

@Composable
fun HomeWeatherBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit = {},
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        WeatherAppColors.GradientTop,
                        WeatherAppColors.GradientMid,
                        WeatherAppColors.GradientBottom,
                    ),
                ),
            ),
        content = content,
    )
}

fun formatForecastDate(raw: String): String {
    if (raw.length != 8) return raw
    val monthNames = listOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December",
    )
    val month = raw.substring(4, 6).toIntOrNull() ?: return raw
    val day = raw.substring(6, 8).toIntOrNull() ?: return raw
    val monthLabel = monthNames.getOrElse(month - 1) { "Month" }
    return "$monthLabel, $day"
}

fun shortWeekday(week: String): String = week.take(3)
