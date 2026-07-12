package com.hkweather.app.location

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

data class GeoPoint(val latitude: Double, val longitude: Double)

object WeatherStationLocator {
    private val stations: Map<String, GeoPoint> = mapOf(
        "King's Park" to GeoPoint(22.3108, 114.1747),
        "Hong Kong Observatory" to GeoPoint(22.3020, 114.1746),
        "Wong Chuk Hang" to GeoPoint(22.2472, 114.1678),
        "Ta Kwu Ling" to GeoPoint(22.5361, 114.1569),
        "Lau Fau Shan" to GeoPoint(22.4689, 113.9836),
        "Tai Po" to GeoPoint(22.4447, 114.1647),
        "Sha Tin" to GeoPoint(22.4025, 114.2100),
        "Tuen Mun" to GeoPoint(22.3919, 113.9778),
        "Tseung Kwan O" to GeoPoint(22.3147, 114.2644),
        "Cheung Chau" to GeoPoint(22.2017, 114.0292),
        "Chek Lap Kok" to GeoPoint(22.3089, 113.9144),
        "Tsing Yi" to GeoPoint(22.3467, 114.1067),
        "Tsuen Wan Ho Koon" to GeoPoint(22.3708, 114.1178),
        "Tsuen Wan Shing Mun Valley" to GeoPoint(22.3789, 114.1250),
        "Hong Kong Park" to GeoPoint(22.2778, 114.1611),
        "Shau Kei Wan" to GeoPoint(22.2792, 114.2281),
        "Kowloon City" to GeoPoint(22.3319, 114.1917),
        "Happy Valley" to GeoPoint(22.2708, 114.1831),
        "Wong Tai Sin" to GeoPoint(22.3428, 114.1931),
        "Stanley" to GeoPoint(22.2186, 114.2119),
        "Kwun Tong" to GeoPoint(22.3133, 114.2256),
        "Sham Shui Po" to GeoPoint(22.3303, 114.1625),
        "Kai Tak Runway Park" to GeoPoint(22.3075, 114.2156),
        "Yuen Long Park" to GeoPoint(22.4453, 114.0222),
        "Tai Mei Tuk" to GeoPoint(22.4711, 114.2367),
    )

    fun nearestStation(latitude: Double, longitude: Double): String {
        val userPoint = GeoPoint(latitude, longitude)
        return stations.minByOrNull { (_, station) ->
            haversineKm(userPoint, station)
        }?.key ?: DEFAULT_STATION
    }

    fun isKnownStation(name: String): Boolean = name in stations

    fun allStations(): Map<String, GeoPoint> = stations

    fun stationLocation(name: String): GeoPoint? = stations[name]

    private fun haversineKm(a: GeoPoint, b: GeoPoint): Double {
        val earthRadiusKm = 6371.0
        val dLat = Math.toRadians(b.latitude - a.latitude)
        val dLon = Math.toRadians(b.longitude - a.longitude)
        val lat1 = Math.toRadians(a.latitude)
        val lat2 = Math.toRadians(b.latitude)
        val h =
            sin(dLat / 2).pow(2) +
                cos(lat1) * cos(lat2) * sin(dLon / 2).pow(2)
        return 2 * earthRadiusKm * atan2(sqrt(h), sqrt(1 - h))
    }

    const val DEFAULT_STATION = "Hong Kong Observatory"
}
