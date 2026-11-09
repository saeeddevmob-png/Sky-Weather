package com.example.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Thunderstorm
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbCloudy
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Grain
import androidx.compose.material.icons.filled.FilterDrama
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

enum class WeatherCondition(
    val title: String,
    val icon: ImageVector,
    val primaryColor: Color,
    val secondaryColor: Color
) {
    CLEAR(
        title = "Sunny & Clear",
        icon = Icons.Filled.WbSunny,
        primaryColor = Color(0xFF1E88E5),
        secondaryColor = Color(0xFFFFB300)
    ),
    PARTLY_CLOUDY(
        title = "Partly Cloudy",
        icon = Icons.Filled.WbCloudy,
        primaryColor = Color(0xFF2E7D32),
        secondaryColor = Color(0xFF42A5F5)
    ),
    CLOUDY(
        title = "Overcast Clouds",
        icon = Icons.Filled.Cloud,
        primaryColor = Color(0xFF455A64),
        secondaryColor = Color(0xFF78909C)
    ),
    RAIN(
        title = "Light Rain",
        icon = Icons.Filled.WaterDrop,
        primaryColor = Color(0xFF1565C0),
        secondaryColor = Color(0xFF00ACC1)
    ),
    HEAVY_RAIN(
        title = "Heavy Rain",
        icon = Icons.Filled.Grain,
        primaryColor = Color(0xFF0D47A1),
        secondaryColor = Color(0xFF26C6DA)
    ),
    THUNDERSTORM(
        title = "Thunderstorm",
        icon = Icons.Filled.Thunderstorm,
        primaryColor = Color(0xFF283593),
        secondaryColor = Color(0xFFFFB300)
    ),
    SNOW(
        title = "Snow Flurries",
        icon = Icons.Filled.AcUnit,
        primaryColor = Color(0xFF37474F),
        secondaryColor = Color(0xFFB0BEC5)
    ),
    FOG(
        title = "Misty Fog",
        icon = Icons.Filled.FilterDrama,
        primaryColor = Color(0xFF546E7A),
        secondaryColor = Color(0xFF90A4AE)
    )
}

data class HourlyWeather(
    val time: String,
    val tempC: Int,
    val tempF: Int,
    val condition: WeatherCondition,
    val precipProbability: Int
)

data class DailyForecast(
    val dayName: String,
    val date: String,
    val minTempC: Int,
    val maxTempC: Int,
    val minTempF: Int,
    val maxTempF: Int,
    val condition: WeatherCondition,
    val humidity: Int,
    val precipProbability: Int,
    val windSpeedMph: Int,
    val windSpeedKmh: Int,
    val summary: String
)

data class CurrentWeather(
    val cityName: String,
    val country: String,
    val tempC: Int,
    val tempF: Int,
    val feelsLikeC: Int,
    val feelsLikeF: Int,
    val minTempC: Int,
    val maxTempC: Int,
    val minTempF: Int,
    val maxTempF: Int,
    val humidity: Int,
    val windSpeedMph: Int,
    val windSpeedKmh: Int,
    val windDirection: String,
    val uvIndex: Int,
    val pressureHpa: Int,
    val visibilityMiles: Float,
    val condition: WeatherCondition,
    val hourlyForecast: List<HourlyWeather>,
    val dailyForecast: List<DailyForecast>,
    val lastUpdated: String,
    val dataSource: String
)

data class LocationPreset(
    val id: String,
    val name: String,
    val country: String,
    val latitude: Double,
    val longitude: Double
)
