package com.hkweather.app.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hkweather.app.ui.theme.GrokColors

@Composable
fun NeonTemperatureDisplay(
    temperature: Int,
    unit: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "$temperature",
            style = MaterialTheme.typography.displayLarge,
            color = GrokColors.TextPrimary,
        )
        Text(
            text = "°$unit",
            style = MaterialTheme.typography.titleLarge.copy(fontSize = 28.sp),
            color = GrokColors.TextSecondary,
            modifier = Modifier.offset(y = (-12).dp),
        )
    }
}

@Composable
fun TechHeaderLabel(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = subtitle,
            style = MaterialTheme.typography.labelLarge,
            color = GrokColors.TextSecondary,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.headlineLarge,
            color = GrokColors.TextPrimary,
        )
    }
}

@Composable
fun TechLocationChip(
    locationName: String,
    usingDeviceLocation: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(999.dp))
            .background(GrokColors.Surface2)
            .border(1.dp, GrokColors.Divider, RoundedCornerShape(999.dp))
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = locationName,
            style = MaterialTheme.typography.titleLarge,
            color = GrokColors.TextPrimary,
        )
        if (usingDeviceLocation) {
            Text(
                text = "GPS",
                style = MaterialTheme.typography.labelLarge,
                color = GrokColors.LinkBlue,
                modifier = Modifier
                    .clip(RoundedCornerShape(999.dp))
                    .background(GrokColors.Surface3)
                    .padding(horizontal = 8.dp, vertical = 3.dp),
            )
        }
    }
}

@Composable
fun TechStatCard(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    HudPanel(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(icon, contentDescription = null, tint = GrokColors.TextSecondary, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = GrokColors.TextSecondary,
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                color = GrokColors.TextPrimary,
            )
        }
    }
}

@Composable
fun TechBottomBar(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(999.dp))
            .background(GrokColors.Surface1)
            .border(1.dp, GrokColors.Divider, RoundedCornerShape(999.dp)),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            content = content,
        )
    }
}

@Composable
fun TechBottomBarItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    showBadge: Boolean = false,
) {
    val tint by animateColorAsState(
        targetValue = if (selected) GrokColors.Canvas else GrokColors.TextSecondary,
        label = "tabTint",
    )
    val bg by animateColorAsState(
        targetValue = if (selected) GrokColors.White else Color.Transparent,
        label = "tabBg",
    )

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .clickable(onClick = onClick)
            .background(bg)
            .padding(horizontal = 10.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box {
            Icon(icon, contentDescription = label, tint = tint, modifier = Modifier.size(20.dp))
            if (showBadge && !selected) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .align(Alignment.TopEnd)
                        .offset(x = 2.dp, y = (-2).dp)
                        .background(GrokColors.LinkBlue, CircleShape),
                )
            }
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge.copy(fontSize = 10.sp),
            color = tint,
        )
    }
}

@Composable
fun TechRefreshButton(
    isRefreshing: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(GrokColors.White)
            .clickable(enabled = !isRefreshing, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        if (isRefreshing) {
            CircularProgressIndicator(
                modifier = Modifier.size(18.dp),
                color = GrokColors.Canvas,
                strokeWidth = 2.dp,
            )
        } else {
            Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = GrokColors.Canvas, modifier = Modifier.size(18.dp))
        }
    }
}

@Composable
fun TechForecastCard(
    title: String,
    body: String,
    accent: Color = GrokColors.Divider,
    modifier: Modifier = Modifier,
) {
    HudPanel(modifier = modifier.fillMaxWidth(), accent = accent) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = GrokColors.TextPrimary,
            )
            Text(
                text = body,
                style = MaterialTheme.typography.bodyLarge,
                color = GrokColors.TextSecondary,
            )
        }
    }
}

@Composable
fun WeatherIconGlow(
    iconCode: Int,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        coil.compose.AsyncImage(
            model = com.hkweather.app.data.api.HkoApiClient.weatherIconUrl(iconCode),
            contentDescription = "Weather icon",
            modifier = Modifier.size(96.dp),
        )
    }
}
