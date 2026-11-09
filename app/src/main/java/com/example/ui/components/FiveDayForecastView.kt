package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DailyForecast

@Composable
fun FiveDayForecastSection(
    dailyList: List<DailyForecast>,
    isCelsius: Boolean,
    onDayClick: (DailyForecast) -> Unit,
    modifier: Modifier = Modifier
) {
    if (dailyList.isEmpty()) return

    // Calculate absolute minimum and maximum across all 5 days for the temperature range bars
    val overallMin = dailyList.minOf { if (isCelsius) it.minTempC else it.minTempF }
    val overallMax = dailyList.maxOf { if (isCelsius) it.maxTempC else it.maxTempF }
    val rangeSpan = (overallMax - overallMin).coerceAtLeast(1)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .testTag("five_day_forecast_card"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Section Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = "5-Day Forecast",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "5-Day Forecast",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                ) {
                    Text(
                        text = "Next 5 Days",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 5 Day Rows
            dailyList.take(5).forEachIndexed { index, day ->
                val dayMin = if (isCelsius) day.minTempC else day.minTempF
                val dayMax = if (isCelsius) day.maxTempC else day.maxTempF

                ForecastDayRow(
                    day = day,
                    minTemp = dayMin,
                    maxTemp = dayMax,
                    overallMin = overallMin,
                    overallMax = overallMax,
                    rangeSpan = rangeSpan,
                    isToday = index == 0,
                    onClick = { onDayClick(day) },
                    modifier = Modifier.testTag("forecast_day_item_$index")
                )

                if (index < 4) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 10.dp),
                        thickness = 0.5.dp,
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
}

@Composable
fun ForecastDayRow(
    day: DailyForecast,
    minTemp: Int,
    maxTemp: Int,
    overallMin: Int,
    overallMax: Int,
    rangeSpan: Int,
    isToday: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Day name & Date
        Column(modifier = Modifier.width(68.dp)) {
            Text(
                text = day.dayName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = if (isToday) FontWeight.ExtraBold else FontWeight.SemiBold,
                color = if (isToday) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = day.date,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Weather Icon & Rain badge
        Row(
            modifier = Modifier.width(72.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = day.condition.icon,
                contentDescription = day.condition.title,
                tint = if (isToday) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(24.dp)
            )

            if (day.precipProbability >= 20) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.WaterDrop,
                        contentDescription = "Precipitation",
                        tint = Color(0xFF0288D1),
                        modifier = Modifier.size(10.dp)
                    )
                    Text(
                        text = "${day.precipProbability}%",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF0288D1),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Min Temp
        Text(
            text = "$minTemp°",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.width(36.dp)
        )

        // Temperature Gradient Bar
        TemperatureRangeBar(
            min = minTemp,
            max = maxTemp,
            overallMin = overallMin,
            overallMax = overallMax,
            modifier = Modifier
                .weight(1f)
                .height(6.dp)
                .padding(horizontal = 4.dp)
        )

        // Max Temp
        Text(
            text = "$maxTemp°",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .width(40.dp)
                .padding(start = 4.dp)
        )

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = "View Day Details",
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
            modifier = Modifier.size(16.dp)
        )
    }
}

@Composable
fun TemperatureRangeBar(
    min: Int,
    max: Int,
    overallMin: Int,
    overallMax: Int,
    modifier: Modifier = Modifier
) {
    val trackColor = MaterialTheme.colorScheme.surfaceVariant

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val totalRange = (overallMax - overallMin).coerceAtLeast(1).toFloat()

        // Draw track line
        drawRoundRect(
            color = trackColor,
            topLeft = Offset(0f, 0f),
            size = Size(width, height),
            cornerRadius = CornerRadius(height / 2, height / 2)
        )

        // Calculate start and end offsets
        val startFraction = ((min - overallMin).toFloat() / totalRange).coerceIn(0f, 0.9f)
        val endFraction = ((max - overallMin).toFloat() / totalRange).coerceIn(startFraction + 0.1f, 1f)

        val barStart = width * startFraction
        val barEnd = width * endFraction
        val barWidth = (barEnd - barStart).coerceAtLeast(height)

        drawRoundRect(
            brush = Brush.horizontalGradient(
                colors = listOf(
                    Color(0xFF42A5F5), // Cool blue for min
                    Color(0xFFFFB300), // Warm amber for mid
                    Color(0xFFFF7043)  // Warm coral for max
                ),
                startX = barStart,
                endX = barEnd
            ),
            topLeft = Offset(barStart, 0f),
            size = Size(barWidth, height),
            cornerRadius = CornerRadius(height / 2, height / 2)
        )
    }
}
