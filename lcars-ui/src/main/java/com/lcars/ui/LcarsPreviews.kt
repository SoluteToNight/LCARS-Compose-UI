package com.lcars.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview(name = "LCARS components narrow", widthDp = 320, heightDp = 620, showBackground = true)
@Composable
private fun LcarsComponentsNarrowPreview() {
    LcarsTheme {
        val colors = LocalLcarsColors.current
        val gap = LocalLcarsSpacing.current.gapStandard

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.background)
                .padding(gap)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(gap),
        ) {
            LcarsBar(
                color = colors.lightBlue,
                height = 22.dp,
                startCap = true,
                endCap = true,
                label = "narrow component preview",
            )
            Row(horizontalArrangement = Arrangement.spacedBy(gap)) {
                LcarsButton(
                    text = "long command label",
                    minWidth = 0.dp,
                    modifier = Modifier.weight(1f),
                    onClick = {},
                )
                LcarsButton(
                    text = "block",
                    shape = LcarsButtonShape.BlockStart,
                    minWidth = 0.dp,
                    modifier = Modifier.weight(1f),
                    onClick = {},
                )
            }
            LcarsAlertBanner(message = "critical interface alert", active = true)
            LcarsTelemetryPanel(
                title = "primary telemetry matrix",
                entries = previewTelemetry(),
            )
            LcarsFramePanel(title = "readout stack", footerLabel = "frame") {
                LcarsProgressBar(progress = 0.72f, label = "reactor balance")
                LcarsSegmentedMeter(activeSegments = 7, totalSegments = 12)
                LcarsReadoutTicker(values = listOf("SP3 FLOW CAPTURED"), running = false)
            }
        }
    }
}

@Preview(name = "LCARS components landscape", widthDp = 720, heightDp = 360, showBackground = true)
@Composable
private fun LcarsComponentsLandscapePreview() {
    LcarsTheme {
        val colors = LocalLcarsColors.current
        val gap = LocalLcarsSpacing.current.gapStandard

        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.background)
                .padding(gap),
            horizontalArrangement = Arrangement.spacedBy(gap),
        ) {
            Column(
                modifier = Modifier
                    .width(154.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(gap),
            ) {
                LcarsElbow(
                    text = "nav-407",
                    color = colors.monoAmber,
                    direction = LcarsElbowDirection.TopLeft,
                    wingWidth = 154.dp,
                    wingHeight = 58.dp,
                    thickness = 30.dp,
                )
                LcarsButton(
                    text = "scanner",
                    minWidth = 0.dp,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {},
                )
                LcarsButton(
                    text = "catalog",
                    shape = LcarsButtonShape.BlockStart,
                    color = colors.lightBlue,
                    minWidth = 0.dp,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {},
                )
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(colors.a8),
                )
                LcarsElbow(
                    text = "term-01",
                    color = colors.monoAmber,
                    direction = LcarsElbowDirection.BottomLeft,
                    wingWidth = 154.dp,
                    wingHeight = 58.dp,
                    thickness = 30.dp,
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(gap),
            ) {
                LcarsBar(
                    color = colors.lightBlue,
                    height = 22.dp,
                    startCap = true,
                    endCap = true,
                    label = "landscape component preview",
                )
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(gap),
                ) {
                    LcarsTelemetryPanel(
                        title = "telemetry",
                        entries = previewTelemetry(),
                        singleColumnBelow = 0.dp,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                    )
                    LcarsFramePanel(
                        title = "dynamic states",
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                    ) {
                        LcarsStatusLight(label = "sensor lock", active = true)
                        LcarsProgressBar(progress = 0.58f, label = "grid sync")
                        LcarsScannerSweep(
                            running = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(88.dp),
                        )
                    }
                }
                LcarsBar(color = colors.a7, height = 8.dp, endCap = true)
            }
        }
    }
}

private fun previewTelemetry(): List<LcarsTelemetryEntry> = listOf(
    LcarsTelemetryEntry("latitude", "30.542314 n"),
    LcarsTelemetryEntry("longitude", "114.367852 e"),
    LcarsTelemetryEntry("sat visible", "24", LcarsTelemetryStatus.Normal),
    LcarsTelemetryEntry("fix status", "high precision", LcarsTelemetryStatus.Warning),
)
