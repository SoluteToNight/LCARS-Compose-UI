package com.lcars.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Preview(name = "LCARS next components narrow", widthDp = 360, heightDp = 780, showBackground = true)
@Composable
private fun LcarsNextComponentsNarrowPreview() {
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
                height = 24.dp,
                startCap = true,
                endCap = true,
                label = "next components",
            )
            LcarsSegmentedControl(
                options = listOf("NAV", "COMM", "SENSOR"),
                selectedOption = "NAV",
                onOptionSelected = {},
            )
            LcarsToggle(
                checked = true,
                onCheckedChange = {},
                checkedLabel = "armed",
                uncheckedLabel = "standby",
            )
            LcarsNumericLabel(label = "407")
            LcarsNumberMatrix(
                rows = 5,
                columns = 8,
                seed = 407,
                running = false,
                highlightedRow = 2,
                modifier = Modifier.height(110.dp),
            )
            LcarsLogConsole(
                entries = previewLogs(),
                maxLines = 4,
                compact = true,
                modifier = Modifier.height(96.dp),
            )
            LcarsDividerGrid(type = LcarsDividerGridType.Type2)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(colors.panel),
            ) {
                LcarsTargetScanner(running = true, modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@Preview(name = "LCARS next components landscape", widthDp = 840, heightDp = 390, showBackground = true)
@Composable
private fun LcarsNextComponentsLandscapePreview() {
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
            LcarsCommandRail(
                items = listOf(
                    LcarsCommandRailItem("nav", "nav", color = colors.a5),
                    LcarsCommandRailItem("comm", "comm", color = colors.lightBlue),
                    LcarsCommandRailItem("alert", "alert", type = LcarsCommandRailItemType.AlertBlock),
                    LcarsCommandRailItem("hold", "hold", type = LcarsCommandRailItemType.PassiveBlock),
                    LcarsCommandRailItem("space", type = LcarsCommandRailItemType.SpacerBlock, weight = 1f),
                ),
                compact = true,
                modifier = Modifier
                    .width(140.dp)
                    .fillMaxHeight(),
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(gap),
            ) {
                LcarsStarChart(
                    mode = LcarsStarChartMode.Navigation,
                    seed = 1701,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                )
                LcarsInspectBracket(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(132.dp),
                ) {
                    LcarsNumberMatrix(
                        rows = 4,
                        columns = 6,
                        seed = 118,
                        running = false,
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }
        }
    }
}

@Preview(name = "LCARS transmission frame", widthDp = 720, heightDp = 420, showBackground = true)
@Composable
private fun LcarsTransmissionFramePreview() {
    LcarsTheme {
        LcarsTransmissionFrame(
            headerLabel = "subspace comm net 407",
            footerLabel = "main bridge",
            title = "incoming transmission",
            subtitle = "starfleet command authorization required",
            modifier = Modifier.fillMaxSize(),
        )
    }
}

private fun previewLogs(): List<LcarsLogEntry> = listOf(
    LcarsLogEntry("sensor fusion active", LcarsLogSeverity.Info, "log"),
    LcarsLogEntry("subspace carrier locked", LcarsLogSeverity.Success, "comm"),
    LcarsLogEntry("dilution threshold rising", LcarsLogSeverity.Warning, "nav"),
    LcarsLogEntry("authorization required", LcarsLogSeverity.Alert, "sec"),
)
