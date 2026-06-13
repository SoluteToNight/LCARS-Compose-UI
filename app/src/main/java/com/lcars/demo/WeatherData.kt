package com.lcars.demo

import java.net.HttpURLConnection
import java.net.URL
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import kotlin.math.roundToInt

internal data class WeatherReport(
    val locationLabel: String,
    val stationLabel: String,
    val latitudeLabel: String,
    val longitudeLabel: String,
    val elevationLabel: String,
    val updatedLabel: String,
    val sourceStatus: WeatherSourceStatus,
    val temperatureC: Int,
    val apparentTemperatureC: Int?,
    val condition: String,
    val humidityPercent: Int,
    val pressureHpa: Double,
    val windDirectionDegrees: Int?,
    val windSpeedKt: Int,
    val windGustKt: Int?,
    val cloudCoverPercent: Int?,
    val precipitationInches: Double?,
    val iconType: WeatherIconType,
    val forecast: List<WeatherForecastPeriod>,
    val dailyForecast: List<WeatherForecastDay>,
    val hourlyForecast: List<WeatherForecastPeriod>,
) {
    val windLabel: String
        get() = "${windDirectionLabel(windDirectionDegrees)} ${windSpeedKt.toString().padStart(2, '0')} kt"

    val live: Boolean
        get() = sourceStatus == WeatherSourceStatus.Live

    val advisorySuggested: Boolean
        get() = windGustKt?.let { it >= 30 } == true ||
            pressureHpa < 1002.0 ||
            condition.contains("thunder", ignoreCase = true)

    companion object {
        fun placeholder(status: WeatherSourceStatus = WeatherSourceStatus.Loading): WeatherReport = WeatherReport(
            locationLabel = "akron oh 44307",
            stationLabel = "akron grid",
            latitudeLabel = "41.0814 n",
            longitudeLabel = "81.5190 w",
            elevationLabel = "306 m",
            updatedLabel = "sync pending",
            sourceStatus = status,
            temperatureC = 28,
            apparentTemperatureC = null,
            condition = "cloudy",
            humidityPercent = 69,
            pressureHpa = 1028.0,
            windDirectionDegrees = 135,
            windSpeedKt = 9,
            windGustKt = null,
            cloudCoverPercent = null,
            precipitationInches = null,
            iconType = WeatherIconType.Cloud,
            forecast = listOf(
                WeatherForecastPeriod("now", "cloudy", "28", "se09", WeatherIconType.Cloud, highlighted = true),
                WeatherForecastPeriod("1600", "overcast", "31", "s10", WeatherIconType.Overcast),
                WeatherForecastPeriod("2000", "rain", "27", "s12", WeatherIconType.Rain),
                WeatherForecastPeriod("0000", "mist", "24", "sw07", WeatherIconType.Fog),
                WeatherForecastPeriod("0600", "clear", "23", "w05", WeatherIconType.Sun),
            ),
            dailyForecast = listOf(
                WeatherForecastDay("preview-0", "today", "cloudy", "31", "24", WeatherIconType.Cloud, highlighted = true),
                WeatherForecastDay("preview-1", "fri", "overcast", "29", "22", WeatherIconType.Overcast),
                WeatherForecastDay("preview-2", "sat", "rain", "27", "21", WeatherIconType.Rain),
                WeatherForecastDay("preview-3", "sun", "fog", "26", "20", WeatherIconType.Fog),
                WeatherForecastDay("preview-4", "mon", "clear", "30", "22", WeatherIconType.Sun),
            ),
            hourlyForecast = listOf(
                WeatherForecastPeriod("0000", "cloudy", "24", "se06", WeatherIconType.Cloud, dateKey = "preview-0"),
                WeatherForecastPeriod("0400", "cloudy", "25", "se07", WeatherIconType.Cloud, dateKey = "preview-0"),
                WeatherForecastPeriod("0800", "overcast", "27", "se08", WeatherIconType.Overcast, dateKey = "preview-0"),
                WeatherForecastPeriod("1200", "overcast", "31", "s09", WeatherIconType.Overcast, dateKey = "preview-0"),
                WeatherForecastPeriod("1600", "rain", "29", "s11", WeatherIconType.Rain, dateKey = "preview-0"),
                WeatherForecastPeriod("2000", "rain", "26", "sw10", WeatherIconType.Rain, dateKey = "preview-0"),
            ),
        )
    }
}

