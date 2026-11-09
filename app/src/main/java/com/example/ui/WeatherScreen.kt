package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.CitySelectionDialog
import com.example.ui.components.DayDetailDialog
import com.example.ui.components.FiveDayForecastSection
import com.example.ui.components.HeroWeatherCard
import com.example.ui.components.HourlyForecastSection
import com.example.ui.components.WeatherAtmosphere
import com.example.ui.components.WeatherHeader
import com.example.ui.components.WeatherMetricsRow

@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val isDark = isSystemInDarkTheme()
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.userMessage) {
        uiState.userMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearUserMessage()
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        // Atmospheric dynamic background canvas
        WeatherAtmosphere(
            condition = uiState.weather.condition,
            isDarkTheme = isDark
        )

        Scaffold(
            containerColor = androidx.compose.ui.graphics.Color.Transparent,
            snackbarHost = { SnackbarHost(snackbarHostState) },
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .statusBarsPadding()
                    .navigationBarsPadding(),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .widthIn(max = 680.dp) // Great tablet / foldable support
                        .verticalScroll(scrollState),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Header (Location picker, °C/°F toggle, refresh)
                    WeatherHeader(
                        cityName = uiState.selectedLocation.name,
                        country = uiState.selectedLocation.country,
                        dataSource = uiState.weather.dataSource,
                        isCelsius = uiState.isCelsius,
                        isLoading = uiState.isLoading,
                        onCityClick = { viewModel.setShowCityDialog(true) },
                        onUnitToggle = { viewModel.toggleUnit() },
                        onRefresh = { viewModel.refreshWeather() }
                    )

                    // Refreshing Progress Bar
                    AnimatedVisibility(
                        visible = uiState.isLoading,
                        enter = fadeIn(),
                        exit = fadeOut(),
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)
                    ) {
                        LinearProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(4.dp)
                                .testTag("refresh_progress_indicator"),
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        )
                    }

                    // Hero Current Weather Card (Temperature, Condition, High/Low, Feels like)
                    HeroWeatherCard(
                        weather = uiState.weather,
                        isCelsius = uiState.isCelsius
                    )

                    // Core Weather Metrics (Humidity, Wind, UV Index & Air Pressure)
                    WeatherMetricsRow(
                        humidity = uiState.weather.humidity,
                        windMph = uiState.weather.windSpeedMph,
                        windKmh = uiState.weather.windSpeedKmh,
                        windDirection = uiState.weather.windDirection,
                        uvIndex = uiState.weather.uvIndex,
                        pressureHpa = uiState.weather.pressureHpa,
                        isCelsius = uiState.isCelsius
                    )

                    // Hourly Forecast Carousel
                    HourlyForecastSection(
                        hourlyList = uiState.weather.hourlyForecast,
                        isCelsius = uiState.isCelsius
                    )

                    // 5-Day Forecast Section (with visual min-max range indicators)
                    FiveDayForecastSection(
                        dailyList = uiState.weather.dailyForecast,
                        isCelsius = uiState.isCelsius,
                        onDayClick = { day -> viewModel.selectDayDetail(day) }
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }

        // Day Detail Dialog when user taps any day in the 5-day forecast
        uiState.selectedDayDetail?.let { day ->
            DayDetailDialog(
                day = day,
                isCelsius = uiState.isCelsius,
                onDismiss = { viewModel.selectDayDetail(null) }
            )
        }

        // City Selector Dialog
        if (uiState.showCityDialog) {
            CitySelectionDialog(
                currentLocation = uiState.selectedLocation,
                availableLocations = uiState.availableLocations,
                onSelectCity = { location -> viewModel.selectLocation(location) },
                onDismiss = { viewModel.setShowCityDialog(false) }
            )
        }
    }
}
