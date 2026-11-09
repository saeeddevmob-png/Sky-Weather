package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.CurrentWeather
import com.example.data.model.DailyForecast
import com.example.data.model.LocationPreset
import com.example.data.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class WeatherUiState(
    val weather: CurrentWeather,
    val selectedLocation: LocationPreset,
    val isCelsius: Boolean = true,
    val isLoading: Boolean = false,
    val selectedDayDetail: DailyForecast? = null,
    val showCityDialog: Boolean = false,
    val availableLocations: List<LocationPreset> = emptyList(),
    val userMessage: String? = null
)

class WeatherViewModel(
    private val repository: WeatherRepository = WeatherRepository()
) : ViewModel() {

    private val defaultLoc = repository.defaultLocations.first()
    private val initialWeather = repository.getOfflineFallbackWeather(defaultLoc)

    private val _uiState = MutableStateFlow(
        WeatherUiState(
            weather = initialWeather,
            selectedLocation = defaultLoc,
            isCelsius = true,
            isLoading = false,
            availableLocations = repository.defaultLocations
        )
    )
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    init {
        // Automatically fetch live weather on startup
        refreshWeather()
    }

    fun toggleUnit() {
        _uiState.update { it.copy(isCelsius = !it.isCelsius) }
    }

    fun selectLocation(location: LocationPreset) {
        _uiState.update {
            it.copy(
                selectedLocation = location,
                showCityDialog = false,
                selectedDayDetail = null
            )
        }
        loadWeatherForLocation(location)
    }

    fun refreshWeather() {
        val currentLoc = _uiState.value.selectedLocation
        loadWeatherForLocation(currentLoc)
    }

    private fun loadWeatherForLocation(location: LocationPreset) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, userMessage = null) }
            val fetchedWeather = repository.getWeatherData(location)
            _uiState.update {
                it.copy(
                    weather = fetchedWeather,
                    isLoading = false,
                    userMessage = "Updated for ${location.name}"
                )
            }
        }
    }

    fun selectDayDetail(day: DailyForecast?) {
        _uiState.update { it.copy(selectedDayDetail = day) }
    }

    fun setShowCityDialog(show: Boolean) {
        _uiState.update { it.copy(showCityDialog = show) }
    }

    fun clearUserMessage() {
        _uiState.update { it.copy(userMessage = null) }
    }
}
