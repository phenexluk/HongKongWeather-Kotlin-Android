package com.hkweather.app.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrentWeatherReport(
    val temperature: TemperatureBlock? = null,
    val humidity: HumidityBlock? = null,
    val icon: List<Int>? = null,
    val warningMessage: List<String>? = null,
    val updateTime: String? = null,
)

@Serializable
data class TemperatureBlock(
    val data: List<PlaceReading> = emptyList(),
    val recordTime: String? = null,
)

@Serializable
data class HumidityBlock(
    val data: List<PlaceReading> = emptyList(),
    val recordTime: String? = null,
)

@Serializable
data class PlaceReading(
    val place: String,
    val value: Int,
    val unit: String,
)

@Serializable
data class LocalForecast(
    val generalSituation: String? = null,
    val tcInfo: String? = null,
    val forecastPeriod: String? = null,
    val forecastDesc: String? = null,
    val outlook: String? = null,
    val updateTime: String? = null,
)

@Serializable
data class TempReading(
    val value: Int,
    val unit: String,
)

@Serializable
data class DailyForecastDay(
    val forecastDate: String,
    val week: String,
    val forecastWeather: String,
    val forecastMaxtemp: TempReading? = null,
    val forecastMintemp: TempReading? = null,
    @SerialName("ForecastIcon")
    val forecastIcon: Int? = null,
    val PSR: String? = null,
)

@Serializable
data class NineDayForecast(
    val generalSituation: String? = null,
    val weatherForecast: List<DailyForecastDay> = emptyList(),
    val updateTime: String? = null,
)

@Serializable
data class WarningSummary(
    val name: String,
    val code: String,
    val actionCode: String? = null,
    val issueTime: String? = null,
    val updateTime: String? = null,
)

data class StationTemperature(
    val place: String,
    val latitude: Double,
    val longitude: Double,
    val temperature: Int,
    val unit: String,
)

data class WeatherSnapshot(
    val locationName: String,
    val temperature: Int,
    val temperatureUnit: String,
    val humidity: Int?,
    val humidityUnit: String?,
    val iconCode: Int?,
    val warningMessages: List<String>,
    val activeWarnings: List<WarningSummary>,
    val forecastDesc: String?,
    val generalSituation: String?,
    val tcInfo: String?,
    val outlook: String?,
    val updateTime: String?,
    val stationTemperatures: List<StationTemperature> = emptyList(),
    val dailyForecast: List<DailyForecastDay> = emptyList(),
)