internal data class WeatherForecastDay(
    val dateKey: String,
    val dayLabel: String,
    val sky: String,
    val highTemperature: String,
    val lowTemperature: String,
    val iconType: WeatherIconType,
    val highlighted: Boolean = false,
)

internal data class WeatherForecastPeriod(
    val period: String,
    val sky: String,
    val temperature: String,
    val wind: String,
    val iconType: WeatherIconType,
    val highlighted: Boolean = false,
    val dateKey: String = "",
)

internal enum class WeatherIconType {
    Sun,
    SunCloud,
    Cloud,
    Overcast,
    Fog,
    Drizzle,
    Rain,
    Snow,
    Showers,
    Thunder,
    Hail,
}

internal enum class WeatherSourceStatus {
    Loading,
    Live,
    Offline,
}

internal object OpenMeteoWeatherClient {
    private const val AKRON_LATITUDE = 41.0814
    private const val AKRON_LONGITUDE = -81.5190
    private const val AKRON_ELEVATION_METERS = 306

    suspend fun fetchAkronWeather(): WeatherReport = fetchWeather(
        location = WeatherLocation(AKRON_LATITUDE, AKRON_LONGITUDE),
        locationLabel = "akron oh 44307",
        stationLabel = "open-meteo station",
        fallbackElevationLabel = "$AKRON_ELEVATION_METERS m",
    )

    suspend fun fetchWeather(location: WeatherLocation): WeatherReport = fetchWeather(
        location = location,
        locationLabel = "local ${location.coordinateLabel()}",
        stationLabel = "device position",
        fallbackElevationLabel = "position fix",
    )

    private suspend fun fetchWeather(
        location: WeatherLocation,
        locationLabel: String,
        stationLabel: String,
        fallbackElevationLabel: String,
    ): WeatherReport = withContext(Dispatchers.IO) {
        val forecastUrl =
            "https://api.open-meteo.com/v1/forecast" +
            "?latitude=${location.latitude}" +
            "&longitude=${location.longitude}" +
            "&current=temperature_2m,relative_humidity_2m,apparent_temperature,weather_code,surface_pressure,wind_speed_10m,wind_direction_10m,wind_gusts_10m,precipitation,cloud_cover" +
            "&hourly=temperature_2m,weather_code,wind_speed_10m,wind_direction_10m,precipitation_probability,relative_humidity_2m" +
            "&daily=weather_code,temperature_2m_max,temperature_2m_min" +
            "&temperature_unit=celsius" +
            "&wind_speed_unit=kn" +
            "&precipitation_unit=inch" +
            "&timezone=auto" +
            "&forecast_days=5"
        val connection = URL(forecastUrl).openConnection() as HttpURLConnection
        try {
            connection.connectTimeout = 8_000
            connection.readTimeout = 8_000
            connection.requestMethod = "GET"
            connection.setRequestProperty("Accept", "application/json")

            if (connection.responseCode !in 200..299) {
                error("weather service returned ${connection.responseCode}")
            }

            val body = connection.inputStream.bufferedReader().use { it.readText() }
            parseWeather(
                root = JSONObject(body),
                location = location,
                locationLabel = locationLabel,
                stationLabel = stationLabel,
                fallbackElevationLabel = fallbackElevationLabel,
            )
        } finally {
            connection.disconnect()
        }
    }

