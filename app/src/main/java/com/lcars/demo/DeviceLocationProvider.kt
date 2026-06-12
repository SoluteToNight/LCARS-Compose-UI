package com.lcars.demo

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.core.content.ContextCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

internal data class WeatherLocation(
    val latitude: Double,
    val longitude: Double,
)

internal fun hasWeatherLocationPermission(context: Context): Boolean =
    ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
        ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

internal object DeviceLocationProvider {
    suspend fun currentLocation(context: Context): WeatherLocation? = withContext(Dispatchers.Main) {
        if (!hasWeatherLocationPermission(context)) return@withContext null

        val manager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val providers = listOf(LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER)
            .filter { provider -> runCatching { manager.isProviderEnabled(provider) }.getOrDefault(false) }

        bestLastKnownLocation(manager, providers)?.let {
            return@withContext WeatherLocation(it.latitude, it.longitude)
        }

        providers.firstOrNull()?.let { provider ->
            requestSingleLocation(manager, provider)?.let {
                WeatherLocation(it.latitude, it.longitude)
            }
        }
    }

    private fun bestLastKnownLocation(
        manager: LocationManager,
        providers: List<String>,
    ): Location? = providers
        .mapNotNull { provider -> runCatching { manager.getLastKnownLocation(provider) }.getOrNull() }
        .maxByOrNull { it.time }

    private suspend fun requestSingleLocation(
        manager: LocationManager,
        provider: String,
    ): Location? = suspendCancellableCoroutine { continuation ->
        val listener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                if (continuation.isActive) continuation.resume(location)
                manager.removeUpdates(this)
            }

            @Deprecated("Deprecated in Java")
            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) = Unit

            override fun onProviderEnabled(provider: String) = Unit

            override fun onProviderDisabled(provider: String) {
                if (continuation.isActive) continuation.resume(null)
                manager.removeUpdates(this)
            }
        }

        runCatching {
            manager.requestSingleUpdate(provider, listener, null)
        }.onFailure {
            if (continuation.isActive) continuation.resume(null)
        }

        continuation.invokeOnCancellation {
            manager.removeUpdates(listener)
        }
    }
}
