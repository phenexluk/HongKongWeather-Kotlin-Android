package com.hkweather.app.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.outlined.Air
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.hkweather.app.data.api.HkoApiClient
import com.hkweather.app.data.model.DailyForecastDay
import com.hkweather.app.data.model.WeatherSnapshot
import com.hkweather.app.ui.components.formatForecastDate
import com.hkweather.app.ui.components.formatReadingValue
import com.hkweather.app.ui.components.shortWeekday
import com.hkweather.app.ui.theme.WeatherAppColors

@Composable
fun HomeNowTab(
    weather: WeatherSnapshot,
    usingDeviceLocation: Boolean,
) {
    val today = weather.dailyForecast.firstOrNull()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp),
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                Icons.Default.LocationOn,
                contentDescription = null,
                tint = WeatherAppColors.AccentYellow,
                modifier = Modifier.size(18.dp),
            )
            Text(
                text = weather.locationName,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                color = WeatherAppColors.TextPrimary,
                modifier = Modifier.padding(start = 6.dp),
            )
            if (usingDeviceLocation) {
                Text(
                    text = " · GPS",
                    style = MaterialTheme.typography.labelLarge,
                    color = WeatherAppColors.TextMuted,
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            weather.iconCode?.let { icon ->
                AsyncImage(
                    model = HkoApiClient.weatherIconUrl(icon),
                    contentDescription = "Weather",
                    modifier = Modifier.size(140.dp),
                )
            }

            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = "${weather.temperature}",
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontSize = 96.sp,
                        fontWeight = FontWeight.Light,
                    ),
                    color = WeatherAppColors.TextPrimary,
                )
                Text(
                    text = "°",
                    style = MaterialTheme.typography.headlineLarge.copy(fontSize = 40.sp),
                    color = WeatherAppColors.AccentYellow,
                    modifier = Modifier.padding(top = 12.dp),
                )
            }

            Text(
                text = today?.forecastWeather ?: weather.forecastDesc.orEmpty(),
                style = MaterialTheme.typography.bodyLarge,
                color = WeatherAppColors.TextMuted,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 12.dp),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(28.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                today?.forecastMaxtemp?.let { max ->
                    TempBoundLabel(label = "Max", value = max.value, unit = max.unit)
                }
                today?.forecastMintemp?.let { min ->
                    TempBoundLabel(label = "Min", value = min.value, unit = min.unit)
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            weather.humidity?.let { humidity ->
                DetailTile(
                    icon = {
                        Icon(Icons.Outlined.Air, null, tint = WeatherAppColors.AccentYellow, modifier = Modifier.size(22.dp))
                    },
                    title = "Humidity",
                    value = formatReadingValue(humidity, weather.humidityUnit),
                    modifier = Modifier.weight(1f),
                )
            }
            today?.PSR?.let { psr ->
                DetailTile(
                    icon = {
                        Icon(Icons.Default.WaterDrop, null, tint = WeatherAppColors.AccentYellow, modifier = Modifier.size(22.dp))
                    },
                    title = "Rain chance",
                    value = psr,
                    modifier = Modifier.weight(1f),
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (weather.dailyForecast.isNotEmpty()) {
            TodayForecastGlassCard(
                days = weather.dailyForecast.take(7),
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun TempBoundLabel(label: String, value: Int, unit: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = WeatherAppColors.TextMuted,
        )
        Text(
            text = "$value°$unit",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
            color = WeatherAppColors.TextPrimary,
        )
    }
}

@Composable
private fun DetailTile(
    icon: @Composable () -> Unit,
    title: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    GlassCard(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            icon()
            Spacer(modifier = Modifier.height(8.dp))
            Text(title, style = MaterialTheme.typography.labelLarge, color = WeatherAppColors.TextMuted)
            Text(
                value,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                color = WeatherAppColors.TextPrimary,
            )
        }
    }
}

@Composable
private fun TodayForecastGlassCard(days: List<DailyForecastDay>) {
    val today = days.first()
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(vertical = 18.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Today",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = WeatherAppColors.TextPrimary,
                )
                Text(
                    text = formatForecastDate(today.forecastDate),
                    style = MaterialTheme.typography.labelLarge,
                    color = WeatherAppColors.AccentYellow,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyRow(
                contentPadding = PaddingValues(horizontal = 18.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(days, key = { it.forecastDate }) { day ->
                    DailyForecastPod(day = day, highlighted = day == today)
                }
            }
        }
    }
}

@Composable
private fun DailyForecastPod(day: DailyForecastDay, highlighted: Boolean) {
    val shape = RoundedCornerShape(20.dp)
    Column(
        modifier = Modifier
            .width(72.dp)
            .clip(shape)
            .background(
                if (highlighted) {
                    Brush.verticalGradient(
                        listOf(Color.White.copy(alpha = 0.22f), Color.White.copy(alpha = 0.08f)),
                    )
                } else {
                    Brush.verticalGradient(
                        listOf(Color.White.copy(alpha = 0.10f), Color.White.copy(alpha = 0.04f)),
                    )
                },
            )
            .border(
                width = 1.dp,
                color = if (highlighted) WeatherAppColors.AccentYellow.copy(alpha = 0.5f) else WeatherAppColors.GlassBorder,
                shape = shape,
            )
            .padding(vertical = 12.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(
            text = "${day.forecastMaxtemp?.value ?: "--"}°",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = WeatherAppColors.TextPrimary,
        )
        day.forecastIcon?.let { icon ->
            AsyncImage(
                model = HkoApiClient.weatherIconUrl(icon),
                contentDescription = day.forecastWeather,
                modifier = Modifier.size(36.dp),
            )
        }
        Text(
            text = shortWeekday(day.week),
            style = MaterialTheme.typography.labelLarge,
            color = if (highlighted) WeatherAppColors.AccentYellow else WeatherAppColors.TextMuted,
        )
    }
}

@Composable
private fun GlassCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(28.dp))
            .background(WeatherAppColors.GlassFill)
            .border(1.dp, WeatherAppColors.GlassBorder, RoundedCornerShape(28.dp)),
    ) {
        content()
    }
}
