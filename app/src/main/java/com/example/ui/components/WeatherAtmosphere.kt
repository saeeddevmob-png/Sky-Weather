package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.data.model.WeatherCondition

@Composable
fun WeatherAtmosphere(
    condition: WeatherCondition,
    isDarkTheme: Boolean,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "AtmospherePulse")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "SunGlowPulse"
    )

    val gradientColors = when (condition) {
        WeatherCondition.CLEAR -> {
            if (isDarkTheme) {
                listOf(
                    Color(0xFF0D1B2A),
                    Color(0xFF1B263B),
                    Color(0xFF0F1A24)
                )
            } else {
                listOf(
                    Color(0xFF2979FF),
                    Color(0xFF42A5F5),
                    Color(0xFFE3F2FD)
                )
            }
        }
        WeatherCondition.PARTLY_CLOUDY -> {
            if (isDarkTheme) {
                listOf(
                    Color(0xFF102027),
                    Color(0xFF263238),
                    Color(0xFF0D151A)
                )
            } else {
                listOf(
                    Color(0xFF1976D2),
                    Color(0xFF64B5F6),
                    Color(0xFFEDE7F6)
                )
            }
        }
        WeatherCondition.CLOUDY, WeatherCondition.FOG -> {
            if (isDarkTheme) {
                listOf(
                    Color(0xFF1C252A),
                    Color(0xFF2A363B),
                    Color(0xFF14191C)
                )
            } else {
                listOf(
                    Color(0xFF607D8B),
                    Color(0xFF90A4AE),
                    Color(0xFFECEFF1)
                )
            }
        }
        WeatherCondition.RAIN, WeatherCondition.HEAVY_RAIN -> {
            if (isDarkTheme) {
                listOf(
                    Color(0xFF0F1B29),
                    Color(0xFF1A3047),
                    Color(0xFF0B141E)
                )
            } else {
                listOf(
                    Color(0xFF1565C0),
                    Color(0xFF37474F),
                    Color(0xFFCFD8DC)
                )
            }
        }
        WeatherCondition.THUNDERSTORM -> {
            listOf(
                Color(0xFF1A1A2E),
                Color(0xFF16213E),
                Color(0xFF0F3460)
            )
        }
        WeatherCondition.SNOW -> {
            if (isDarkTheme) {
                listOf(
                    Color(0xFF1E2830),
                    Color(0xFF2C3E50),
                    Color(0xFF121920)
                )
            } else {
                listOf(
                    Color(0xFF78909C),
                    Color(0xFFB0BEC5),
                    Color(0xFFF5F7FA)
                )
            }
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        // Background linear atmospheric gradient
        drawRect(
            brush = Brush.verticalGradient(
                colors = gradientColors,
                startY = 0f,
                endY = height * 0.9f
            )
        )

        // Decorative celestial orb / glowing weather halo
        when (condition) {
            WeatherCondition.CLEAR -> {
                val sunCenter = Offset(width * 0.85f, height * 0.12f)
                val baseRadius = width * 0.28f * pulse
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0x66FFD54F),
                            Color(0x22FFA000),
                            Color.Transparent
                        ),
                        center = sunCenter,
                        radius = baseRadius * 1.5f
                    ),
                    center = sunCenter,
                    radius = baseRadius * 1.5f
                )
            }
            WeatherCondition.RAIN, WeatherCondition.HEAVY_RAIN -> {
                // Subtle cool ambient mist
                val mistCenter = Offset(width * 0.2f, height * 0.15f)
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0x2280D8FF),
                            Color.Transparent
                        ),
                        center = mistCenter,
                        radius = width * 0.5f
                    ),
                    center = mistCenter,
                    radius = width * 0.5f
                )
            }
            else -> {
                val cloudCenter = Offset(width * 0.8f, height * 0.15f)
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0x1AFFFFFF),
                            Color.Transparent
                        ),
                        center = cloudCenter,
                        radius = width * 0.4f
                    ),
                    center = cloudCenter,
                    radius = width * 0.4f
                )
            }
        }
    }
}
