package com.hkweather.app

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.content.ContextCompat
import com.hkweather.app.ui.WeatherScreen
import com.hkweather.app.ui.WeatherViewModel
import com.hkweather.app.ui.theme.HongKongWeatherTheme

class MainActivity : ComponentActivity() {
    private val viewModel: WeatherViewModel by viewModels()

    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
            val granted = results.values.any { it }
            viewModel.onLocationPermissionResult(granted)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
        )

        requestLocationIfNeeded()

        setContent {
            HongKongWeatherTheme {
                val uiState by viewModel.uiState.collectAsState()
                WeatherScreen(
                    uiState = uiState,
                    onRefresh = viewModel::refresh,
                )
            }
        }

        viewModel.refresh()
    }

    private fun requestLocationIfNeeded() {
        val fineGranted =
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED
        val coarseGranted =
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED

        if (fineGranted || coarseGranted) {
            viewModel.onLocationPermissionResult(true)
        } else {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                ),
            )
        }
    }
}
