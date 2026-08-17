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
import com.lcars.ui.display.LcarsHistogram
import com.lcars.ui.display.LcarsHistogramBar
import com.lcars.ui.foundation.LcarsText
import com.lcars.ui.layout.LcarsBracketFrame
import com.lcars.ui.theme.LcarsPreset
import com.lcars.ui.theme.LcarsTheme

/**
 * Pure Landscape Stage 9 Workstation Console: Sickbay Biomedical Scan 0208.
 */
@Composable
fun SickbayBiomedicalConsoleScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DemoLcarsTheme(preset = LcarsPreset.NemesisBlueUltra) {
        val colors = LcarsTheme.colorScheme
        val typography = LcarsTheme.typography

        var activePatientIndex by remember { mutableStateOf(0) }

        val keypadItems = listOf(
            LcarsKeypadItem(code = "02-24156", label = "NEURAL", onClick = { activePatientIndex = 0 }),
            LcarsKeypadItem(code = "03-41248", label = "CARDIO", onClick = { activePatientIndex = 1 }),
            LcarsKeypadItem(code = "04-14702", label = "RESPIRATORY", onClick = { activePatientIndex = 2 }),
            LcarsKeypadItem(code = "05-32456", label = "CELLULAR", onClick = { activePatientIndex = 3 }),
        )

        val anatomicalBars = listOf(
            LcarsHistogramBar("NEUR", 0.78f, displayValue = "98.2 SYN", customColor = colors.monoAmber),
            LcarsHistogramBar("OXYG", 0.96f, displayValue = "99.4 %", customColor = colors.lightBlue),
            LcarsHistogramBar("PULS", 0.68f, displayValue = "72 BPM", customColor = colors.auxiliaryTan),
            LcarsHistogramBar("TEMP", 0.55f, displayValue = "37.1 C", customColor = colors.tacticalGreen),
        )

        LcarsBracketFrame(
            title = "BIOMEDICAL & ANATOMICAL SCAN 0208",
            frameColor = colors.framePrimary,
            accentColor = colors.auxiliaryTan,
            topEndCapText = "LCARS 23295",
            bottomEndCapText = "BIO-BED 01 ACTIVE",
            leftSidebarContent = {
                LcarsKeypadColumn(
                    items = keypadItems,
                    modifier = Modifier.fillMaxWidth(),
                    buttonHeight = 36.dp,
                    colorOffset = 1,
                    shape = LcarsButtonShape.BlockStart
                )

                Spacer(modifier = Modifier.weight(1f))

                LcarsButton(
                    text = "BACK",
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth(),
                    color = colors.commandSecondary,
                    shape = LcarsButtonShape.BlockStart
                )
            },
            rightSidebarContent = {
                // Right Action Capsules
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    LcarsButton(
                        text = "07-3215",
                        onClick = {},
                        modifier = Modifier.fillMaxWidth(),
                        color = colors.lightBlue,
                        shape = LcarsButtonShape.Pill
                    )

                    LcarsButton(
                        text = "STEP",
                        onClick = {},
                        modifier = Modifier.fillMaxWidth(),
                        color = colors.violet,
                        shape = LcarsButtonShape.Pill
                    )

                    LcarsButton(
                        text = "QUIT",
                        onClick = onBack,
                        modifier = Modifier.fillMaxWidth(),
                        color = colors.monoAmber,
                        shape = LcarsButtonShape.Pill
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .background(colors.auxiliaryTan, RoundedCornerShape(topEnd = 24.dp, bottomEnd = 24.dp))
                            .padding(8.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        LcarsText(
                            text = "STATUS: OK",
                            style = typography.labelSmall.copy(color = Color.Black)
                        )
                    }
                }
            }
        ) {
            // Main Central Viewport
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 6.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Top Anatomical Histogram Scale
                LcarsHistogram(
                    bars = anatomicalBars,
                    title = "HUMAN ANATOMY M-63 • PHYSIOLOGICAL METRICS",
                    modifier = Modifier.weight(0.45f),
                    barHeight = 20.dp,
                    gap = 4.dp
                )

                // Bottom Diagnostic Stream & Waveform
                LcarsDiagnosticGrid(
                    title = "NEURAL SCAN & OPTICAL SYNAPSE TELEMETRY",
                    subtitle = "CEREBRAL CORTEX LEVEL 3",
                    modifier = Modifier.weight(0.55f),
                    matrixRows = 5,
                    matrixColumns = 5,
                    hasAlertZone = true,
                    alertZoneColor = colors.alertRed
                )
            }
        }
    }
}
