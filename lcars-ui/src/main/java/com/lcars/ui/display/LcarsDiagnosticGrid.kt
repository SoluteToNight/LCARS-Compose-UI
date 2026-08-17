package com.lcars.ui.display

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lcars.ui.foundation.LcarsText
import com.lcars.ui.theme.LocalLcarsColors
import com.lcars.ui.theme.LocalLcarsTypography
import com.lcars.ui.theme.rememberLcarsDataStream
import com.lcars.ui.theme.rememberLcarsSmoothPulse

/**
 * Stage 9 Optical Subprocessor & System Diagnostic Grid.
 * Displays high-density hexadecimal / decimal telemetry streams with a smooth-pulsing diagnostic alert sector.
 */
@Composable
fun LcarsDiagnosticGrid(
    title: String = "LEVEL 3 DIAGNOSTIC SCAN",
    subtitle: String = "OPTICAL SUBPROCESSOR",
    modifier: Modifier = Modifier,
    matrixRows: Int = 6,
    matrixColumns: Int = 4,
    hasAlertZone: Boolean = true,
    alertZoneColor: Color = LocalLcarsColors.current.alertRed,
) {
    val colors = LocalLcarsColors.current
    val typography = LocalLcarsTypography.current
    val dataMatrix by rememberLcarsDataStream(rows = matrixRows, columns = matrixColumns)
    val pulseAlpha by rememberLcarsSmoothPulse(durationMillis = 800, minAlpha = 0.35f, maxAlpha = 1.0f)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // TOP HEADER
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LcarsText(
                text = title.uppercase(),
                style = typography.titleSmall.copy(color = colors.monoAmber)
            )

            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(14.dp)
                        .background(colors.lightBlue, RoundedCornerShape(2.dp))
                )
                Box(
                    modifier = Modifier
                        .width(24.dp)
                        .height(14.dp)
                        .background(colors.auxiliaryTan, RoundedCornerShape(2.dp))
                )
            }
        }

        // TELEMETRY NUMBER STREAM MATRIX
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.45f)
                .background(Color(0xFF070B12), RoundedCornerShape(4.dp))
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            for (col in 0 until matrixColumns) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.SpaceAround
                ) {
                    for (row in 0 until matrixRows) {
                        val value = dataMatrix.getOrNull(row)?.getOrNull(col) ?: "0000"
                        val textColor = if (col == 0) colors.lightBlue else if (row % 2 == 0) colors.auxiliaryTan else colors.monoAmber
                        LcarsText(
                            text = value,
                            style = typography.labelSmall.copy(color = textColor),
                            maxLines = 1
                        )
                    }
                }
            }
        }

        // DIAGNOSTIC TOPOLOGY & SUBPROCESSOR VISUAL
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.55f),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Left Subsystem Block with Pulsing Warning Core
            Box(
                modifier = Modifier
                    .weight(0.6f)
                    .fillMaxHeight()
                    .background(Color(0xFF0A0E18), RoundedCornerShape(4.dp))
                    .padding(8.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        LcarsText(
                            text = "CORE ISOLATION",
                            style = typography.labelSmall.copy(color = colors.auxiliaryTan)
                        )
                        LcarsText(
                            text = "STATUS: ACTIVE",
                            style = typography.labelSmall.copy(color = colors.tacticalGreen)
                        )
                    }

                    // Pulsing Alert Core Box
                    if (hasAlertZone) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.85f)
                                .height(48.dp)
                                .align(Alignment.CenterHorizontally)
                                .alpha(pulseAlpha)
                                .background(alertZoneColor, RoundedCornerShape(4.dp))
                                .padding(horizontal = 8.dp, vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            LcarsText(
                                text = "CIRCUIT OVERLOAD DETECTED",
                                style = typography.labelMedium.copy(color = Color.Black)
                            )
                        }
                    }

                    LcarsText(
                        text = subtitle.uppercase(),
                        style = typography.titleSmall.copy(color = colors.monoAmber)
                    )
                }
            }

            // Right Status Pillars
            Column(
                modifier = Modifier
                    .weight(0.4f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                listOf(
                    Pair("OPTIC 1", colors.lightBlue),
                    Pair("SUB-BUS", colors.auxiliaryTan),
                    Pair("RELAY 4", colors.monoAmber),
                    Pair("BUFFER", colors.violet),
                ).forEach { (label, col) ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .background(col, RoundedCornerShape(4.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        LcarsText(
                            text = label,
                            style = typography.labelSmall.copy(color = Color.Black)
                        )
                    }
                }
            }
        }
    }
}
