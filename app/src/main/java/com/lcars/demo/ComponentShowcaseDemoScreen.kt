package com.lcars.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lcars.ui.LcarsBar
import com.lcars.ui.LcarsBarSegment
import com.lcars.ui.LcarsButton
import com.lcars.ui.LcarsButtonShape
import com.lcars.ui.LcarsCommandRailItemType
import com.lcars.ui.LcarsFramePanel
import com.lcars.ui.LcarsFramedCommandRail
import com.lcars.ui.LcarsFramedRailItem
import com.lcars.ui.LcarsLabelAlign
import com.lcars.ui.LcarsLogConsole
import com.lcars.ui.LcarsLogEntry
import com.lcars.ui.LcarsLogSeverity
import com.lcars.ui.LcarsNumberMatrix
import com.lcars.ui.LcarsOptionStrip
import com.lcars.ui.LcarsProgressBar
import com.lcars.ui.LcarsSegmentedBar
import com.lcars.ui.LcarsSegmentedControl
import com.lcars.ui.LcarsSegmentedMeter
import com.lcars.ui.LcarsStatusLight
import com.lcars.ui.LcarsTelemetryEntry
import com.lcars.ui.LcarsTelemetryLayout
import com.lcars.ui.LcarsTelemetryPanel
import com.lcars.ui.LcarsTelemetryStatus
import com.lcars.ui.LcarsText
import com.lcars.ui.LocalLcarsColors
import com.lcars.ui.LocalLcarsSpacing
import com.lcars.ui.LocalLcarsTypography

@Composable
fun ComponentShowcaseDemoScreen(
    modifier: Modifier = Modifier,
) {
    val colors = LocalLcarsColors.current
    val spacing = LocalLcarsSpacing.current
    var selectedGroup by remember { mutableStateOf("patterns") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
            .padding(spacing.gapLarge)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(spacing.gapLarge),
    ) {
        LcarsBar(
            label = "component showcase",
            labelAlign = LcarsLabelAlign.Start,
            color = colors.framePrimary,
            labelColor = colors.a1,
            height = 34.dp,
            startCap = true,
            endCap = true,
        )
        LcarsSegmentedControl(
            options = listOf("core", "atoms", "patterns", "scenes"),
            selectedOption = selectedGroup,
            onOptionSelected = { selectedGroup = it },
        )
        TokenAndAtomSection()
        PatternSection()
        DataDisplaySection()
        PaddVariantSection()
    }
}

@Composable
private fun TokenAndAtomSection() {
    val colors = LocalLcarsColors.current
    val spacing = LocalLcarsSpacing.current

    LcarsFramePanel(title = "core tokens + atoms", footerLabel = "design genes") {
        LcarsSegmentedBar(
            segments = listOf(
                LcarsBarSegment(0.18f, colors.framePrimary, "frame"),
                LcarsBarSegment(0.18f, colors.commandPrimary, "command"),
                LcarsBarSegment(0.18f, colors.commandSecondary, "secondary"),
                LcarsBarSegment(0.18f, colors.activeAccent, "active"),
                LcarsBarSegment(0.18f, colors.inactiveAccent, "inactive"),
            ),
            height = 42.dp,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing.gapStandard),
        ) {
            LcarsButton(
                text = "capsule",
                onClick = {},
                color = colors.commandPrimary,
                modifier = Modifier.weight(1f),
            )
            LcarsButton(
                text = "block start",
                onClick = {},
                color = colors.commandSecondary,
                shape = LcarsButtonShape.BlockStart,
                modifier = Modifier.weight(1f),
            )
            LcarsButton(
                text = "alert",
                onClick = {},
                color = colors.alertRed,
                alerting = true,
                shape = LcarsButtonShape.BlockEnd,
                modifier = Modifier.weight(1f),
            )
        }
        LcarsText(
            text = "ANTONIO STYLE TEXT / PUBLIC LCARS TEXT RENDERING",
            style = LocalLcarsTypography.current.telemetry.copy(color = colors.readoutAccent),
        )
    }
}

