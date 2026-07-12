package com.hkweather.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/** Grok / X dark UI tokens */
object GrokColors {
    val Canvas = Color(0xFF000000)
    val Surface1 = Color(0xFF16181C)
    val Surface2 = Color(0xFF1E2126)
    val Surface3 = Color(0xFF272A2E)
    val Divider = Color(0xFF2F3336)

    val TextPrimary = Color(0xFFE7E9EA)
    val TextSecondary = Color(0xFF71767B)
    val TextTertiary = Color(0xFF4D5156)

    val White = Color(0xFFFFFFFF)
    val LinkBlue = Color(0xFF1D9BF0)
    val Error = Color(0xFFF4212E)
    val Success = Color(0xFF00BA7C)
}

/** Reference-style weather home gradient */
object WeatherAppColors {
    val GradientTop = Color(0xFF5B2D8E)
    val GradientMid = Color(0xFF3D2068)
    val GradientBottom = Color(0xFF1A1F4B)
    val AccentYellow = Color(0xFFFFD166)
    val GlassFill = Color(0x33FFFFFF)
    val GlassBorder = Color(0x40FFFFFF)
    val TextPrimary = Color(0xFFFFFFFF)
    val TextMuted = Color(0xB3FFFFFF)
}

private val GrokColorScheme = darkColorScheme(
    primary = GrokColors.White,
    onPrimary = GrokColors.Canvas,
    secondary = GrokColors.TextSecondary,
    onSecondary = GrokColors.TextPrimary,
    tertiary = GrokColors.LinkBlue,
    onTertiary = GrokColors.White,
    error = GrokColors.Error,
    background = GrokColors.Canvas,
    surface = GrokColors.Surface1,
    onSurface = GrokColors.TextPrimary,
    onSurfaceVariant = GrokColors.TextSecondary,
    outline = GrokColors.Divider,
    surfaceVariant = GrokColors.Surface2,
)

private val GrokTypography = Typography(
    displayLarge = TextStyle(
        fontSize = 88.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = (-2).sp,
    ),
    headlineLarge = TextStyle(
        fontSize = 28.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = (-0.5).sp,
    ),
    titleLarge = TextStyle(
        fontSize = 20.sp,
        fontWeight = FontWeight.Normal,
    ),
    bodyLarge = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 24.sp,
    ),
    labelLarge = TextStyle(
        fontSize = 13.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.2.sp,
    ),
)

@Composable
fun HongKongWeatherTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = GrokColorScheme,
        typography = GrokTypography,
        content = content,
    )
}
