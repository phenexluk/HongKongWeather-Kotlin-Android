package com.hkweather.app.data.repository

import com.hkweather.app.data.api.HkoApiClient
import com.hkweather.app.data.model.PlaceReading
import com.hkweather.app.data.model.StationTemperature
import com.hkweather.app.data.model.WeatherSnapshot
import com.hkweather.app.location.WeatherStationLocator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WeatherRepository(
    private val api: HkoApiClient = HkoApiClient(),
) {
    suspend fun loadWeather(
        latitude: Double?,
        longitude: Double?,
        preferredStation: String? = null,
    ): WeatherSnapshot = withContext(Dispatchers.IO) {
        val current = api.fetchCurrentWeather()
        val forecast = api.fetchLocalForecast()
        val nineDay = api.fetchNineDayForecast()
        val warnings = api.fetchWarningSummary()

        val stationName = when {
            preferredStation != null && WeatherStationLocator.isKnownStation(preferredStation) ->
                preferredStation
            latitude != null && longitude != null ->
                WeatherStationLocator.nearestStation(latitude, longitude)
            else -> WeatherStationLocator.DEFAULT_STATION
        }

        val temperature = findReading(current.temperature?.data.orEmpty(), stationName)
            ?: current.temperature?.data?.firstOrNull()
        val humidity = findReading(current.humidity?.data.orEmpty(), stationName)
            ?: current.humidity?.data?.firstOrNull()

        WeatherSnapshot(
            locationName = temperature?.place ?: stationName,
            temperature = temperature?.value ?: 0,
            temperatureUnit = temperature?.unit ?: "C",
            humidity = humidity?.value,
            humidityUnit = humidity?.unit,
            iconCode = current.icon?.firstOrNull(),
            warningMessages = current.warningMessage.orEmpty(),
            activeWarnings = warnings.values.sortedBy { it.name },
            forecastDesc = forecast.forecastDesc,
            generalSituation = forecast.generalSituation,
            tcInfo = forecast.tcInfo,
            outlook = forecast.outlook,
            updateTime = current.updateTime ?: forecast.updateTime,
            stationTemperatures = buildStationTemperatures(current.temperature?.data.orEmpty()),
            dailyForecast = nineDay.weatherForecast,
        )
    }

    private fun buildStationTemperatures(readings: List<PlaceReading>): List<StationTemperature> {
        val readingByPlace = readings.associateBy { it.place }
        return WeatherStationLocator.allStations().mapNotNull { (place, location) ->
            val reading = readingByPlace[place] ?: return@mapNotNull null
            StationTemperature(
                place = place,
                latitude = location.latitude,
                longitude = location.longitude,
                temperature = reading.value,
                unit = reading.unit,
            )
        }
    }

    private fun findReading(readings: List<PlaceReading>, place: String): PlaceReading? =
        readings.firstOrNull { it.place.equals(place, ignoreCase = true) }
}
