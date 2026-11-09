package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = SkyBlueLight,
    onPrimary = SkyBlueDark,
    primaryContainer = SkyBlueDark,
    onPrimaryContainer = Color.White,
    secondary = SkyCyan,
    onSecondary = Color(0xFF00363D),
    tertiary = SunAmber,
    onTertiary = Color(0xFF3E2723),
    background = WeatherDarkBackground,
    onBackground = WeatherDarkOnSurface,
    surface = WeatherDarkSurface,
    onSurface = WeatherDarkOnSurface,
    surfaceVariant = WeatherDarkSurfaceVariant,
    onSurfaceVariant = WeatherDarkOnSurfaceVariant
)

private val LightColorScheme = lightColorScheme(
    primary = SkyBluePrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD6E4FF),
    onPrimaryContainer = SkyBlueDark,
    secondary = Color(0xFF006877),
    onSecondary = Color.White,
    tertiary = SunWarmOrange,
    onTertiary = Color.White,
    background = WeatherLightBackground,
    onBackground = WeatherLightOnSurface,
    surface = WeatherLightSurface,
    onSurface = WeatherLightOnSurface,
    surfaceVariant = WeatherLightSurfaceVariant,
    onSurfaceVariant = WeatherLightOnSurfaceVariant
)

@Composable
fun SkyCastTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Weather app aesthetics look best with tailored weather gradients
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
