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
    val grid: String = "GRID 01",
)

internal enum class NmcAlertLevel {
    Blue,
    Yellow,
    Orange,
    Red,
    Unknown,
}

internal object NmcAlertClient {

    val mockAlerts = listOf(
        NmcAlert(
            alertId = "alert-001",
            issueTime = "2026-06-24 10:15:00",
            title = "BEIJING: THUNDERSTORM RED WARNING",
            detailUrl = "http://www.nmc.cn/alarm/1",
            level = NmcAlertLevel.Red,
            grid = "BEIJING"
        ),
        NmcAlert(
            alertId = "alert-002",
            issueTime = "2026-06-24 09:30:00",
            title = "SHANGHAI: DENSE FOG YELLOW WARNING",
            detailUrl = "http://www.nmc.cn/alarm/2",
            level = NmcAlertLevel.Yellow,
            grid = "SHANGHAI"
        ),
        NmcAlert(
            alertId = "alert-003",
            issueTime = "2026-06-24 08:45:00",
            title = "GUANGDONG: HEAVY RAIN ORANGE WARNING",
            detailUrl = "http://www.nmc.cn/alarm/3",
            level = NmcAlertLevel.Orange,
            grid = "GUANGDONG"
        ),
        NmcAlert(
            alertId = "alert-004",
            issueTime = "2026-06-24 07:15:00",
            title = "HUBEI: GALE BLUE WARNING",
            detailUrl = "http://www.nmc.cn/alarm/4",
            level = NmcAlertLevel.Blue,
            grid = "HUBEI"
        ),
        NmcAlert(
            alertId = "alert-005",
            issueTime = "2026-06-24 06:00:00",
            title = "SICHUAN: HEATWAVE YELLOW WARNING",
            detailUrl = "http://www.nmc.cn/alarm/5",
            level = NmcAlertLevel.Yellow,
            grid = "SICHUAN"
        ),
        NmcAlert(
            alertId = "alert-006",
            issueTime = "2026-06-24 05:40:00",
            title = "BEIJING: HEAVY RAIN BLUE WARNING",
            detailUrl = "http://www.nmc.cn/alarm/6",
            level = NmcAlertLevel.Blue,
            grid = "BEIJING"
        )
    )

    suspend fun fetchAlerts(): List<NmcAlert> = withContext(Dispatchers.IO) {
        val fetched = runCatching {
            val connection = URL("http://www.nmc.cn/rest/findAlarm").openConnection() as HttpURLConnection
            try {
                connection.connectTimeout = 8_000
                connection.readTimeout = 8_000
                connection.requestMethod = "GET"
                connection.setRequestProperty("Accept", "application/json")
                if (connection.responseCode !in 200..299) return@withContext mockAlerts
                val body = connection.inputStream.bufferedReader().use { it.readText() }
                parseAlerts(JSONObject(body))
            } finally {
                connection.disconnect()
            }
        }.getOrElse { emptyList() }
        if (fetched.isEmpty()) mockAlerts else fetched
    }

    fun translateAndMap(title: String): Pair<String, String> {
        val level = when {
            title.contains("红色") -> "RED WARNING"
            title.contains("橙色") -> "ORANGE WARNING"
            title.contains("黄色") -> "YELLOW WARNING"
            title.contains("蓝色") -> "BLUE WARNING"
            else -> "WARNING"
        }
        val type = when {
            title.contains("雷电") -> "THUNDERSTORM"
            title.contains("暴雨") || title.contains("大雨") -> "HEAVY RAIN"
            title.contains("大风") -> "GALE"
            title.contains("高温") -> "HEATWAVE"
            title.contains("大雾") -> "DENSE FOG"
            title.contains("寒潮") -> "COLD WAVE"
            title.contains("台风") -> "TYPHOON"
            title.contains("冰雹") -> "HAIL"
            title.contains("沙尘") -> "DUST STORM"
            title.contains("道路结冰") -> "ROAD ICING"
            title.contains("霜冻") -> "FROST"
            else -> "WEATHER"
        }
        val province = when {
            title.contains("北京") -> "BEIJING"
            title.contains("上海") -> "SHANGHAI"
            title.contains("广东") -> "GUANGDONG"
            title.contains("湖北") -> "HUBEI"
            title.contains("四川") -> "SICHUAN"
            title.contains("浙江") -> "ZHEJIANG"
            title.contains("海南") -> "HAINAN"
            else -> "UNKNOWN"
        }
        val displayTitle = if (province == "UNKNOWN") {
            "$type $level"
        } else {
            "$province: $type $level"
        }
        return Pair(displayTitle, province)
    }

    suspend fun resolveProvince(context: Context, latitude: Double, longitude: Double): String? =
        withContext(Dispatchers.IO) {
            runCatching {
                val geocoder = Geocoder(context, Locale.CHINA)
                @Suppress("DEPRECATION")
                val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                val adminArea = addresses?.firstOrNull()?.adminArea ?: ""
                when {
                    adminArea.contains("北京") -> "BEIJING"
                    adminArea.contains("上海") -> "SHANGHAI"
                    adminArea.contains("广东") -> "GUANGDONG"
                    adminArea.contains("湖北") -> "HUBEI"
                    adminArea.contains("四川") -> "SICHUAN"
                    adminArea.contains("浙江") -> "ZHEJIANG"
                    adminArea.contains("海南") -> "HAINAN"
                    else -> null
                }
            }.getOrNull()
        }

    fun filterByProvince(alerts: List<NmcAlert>, province: String?): List<NmcAlert> {
        if (province.isNullOrBlank()) return emptyList()
        return alerts.filter { alert ->
            alert.grid == province
        }
    }

    private fun parseAlerts(root: JSONObject): List<NmcAlert> {
        val page = root.optJSONObject("data")?.optJSONObject("page") ?: return emptyList()
        val list = page.optJSONArray("list") ?: return emptyList()
        return (0 until list.length()).mapNotNull { i ->
            val item = list.optJSONObject(i) ?: return@mapNotNull null
            val rawTitle = item.optString("title", "")
            val (englishTitle, grid) = translateAndMap(rawTitle)
            NmcAlert(
                alertId = item.optString("alertid", ""),
                issueTime = item.optString("issuetime", ""),
                title = englishTitle,
                detailUrl = "http://www.nmc.cn${item.optString("url", "")}",
                level = parseLevel(rawTitle),
                grid = grid
            )
        }
    }

    private fun parseLevel(title: String): NmcAlertLevel = when {
        title.contains("红色") || title.contains("RED") -> NmcAlertLevel.Red
        title.contains("橙色") || title.contains("ORANGE") -> NmcAlertLevel.Orange
        title.contains("黄色") || title.contains("YELLOW") -> NmcAlertLevel.Yellow
        title.contains("蓝色") || title.contains("BLUE") -> NmcAlertLevel.Blue
        else -> NmcAlertLevel.Unknown
    }
}
