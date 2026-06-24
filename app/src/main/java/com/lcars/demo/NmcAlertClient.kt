package com.lcars.demo

import android.content.Context
import android.location.Geocoder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.util.Locale

internal data class NmcAlert(
    val alertId: String,
    val issueTime: String,
    val title: String,
    val detailUrl: String,
    val level: NmcAlertLevel,
)

internal enum class NmcAlertLevel {
    Blue,
    Yellow,
    Orange,
    Red,
    Unknown,
}

internal object NmcAlertClient {

    suspend fun fetchAlerts(): List<NmcAlert> = withContext(Dispatchers.IO) {
        runCatching {
            val connection = URL("http://www.nmc.cn/rest/findAlarm").openConnection() as HttpURLConnection
            try {
                connection.connectTimeout = 8_000
                connection.readTimeout = 8_000
                connection.requestMethod = "GET"
                connection.setRequestProperty("Accept", "application/json")
                if (connection.responseCode !in 200..299) return@withContext emptyList()
                val body = connection.inputStream.bufferedReader().use { it.readText() }
                parseAlerts(JSONObject(body))
            } finally {
                connection.disconnect()
            }
        }.getOrElse { emptyList() }
    }

    suspend fun resolveProvince(context: Context, latitude: Double, longitude: Double): String? =
        withContext(Dispatchers.IO) {
            runCatching {
                val geocoder = Geocoder(context, Locale.CHINA)
                @Suppress("DEPRECATION")
                val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                addresses?.firstOrNull()?.adminArea
            }.getOrNull()
        }

    fun filterByProvince(alerts: List<NmcAlert>, province: String?): List<NmcAlert> {
        if (province.isNullOrBlank()) return emptyList()
        val shortProvince = province.removeSuffix("省").removeSuffix("市")
        return alerts.filter { alert ->
            alert.title.contains(shortProvince)
        }
    }

    private fun parseAlerts(root: JSONObject): List<NmcAlert> {
        val page = root.optJSONObject("data")?.optJSONObject("page") ?: return emptyList()
        val list = page.optJSONArray("list") ?: return emptyList()
        return (0 until list.length()).mapNotNull { i ->
            val item = list.optJSONObject(i) ?: return@mapNotNull null
            NmcAlert(
                alertId = item.optString("alertid", ""),
                issueTime = item.optString("issuetime", ""),
                title = item.optString("title", ""),
                detailUrl = "http://www.nmc.cn${item.optString("url", "")}",
                level = parseLevel(item.optString("title", "")),
            )
        }
    }

    private fun parseLevel(title: String): NmcAlertLevel = when {
        title.contains("红色") -> NmcAlertLevel.Red
        title.contains("橙色") -> NmcAlertLevel.Orange
        title.contains("黄色") -> NmcAlertLevel.Yellow
        title.contains("蓝色") -> NmcAlertLevel.Blue
        else -> NmcAlertLevel.Unknown
    }
}
