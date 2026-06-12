package com.lcars.demo

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lcars.ui.LcarsAlertBanner
import com.lcars.ui.LcarsBar
import com.lcars.ui.LcarsButton
import com.lcars.ui.LcarsButtonShape
import com.lcars.ui.LcarsDataRow
import com.lcars.ui.LcarsDataTable
import com.lcars.ui.LcarsDividerGrid
import com.lcars.ui.LcarsDividerGridType
import com.lcars.ui.LcarsElbow
import com.lcars.ui.LcarsElbowDirection
import com.lcars.ui.LcarsFramePanel
import com.lcars.ui.LcarsInspectBracket
import com.lcars.ui.LcarsLabelAlign
import com.lcars.ui.LcarsLogConsole
import com.lcars.ui.LcarsLogEntry
import com.lcars.ui.LcarsLogSeverity
import com.lcars.ui.LcarsNumberMatrix
import com.lcars.ui.LcarsNumericLabel
import com.lcars.ui.LcarsProgressBar
import com.lcars.ui.LcarsReadoutTicker
import com.lcars.ui.LcarsResponsiveScaffold
import com.lcars.ui.LcarsSegmentedMeter
import com.lcars.ui.LcarsStatusLight
import com.lcars.ui.LcarsTargetScanner
import com.lcars.ui.LcarsTelemetryEntry
import com.lcars.ui.LcarsTelemetryPanel
import com.lcars.ui.LcarsTelemetryStatus
import com.lcars.ui.LcarsText
import com.lcars.ui.LocalLcarsColors
import com.lcars.ui.LocalLcarsSpacing
import com.lcars.ui.LocalLcarsTypography
import java.util.Locale
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun AtmosphericConditionsDemoScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val inPreview = LocalInspectionMode.current
    var alertActive by rememberSaveable { mutableStateOf(false) }
    var weatherReport by remember { mutableStateOf(WeatherReport.placeholder()) }
    var locationPermissionGranted by remember { mutableStateOf(hasWeatherLocationPermission(context)) }
    var locationPermissionRequested by rememberSaveable { mutableStateOf(false) }
    val colors = LocalLcarsColors.current
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions(),
    ) { grants ->
        locationPermissionGranted = grants.values.any { it }
    }

    LaunchedEffect(Unit) {
        locationPermissionGranted = hasWeatherLocationPermission(context)
    }

    LaunchedEffect(inPreview, locationPermissionGranted, locationPermissionRequested) {
        if (!inPreview && !locationPermissionGranted && !locationPermissionRequested) {
            locationPermissionRequested = true
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                ),
            )
        }
    }

    LaunchedEffect(inPreview, locationPermissionGranted) {
        if (inPreview) return@LaunchedEffect
        weatherReport = runCatching {
            val location = if (locationPermissionGranted) {
                DeviceLocationProvider.currentLocation(context)
            } else {
                null
            }
            if (location == null) {
                OpenMeteoWeatherClient.fetchAkronWeather()
            } else {
                OpenMeteoWeatherClient.fetchWeather(location)
            }
        }.getOrElse {
            WeatherReport.placeholder(WeatherSourceStatus.Offline)
        }
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
            .safeDrawingPadding(),
    ) {
        LcarsResponsiveScaffold(
            modifier = Modifier.fillMaxSize(),
            portrait = {
                WeatherPortrait(
                    report = weatherReport,
                    alertActive = alertActive,
                    onToggleAlert = { alertActive = !alertActive },
                    modifier = Modifier.fillMaxSize(),
                )
            },
            compactLandscape = {
                WeatherCompactLandscape(
                    report = weatherReport,
                    alertActive = alertActive,
                    onToggleAlert = { alertActive = !alertActive },
                    modifier = Modifier.fillMaxSize(),
                )
            },
            wideLandscape = {
                WeatherWideLandscape(
                    report = weatherReport,
                    alertActive = alertActive,
                    onToggleAlert = { alertActive = !alertActive },
                    modifier = Modifier.fillMaxSize(),
                )
            },
        )
    }
}

