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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lcars.ui.controls.LcarsButton
import com.lcars.ui.controls.LcarsButtonShape
import com.lcars.ui.controls.LcarsKeypadColumn
import com.lcars.ui.controls.LcarsKeypadItem
import com.lcars.ui.display.LcarsDiagnosticGrid
import com.lcars.ui.display.LcarsEnergyPipe
import com.lcars.ui.display.LcarsFlowDirection
import com.lcars.ui.display.LcarsReactantInjector
import com.lcars.ui.display.LcarsHistogram
import com.lcars.ui.display.LcarsHistogramBar
import com.lcars.ui.foundation.LcarsText
import com.lcars.ui.layout.LcarsBracketFrame
import com.lcars.ui.theme.LcarsPreset
import com.lcars.ui.theme.LcarsTheme

/**
 * Pure Landscape Stage 9 Workstation Console: Main Engineering & EPS Flow Monitor.
 */
@Composable
fun EngineeringEpsConsoleScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DemoLcarsTheme(preset = LcarsPreset.ClassicUltra) {
        val colors = LcarsTheme.colorScheme
        val typography = LcarsTheme.typography

        var activeSystemIndex by remember { mutableStateOf(0) }
        var warpSpeedFraction by remember { mutableStateOf(0.72f) }

        val keypadItems = listOf(
            LcarsKeypadItem(code = "0156", label = "WARP CORE", onClick = { activeSystemIndex = 0 }),
            LcarsKeypadItem(code = "069", label = "EPS GRID", onClick = { activeSystemIndex = 1 }),
            LcarsKeypadItem(code = "7608", label = "DILITHIUM", onClick = { activeSystemIndex = 2 }),
            LcarsKeypadItem(code = "5230", label = "INJECTOR", onClick = { activeSystemIndex = 3 }),
            LcarsKeypadItem(code = "0527", label = "FLOW RATE", onClick = { activeSystemIndex = 4 }),
        )

        val histogramBars = listOf(
            LcarsHistogramBar("01-WARP", warpSpeedFraction, displayValue = "WARP 7.2", customColor = colors.monoAmber),
            LcarsHistogramBar("02-MATT", 0.88f, displayValue = "88% FLOW", customColor = colors.auxiliaryTan),
            LcarsHistogramBar("03-ANTI", 0.85f, displayValue = "85% FLOW", customColor = colors.lightBlue),
            LcarsHistogramBar("04-EPS", 0.64f, displayValue = "4.2 GW", customColor = colors.violet),
            LcarsHistogramBar("05-PLAS", 0.92f, displayValue = "NOMINAL", customColor = colors.tacticalGreen),
        )

        LcarsBracketFrame(
            title = "ENGINEERING PROPULSION & EPS CONDUIT MONITOR",
            frameColor = colors.framePrimary,
            accentColor = colors.monoAmber,
            topEndCapText = "SYS-ENG 47",
            bottomEndCapText = "WARP 1 - 9 COCHRANES",
            leftSidebarContent = {
                LcarsKeypadColumn(
                    items = keypadItems,
                    modifier = Modifier.fillMaxWidth(),
                    buttonHeight = 36.dp,
                    colorOffset = 0,
                    shape = LcarsButtonShape.BlockStart,
                )

                Spacer(modifier = Modifier.weight(1f))

                LcarsButton(
                    text = "BACK",
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth(),
                    color = colors.commandSecondary,
                    shape = LcarsButtonShape.BlockStart,
                )
            },
            rightSidebarContent = {
                // Secondary Telemetry Column
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(28.dp)
                            .background(colors.monoAmber, RoundedCornerShape(topEnd = 14.dp, bottomEnd = 14.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        LcarsText(text = "OVERRIDE", style = typography.labelSmall.copy(color = Color.Black))
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .background(Color(0xFF0A0F1A), RoundedCornerShape(4.dp))
                            .padding(6.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.SpaceAround
                        ) {
                            LcarsText("CHAMBER 1", style = typography.labelSmall.copy(color = colors.lightBlue))
                            LcarsText("98.4 %", style = typography.titleSmall.copy(color = colors.monoAmber))
                            LcarsText("MAGNETIC", style = typography.labelSmall.copy(color = colors.auxiliaryTan))
                            LcarsText("2.4 TESLA", style = typography.titleSmall.copy(color = colors.tacticalGreen))
                        }
                    }
                }
            }
        ) {
            // Main Central Viewport: EPS Pipes + Sensor Histogram
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 6.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Matter Reactant Injector Assembly (Stage 9 Authentic Replica)
                LcarsReactantInjector(
                    title = "MATTER REACTANT INJECTOR (WARP CORE TIE-IN)",
                    primaryColor = colors.monoAmber,
                    conduitColor = colors.lightBlue,
                    durationMillis = 1600,
                )

                // Secondary EPS Conduits
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LcarsEnergyPipe(
                        modifier = Modifier.weight(1f),
                        pipeHeight = 16.dp,
                        baseColor = colors.lightBlue,
                        glowColor = colors.spaceWhite,
                        waveCount = 3,
                        direction = LcarsFlowDirection.LeftToRight,
                        durationMillis = 1400,
                    )
                    LcarsEnergyPipe(
                        modifier = Modifier.weight(1f),
                        pipeHeight = 16.dp,
                        baseColor = colors.violet,
                        glowColor = colors.spaceWhite,
                        waveCount = 2,
                        direction = LcarsFlowDirection.LeftToRight,
                        durationMillis = 1800,
                    )
                }

                // Sensor Analysis & Reactor Subprocessor Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Left: Sensor Histogram
                    Box(modifier = Modifier.weight(1.1f)) {
                        LcarsHistogram(
                            bars = histogramBars,
                            title = "COMPOSITE SENSOR & FIELD STRESS",
                            modifier = Modifier.fillMaxSize(),
                            barHeight = 20.dp,
                            gap = 4.dp,
                        )
                    }

                    // Right: Magnetic Containment & Subprocessor Diagnostics
                    Box(modifier = Modifier.weight(0.9f)) {
                        LcarsDiagnosticGrid(
                            title = "CORE MAGNETIC CONTAINMENT 047",
                            subtitle = "OPTICAL SUBPROCESSOR ACTIVE",
                            matrixRows = 4,
                            matrixColumns = 4,
                            hasAlertZone = true,
                            alertZoneColor = colors.tacticalGreen,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}
