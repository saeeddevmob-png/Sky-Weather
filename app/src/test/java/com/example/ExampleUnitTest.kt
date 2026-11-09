package com.example

import com.example.data.repository.WeatherRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testWeatherRepositoryOfflineFallback() {
        val repo = WeatherRepository()
        val loc = repo.defaultLocations.first()
        val weather = repo.getOfflineFallbackWeather(loc)

        assertNotNull(weather)
        assertEquals("San Francisco", weather.cityName)
        assertTrue(weather.humidity in 0..100)
        assertEquals(5, weather.dailyForecast.size)
        assertTrue(weather.hourlyForecast.isNotEmpty())
    }
}