    private fun parseWeather(
        root: JSONObject,
        location: WeatherLocation,
        locationLabel: String,
        stationLabel: String,
        fallbackElevationLabel: String,
    ): WeatherReport {
        val current = root.getJSONObject("current")
        val hourly = root.getJSONObject("hourly")
        val daily = root.getJSONObject("daily")
        val temperatureC = current.requiredRounded("temperature_2m")
        val weatherCode = current.requiredRounded("weather_code")
        val pressureHpa = current.requiredDouble("surface_pressure")
        val windDirection = current.optionalRounded("wind_direction_10m")
        val hourlyForecast = parseHourlyForecast(hourly)

        return WeatherReport(
            locationLabel = locationLabel,
            stationLabel = stationLabel,
            latitudeLabel = latitudeLabel(location.latitude),
            longitudeLabel = longitudeLabel(location.longitude),
            elevationLabel = root.optionalDouble("elevation")?.roundToInt()?.let { "$it m" } ?: fallbackElevationLabel,
            updatedLabel = formatUpdated(current.optString("time")),
            sourceStatus = WeatherSourceStatus.Live,
            temperatureC = temperatureC,
            apparentTemperatureC = current.optionalRounded("apparent_temperature"),
            condition = conditionName(weatherCode),
            humidityPercent = current.requiredRounded("relative_humidity_2m"),
            pressureHpa = pressureHpa,
            windDirectionDegrees = windDirection,
            windSpeedKt = current.requiredRounded("wind_speed_10m"),
            windGustKt = current.optionalRounded("wind_gusts_10m"),
            cloudCoverPercent = current.optionalRounded("cloud_cover"),
            precipitationInches = current.optionalDouble("precipitation"),
            iconType = weatherIconType(weatherCode),
            forecast = parseCurrentForecast(hourlyForecast),
            dailyForecast = parseDailyForecast(daily),
            hourlyForecast = hourlyForecast,
        )
    }

    private fun parseHourlyForecast(hourly: JSONObject): List<WeatherForecastPeriod> {
        val times = hourly.getJSONArray("time")
        val temperatures = hourly.getJSONArray("temperature_2m")
        val weatherCodes = hourly.getJSONArray("weather_code")
        val windSpeeds = hourly.getJSONArray("wind_speed_10m")
        val windDirections = hourly.getJSONArray("wind_direction_10m")
        val count = minOf(
            times.length(),
            temperatures.length(),
            weatherCodes.length(),
            windSpeeds.length(),
            windDirections.length(),
        )

        return (0 until count).map { index ->
            val time = times.optString(index)
            val weatherCode = weatherCodes.optInt(index)
            WeatherForecastPeriod(
                period = formatHour(time),
                sky = conditionName(weatherCode),
                temperature = temperatures.optDouble(index).roundToInt().toString(),
                wind = "${windDirectionLabel(windDirections.optDouble(index).roundToInt())}${windSpeeds.optDouble(index).roundToInt().toString().padStart(2, '0')}",
                iconType = weatherIconType(weatherCode),
                highlighted = index == 0,
                dateKey = formatDateKey(time),
            )
        }
    }

    private fun parseCurrentForecast(hourlyForecast: List<WeatherForecastPeriod>): List<WeatherForecastPeriod> =
        hourlyForecast.take(5).mapIndexed { index, period ->
            period.copy(period = if (index == 0) "now" else period.period, highlighted = index == 0)
        }

    private fun parseDailyForecast(daily: JSONObject): List<WeatherForecastDay> {
        val dates = daily.getJSONArray("time")
        val weatherCodes = daily.getJSONArray("weather_code")
        val highTemperatures = daily.getJSONArray("temperature_2m_max")
        val lowTemperatures = daily.getJSONArray("temperature_2m_min")
        val count = minOf(5, dates.length(), weatherCodes.length(), highTemperatures.length(), lowTemperatures.length())

        return (0 until count).map { index ->
            val dateKey = dates.optString(index)
            val weatherCode = weatherCodes.optInt(index)
            WeatherForecastDay(
                dateKey = dateKey,
                dayLabel = if (index == 0) "today" else formatDayLabel(dateKey),
                sky = conditionName(weatherCode),
                highTemperature = highTemperatures.optDouble(index).roundToInt().toString(),
                lowTemperature = lowTemperatures.optDouble(index).roundToInt().toString(),
                iconType = weatherIconType(weatherCode),
                highlighted = index == 0,
            )
        }
    }
}

