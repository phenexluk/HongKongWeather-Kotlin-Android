package com.hkweather.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hkweather.app.data.model.WeatherSnapshot
import com.hkweather.app.ui.components.HudPanel
import com.hkweather.app.ui.components.HomeWeatherBackground
import com.hkweather.app.ui.components.TechBackground
import com.hkweather.app.ui.components.TechBottomBar
import com.hkweather.app.ui.components.TechBottomBarItem
import com.hkweather.app.ui.components.TechForecastCard
import com.hkweather.app.ui.components.TechHeaderLabel
import com.hkweather.app.ui.components.TechRefreshButton
import com.hkweather.app.ui.components.formatUpdateTime
import com.hkweather.app.ui.home.HomeNowTab
import com.hkweather.app.ui.map.TemperatureMapView
import com.hkweather.app.ui.theme.GrokColors

private enum class WeatherTab(val label: String) {
    Now("Now"),
    Map("Map"),
    Alerts("Alerts"),
    Forecast("Forecast"),
}

@Composable
fun WeatherScreen(
    uiState: WeatherUiState,
    onRefresh: () -> Unit,
) {
    var selectedTab by rememberSaveable { mutableStateOf(WeatherTab.Now) }

    Box(modifier = Modifier.fillMaxSize().background(GrokColors.Canvas)) {
        when (selectedTab) {
            WeatherTab.Now -> HomeWeatherBackground(modifier = Modifier.fillMaxSize()) {}
            WeatherTab.Map -> Box(Modifier.fillMaxSize().background(GrokColors.Canvas))
            else -> TechBackground(modifier = Modifier.fillMaxSize()) {}
        }

        when {
            uiState.isLoading && uiState.weather == null -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = GrokColors.TextPrimary,
                )
            }

            uiState.errorMessage != null && uiState.weather == null -> {
                ErrorState(
                    message = uiState.errorMessage,
                    onRetry = onRefresh,
                    modifier = Modifier.align(Alignment.Center),
                )
            }

            uiState.weather != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                        .padding(bottom = 96.dp),
                ) {
                    if (uiState.isLoading) {
                        LinearLoadingBar()
                    }

                    when (selectedTab) {
                        WeatherTab.Now -> HomeNowTab(
                            weather = uiState.weather,
                            usingDeviceLocation = uiState.usingDeviceLocation,
                        )
                        WeatherTab.Map -> MapTab(
                            weather = uiState.weather,
                            userLatitude = uiState.userLatitude,
                            userLongitude = uiState.userLongitude,
                        )
                        WeatherTab.Alerts -> AlertsTab(weather = uiState.weather)
                        WeatherTab.Forecast -> ForecastTab(weather = uiState.weather)
                    }
                }
            }
        }

        TechBottomBar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 14.dp),
        ) {
            TechBottomBarItem(
                icon = Icons.Outlined.Home,
                label = WeatherTab.Now.label,
                selected = selectedTab == WeatherTab.Now,
                onClick = { selectedTab = WeatherTab.Now },
            )
            TechBottomBarItem(
                icon = Icons.Outlined.Map,
                label = WeatherTab.Map.label,
                selected = selectedTab == WeatherTab.Map,
                onClick = { selectedTab = WeatherTab.Map },
            )
            TechBottomBarItem(
                icon = Icons.Outlined.WarningAmber,
                label = WeatherTab.Alerts.label,
                selected = selectedTab == WeatherTab.Alerts,
                onClick = { selectedTab = WeatherTab.Alerts },
                showBadge = uiState.weather?.let {
                    it.activeWarnings.isNotEmpty() || it.warningMessages.isNotEmpty()
                } ?: false,
            )
            TechBottomBarItem(
                icon = Icons.Outlined.Cloud,
                label = WeatherTab.Forecast.label,
                selected = selectedTab == WeatherTab.Forecast,
                onClick = { selectedTab = WeatherTab.Forecast },
            )
            TechRefreshButton(isRefreshing = uiState.isLoading, onClick = onRefresh)
        }
    }
}

@Composable
private fun MapTab(
    weather: WeatherSnapshot,
    userLatitude: Double?,
    userLongitude: Double?,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(GrokColors.Surface1),
    ) {
        TemperatureMapView(
            stations = weather.stationTemperatures,
            highlightedPlace = weather.locationName,
            userLatitude = userLatitude,
            userLongitude = userLongitude,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
private fun AlertsTab(weather: WeatherSnapshot) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        TechHeaderLabel(title = "Warnings", subtitle = "HKO")

        if (weather.activeWarnings.isEmpty() && weather.warningMessages.isEmpty()) {
            HudPanel(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Icon(Icons.Outlined.Cloud, null, tint = GrokColors.TextSecondary, modifier = Modifier.size(40.dp))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("All clear", style = MaterialTheme.typography.titleLarge, color = GrokColors.TextPrimary)
                    Text(
                        "No active weather warnings",
                        style = MaterialTheme.typography.bodyLarge,
                        color = GrokColors.TextSecondary,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        } else {
            weather.activeWarnings.forEach { warning ->
                HudPanel(modifier = Modifier.fillMaxWidth(), accent = GrokColors.Error.copy(alpha = 0.5f)) {
                    Row(
                        modifier = Modifier.padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(Icons.Default.Warning, null, tint = GrokColors.Error, modifier = Modifier.size(24.dp))
                        Column(modifier = Modifier.padding(start = 14.dp)) {
                            Text(
                                warning.name,
                                style = MaterialTheme.typography.titleLarge,
                                color = GrokColors.TextPrimary,
                                fontWeight = FontWeight.Normal,
                            )
                            warning.updateTime?.let {
                                Text(
                                    formatUpdateTime(it),
                                    style = MaterialTheme.typography.labelLarge,
                                    color = GrokColors.TextSecondary,
                                )
                            }
                        }
                    }
                }
            }
            weather.warningMessages.forEach { message ->
                TechForecastCard(title = "Detail", body = message)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun ForecastTab(weather: WeatherSnapshot) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        TechHeaderLabel(title = "Forecast", subtitle = "HKO")

        weather.forecastDesc?.let { TechForecastCard(title = "Today", body = it) }
        weather.generalSituation?.let { TechForecastCard(title = "General", body = it) }
        weather.tcInfo?.takeIf { it.isNotBlank() }?.let {
            TechForecastCard(title = "Tropical cyclone", body = it, accent = GrokColors.LinkBlue.copy(alpha = 0.4f))
        }
        weather.outlook?.let { TechForecastCard(title = "Outlook", body = it) }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun LinearLoadingBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(GrokColors.Divider),
    ) {
        CircularProgressIndicator(
            modifier = Modifier.fillMaxWidth().height(1.dp),
            color = GrokColors.TextPrimary,
            strokeWidth = 1.dp,
        )
    }
}

@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(message, style = MaterialTheme.typography.bodyLarge, color = GrokColors.TextPrimary, textAlign = TextAlign.Center)
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(GrokColors.Surface2)
                .clickable(onClick = onRetry)
                .padding(16.dp),
        ) {
            Icon(Icons.Default.Refresh, "Retry", tint = GrokColors.TextPrimary)
        }
    }
}
