package com.lcars.demo

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lcars.ui.controls.LcarsButton
import com.lcars.ui.controls.LcarsButtonShape
import com.lcars.ui.controls.LcarsDirectionalDisc
import com.lcars.ui.controls.LcarsDiscDirection
import com.lcars.ui.controls.LcarsKeypadColumn
import com.lcars.ui.controls.LcarsKeypadItem
import com.lcars.ui.display.LcarsAlertLevel
import com.lcars.ui.display.LcarsDiagnosticGrid
import com.lcars.ui.display.LcarsForwardNavigationScan
import com.lcars.ui.display.LcarsHistogram
import com.lcars.ui.display.LcarsHistogramBar
import com.lcars.ui.foundation.LcarsText
import com.lcars.ui.layout.LcarsBracketFrame
import com.lcars.ui.theme.LcarsPreset
import com.lcars.ui.theme.LcarsTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Stage 9 Video Reference Faithful: Bridge Forward Navigation Scan & Tactical Console.
 * Directly reproduces the layout, animation rhythm, and nested telemetry brackets from:
 * "Virtual-Enterprise-D-某扫描仪动画.mp4"
 */
@Composable
fun BridgeNavTacticalConsoleScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DemoLcarsTheme(preset = LcarsPreset.ClassicUltra) {
        val colors = LcarsTheme.colorScheme
        val typography = LcarsTheme.typography
        val coroutineScope = rememberCoroutineScope()

        var attitudeBearing by remember { mutableStateOf("042.8") }
        var isRedAlert by remember { mutableStateOf(false) }
        var targetLocked by remember { mutableStateOf(true) }
        var targetDistance by remember { mutableStateOf("38,400 KM") }

        // Weapons state
        var phaserCharge by remember { mutableStateOf(0.95f) }
        var torpedoCount by remember { mutableStateOf(4) }
        var firingWeaponText by remember { mutableStateOf("READY") }

        val animatedPhaserCharge by animateFloatAsState(
            targetValue = phaserCharge,
            animationSpec = tween(durationMillis = 600),
            label = "PhaserCharge"
        )

        val navKeypadItems = listOf(
            LcarsKeypadItem(code = "100273", label = "COURSE", onClick = { attitudeBearing = "300.0"; targetDistance = "24,120 KM" }),
            LcarsKeypadItem(code = "01827", label = "IMPULSE", onClick = { attitudeBearing = "094.7"; targetDistance = "19,500 KM" }),
            LcarsKeypadItem(code = "1002", label = "WARP", onClick = { attitudeBearing = "188.4"; targetDistance = "42,800 KM" }),
            LcarsKeypadItem(code = "901", label = "AUTO 12", onClick = { targetLocked = !targetLocked }),
        )

        val weaponsBars = listOf(
            LcarsHistogramBar(
                code = "PH-A",
                valueFraction = animatedPhaserCharge,
                displayValue = "${(animatedPhaserCharge * 100).toInt()}% CHG",
                customColor = if (isRedAlert) colors.alertRed else colors.monoAmber
            ),
            LcarsHistogramBar(
                code = "PH-B",
                valueFraction = (animatedPhaserCharge * 0.92f).coerceIn(0f, 1f),
                displayValue = "${((animatedPhaserCharge * 0.92f) * 100).toInt()}% CHG",
                customColor = if (isRedAlert) colors.monoAmber else colors.butterscotch
            ),
            LcarsHistogramBar(
                code = "TORP-1",
                valueFraction = if (torpedoCount >= 2) 1.0f else 0.4f,
                displayValue = if (torpedoCount >= 2) "QUANTUM (2)" else "RELOADING",
                customColor = colors.lightBlue
            ),
            LcarsHistogramBar(
                code = "TORP-2",
                valueFraction = if (torpedoCount >= 1) 1.0f else 0.2f,
                displayValue = if (torpedoCount >= 1) "PHOTON (2)" else "DEPLETED",
                customColor = colors.violet
            ),
        )

        LcarsBracketFrame(
            title = "FORWARD NAVIGATION SCAN",
            frameColor = if (isRedAlert) colors.alertRed else colors.framePrimary,
            accentColor = colors.monoAmber,
            topEndCapText = "AUTO 12",
            bottomEndCapText = "TACTICAL SECTOR 2166 • GRID 47-B",
            leftSidebarContent = {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LcarsText(
                        text = "ATTITUDE / THRUST",
                        style = typography.labelSmall.copy(color = colors.auxiliaryTan)
                    )

                    // Directional Disc Navigator
                    LcarsDirectionalDisc(
                        size = 88.dp,
                        baseColor = colors.auxiliaryTan,
                        crossColor = colors.monoAmber,
                        onDirectionSelected = { dir ->
                            attitudeBearing = when (dir) {
                                LcarsDiscDirection.North -> "000.0"
                                LcarsDiscDirection.East -> "090.0"
                                LcarsDiscDirection.South -> "180.0"
                                LcarsDiscDirection.West -> "270.0"
                                LcarsDiscDirection.Center -> "042.8"
                            }
                        }
                    )

                    LcarsKeypadColumn(
                        items = navKeypadItems,
                        modifier = Modifier.fillMaxWidth(),
                        buttonHeight = 32.dp,
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
                }
            },
            rightSidebarContent = {
                // Tactical Actions & Weapon Controls
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    LcarsButton(
                        text = if (isRedAlert) "STAND DOWN" else "RED ALERT",
                        onClick = { isRedAlert = !isRedAlert },
                        modifier = Modifier.fillMaxWidth(),
                        color = if (isRedAlert) colors.alertRed else colors.commandPrimary,
                        alertLevel = if (isRedAlert) LcarsAlertLevel.Critical else null,
                        shape = LcarsButtonShape.Pill
                    )

                    LcarsButton(
                        text = if (targetLocked) "UNLOCK" else "LOCK TARGET",
                        onClick = { targetLocked = !targetLocked },
                        modifier = Modifier.fillMaxWidth(),
                        color = colors.lightBlue,
                        shape = LcarsButtonShape.Pill
                    )

                    LcarsButton(
                        text = "FIRE PHASERS",
                        onClick = {
                            coroutineScope.launch {
                                firingWeaponText = "DISCHARGING..."
                                phaserCharge = 0.15f
                                delay(700)
                                phaserCharge = 0.95f
                                firingWeaponText = "RECHARGED"
                                delay(800)
                                firingWeaponText = "READY"
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        color = colors.monoAmber,
                        shape = LcarsButtonShape.Pill
                    )

                    LcarsButton(
                        text = "FIRE TORPEDO",
                        onClick = {
                            coroutineScope.launch {
                                if (torpedoCount > 0) {
                                    torpedoCount--
                                    firingWeaponText = "TORPEDO OUT"
                                    delay(1000)
                                    torpedoCount = 4
                                    firingWeaponText = "RELOADED"
                                    delay(800)
                                    firingWeaponText = "READY"
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        color = colors.violet,
                        shape = LcarsButtonShape.Pill
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(38.dp)
                            .background(
                                color = if (isRedAlert) colors.alertRed else colors.tacticalGreen,
                                shape = RoundedCornerShape(topEnd = 19.dp, bottomEnd = 19.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 6.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        LcarsText(
                            text = if (isRedAlert) "CONDITION RED" else "SHIELDS 100%",
                            style = typography.labelSmall.copy(color = Color.Black)
                        )
                    }
                }
            }
        ) {
            // Main Viewport:
            // 1. Top Section (Weight 1.4): Video Reference Forward Navigation Scan Screen
            // 2. Bottom Section (Weight 0.8): Weapons & Shield Diagnostics
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 4.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Top: Stage 9 Video-Accurate Forward Navigation Scanner
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1.4f)
                        .background(Color.Black, RoundedCornerShape(4.dp))
                ) {
                    LcarsForwardNavigationScan(
                        modifier = Modifier.fillMaxSize(),
                        targetLocked = targetLocked,
                        topAttachedValue = "201804",
                        bottomAttachedValue = "388720",
                        bearing = "BEARING: $attitudeBearing MARK 12",
                        range = "RANGE: $targetDistance • ORDNANCE: $firingWeaponText",
                        primaryColor = if (isRedAlert) colors.alertRed else colors.monoAmber,
                        accentColor = colors.auxiliaryTan,
                        gridColor = colors.lightBlue.copy(alpha = 0.2f),
                        rulerColor = colors.lightBlue.copy(alpha = 0.45f),
                    )
                }

                // Bottom: Weapons Histogram & Shield Frequency Grid
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.85f),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Left: Tactical Weapons Histogram
                    Box(
                        modifier = Modifier
                            .weight(1.1f)
                            .fillMaxHeight()
                    ) {
                        LcarsHistogram(
                            bars = weaponsBars,
                            title = "TACTICAL WEAPONS ORDNANCE & CAPACITORS",
                            modifier = Modifier.fillMaxSize(),
                            barHeight = 20.dp,
                            gap = 4.dp
                        )
                    }

                    // Right: Deflector Shield Frequency Grid
                    Box(
                        modifier = Modifier
                            .weight(0.9f)
                            .fillMaxHeight()
                    ) {
                        LcarsDiagnosticGrid(
                            title = "DEFLECTOR SHIELD FREQUENCY GRID",
                            subtitle = "4-QUADRANT HARMONIC ARRAY",
                            matrixRows = 4,
                            matrixColumns = 4,
                            hasAlertZone = isRedAlert,
                            alertZoneColor = if (isRedAlert) colors.alertRed else colors.tacticalGreen,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}
