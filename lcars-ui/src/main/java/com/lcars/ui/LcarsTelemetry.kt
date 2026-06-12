package com.lcars.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class LcarsTelemetryStatus {
    Neutral,
    Normal,
    Warning,
    Alert,
}

data class LcarsTelemetryEntry(
    val label: String,
    val value: String,
    val status: LcarsTelemetryStatus = LcarsTelemetryStatus.Neutral,
)

@Composable
fun LcarsTelemetryPanel(
    title: String,
    entries: List<LcarsTelemetryEntry>,
    modifier: Modifier = Modifier,
    alerting: Boolean = false,
    singleColumnBelow: Dp = 480.dp,
    compact: Boolean = false,
) {
    val colors = LocalLcarsColors.current
    val typography = LocalLcarsTypography.current
    val spacing = LocalLcarsSpacing.current
    val gap = spacing.gapStandard

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.panel)
            .padding(if (compact) gap else spacing.panelPadding),
        verticalArrangement = Arrangement.spacedBy(if (compact) gap / 2f else gap),
    ) {
        LcarsText(
            text = title,
            style = if (compact) {
                typography.labelSmall.copy(
                    color = colors.auxiliaryTan,
                    fontSize = 13.sp,
                    lineHeight = 14.sp,
                )
            } else {
                typography.telemetry.copy(color = colors.auxiliaryTan)
            },
            maxLines = 1,
        )
        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            if (maxWidth < singleColumnBelow) {
                Column(verticalArrangement = Arrangement.spacedBy(if (compact) gap / 2f else gap)) {
                    entries.forEach { entry ->
                        TelemetryCell(
                            entry = entry,
                            alerting = alerting,
                            compact = compact,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
            } else {
                val columns = if (compact && maxWidth >= 300.dp) 3 else 2
                Column(verticalArrangement = Arrangement.spacedBy(if (compact) gap / 2f else gap)) {
                    entries.chunked(columns).forEach { rowEntries ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(if (compact) gap / 2f else gap),
                        ) {
                            rowEntries.forEach { entry ->
                                TelemetryCell(
                                    entry = entry,
                                    alerting = alerting,
                                    compact = compact,
                                    modifier = Modifier.weight(1f),
                                )
                            }
                            repeat(columns - rowEntries.size) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TelemetryCell(
    entry: LcarsTelemetryEntry,
    alerting: Boolean,
    modifier: Modifier = Modifier,
    compact: Boolean = false,
) {
    val colors = LocalLcarsColors.current
    val typography = LocalLcarsTypography.current
    val valueColor = when {
        alerting && entry.status == LcarsTelemetryStatus.Normal -> colors.alertRed
        entry.status == LcarsTelemetryStatus.Normal -> colors.tacticalGreen
        entry.status == LcarsTelemetryStatus.Warning -> colors.monoAmber
        entry.status == LcarsTelemetryStatus.Alert -> colors.alertRed
        else -> colors.lightBlue
    }

    Column(
        modifier = modifier
            .background(Color.Black)
            .padding(horizontal = if (compact) 6.dp else 8.dp, vertical = if (compact) 3.dp else 6.dp),
    ) {
        LcarsText(
            text = entry.label,
            style = if (compact) {
                typography.labelSmall.copy(
                    color = colors.violet,
                    fontSize = 12.sp,
                    lineHeight = 13.sp,
                )
            } else {
                typography.labelSmall.copy(color = colors.violet)
            },
            maxLines = 1,
        )
        LcarsText(
            text = entry.value,
            style = if (compact) {
                typography.labelSmall.copy(
                    color = valueColor,
                    fontSize = 12.sp,
                    lineHeight = 13.sp,
                )
            } else {
                typography.telemetry.copy(color = valueColor)
            },
            maxLines = 1,
        )
    }
}
