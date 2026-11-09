package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.repository.WeatherRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("SkyCast Weather", appName)
    }

    @Test
    fun `weather repository provides 5-day forecast and humidity`() {
        val repository = WeatherRepository()
        val defaultLoc = repository.defaultLocations.first()
        val weather = repository.getOfflineFallbackWeather(defaultLoc)

        assertNotNull(weather)
        assertTrue(weather.humidity in 0..100)
        assertEquals(5, weather.dailyForecast.size)
        assertTrue(weather.hourlyForecast.isNotEmpty())
    }
}
