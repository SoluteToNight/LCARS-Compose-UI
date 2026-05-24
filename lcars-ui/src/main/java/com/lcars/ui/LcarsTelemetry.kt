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
) {
    val colors = LocalLcarsColors.current
    val typography = LocalLcarsTypography.current
    val spacing = LocalLcarsSpacing.current
    val gap = spacing.gapStandard

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.panel)
            .padding(spacing.panelPadding),
        verticalArrangement = Arrangement.spacedBy(gap),
    ) {
        LcarsText(
            text = title,
            style = typography.telemetry.copy(color = colors.auxiliaryTan),
            maxLines = 1,
        )
        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            if (maxWidth < singleColumnBelow) {
                Column(verticalArrangement = Arrangement.spacedBy(gap)) {
                    entries.forEach { entry ->
                        TelemetryCell(
                            entry = entry,
                            alerting = alerting,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(gap)) {
                    entries.chunked(2).forEach { rowEntries ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(gap),
                        ) {
                            rowEntries.forEach { entry ->
                                TelemetryCell(
                                    entry = entry,
                                    alerting = alerting,
                                    modifier = Modifier.weight(1f),
                                )
                            }
                            if (rowEntries.size == 1) {
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
            .padding(horizontal = 8.dp, vertical = 6.dp),
    ) {
        LcarsText(
            text = entry.label,
            style = typography.labelSmall.copy(color = colors.violet),
            maxLines = 1,
        )
        LcarsText(
            text = entry.value,
            style = typography.telemetry.copy(color = valueColor),
            maxLines = 1,
        )
    }
}
