package com.hkweather.app.data.api

import com.hkweather.app.data.model.CurrentWeatherReport
import com.hkweather.app.data.model.LocalForecast
import com.hkweather.app.data.model.NineDayForecast
import com.hkweather.app.data.model.WarningSummary
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request

class HkoApiClient(
    private val httpClient: OkHttpClient = OkHttpClient(),
    private val json: Json = Json { ignoreUnknownKeys = true },
) {
    suspend fun fetchCurrentWeather(lang: String = "en"): CurrentWeatherReport =
        get("rhrread", lang)

    suspend fun fetchLocalForecast(lang: String = "en"): LocalForecast =
        get("flw", lang)

    suspend fun fetchNineDayForecast(lang: String = "en"): NineDayForecast =
        get("fnd", lang)

    suspend fun fetchWarningSummary(lang: String = "en"): Map<String, WarningSummary> {
        val body = fetchRaw("warnsum", lang)
        if (body.isBlank()) return emptyMap()
        return json.decodeFromString(body)
    }

    private inline fun <reified T> get(dataType: String, lang: String): T {
        val body = fetchRaw(dataType, lang)
        return json.decodeFromString(body)
    }

    private fun fetchRaw(dataType: String, lang: String): String {
        val url =
            "$BASE_URL?dataType=$dataType&lang=$lang"
        val request = Request.Builder().url(url).get().build()
        httpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                error("HKO API failed: ${response.code}")
            }
            return response.body?.string().orEmpty()
        }
    }

    companion object {
        const val BASE_URL = "https://data.weather.gov.hk/weatherAPI/opendata/weather.php"
        fun weatherIconUrl(iconCode: Int): String =
            "https://www.hko.gov.hk/images/HKO_v2_icons/$iconCode.png"
    }
}