@Composable
private fun PatternSection() {
    val colors = LocalLcarsColors.current
    val spacing = LocalLcarsSpacing.current

    LcarsFramePanel(title = "patterns", footerLabel = "reusable composition layer") {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp),
            horizontalArrangement = Arrangement.spacedBy(spacing.gapStandard),
        ) {
            LcarsFramedCommandRail(
                items = listOf(
                    LcarsFramedRailItem("mode", "mode select", height = 46.dp),
                    LcarsFramedRailItem("panel", "panel shell", height = 58.dp),
                    LcarsFramedRailItem("space", type = LcarsCommandRailItemType.SpacerBlock, weight = 1f),
                    LcarsFramedRailItem("red", "red alert", type = LcarsCommandRailItemType.AlertBlock, height = 54.dp),
                ),
                modifier = Modifier.weight(0.35f),
            )
            Column(
                modifier = Modifier.weight(0.65f),
                verticalArrangement = Arrangement.spacedBy(spacing.gapStandard),
            ) {
                LcarsOptionStrip(
                    items = listOf("std", "wide", "compact", "data"),
                    selectedItem = "wide",
                    onSelect = {},
                    label = { it },
                    compact = true,
                    modifier = Modifier.fillMaxWidth(),
                ) { item, selected ->
                    LcarsText(
                        text = if (selected) "ACTIVE $item" else item,
                        style = LocalLcarsTypography.current.labelSmall.copy(
                            color = if (selected) colors.activeAccent else colors.inactiveAccent,
                        ),
                        modifier = Modifier.padding(8.dp),
                    )
                }
                LcarsTelemetryPanel(
                    title = "inline telemetry pattern",
                    entries = showcaseTelemetry(),
                    compact = true,
                    layout = LcarsTelemetryLayout.Inline,
                )
                LcarsProgressBar(
                    progress = 0.72f,
                    label = "pattern extraction",
                    color = colors.activeAccent,
                )
            }
        }
    }
}

@Composable
private fun DataDisplaySection() {
    val colors = LocalLcarsColors.current
    val spacing = LocalLcarsSpacing.current

    LcarsFramePanel(title = "data displays", footerLabel = "medium-level primitives") {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing.gapStandard),
        ) {
            LcarsNumberMatrix(
                rows = 4,
                columns = 7,
                seed = 1701,
                modifier = Modifier
                    .weight(1f)
                    .height(112.dp),
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(spacing.gapStandard),
            ) {
                LcarsSegmentedMeter(
                    activeSegments = 7,
                    totalSegments = 10,
                    color = colors.sensorBar,
                    inactiveColor = colors.meterInactive,
                    modifier = Modifier.height(32.dp),
                )
                LcarsStatusLight(
                    label = "library link",
                    active = true,
                    color = colors.tacticalGreen,
                    modifier = Modifier.fillMaxWidth(),
                )
                LcarsLogConsole(
                    entries = listOf(
                        LcarsLogEntry("tokens resolved", LcarsLogSeverity.Success, "CORE"),
                        LcarsLogEntry("atoms stateless", LcarsLogSeverity.Info, "ATOM"),
                        LcarsLogEntry("patterns extracted", LcarsLogSeverity.Warning, "MID"),
                    ),
                    maxLines = 3,
                    compact = true,
                )
            }
        }
    }
}

@Composable
private fun PaddVariantSection() {
    val colors = LocalLcarsColors.current

    LcarsFramePanel(title = "padd variants", footerLabel = "showcase sample, not business demo") {
        LcarsSegmentedBar(
            segments = listOf(
                LcarsBarSegment(0.42f, colors.framePrimary, "standard padd"),
                LcarsBarSegment(0.22f, colors.commandPrimary, "classic"),
                LcarsBarSegment(0.18f, colors.commandSecondary, "compact"),
                LcarsBarSegment(0.18f, colors.subHighlight, "library"),
            ),
            height = 54.dp,
            labelColor = Color.Black,
        )
        LcarsTelemetryPanel(
            title = "padd taxonomy",
            entries = listOf(
                LcarsTelemetryEntry("role", "component specimen"),
                LcarsTelemetryEntry("scope", "visual variants"),
                LcarsTelemetryEntry("state", "stateless"),
                LcarsTelemetryEntry("demo", "catalog"),
            ),
            compact = true,
            layout = LcarsTelemetryLayout.CompactGrid,
        )
    }
}

private fun showcaseTelemetry(): List<LcarsTelemetryEntry> = listOf(
    LcarsTelemetryEntry("layer", "patterns", LcarsTelemetryStatus.Normal),
    LcarsTelemetryEntry("reuse", "high", LcarsTelemetryStatus.Normal),
    LcarsTelemetryEntry("business", "none"),
    LcarsTelemetryEntry("status", "extracted", LcarsTelemetryStatus.Warning),
)

@Preview(widthDp = 900, heightDp = 720, showBackground = true)
@Composable
private fun ComponentShowcaseDemoScreenPreview() {
    DemoLcarsTheme {
        ComponentShowcaseDemoScreen()
    }
}