@Composable
private fun WeatherWideLandscape(
    report: WeatherReport,
    alertActive: Boolean,
    onToggleAlert: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(modifier = modifier) {
        val dense = maxHeight < 760.dp

        WeatherPaddLandscapeFrame(
            alertActive = alertActive,
            onToggleAlert = onToggleAlert,
            compact = false,
            dense = dense,
            modifier = Modifier.fillMaxSize(),
        ) {
            WeatherMainDeck(
                report = report,
                alertActive = alertActive,
                compact = false,
                dense = dense,
                framed = true,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
private fun WeatherCompactLandscape(
    report: WeatherReport,
    alertActive: Boolean,
    onToggleAlert: () -> Unit,
    modifier: Modifier = Modifier,
) {
    WeatherPaddLandscapeFrame(
        alertActive = alertActive,
        onToggleAlert = onToggleAlert,
        compact = true,
        modifier = modifier,
    ) {
        WeatherMainDeck(
            report = report,
            alertActive = alertActive,
            compact = true,
            framed = true,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
private fun WeatherPaddLandscapeFrame(
    alertActive: Boolean,
    onToggleAlert: () -> Unit,
    compact: Boolean,
    dense: Boolean = compact,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val colors = LocalLcarsColors.current
    val spacing = LocalLcarsSpacing.current
    val compactFrame = compact || dense
    val leftWidth = if (compactFrame) 154.dp else 224.dp
    val barHeight = if (compactFrame) 18.dp else 28.dp
    val contentStart = if (compactFrame) 22.dp else 32.dp
    val contentTop = if (compactFrame) 22.dp else 28.dp

    Row(
        modifier = modifier
            .background(colors.background)
            .padding(spacing.gapLarge),
    ) {
        WeatherLeftFrame(
            alertActive = alertActive,
            onToggleAlert = onToggleAlert,
            compact = compactFrame,
            modifier = Modifier
                .width(leftWidth)
                .fillMaxHeight(),
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
        ) {
            RightFrameCorner(
                barHeight = barHeight,
                compact = compactFrame,
                modifier = Modifier.matchParentSize(),
            )
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(spacing.gapStandard),
            ) {
                WeatherBarPanel(
                    compact = compactFrame,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(barHeight),
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(start = contentStart, top = contentTop),
                ) {
                    content()
                }
            }
        }
    }
}

@Composable
private fun WeatherLeftFrame(
    alertActive: Boolean,
    onToggleAlert: () -> Unit,
    compact: Boolean,
    modifier: Modifier = Modifier,
) {
    val colors = LocalLcarsColors.current
    val gap = LocalLcarsSpacing.current.gapStandard

    Box(modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val radius = if (compact) 64.dp.toPx() else 98.dp.toPx()
            val body = Path().apply {
                moveTo(0f, size.height)
                lineTo(0f, radius)
                quadraticTo(0f, 0f, radius, 0f)
                lineTo(size.width, 0f)
                lineTo(size.width, size.height)
                close()
            }
            drawPath(path = body, color = colors.lightBlue)
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = if (compact) 64.dp else 96.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(gap)) {
                WeatherFramePanel("03-111968", colors.lightBlue, if (compact) 42.dp else 58.dp)
                WeatherFramePanel("04-041969", colors.violet, if (compact) 48.dp else 68.dp)
                WeatherFramePanel("05-1701D", colors.a7, if (compact) 54.dp else 88.dp)
                WeatherFramePanel(
                    text = "storm advisory",
                    color = if (alertActive) colors.alertRed else colors.lightBlue,
                    height = if (compact) 46.dp else 64.dp,
                    onClick = onToggleAlert,
                )
                WeatherFramePanel("forecast", colors.auxiliaryTan, if (compact) 42.dp else 58.dp)
            }
            WeatherFramePanel(
                text = if (alertActive) "clear advisory" else "met-07",
                color = if (alertActive) colors.alertRed else colors.a6,
                height = if (compact) 64.dp else 110.dp,
                onClick = onToggleAlert,
            )
        }
    }
}

@Composable
private fun WeatherFramePanel(
    text: String,
    color: Color,
    height: androidx.compose.ui.unit.Dp,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .background(color)
            .then(if (onClick == null) Modifier else Modifier.clickable(role = Role.Button, onClick = onClick))
            .padding(end = 14.dp, bottom = 8.dp),
        contentAlignment = Alignment.BottomEnd,
    ) {
        WeatherLabel(
            text = text,
            style = LocalLcarsTypography.current.labelSmall.copy(color = Color.Black),
        )
    }
}

@Composable
private fun WeatherBarPanel(
    compact: Boolean,
    modifier: Modifier = Modifier,
) {
    val colors = LocalLcarsColors.current
    val gap = LocalLcarsSpacing.current.gapStandard

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(gap),
    ) {
        Box(
            modifier = Modifier
                .weight(0.10f)
                .fillMaxHeight()
                .background(colors.lightBlue),
        )
        Box(
            modifier = Modifier
                .weight(0.28f)
                .fillMaxHeight()
                .background(colors.tacticalGreen),
        )
        Box(
            modifier = Modifier
                .weight(0.07f)
                .fillMaxHeight()
                .background(colors.violet),
        )
        Box(
            modifier = Modifier
                .weight(0.50f)
                .fillMaxHeight()
                .background(colors.lightBlue),
        )
        Box(
            modifier = Modifier
                .weight(if (compact) 0.08f else 0.05f)
                .fillMaxHeight()
                .background(colors.a2),
        )
    }
}

@Composable
private fun RightFrameCorner(
    barHeight: androidx.compose.ui.unit.Dp,
    compact: Boolean,
    modifier: Modifier = Modifier,
) {
    val colors = LocalLcarsColors.current

    Canvas(modifier = modifier) {
        val corner = if (compact) 32.dp.toPx() else 44.dp.toPx()
        val top = barHeight.toPx()
        val colorBridge = Path().apply {
            moveTo(0f, top)
            lineTo(corner, top)
            lineTo(0f, top + corner)
            close()
        }
        drawPath(path = colorBridge, color = colors.lightBlue)
        val maskRadius = corner * 0.62f
        val mask = Path().apply {
            moveTo(0f, top + maskRadius)
            quadraticTo(0f, top, maskRadius, top)
            lineTo(corner, top)
            lineTo(corner, top + corner)
            lineTo(0f, top + corner)
            close()
        }
        drawPath(path = mask, color = colors.background)
    }
}

@Composable
private fun WeatherPortrait(
    report: WeatherReport,
    alertActive: Boolean,
    onToggleAlert: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalLcarsColors.current
    val spacing = LocalLcarsSpacing.current
    val scroll = rememberScrollState()

    Column(
        modifier = modifier
            .padding(spacing.gapStandard)
            .verticalScroll(scroll),
        verticalArrangement = Arrangement.spacedBy(spacing.gapStandard),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(62.dp),
            horizontalArrangement = Arrangement.spacedBy(spacing.gapStandard),
        ) {
            LcarsElbow(
                text = "met",
                color = colors.lightBlue,
                direction = LcarsElbowDirection.TopLeft,
                wingWidth = 106.dp,
                wingHeight = 62.dp,
                thickness = 30.dp,
            )
            LcarsBar(
                color = colors.tacticalGreen,
                height = 34.dp,
                endCap = true,
                label = "atmospheric conditions",
                labelAlign = LcarsLabelAlign.End,
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.Top),
            )
        }
        WeatherButtonRow(alertActive = alertActive, onToggleAlert = onToggleAlert)
        AlertStrip(report = report, alertActive = alertActive)
        CurrentConditionsPanel(report = report, alertActive = alertActive, compact = true)
        ForecastPanel(report = report, compact = true)
        StationPanel(report = report, alertActive = alertActive, compact = true)
        LcarsFramePanel(title = "sensor cascade", footerLabel = "lower decks padd 24.2") {
            LcarsNumberMatrix(
                rows = 5,
                columns = 7,
                seed = if (alertActive) 44307 else 57436,
                highlightedRow = if (alertActive) 1 else 3,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(116.dp),
            )
            LcarsLogConsole(
                entries = weatherLogEntries(report, alertActive),
                maxLines = 4,
                autoScroll = false,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun WeatherMainDeck(
    report: WeatherReport,
    alertActive: Boolean,
    compact: Boolean,
    dense: Boolean = compact,
    framed: Boolean = false,
    modifier: Modifier = Modifier,
) {
    val colors = LocalLcarsColors.current
    val spacing = LocalLcarsSpacing.current

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(spacing.gapStandard),
    ) {
        if (framed) {
            WeatherDeckHeader(report = report, compact = compact || dense)
        } else {
            LcarsBar(
                color = colors.tacticalGreen,
                height = if (compact) 24.dp else 32.dp,
                startCap = true,
                endCap = true,
                label = if (compact) "wx ${report.locationLabel}" else "atmospheric conditions / ${report.locationLabel}",
                labelAlign = LcarsLabelAlign.End,
                labelColor = colors.a1,
            )
            LcarsDividerGrid(
                type = if (alertActive) LcarsDividerGridType.Type3 else LcarsDividerGridType.Type2,
                topHeight = if (compact) 10.dp else 16.dp,
                bottomHeight = if (compact) 10.dp else 16.dp,
            )
        }
        AlertStrip(report = report, alertActive = alertActive)
        if (compact) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(spacing.gapStandard),
            ) {
                CurrentConditionsPanel(
                    report = report,
                    alertActive = alertActive,
                    compact = true,
                    modifier = Modifier
                        .weight(0.58f)
                        .fillMaxHeight(),
                )
                Column(
                    modifier = Modifier
                        .weight(0.42f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(spacing.gapStandard),
                ) {
                    ForecastPanel(report = report, compact = true, modifier = Modifier.weight(1f))
                    LcarsLogConsole(
                        entries = weatherLogEntries(report, alertActive),
                        maxLines = 3,
                        autoScroll = false,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(spacing.gapStandard),
            ) {
                Column(
                    modifier = Modifier
                        .weight(0.58f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(spacing.gapStandard),
                ) {
                    CurrentConditionsPanel(
                        report = report,
                        alertActive = alertActive,
                        compact = false,
                        dense = dense,
                        modifier = Modifier.weight(1f),
                    )
                    LcarsLogConsole(
                        entries = weatherLogEntries(report, alertActive),
                        maxLines = if (dense) 3 else 4,
                        autoScroll = false,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(0.42f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(spacing.gapStandard),
                ) {
                    ForecastPanel(
                        report = report,
                        compact = false,
                        dense = dense,
                        modifier = Modifier.weight(if (dense) 0.58f else 0.56f),
                    )
                    StationPanel(
                        report = report,
                        alertActive = alertActive,
                        compact = false,
                        dense = dense,
                        modifier = Modifier.weight(if (dense) 0.42f else 0.44f),
                    )
                }
            }
        }
        if (!framed) {
            LcarsBar(
                color = colors.a8,
                height = if (compact) 8.dp else 12.dp,
                endCap = true,
                label = "lcars 57436.2 / lower decks padd palette",
                labelAlign = LcarsLabelAlign.End,
                labelColor = colors.a1,
            )
        }
    }
}

@Composable
private fun WeatherDeckHeader(report: WeatherReport, compact: Boolean) {
    val colors = LocalLcarsColors.current
    val typography = LocalLcarsTypography.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(if (compact) 44.dp else 58.dp),
        verticalAlignment = Alignment.Bottom,
    ) {
        WeatherLabel(
            text = "atmospheric conditions",
            style = typography.header.copy(
                color = colors.a1,
                fontSize = if (compact) 30.sp else 48.sp,
                lineHeight = if (compact) 32.sp else 50.sp,
            ),
            modifier = Modifier.weight(1f),
        )
        WeatherLabel(
            text = report.locationLabel,
            style = typography.button.copy(
                color = colors.tacticalGreen,
                fontSize = if (compact) 18.sp else 28.sp,
                lineHeight = if (compact) 20.sp else 30.sp,
                textAlign = TextAlign.End,
            ),
        )
    }
}

@Composable
private fun WeatherCommandRail(
    alertActive: Boolean,
    onToggleAlert: () -> Unit,
    compact: Boolean,
    modifier: Modifier = Modifier,
) {
    val colors = LocalLcarsColors.current
    val spacing = LocalLcarsSpacing.current
    val width = if (compact) 118.dp else 156.dp
    val elbowHeight = if (compact) 52.dp else 70.dp

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(spacing.gapStandard),
    ) {
        LcarsElbow(
            text = "wx-24",
            color = colors.lightBlue,
            direction = LcarsElbowDirection.TopLeft,
            wingWidth = width,
            wingHeight = elbowHeight,
            thickness = if (compact) 28.dp else 36.dp,
        )
        LcarsButton(
            text = "local wx",
            color = colors.lightBlue,
            shape = LcarsButtonShape.BlockStart,
            minWidth = 0.dp,
            minHeight = if (compact) 42.dp else 54.dp,
            modifier = Modifier.fillMaxWidth(),
            onClick = {},
        )
        LcarsButton(
            text = "radar",
            color = colors.tacticalGreen,
            shape = LcarsButtonShape.Rectangle,
            minWidth = 0.dp,
            minHeight = if (compact) 42.dp else 54.dp,
            modifier = Modifier.fillMaxWidth(),
            onClick = {},
        )
        LcarsButton(
            text = if (alertActive) "clear advisory" else "storm advisory",
            color = colors.violet,
            shape = LcarsButtonShape.Rectangle,
            alerting = alertActive,
            minWidth = 0.dp,
            minHeight = if (compact) 42.dp else 54.dp,
            modifier = Modifier.fillMaxWidth(),
            onClick = onToggleAlert,
        )
        LcarsButton(
            text = "forecast",
            color = colors.auxiliaryTan,
            shape = LcarsButtonShape.Rectangle,
            minWidth = 0.dp,
            minHeight = if (compact) 42.dp else 54.dp,
            modifier = Modifier.fillMaxWidth(),
            onClick = {},
        )
        LcarsButton(
            text = "archive",
            color = colors.a6,
            shape = LcarsButtonShape.BlockEnd,
            minWidth = 0.dp,
            minHeight = if (compact) 42.dp else 54.dp,
            modifier = Modifier.fillMaxWidth(),
            onClick = {},
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(colors.a7),
        )
        LcarsElbow(
            text = "met-07",
            color = if (alertActive) colors.alertRed else colors.lightBlue,
            direction = LcarsElbowDirection.BottomLeft,
            wingWidth = width,
            wingHeight = elbowHeight,
            thickness = if (compact) 28.dp else 36.dp,
        )
    }
}

@Composable
private fun WeatherButtonRow(
    alertActive: Boolean,
    onToggleAlert: () -> Unit,
) {
    val colors = LocalLcarsColors.current
    val spacing = LocalLcarsSpacing.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        horizontalArrangement = Arrangement.spacedBy(spacing.gapStandard),
    ) {
        LcarsButton(
            text = "local wx",
            color = colors.lightBlue,
            shape = LcarsButtonShape.BlockStart,
            minWidth = 0.dp,
            minHeight = 0.dp,
            modifier = Modifier.weight(1f),
            onClick = {},
        )
        LcarsButton(
            text = if (alertActive) "clear advisory" else "storm advisory",
            color = colors.violet,
            shape = LcarsButtonShape.Rectangle,
            alerting = alertActive,
            minWidth = 0.dp,
            minHeight = 0.dp,
            modifier = Modifier.weight(1f),
            onClick = onToggleAlert,
        )
        LcarsButton(
            text = "radar",
            color = colors.auxiliaryTan,
            shape = LcarsButtonShape.BlockEnd,
            minWidth = 0.dp,
            minHeight = 0.dp,
            modifier = Modifier.weight(1f),
            onClick = {},
        )
    }
}

@Composable
private fun AlertStrip(report: WeatherReport, alertActive: Boolean) {
    val message = when {
        alertActive -> "storm advisory active / pressure drop detected"
        !report.live -> "weather uplink pending / local cache displayed"
        report.advisorySuggested -> "live weather advisory suggested / review conditions"
        else -> "surface weather nominal / live uplink active"
    }

    LcarsAlertBanner(
        message = message,
        active = alertActive || report.advisorySuggested,
        level = if (alertActive || report.advisorySuggested) com.lcars.ui.LcarsAlertLevel.Warning else com.lcars.ui.LcarsAlertLevel.Normal,
    )
}

@Composable
private fun CurrentConditionsPanel(
    report: WeatherReport,
    alertActive: Boolean,
    compact: Boolean,
    dense: Boolean = compact,
    modifier: Modifier = Modifier,
) {
    val colors = LocalLcarsColors.current
    val typography = LocalLcarsTypography.current
    val spacing = LocalLcarsSpacing.current

    LcarsFramePanel(
        title = "current conditions",
        footerLabel = "${report.stationLabel} / ${String.format(Locale.US, "%.0f", report.pressureHpa)} hpa",
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(
                    when {
                        compact -> 132.dp
                        dense -> 224.dp
                        else -> 212.dp
                    },
                ),
            horizontalArrangement = Arrangement.spacedBy(spacing.gapStandard),
        ) {
            WeatherInspectGraphic(
                alertActive = alertActive,
                iconType = report.iconType,
                color = if (alertActive) colors.alertRed else colors.tacticalGreen,
                running = true,
                modifier = Modifier
                    .weight(0.42f)
                    .fillMaxHeight(),
            )
            Column(
                modifier = Modifier
                    .weight(0.58f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(spacing.gapStandard),
            ) {
                LcarsNumericLabel(
                    label = report.temperatureC.toString().padStart(3, '0'),
                    color = if (alertActive) colors.alertRed else colors.tacticalGreen,
                    height = if (compact) 34.dp else 36.dp,
                    labelWeight = if (compact) 0.20f else 0.17f,
                    rightWeight = if (compact) 0.38f else 0.41f,
                )
                WeatherLabel(
                    text = "${report.temperatureC} degrees / ${report.condition}",
                    style = typography.header.copy(
                        color = colors.a1,
                        fontSize = if (compact || dense) 28.sp else 42.sp,
                        lineHeight = if (compact || dense) 30.sp else 44.sp,
                        textAlign = TextAlign.End,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                )
                LcarsTelemetryPanel(
                    title = "surface telemetry",
                    entries = weatherTelemetry(report, alertActive),
                    alerting = alertActive,
                    singleColumnBelow = if (dense && !compact) 1.dp else 700.dp,
                    compact = dense && !compact,
                    modifier = Modifier.weight(1f),
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = if (dense && !compact) 14.dp else 0.dp),
            horizontalArrangement = Arrangement.spacedBy(spacing.gapStandard),
        ) {
            LcarsProgressBar(
                progress = (report.humidityPercent / 100f).coerceIn(0f, 1f),
                label = "humidity",
                color = colors.tacticalGreen,
                modifier = Modifier.weight(1f),
            )
            LcarsProgressBar(
                progress = ((report.windGustKt ?: report.windSpeedKt) / 40f).coerceIn(0f, 1f),
                label = "wind shear",
                color = colors.lightBlue,
                alerting = alertActive,
                modifier = Modifier.weight(1f),
            )
        }
        LcarsReadoutTicker(
            values = listOf(
                "cloud cover ${report.cloudCoverPercent?.let { "$it percent" } ?: "unavailable"}",
                "${report.windLabel} winds holding from station relay",
                "barometer reading ${String.format(Locale.US, "%.0f", report.pressureHpa)} hpa",
                "last station ${report.updatedLabel}",
            ),
            running = true,
            color = colors.a2,
        )
    }
}

@Composable
private fun WeatherInspectGraphic(
    alertActive: Boolean,
    iconType: WeatherIconType,
    color: Color,
    running: Boolean,
    modifier: Modifier = Modifier,
) {
    val spacing = LocalLcarsSpacing.current
    val colors = LocalLcarsColors.current

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spacing.gapStandard),
    ) {
        LcarsInspectBracket(
            color = color,
            markerColor = if (alertActive) colors.alertRed else colors.lightBlue,
            running = running,
            showSideRails = false,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
        ) {
            WeatherConditionGraphic(
                alertActive = alertActive,
                iconType = iconType,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
private fun ForecastPanel(
    report: WeatherReport,
    compact: Boolean,
    dense: Boolean = compact,
    modifier: Modifier = Modifier,
) {
    val colors = LocalLcarsColors.current
    val spacing = LocalLcarsSpacing.current
    val dailyForecast = report.dailyForecast.ifEmpty {
        report.forecast.mapIndexed { index, period ->
            WeatherForecastDay(
                dateKey = "fallback-$index",
                dayLabel = period.period,
                sky = period.sky,
                highTemperature = period.temperature,
                lowTemperature = period.temperature,
                iconType = period.iconType,
                highlighted = index == 0,
            )
        }
    }.take(5)
    var selectedDateKey by rememberSaveable { mutableStateOf(dailyForecast.firstOrNull()?.dateKey.orEmpty()) }

    LaunchedEffect(dailyForecast.firstOrNull()?.dateKey) {
        val validDateKeys = dailyForecast.map { it.dateKey }
        if (selectedDateKey !in validDateKeys) {
            selectedDateKey = validDateKeys.firstOrNull().orEmpty()
        }
    }

    val selectedDay = dailyForecast.firstOrNull { it.dateKey == selectedDateKey } ?: dailyForecast.firstOrNull()
    val forecastRows = forecastRowsForSelectedDay(
        report = report,
        selectedDateKey = selectedDay?.dateKey.orEmpty(),
    )

    LcarsFramePanel(
        title = "forecast matrix",
        footerLabel = selectedDay?.let { "${it.dayLabel} / four hour model" } ?: "five day model",
        modifier = modifier,
    ) {
        WeatherForecastIconStrip(
            days = dailyForecast,
            selectedDateKey = selectedDay?.dateKey.orEmpty(),
            onSelectDay = { selectedDateKey = it.dateKey },
            compact = dense,
            modifier = Modifier.fillMaxWidth(),
        )
        LcarsDataTable(
            headers = listOf("period", "sky", "temp", "wind"),
            compact = dense,
            rows = forecastRows.map { period ->
                LcarsDataRow(
                    listOf(period.period, period.sky, period.temperature, period.wind),
                    highlighted = period.highlighted,
                )
            },
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing.gapStandard),
        ) {
            LcarsStatusLight(
                label = "station link",
                active = report.live,
                color = colors.tacticalGreen,
                size = if (dense) 14.dp else 18.dp,
                compact = dense,
                modifier = Modifier.weight(1f),
            )
            LcarsStatusLight(
                label = "radar sweep",
                active = true,
                color = colors.a2,
                size = if (dense) 14.dp else 18.dp,
                compact = dense,
                modifier = Modifier.weight(1f),
            )
        }
        LcarsSegmentedMeter(
            activeSegments = when (report.sourceStatus) {
                WeatherSourceStatus.Live -> 9
                WeatherSourceStatus.Loading -> 5
                WeatherSourceStatus.Offline -> 3
            },
            totalSegments = 12,
            color = colors.tacticalGreen,
            inactiveColor = colors.a7,
            height = if (dense) 24.dp else 36.dp,
        )
    }
}

private fun forecastRowsForSelectedDay(
    report: WeatherReport,
    selectedDateKey: String,
): List<WeatherForecastPeriod> {
    val selectedRows = report.hourlyForecast
        .filter { it.dateKey == selectedDateKey }
        .filterIndexed { index, _ -> index % 4 == 0 }
        .take(6)

    return selectedRows.ifEmpty { report.forecast }.mapIndexed { index, period ->
        period.copy(highlighted = index == 0)
    }
}

@Composable
private fun StationPanel(
    report: WeatherReport,
    alertActive: Boolean,
    compact: Boolean,
    dense: Boolean = compact,
    modifier: Modifier = Modifier,
) {
    val colors = LocalLcarsColors.current
    val spacing = LocalLcarsSpacing.current
    val compressed = dense && !compact
    val relayLabel = when (report.sourceStatus) {
        WeatherSourceStatus.Live -> "live"
        WeatherSourceStatus.Loading -> "pending"
        WeatherSourceStatus.Offline -> "cached"
    }

    LcarsFramePanel(
        title = "station diagnostics",
        footerLabel = if (compressed) "${report.updatedLabel} / relay $relayLabel" else report.updatedLabel,
        modifier = modifier,
    ) {
        LcarsNumberMatrix(
            rows = if (compact || compressed) 4 else 6,
            columns = if (compact || compressed) 6 else 9,
            seed = if (alertActive) 44307 else 67081,
            running = true,
            highlightedRow = if (alertActive) 2 else null,
            modifier = Modifier
                .fillMaxWidth()
                .height(if (compact) 92.dp else if (compressed) 48.dp else 132.dp),
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing.gapStandard),
        ) {
            LcarsProgressBar(
                progress = 0.78f,
                label = "uplink",
                color = colors.lightBlue,
                height = if (compressed) 14.dp else if (compact) 18.dp else 24.dp,
                modifier = Modifier.weight(1f),
            )
            LcarsProgressBar(
                progress = if (alertActive) 0.91f else 0.46f,
                label = "variance",
                color = colors.violet,
                alerting = alertActive,
                height = if (compressed) 14.dp else if (compact) 18.dp else 24.dp,
                modifier = Modifier.weight(1f),
            )
        }
        if (compressed) {
            StationVectorsInlinePanel(
                entries = stationVectorEntries(report = report, relayLabel = relayLabel),
                alerting = alertActive,
            )
        } else {
            LcarsTelemetryPanel(
                title = "station vectors",
                entries = stationVectorEntries(report = report, relayLabel = relayLabel),
                alerting = alertActive,
                singleColumnBelow = 700.dp,
            )
        }
    }
}

private fun stationVectorEntries(
    report: WeatherReport,
    relayLabel: String,
): List<LcarsTelemetryEntry> = listOf(
    LcarsTelemetryEntry("lat", report.latitudeLabel),
    LcarsTelemetryEntry("lon", report.longitudeLabel),
    LcarsTelemetryEntry("alt", report.elevationLabel),
    LcarsTelemetryEntry(
        "relay",
        relayLabel,
        if (report.live) LcarsTelemetryStatus.Normal else LcarsTelemetryStatus.Warning,
    ),
)

@Composable
private fun StationVectorsInlinePanel(
    entries: List<LcarsTelemetryEntry>,
    alerting: Boolean,
    modifier: Modifier = Modifier,
) {
    val colors = LocalLcarsColors.current
    val spacing = LocalLcarsSpacing.current
    val typography = LocalLcarsTypography.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.panel)
            .padding(spacing.gapStandard),
        verticalArrangement = Arrangement.spacedBy(spacing.gapStandard / 2f),
    ) {
        WeatherLabel(
            text = "station vectors",
            style = typography.labelSmall.copy(
                color = colors.auxiliaryTan,
                fontSize = 13.sp,
                lineHeight = 14.sp,
            ),
            maxLines = 1,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing.gapStandard / 2f),
        ) {
            entries.forEach { entry ->
                val valueColor = when {
                    alerting -> colors.alertRed
                    entry.status == LcarsTelemetryStatus.Warning -> colors.monoAmber
                    entry.status == LcarsTelemetryStatus.Normal -> colors.tacticalGreen
                    else -> colors.lightBlue
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .background(Color.Black)
                        .padding(horizontal = 6.dp, vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(0.dp),
                ) {
                    WeatherLabel(
                        text = entry.label,
                        style = typography.labelSmall.copy(
                            color = colors.a3,
                            fontSize = 10.sp,
                            lineHeight = 11.sp,
                        ),
                        maxLines = 1,
                    )
                    WeatherLabel(
                        text = entry.value,
                        style = typography.telemetry.copy(
                            color = valueColor,
                            fontSize = 12.sp,
                            lineHeight = 13.sp,
                        ),
                        maxLines = 1,
                    )
                }
            }
        }
    }
}

@Composable
private fun WeatherConditionGraphic(
    alertActive: Boolean,
    iconType: WeatherIconType,
    modifier: Modifier = Modifier,
) {
    val colors = LocalLcarsColors.current

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        WeatherGlyph(
            iconType = iconType,
            modifier = Modifier
                .fillMaxWidth(0.72f)
                .fillMaxHeight(0.72f),
        )
        LcarsTargetScanner(
            running = true,
            color = if (alertActive) colors.alertRed else colors.a2,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
private fun WeatherGlyph(
    iconType: WeatherIconType,
    modifier: Modifier = Modifier,
) {
    val colors = LocalLcarsColors.current

    Canvas(modifier = modifier) {
        drawRect(
            color = colors.lightBlue.copy(alpha = 0.12f),
            size = size,
        )
        drawWeatherIcon(iconType = iconType, colors = colors)
        drawRect(
            color = colors.a2,
            style = Stroke(width = 3.dp.toPx()),
            size = Size(size.width, size.height),
        )
    }
}

@Composable
private fun WeatherForecastIconStrip(
    days: List<WeatherForecastDay>,
    selectedDateKey: String,
    onSelectDay: (WeatherForecastDay) -> Unit,
    compact: Boolean,
    modifier: Modifier = Modifier,
) {
    val colors = LocalLcarsColors.current
    val spacing = LocalLcarsSpacing.current
    val typography = LocalLcarsTypography.current

    Column(
        modifier = modifier.height(if (compact) 48.dp else 64.dp),
        verticalArrangement = Arrangement.spacedBy(spacing.gapStandard),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(spacing.gapStandard),
        ) {
            days.forEach { day ->
                val selected = day.dateKey == selectedDateKey
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(if (selected) colors.panel else Color(0xFF050505))
                        .clickable(role = Role.Button) { onSelectDay(day) },
                ) {
                    WeatherMiniIcon(
                        iconType = day.iconType,
                        highlighted = selected,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 3.dp, vertical = 2.dp),
                    )
                    if (selected) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            drawRect(
                                color = colors.monoAmber,
                                style = Stroke(width = if (compact) 2.dp.toPx() else 3.dp.toPx()),
                                size = Size(size.width, size.height),
                            )
                            drawRect(
                                color = colors.tacticalGreen,
                                size = Size(width = if (compact) 3.dp.toPx() else 4.dp.toPx(), height = size.height),
                            )
                        }
                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(if (compact) 10.dp else 12.dp),
            horizontalArrangement = Arrangement.spacedBy(spacing.gapStandard),
        ) {
            days.forEach { day ->
                val selected = day.dateKey == selectedDateKey
                WeatherLabel(
                    text = "${day.dayLabel} ${day.highTemperature}/${day.lowTemperature}",
                    style = typography.labelSmall.copy(
                        color = if (selected) colors.monoAmber else colors.a1,
                        fontSize = if (compact) 8.sp else 10.sp,
                        lineHeight = if (compact) 9.sp else 11.sp,
                        textAlign = TextAlign.Center,
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(Color(0xFF050505)),
                )
            }
        }
    }
}

@Composable
private fun WeatherMiniIcon(
    iconType: WeatherIconType,
    highlighted: Boolean,
    modifier: Modifier = Modifier,
) {
    val colors = LocalLcarsColors.current

    Canvas(modifier = modifier) {
        if (highlighted) {
            drawRect(color = colors.tacticalGreen.copy(alpha = 0.16f), size = size)
        }
        drawWeatherIcon(iconType = iconType, colors = colors, compact = true)
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawWeatherIcon(
    iconType: WeatherIconType,
    colors: com.lcars.ui.LcarsColors,
    compact: Boolean = false,
) {
    val stroke = if (compact) 2.dp.toPx() else 3.dp.toPx()
    val sunRadius = size.minDimension * if (compact) 0.20f else 0.18f

    fun drawSun(center: Offset, radius: Float) {
        drawCircle(color = colors.tacticalGreen, radius = radius, center = center)
        repeat(12) { index ->
            val angle = (index / 12f) * PI.toFloat() * 2f
            drawLine(
                color = colors.a1,
                start = Offset(
                    center.x + cos(angle) * radius * 1.30f,
                    center.y + sin(angle) * radius * 1.30f,
                ),
                end = Offset(
                    center.x + cos(angle) * radius * 1.78f,
                    center.y + sin(angle) * radius * 1.78f,
                ),
                strokeWidth = stroke,
            )
        }
    }

    fun drawRainLines(count: Int, color: Color = colors.lightBlue) {
        repeat(count) { index ->
            val x = size.width * (0.30f + index * 0.12f)
            drawLine(
                color = color,
                start = Offset(x, size.height * 0.66f),
                end = Offset(x - size.width * 0.05f, size.height * 0.88f),
                strokeWidth = stroke,
            )
        }
    }

    fun drawSnowMarks() {
        repeat(3) { index ->
            val center = Offset(size.width * (0.32f + index * 0.17f), size.height * 0.78f)
            val radius = size.minDimension * 0.035f
            drawLine(colors.a1, Offset(center.x - radius, center.y), Offset(center.x + radius, center.y), stroke)
            drawLine(colors.a1, Offset(center.x, center.y - radius), Offset(center.x, center.y + radius), stroke)
        }
    }

    fun drawFogLines() {
        repeat(3) { index ->
            val y = size.height * (0.66f + index * 0.10f)
            drawLine(
                color = colors.a2,
                start = Offset(size.width * 0.22f, y),
                end = Offset(size.width * 0.78f, y),
                strokeWidth = stroke,
            )
        }
    }

    fun drawLightning() {
        val path = Path().apply {
            moveTo(size.width * 0.53f, size.height * 0.56f)
            lineTo(size.width * 0.42f, size.height * 0.76f)
            lineTo(size.width * 0.53f, size.height * 0.74f)
            lineTo(size.width * 0.45f, size.height * 0.94f)
            lineTo(size.width * 0.67f, size.height * 0.66f)
            lineTo(size.width * 0.56f, size.height * 0.68f)
            close()
        }
        drawPath(path = path, color = colors.monoAmber)
    }

    fun drawHail() {
        repeat(3) { index ->
            drawCircle(
                color = colors.a1,
                radius = size.minDimension * 0.035f,
                center = Offset(size.width * (0.32f + index * 0.17f), size.height * 0.78f),
            )
        }
    }

    when (iconType) {
        WeatherIconType.Sun -> drawSun(
            center = Offset(size.width * 0.50f, size.height * 0.50f),
            radius = sunRadius * 1.15f,
        )
        WeatherIconType.SunCloud -> {
            drawSun(Offset(size.width * 0.62f, size.height * 0.36f), sunRadius)
            drawCloud(Offset(size.width * 0.42f, size.height * 0.62f), size.width * 0.54f, colors.a4)
        }
        WeatherIconType.Cloud -> {
            drawCloud(Offset(size.width * 0.48f, size.height * 0.58f), size.width * 0.62f, colors.a4)
        }
        WeatherIconType.Overcast -> {
            drawCloud(Offset(size.width * 0.42f, size.height * 0.52f), size.width * 0.58f, colors.a7)
            drawCloud(Offset(size.width * 0.56f, size.height * 0.64f), size.width * 0.56f, colors.a4)
        }
        WeatherIconType.Fog -> {
            drawCloud(Offset(size.width * 0.48f, size.height * 0.48f), size.width * 0.56f, colors.a4)
            drawFogLines()
        }
        WeatherIconType.Drizzle -> {
            drawCloud(Offset(size.width * 0.48f, size.height * 0.48f), size.width * 0.58f, colors.a4)
            drawRainLines(count = 3, color = colors.a2)
        }
        WeatherIconType.Rain -> {
            drawCloud(Offset(size.width * 0.48f, size.height * 0.46f), size.width * 0.60f, colors.a4)
            drawRainLines(count = 4)
        }
        WeatherIconType.Snow -> {
            drawCloud(Offset(size.width * 0.48f, size.height * 0.46f), size.width * 0.60f, colors.a4)
            drawSnowMarks()
        }
        WeatherIconType.Showers -> {
            drawCloud(Offset(size.width * 0.44f, size.height * 0.44f), size.width * 0.52f, colors.a7)
            drawCloud(Offset(size.width * 0.56f, size.height * 0.54f), size.width * 0.54f, colors.a4)
            drawRainLines(count = 5)
        }
        WeatherIconType.Thunder -> {
            drawCloud(Offset(size.width * 0.48f, size.height * 0.44f), size.width * 0.60f, colors.a4)
            drawLightning()
        }
        WeatherIconType.Hail -> {
            drawCloud(Offset(size.width * 0.48f, size.height * 0.44f), size.width * 0.60f, colors.a4)
            drawHail()
        }
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawCloud(
    origin: Offset,
    width: Float,
    color: Color,
) {
    val height = width * 0.32f
    val path = Path().apply {
        moveTo(origin.x - width * 0.44f, origin.y + height * 0.28f)
        cubicTo(
            origin.x - width * 0.38f,
            origin.y - height * 0.16f,
            origin.x - width * 0.18f,
            origin.y - height * 0.22f,
            origin.x - width * 0.10f,
            origin.y - height * 0.34f,
        )
        cubicTo(
            origin.x + width * 0.02f,
            origin.y - height * 0.68f,
            origin.x + width * 0.24f,
            origin.y - height * 0.48f,
            origin.x + width * 0.26f,
            origin.y - height * 0.12f,
        )
        cubicTo(
            origin.x + width * 0.48f,
            origin.y - height * 0.14f,
            origin.x + width * 0.54f,
            origin.y + height * 0.26f,
            origin.x + width * 0.36f,
            origin.y + height * 0.42f,
        )
        lineTo(origin.x - width * 0.36f, origin.y + height * 0.42f)
        cubicTo(
            origin.x - width * 0.48f,
            origin.y + height * 0.38f,
            origin.x - width * 0.52f,
            origin.y + height * 0.30f,
            origin.x - width * 0.44f,
            origin.y + height * 0.28f,
        )
        close()
    }
    drawPath(path = path, color = color)
}

private const val USE_FALLBACK_TEXT = false

@Composable
private fun WeatherLabel(
    text: String,
    style: TextStyle,
    modifier: Modifier = Modifier,
    maxLines: Int = 1,
) {
    if (USE_FALLBACK_TEXT) {
        BasicText(
            text = text.uppercase(Locale.US),
            modifier = modifier,
            style = style,
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis,
        )
    } else {
        LcarsText(
            text = text,
            style = style,
            modifier = modifier,
            maxLines = maxLines,
        )
    }
}

private fun weatherTelemetry(report: WeatherReport, alertActive: Boolean): List<LcarsTelemetryEntry> = listOf(
    LcarsTelemetryEntry(
        "conditions",
        if (alertActive) "storm watch" else report.condition,
        if (report.advisorySuggested) LcarsTelemetryStatus.Warning else LcarsTelemetryStatus.Normal,
    ),
    LcarsTelemetryEntry(
        "humidity",
        "${report.humidityPercent}%",
        if (report.humidityPercent >= 85) LcarsTelemetryStatus.Warning else LcarsTelemetryStatus.Normal,
    ),
    LcarsTelemetryEntry("wind", report.windLabel, LcarsTelemetryStatus.Neutral),
    LcarsTelemetryEntry(
        "pressure",
        "${String.format(Locale.US, "%.0f", report.pressureHpa)} hpa",
        if (alertActive || report.pressureHpa < 1002.0) LcarsTelemetryStatus.Warning else LcarsTelemetryStatus.Normal,
    ),
    LcarsTelemetryEntry(
        "feels like",
        report.apparentTemperatureC?.let { "$it degrees" } ?: "no data",
        LcarsTelemetryStatus.Neutral,
    ),
    LcarsTelemetryEntry(
        "precip",
        report.precipitationInches?.let { String.format(Locale.US, "%.2f in", it) } ?: "no data",
        LcarsTelemetryStatus.Neutral,
    ),
)

private fun weatherLogEntries(report: WeatherReport, alertActive: Boolean): List<LcarsLogEntry> = buildList {
    when (report.sourceStatus) {
        WeatherSourceStatus.Live -> add(LcarsLogEntry("open-meteo uplink established", LcarsLogSeverity.Success, "wx"))
        WeatherSourceStatus.Loading -> add(LcarsLogEntry("weather uplink acquisition pending", LcarsLogSeverity.Info, "wx"))
        WeatherSourceStatus.Offline -> add(LcarsLogEntry("weather uplink offline; local cache active", LcarsLogSeverity.Alert, "wx"))
    }
    add(LcarsLogEntry("${report.locationLabel} loaded", LcarsLogSeverity.Info, "loc"))
    add(LcarsLogEntry("sky classified: ${report.condition}", LcarsLogSeverity.Info, "sky"))
    if (alertActive) {
        add(LcarsLogEntry("pressure variance exceeds advisory limit", LcarsLogSeverity.Alert, "met"))
    } else if (report.advisorySuggested) {
        add(LcarsLogEntry("live conditions suggest advisory review", LcarsLogSeverity.Alert, "met"))
    } else {
        add(LcarsLogEntry("pressure trend inside nominal corridor", LcarsLogSeverity.Success, "met"))
    }
    add(LcarsLogEntry("forecast model ${report.updatedLabel}", if (report.live) LcarsLogSeverity.Success else LcarsLogSeverity.Info, "fct"))
}

@Preview(widthDp = 1366, heightDp = 768, showBackground = true)
@Composable
private fun AtmosphericConditionsLandscapePreview() {
    DemoLcarsTheme {
        AtmosphericConditionsDemoScreen(modifier = Modifier.fillMaxSize())
    }
}

@Preview(widthDp = 390, heightDp = 820, showBackground = true)
@Composable
private fun AtmosphericConditionsPortraitPreview() {
    DemoLcarsTheme {
        AtmosphericConditionsDemoScreen(modifier = Modifier.fillMaxSize())
    }
}
