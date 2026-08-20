package com.lcars.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lcars.ui.controls.LcarsButton
import com.lcars.ui.controls.LcarsButtonShape
import com.lcars.ui.display.LcarsAlertBanner
import com.lcars.ui.display.LcarsAlertLevel
import com.lcars.ui.display.LcarsDataCascade
import com.lcars.ui.display.LcarsDiagnosticGrid
import com.lcars.ui.display.LcarsHistogram
import com.lcars.ui.display.LcarsHistogramBar
import com.lcars.ui.display.LcarsLogConsole
import com.lcars.ui.display.LcarsLogEntry
import com.lcars.ui.display.LcarsLogSeverity
import com.lcars.ui.display.LcarsNumberMatrix
import com.lcars.ui.display.LcarsProgressBar
import com.lcars.ui.display.LcarsSegmentedMeter
import com.lcars.ui.display.LcarsSegmentedSlider
import com.lcars.ui.display.LcarsStatusLight
import com.lcars.ui.display.LcarsTelemetryEntry
import com.lcars.ui.display.LcarsTelemetryPanel
import com.lcars.ui.display.LcarsTelemetryStatus
import com.lcars.ui.foundation.LcarsText
import com.lcars.ui.layout.LcarsStandardFrame
import com.lcars.ui.theme.LcarsTheme

