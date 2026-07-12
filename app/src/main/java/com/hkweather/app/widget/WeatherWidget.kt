package com.hkweather.app.widget

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.hkweather.app.MainActivity
import com.hkweather.app.WeatherApplication
import com.hkweather.app.data.model.WeatherSnapshot
import com.hkweather.app.ui.theme.GrokColors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private val WidgetBackground = ColorProvider(GrokColors.Canvas)
private val WidgetText = ColorProvider(GrokColors.TextPrimary)
private val WidgetMuted = ColorProvider(GrokColors.TextSecondary)
private val WidgetLink = ColorProvider(GrokColors.LinkBlue)

class WeatherWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val snapshot = loadSnapshot(context)
        provideContent {
            GlanceTheme {
                WeatherWidgetContent(context = context, snapshot = snapshot)
            }
        }
    }

    private suspend fun loadSnapshot(context: Context): WeatherSnapshot? = withContext(Dispatchers.IO) {
        try {
            val repository = (context.applicationContext as WeatherApplication).weatherRepository
            repository.loadWeather(latitude = null, longitude = null)
        } catch (_: Exception) {
            null
        }
    }
}

@Composable
private fun WeatherWidgetContent(context: Context, snapshot: WeatherSnapshot?) {
    val launchIntent = actionStartActivity(Intent(context, MainActivity::class.java))
    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(WidgetBackground)
            .clickable(launchIntent)
            .padding(12.dp),
        verticalAlignment = Alignment.Vertical.CenterVertically,
    ) {
        Text(
            text = "HK Weather",
            style = TextStyle(color = WidgetMuted, fontSize = 12.sp),
        )

        Spacer(modifier = GlanceModifier.height(8.dp))

        if (snapshot == null) {
            Text(
                text = "Unable to load",
                style = TextStyle(color = WidgetText, fontSize = 12.sp),
            )
            return@Column
        }

        Row(
            modifier = GlanceModifier.fillMaxWidth(),
            verticalAlignment = Alignment.Vertical.CenterVertically,
        ) {
            Column(modifier = GlanceModifier.defaultWeight()) {
                Text(
                    text = snapshot.locationName,
                    style = TextStyle(color = WidgetMuted, fontSize = 11.sp),
                )
                Text(
                    text = "${snapshot.temperature}°${snapshot.temperatureUnit}",
                    style = TextStyle(
                        color = WidgetText,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Normal,
                    ),
                )
            }
        }

        Spacer(modifier = GlanceModifier.height(8.dp))

        val warningText = when {
            snapshot.activeWarnings.isNotEmpty() ->
                snapshot.activeWarnings.joinToString("\n") { it.name }
            snapshot.warningMessages.isNotEmpty() ->
                snapshot.warningMessages.first()
            else -> snapshot.forecastDesc ?: "No active warnings"
        }

        Text(
            text = warningText,
            style = TextStyle(
                color = if (snapshot.activeWarnings.isNotEmpty()) WidgetLink else WidgetMuted,
                fontSize = 11.sp,
            ),
            maxLines = 3,
        )
    }
}

class WeatherWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = WeatherWidget()
}
