package com.hkweather.app.ui.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.hkweather.app.data.model.StationTemperature
import com.hkweather.app.ui.theme.GrokColors
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun TemperatureMapView(
    stations: List<StationTemperature>,
    highlightedPlace: String?,
    userLatitude: Double?,
    userLongitude: Double?,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val mapView = remember {
        MapView(context).apply {
            setMultiTouchControls(true)
            controller.setZoom(11.0)
            controller.setCenter(GeoPoint(22.35, 114.15))
            setTileSource(TileSourceFactory.MAPNIK)
        }
    }

    DisposableEffect(lifecycleOwner, mapView) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        if (lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
            mapView.onResume()
        }
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            mapView.onPause()
            mapView.onDetach()
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { mapView },
            update = { view ->
                view.overlays.clear()

                stations.forEach { station ->
                    val marker = Marker(view).apply {
                        position = GeoPoint(station.latitude, station.longitude)
                        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
                        icon = android.graphics.drawable.BitmapDrawable(
                            context.resources,
                            TemperatureMarkerFactory.create(
                                context = context,
                                temperature = station.temperature,
                                unit = station.unit,
                                highlighted = station.place == highlightedPlace,
                            ),
                        )
                        title = station.place
                        snippet = "${station.temperature}°${station.unit}"
                    }
                    view.overlays.add(marker)
                }

                if (userLatitude != null && userLongitude != null) {
                    val userMarker = Marker(view).apply {
                        position = GeoPoint(userLatitude, userLongitude)
                        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
                        icon = android.graphics.drawable.BitmapDrawable(
                            context.resources,
                            TemperatureMarkerFactory.createUserDot(),
                        )
                        title = "Your location"
                    }
                    view.overlays.add(userMarker)
                }

                view.invalidate()
            },
        )

        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 12.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(GrokColors.Surface2)
                .padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            Text(
                text = "OpenStreetMap · HKO stations",
                style = MaterialTheme.typography.labelLarge,
                color = GrokColors.TextSecondary,
            )
        }
    }
}
