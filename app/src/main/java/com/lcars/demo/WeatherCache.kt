package com.lcars.demo

import android.content.Context
import org.json.JSONObject

internal object WeatherCache {
    private const val PREFS_NAME = "lcars_weather_cache"
    private const val KEY_REPORT = "last_report"
    private const val KEY_TIMESTAMP = "last_sync_epoch_ms"

    fun save(context: Context, report: WeatherReport) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putString(KEY_REPORT, report.toJson().toString())
            .putLong(KEY_TIMESTAMP, System.currentTimeMillis())
            .apply()
    }

    fun load(context: Context): CachedWeather? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val raw = prefs.getString(KEY_REPORT, null) ?: return null
        val timestamp = prefs.getLong(KEY_TIMESTAMP, 0L)
        return runCatching {
            val report = WeatherReport.fromJson(JSONObject(raw))
            CachedWeather(report = report, cachedAtEpochMs = timestamp)
        }.getOrNull()
    }
}

internal data class CachedWeather(
    val report: WeatherReport,
    val cachedAtEpochMs: Long,
)
