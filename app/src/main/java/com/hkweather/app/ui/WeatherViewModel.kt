package com.hkweather.app.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.hkweather.app.WeatherApplication
import com.hkweather.app.data.model.WeatherSnapshot
import com.hkweather.app.location.LocationProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class WeatherUiState(
    val isLoading: Boolean = true,
    val weather: WeatherSnapshot? = null,
    val errorMessage: String? = null,
    val locationPermissionGranted: Boolean = false,
    val usingDeviceLocation: Boolean = false,
    val userLatitude: Double? = null,
    val userLongitude: Double? = null,
)

class WeatherViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = (application as WeatherApplication).weatherRepository
    private val locationProvider = LocationProvider(application)

    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    fun onLocationPermissionResult(granted: Boolean) {
        _uiState.update { it.copy(locationPermissionGranted = granted) }
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val coordinates =
                    if (_uiState.value.locationPermissionGranted) {
                        locationProvider.getCurrentLocation()
                    } else {
                        null
                    }

                val weather = repository.loadWeather(
                    latitude = coordinates?.first,
                    longitude = coordinates?.second,
                )

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        weather = weather,
                        usingDeviceLocation = coordinates != null,
                        userLatitude = coordinates?.first,
                        userLongitude = coordinates?.second,
                        errorMessage = null,
                    )
                }
            } catch (error: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "Failed to load weather",
                    )
                }
            }
        }
    }
}
