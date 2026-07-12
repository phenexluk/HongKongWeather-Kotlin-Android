package com.hkweather.app

import android.app.Application
import com.hkweather.app.data.repository.WeatherRepository
import com.hkweather.app.widget.WeatherWidgetWorker
import org.osmdroid.config.Configuration
import java.io.File

class WeatherApplication : Application() {
    val weatherRepository: WeatherRepository by lazy { WeatherRepository() }

    override fun onCreate() {
        super.onCreate()
        val osmdroidBase = File(cacheDir, "osmdroid")
        Configuration.getInstance().apply {
            userAgentValue = packageName
            osmdroidBasePath = osmdroidBase
            osmdroidTileCache = File(osmdroidBase, "tiles")
        }
        WeatherWidgetWorker.schedule(this)
    }
}