@Composable
fun TelemetryDisplaysShowcaseScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LcarsTheme.colorScheme
    val spacing = LcarsTheme.dimensions

    var activeCategory by rememberSaveable { mutableStateOf("METERS & SLIDERS") }
    var sliderValue by rememberSaveable { mutableIntStateOf(14) }
    var logCount by rememberSaveable { mutableIntStateOf(4) }

    val logEntries = remember {
        mutableStateListOf(
            LcarsLogEntry("OPTICAL SUBPROCESSOR 047 INITIALIZED", LcarsLogSeverity.Info, "047"),
            LcarsLogEntry("SUBSYSTEM BUS SYNC STABLE", LcarsLogSeverity.Success, "BUS"),
            LcarsLogEntry("PLASMA CONDUIT PRESSURE ELEVATED", LcarsLogSeverity.Warning, "EPS"),
            LcarsLogEntry("DEFLECTOR SHIELD MODULATION NOMINAL", LcarsLogSeverity.Info, "SHD"),
        )
    }

    val categories = listOf("METERS & SLIDERS", "DIAGNOSTIC & LOG", "TELEMETRY & CASCADE")

    LcarsStandardFrame(
        title = "TELEMETRY & DATA DISPLAYS",
        headerCode = "02 / DATA",
        footerCode = "SYS 42",
        statusText = "TELEMETRY DATA STREAMS // ACTIVE",
        frameColor = colors.lightBlue,
        accentColor = colors.monoAmber,
        leftRailWidth = 140.dp,
        modifier = modifier,
        leftRailContent = {
            LcarsButton(
                text = "RETURN",
                onClick = onBack,
                color = colors.alertRed,
                shape = LcarsButtonShape.BlockStart,
                modifier = Modifier.fillMaxWidth(),
                minHeight = 44.dp,
            )
            Spacer(modifier = Modifier.height(spacing.gapStandard))
            categories.forEach { category ->
                val isSelected = category == activeCategory
                LcarsButton(
                    text = category,
                    onClick = { activeCategory = category },
                    color = if (isSelected) Color.White else colors.commandSecondary,
                    shape = LcarsButtonShape.Rectangle,
                    selected = isSelected,
                    modifier = Modifier.fillMaxWidth(),
                    minHeight = 40.dp,
                )
                Spacer(modifier = Modifier.height(spacing.gapStandard))
            }
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            when (activeCategory) {
                "METERS & SLIDERS" -> {
                    // SECTION 1: SEGMENTED SLIDER
                    ShowcaseCard(title = "SEGMENTED SLIDER (VALUE: $sliderValue / 20)") {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            LcarsSegmentedSlider(
                                value = sliderValue,
                                onValueChange = { sliderValue = it },
                                totalSegments = 20,
                                color = colors.monoAmber,
                                inactiveColor = colors.a8,
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                    }

                    // SECTION 2: HISTOGRAM SENSOR METERS
                    ShowcaseCard(title = "HISTOGRAM SENSOR SPECTRUM") {
                        val histogramBars = listOf(
                            LcarsHistogramBar("RAD", (sliderValue / 20f).coerceIn(0.1f, 1f), "${sliderValue * 5}%", colors.monoAmber),
                            LcarsHistogramBar("EPS", 0.85f, "85%", colors.lightBlue),
                            LcarsHistogramBar("MAG", 0.62f, "62%", colors.auxiliaryTan),
                            LcarsHistogramBar("THM", 0.44f, "44%", colors.tacticalGreen),
                        )
                        LcarsHistogram(
                            bars = histogramBars,
                            barHeight = 22.dp,
                            gap = 6.dp,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }

                    // SECTION 3: PROGRESS BARS & STATUS LIGHTS
                    ShowcaseCard(title = "PROGRESS BARS & ALERT BANNERS") {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            LcarsProgressBar(
                                progress = (sliderValue / 20f).coerceIn(0f, 1f),
                                label = "ENERGY BUFFER",
                                color = colors.monoAmber,
                                modifier = Modifier.fillMaxWidth(),
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                LcarsStatusLight(label = "CORE", active = true, color = colors.tacticalGreen, modifier = Modifier.weight(1f))
                                LcarsStatusLight(label = "WARP", active = sliderValue > 10, color = colors.monoAmber, modifier = Modifier.weight(1f))
                                LcarsStatusLight(label = "ALERT", active = sliderValue > 16, color = colors.alertRed, modifier = Modifier.weight(1f))
                            }
                            if (sliderValue > 16) {
                                LcarsAlertBanner(
                                    message = "HIGH POWER SURGE DETECTED",
                                    active = true,
                                    level = LcarsAlertLevel.Warning,
                                    modifier = Modifier.fillMaxWidth(),
                                )
                            }
                        }
                    }
                }

                "DIAGNOSTIC & LOG" -> {
                    // SECTION 1: OPTICAL SUBPROCESSOR DIAGNOSTIC GRID
                    ShowcaseCard(title = "DIAGNOSTIC GRID MATRIX") {
                        Box(modifier = Modifier.fillMaxWidth().height(160.dp)) {
                            LcarsDiagnosticGrid(
                                title = "OPTICAL SUBPROCESSOR 047",
                                subtitle = "DYNAMIC TELEMETRY MATRIX",
                                matrixRows = 4,
                                matrixColumns = 4,
                                hasAlertZone = true,
                                alertZoneColor = colors.monoAmber,
                                modifier = Modifier.fillMaxSize(),
                            )
                        }
                    }

                    // SECTION 2: REAL-TIME LOG CONSOLE
                    ShowcaseCard(title = "LOG CONSOLE STREAM") {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            LcarsLogConsole(
                                entries = logEntries,
                                maxLines = 5,
                                autoScroll = true,
                                modifier = Modifier.fillMaxWidth(),
                            )
                            LcarsButton(
                                text = "EMIT LOG ENTRY",
                                onClick = {
                                    logCount++
                                    logEntries.add(
                                        LcarsLogEntry(
                                            message = "CYCLE $logCount: BUFFER DRAIN ${(logCount * 17) % 100}%",
                                            severity = if (logCount % 3 == 0) LcarsLogSeverity.Warning else LcarsLogSeverity.Info,
                                            code = "CYC",
                                        )
                                    )
                                },
                                color = colors.monoAmber,
                                shape = LcarsButtonShape.Pill,
                                modifier = Modifier.fillMaxWidth(),
                                minHeight = 36.dp,
                            )
                        }
                    }
                }

                "TELEMETRY & CASCADE" -> {
                    // SECTION 1: 7-ROW DATA CASCADE
                    ShowcaseCard(title = "DATA CASCADE 24.2") {
                        LcarsDataCascade(
                            modifier = Modifier.fillMaxWidth(),
                            accentColor = colors.monoAmber,
                            highlightColor = Color.White,
                        )
                    }

                    // SECTION 2: NUMBER MATRIX
                    ShowcaseCard(title = "NUMBER MATRIX (SCANNING REVEAL)") {
                        LcarsNumberMatrix(
                            rows = 4,
                            columns = 6,
                            running = true,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }

                    // SECTION 3: TELEMETRY PANEL
                    ShowcaseCard(title = "TELEMETRY PANEL") {
                        val telemetryEntries = listOf(
                            LcarsTelemetryEntry("HELM SPEED", "0.45 C", LcarsTelemetryStatus.Normal),
                            LcarsTelemetryEntry("HEADING", "184.2 MK", LcarsTelemetryStatus.Normal),
                            LcarsTelemetryEntry("SHIELD HARMONIC", "434.9 MHZ", LcarsTelemetryStatus.Warning),
                            LcarsTelemetryEntry("REACTOR", "ONLINE", LcarsTelemetryStatus.Normal),
                        )
                        LcarsTelemetryPanel(
                            title = "PROPULSION TELEMETRY",
                            entries = telemetryEntries,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ShowcaseCard(
    title: String,
    content: @Composable () -> Unit,
) {
    val colors = LcarsTheme.colorScheme
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.panel)
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        LcarsText(
            text = title,
            style = LcarsTheme.typography.labelSmall.copy(color = colors.monoAmber, fontSize = 12.sp),
        )
        content()
    }
}