private fun JSONObject.requiredDouble(name: String): Double {
    if (!has(name) || isNull(name)) error("missing weather field $name")
    return getDouble(name)
}

private fun JSONObject.requiredRounded(name: String): Int = requiredDouble(name).roundToInt()

private fun JSONObject.optionalDouble(name: String): Double? =
    if (!has(name) || isNull(name)) null else optDouble(name)

private fun JSONObject.optionalRounded(name: String): Int? = optionalDouble(name)?.roundToInt()

private fun formatUpdated(value: String): String {
    if (value.isBlank()) return "sync complete"
    return runCatching {
        val time = LocalDateTime.parse(value)
        "updated ${time.format(DateTimeFormatter.ofPattern("HHmm", Locale.US))}"
    }.getOrDefault("sync complete")
}

private fun formatHour(value: String): String {
    if (value.isBlank()) return "----"
    return runCatching {
        LocalDateTime.parse(value).format(DateTimeFormatter.ofPattern("HHmm", Locale.US))
    }.getOrDefault("----")
}

private fun formatDateKey(value: String): String {
    if (value.isBlank()) return ""
    return runCatching {
        LocalDateTime.parse(value).toLocalDate().toString()
    }.getOrDefault("")
}

private fun formatDayLabel(value: String): String {
    if (value.isBlank()) return "---"
    return runCatching {
        LocalDate.parse(value).format(DateTimeFormatter.ofPattern("EEE", Locale.US))
    }.getOrDefault("---")
}

private fun conditionName(code: Int): String = when (code) {
    0 -> "clear"
    1 -> "mainly clear"
    2 -> "partly cloudy"
    3 -> "overcast"
    45, 48 -> "fog"
    51, 53, 55, 56, 57 -> "drizzle"
    61, 63, 65, 66, 67 -> "rain"
    71, 73, 75, 77 -> "snow"
    80, 81, 82 -> "showers"
    85, 86 -> "snow showers"
    95 -> "thunderstorm"
    96, 99 -> "hail storm"
    else -> "code $code"
}

private fun weatherIconType(code: Int): WeatherIconType = when (code) {
    0 -> WeatherIconType.Sun
    1, 2 -> WeatherIconType.SunCloud
    3 -> WeatherIconType.Overcast
    45, 48 -> WeatherIconType.Fog
    51, 53, 55, 56, 57 -> WeatherIconType.Drizzle
    61, 63, 65, 66, 67 -> WeatherIconType.Rain
    71, 73, 75, 77 -> WeatherIconType.Snow
    80, 81, 82 -> WeatherIconType.Showers
    85, 86 -> WeatherIconType.Snow
    95 -> WeatherIconType.Thunder
    96, 99 -> WeatherIconType.Hail
    else -> WeatherIconType.Cloud
}

internal fun windDirectionLabel(degrees: Int?): String {
    if (degrees == null) return "vrb"
    val directions = listOf("n", "ne", "e", "se", "s", "sw", "w", "nw")
    val index = ((degrees + 22.5) / 45.0).toInt() % directions.size
    return directions[index]
}

private fun Double.formatCoordinate(): String = String.format(Locale.US, "%.4f", this)

private fun WeatherLocation.coordinateLabel(): String =
    "${latitudeLabel(latitude)} ${longitudeLabel(longitude)}"

private fun latitudeLabel(latitude: Double): String =
    "${kotlin.math.abs(latitude).formatCoordinate()} ${if (latitude >= 0) "n" else "s"}"

private fun longitudeLabel(longitude: Double): String =
    "${kotlin.math.abs(longitude).formatCoordinate()} ${if (longitude >= 0) "e" else "w"}"
