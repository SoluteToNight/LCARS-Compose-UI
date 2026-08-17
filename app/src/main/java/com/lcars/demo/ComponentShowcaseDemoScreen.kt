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
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lcars.ui.controls.LcarsButton
import com.lcars.ui.controls.LcarsButtonShape
import com.lcars.ui.controls.LcarsCommandRailItem
import com.lcars.ui.controls.LcarsCommandRailItemType
import com.lcars.ui.controls.LcarsCommandRailSide
import com.lcars.ui.controls.LcarsSegmentedControl
import com.lcars.ui.controls.LcarsToggle
import com.lcars.ui.display.LcarsAlertBanner
import com.lcars.ui.display.LcarsAlertLevel
import com.lcars.ui.display.LcarsDataCascade
import com.lcars.ui.display.LcarsLogConsole
import com.lcars.ui.display.LcarsLogEntry
import com.lcars.ui.display.LcarsLogSeverity
import com.lcars.ui.display.LcarsNumberMatrix
import com.lcars.ui.display.LcarsProgressBar
import com.lcars.ui.display.LcarsSegmentedMeter
import com.lcars.ui.display.LcarsSegmentedSlider
import com.lcars.ui.display.LcarsStatusLight
import com.lcars.ui.display.LcarsTelemetryEntry
import com.lcars.ui.display.LcarsTelemetryLayout
import com.lcars.ui.display.LcarsTelemetryPanel
import com.lcars.ui.display.LcarsTelemetryStatus
import com.lcars.ui.display.LcarsWarpCoreMeter
import androidx.compose.foundation.shape.RoundedCornerShape
import com.lcars.ui.foundation.LcarsText
import com.lcars.ui.layout.LcarsBar
import com.lcars.ui.layout.LcarsBarSegment
import com.lcars.ui.layout.LcarsCascadeContainer
import com.lcars.ui.layout.LcarsFocusScreenFrame
import com.lcars.ui.layout.LcarsFramedCommandRail
import com.lcars.ui.layout.LcarsFramedRailItem
import com.lcars.ui.layout.LcarsInspectBracket
import com.lcars.ui.layout.LcarsLabelAlign
import com.lcars.ui.layout.LcarsOptionStrip
import com.lcars.ui.layout.LcarsSegmentedBar
import com.lcars.ui.layout.LcarsStandardFrame
import com.lcars.ui.layout.lcarsCascadeItem
import com.lcars.ui.layout.rememberLcarsCascadeState
import com.lcars.ui.scene.LcarsStarChart
import com.lcars.ui.scene.LcarsStarChartMode
import com.lcars.ui.theme.LcarsMotionMode
import com.lcars.ui.theme.LcarsPreset
import com.lcars.ui.theme.LcarsTheme
import com.lcars.ui.theme.spec

/**
 * Categorized tabs for the LCARS component catalog.
 * Avoids vertical endless stacking by providing focused, dedicated interactive stages.
 */
enum class ShowcaseTab(val code: String, val label: String) {
    Controls("01", "CONTROLS"),
    Data("02", "DATA & TELEMETRY"),
    Motion("03", "PROPULSION & MOTION"),
    Sensors("04", "TACTICAL & SENSORS"),
    Patterns("05", "CONSOLE PATTERNS"),
}

@Composable
fun ComponentShowcaseDemoScreen(
    initialTab: Int = 0,
    onBack: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    var preset by rememberSaveable { mutableStateOf(LcarsPreset.NemesisBlueUltra) }
    var motionMode by rememberSaveable { mutableStateOf(LcarsMotionMode.System) }
    var selectedTab by rememberSaveable {
        mutableStateOf(ShowcaseTab.entries.getOrElse(initialTab) { ShowcaseTab.Controls })
    }

    LcarsTheme(spec = preset.spec, motionMode = motionMode) {
        val colors = LcarsTheme.colorScheme
        val spacing = LcarsTheme.dimensions

        Row(
            modifier = modifier
                .fillMaxSize()
                .background(colors.background)
                .safeDrawingPadding()
                .padding(spacing.gapStandard),
            horizontalArrangement = Arrangement.spacedBy(spacing.gapStandard),
        ) {
            // Left Wing / Command Rail (Stage Navigator)
            Column(
                modifier = Modifier
                    .width(180.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(spacing.gapStandard),
            ) {
                if (onBack != null) {
                    LcarsButton(
                        text = "RETURN",
                        onClick = onBack,
                        color = colors.a5,
                        shape = LcarsButtonShape.BlockStart,
                        minHeight = 44.dp,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                // Vertical Command Rail of the 5 Catalog Stages
                ShowcaseTab.entries.forEachIndexed { index, tab ->
                    val isSelected = tab == selectedTab
                    val shape = when (index) {
                        0 -> if (onBack == null) LcarsButtonShape.BlockStart else LcarsButtonShape.Rectangle
                        ShowcaseTab.entries.lastIndex -> LcarsButtonShape.BlockEnd
                        else -> LcarsButtonShape.Rectangle
                    }

                    val buttonColor = if (isSelected) {
                        colors.commandPrimary
                    } else {
                        when (index % 4) {
                            0 -> colors.commandSecondary
                            1 -> colors.auxiliaryTan
                            2 -> colors.lightBlue
                            else -> colors.violet
                        }
                    }

                    LcarsButton(
                        text = "${tab.code} ${tab.label}",
                        onClick = { selectedTab = tab },
                        color = buttonColor,
                        contentColor = Color.Black,
                        shape = shape,
                        selected = isSelected,
                        role = Role.Tab,
                        minHeight = 48.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                    )
                }

                // Bottom Status Block
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp)
                        .background(colors.frameSecondary)
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    contentAlignment = Alignment.BottomEnd,
                ) {
                    LcarsText(
                        text = "SYS 47 // OK",
                        style = LcarsTheme.typography.labelSmall.copy(color = Color.Black),
                    )
                }
            }

            // Right Column: Main Console Deck
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(spacing.gapStandard),
            ) {
                // Top Header Beam (Title + Embedded Theme & Motion Controls)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp),
                    horizontalArrangement = Arrangement.spacedBy(spacing.gapStandard),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    LcarsBar(
                        label = "LCARS // COMPONENT CATALOG [${selectedTab.code}]",
                        labelAlign = LcarsLabelAlign.Start,
                        color = colors.framePrimary,
                        labelColor = Color.Black,
                        height = 36.dp,
                        startCap = false,
                        endCap = true,
                        modifier = Modifier.weight(1f),
                    )

                    // Theme Presets (Integrated Segmented Bar)
                    LcarsSegmentedControl(
                        options = listOf("nemesis", "classic", "padd"),
                        selectedOption = when (preset) {
                            LcarsPreset.NemesisBlueUltra -> "nemesis"
                            LcarsPreset.ClassicUltra -> "classic"
                            LcarsPreset.LowerDecksPadd -> "padd"
                        },
                        onOptionSelected = { label ->
                            preset = when (label) {
                                "classic" -> LcarsPreset.ClassicUltra
                                "padd" -> LcarsPreset.LowerDecksPadd
                                else -> LcarsPreset.NemesisBlueUltra
                            }
                        },
                        selectedColor = colors.commandPrimary,
                        unselectedColor = colors.commandSecondary,
                        modifier = Modifier.width(240.dp),
                    )

                    // Motion Mode
                    LcarsSegmentedControl(
                        options = listOf("motion", "reduced", "off"),
                        selectedOption = when (motionMode) {
                            LcarsMotionMode.System -> "motion"
                            LcarsMotionMode.Reduced -> "reduced"
                            LcarsMotionMode.Off -> "off"
                        },
                        onOptionSelected = { label ->
                            motionMode = when (label) {
                                "motion" -> LcarsMotionMode.System
                                "reduced" -> LcarsMotionMode.Reduced
                                else -> LcarsMotionMode.Off
                            }
                        },
                        selectedColor = colors.activeAccent,
                        unselectedColor = colors.commandSecondary,
                        modifier = Modifier.width(190.dp),
                    )
                }

                // Dedicated Interactive Stage Viewport
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(colors.panel)
                        .padding(spacing.gapLarge),
                ) {
                    when (selectedTab) {
                        ShowcaseTab.Controls -> ControlsStage()
                        ShowcaseTab.Data -> DataStage()
                        ShowcaseTab.Motion -> MotionStage()
                        ShowcaseTab.Sensors -> SensorsStage()
                        ShowcaseTab.Patterns -> PatternsStage()
                    }
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------------
// 01 // CONTROLS STAGE (Buttons, Caps, Toggles, Interaction)
// ---------------------------------------------------------------------------------
@Composable
private fun ControlsStage() {
    val colors = LcarsTheme.colorScheme
    val spacing = LcarsTheme.dimensions
    var toggleState by rememberSaveable { mutableStateOf(true) }
    var lastInteracted by rememberSaveable { mutableStateOf("READY") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(spacing.gapLarge),
    ) {
        LcarsText(
            text = "INTERLOCKING COMMAND BEAM (BLOCK START + RECTANGLES + BLOCK END)",
            style = LcarsTheme.typography.labelSmall.copy(color = colors.lightBlue),
        )

        // 1. Interlocking 3-Segment Modular Command Beam
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing.gapStandard),
        ) {
            LcarsButton(
                text = "TRANSMIT",
                color = colors.commandPrimary,
                shape = LcarsButtonShape.BlockStart,
                onClick = { lastInteracted = "TOUCH: BEAM START [TRANSMIT]" },
                modifier = Modifier.weight(1f),
            )
            LcarsButton(
                text = "DIAGNOSTIC",
                color = colors.commandSecondary,
                shape = LcarsButtonShape.Rectangle,
                onClick = { lastInteracted = "TOUCH: BEAM MID [DIAGNOSTIC]" },
                modifier = Modifier.weight(1.2f),
            )
            LcarsButton(
                text = "RECEIVE",
                color = colors.commandPrimary,
                shape = LcarsButtonShape.BlockEnd,
                onClick = { lastInteracted = "TOUCH: BEAM END [RECEIVE]" },
                modifier = Modifier.weight(1f),
            )
        }

        LcarsText(
            text = "STANDALONE TRIGGER CAPSULES & ALERT MODES",
            style = LcarsTheme.typography.labelSmall.copy(color = colors.monoAmber),
        )

        // 2. Standalone Pills for triggers/alerts
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing.gapStandard),
        ) {
            LcarsButton(
                text = "SENSOR PULSE",
                color = colors.lightBlue,
                shape = LcarsButtonShape.Pill,
                onClick = { lastInteracted = "TRIGGER: SENSOR PULSE" },
                modifier = Modifier.weight(1f),
            )
            LcarsButton(
                text = "WARNING FLASH",
                color = colors.monoAmber,
                shape = LcarsButtonShape.Pill,
                alertLevel = LcarsAlertLevel.Warning,
                onClick = { lastInteracted = "ALERT: WARNING ACK" },
                modifier = Modifier.weight(1f),
            )
            LcarsButton(
                text = "CRITICAL ALERT",
                color = colors.alertRed,
                shape = LcarsButtonShape.Pill,
                alertLevel = LcarsAlertLevel.Critical,
                onClick = { lastInteracted = "ALERT: CRITICAL ACK" },
                modifier = Modifier.weight(1f),
            )
        }

        LcarsText(
            text = "INTERLOCKING DUAL-BLOCK TOGGLE SWITCH",
            style = LcarsTheme.typography.labelSmall.copy(color = colors.violet),
        )

        // 3. Interlocking Dual-Block Switch
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing.gapStandard),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            LcarsToggle(
                checked = toggleState,
                onCheckedChange = {
                    toggleState = it
                    lastInteracted = if (it) "TOGGLE: ONLINE" else "TOGGLE: STANDBY"
                },
                checkedLabel = "SUBSPACE ONLINE",
                uncheckedLabel = "STANDBY",
                activeColor = colors.commandPrimary,
                inactiveColor = colors.commandSecondary.copy(alpha = 0.45f),
                modifier = Modifier.weight(1f),
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp)
                    .background(colors.background)
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                contentAlignment = Alignment.CenterEnd,
            ) {
                LcarsText(
                    text = lastInteracted,
                    style = LcarsTheme.typography.telemetry.copy(color = colors.monoAmber),
                    maxLines = 1,
                )
            }
        }

        // 4. LCARS Keypad & Numeric Buffer Entry
        LcarsText(
            text = "TACTICAL INPUT MATRIX & KEYPAD TERMINAL",
            style = LcarsTheme.typography.labelSmall.copy(color = colors.lightBlue),
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing.gapStandard),
        ) {
            // 3x4 Numeric Matrix
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(spacing.gapStandard),
            ) {
                val keyRows = listOf(
                    listOf("1", "2", "3"),
                    listOf("4", "5", "6"),
                    listOf("7", "8", "9"),
                    listOf("CLR", "0", "ENT"),
                )
                keyRows.forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(spacing.gapStandard),
                    ) {
                        row.forEach { key ->
                            val keyColor = when (key) {
                                "CLR" -> colors.a9
                                "ENT" -> colors.commandPrimary
                                else -> colors.commandSecondary
                            }
                            LcarsButton(
                                text = key,
                                color = keyColor,
                                shape = LcarsButtonShape.Rectangle,
                                minWidth = 0.dp,
                                minHeight = 36.dp,
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    lastInteracted = when (key) {
                                        "CLR" -> "BUFFER CLEARED"
                                        "ENT" -> "COMMAND EXECUTED"
                                        else -> "KEYPAD INPUT: $key"
                                    }
                                },
                            )
                        }
                    }
                }
            }

            // Status feedback panel
            Column(
                modifier = Modifier
                    .weight(1f)
                    .background(colors.background)
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(spacing.gapStandard),
            ) {
                LcarsText(
                    text = "COMMAND BUFFER",
                    style = LcarsTheme.typography.labelSmall.copy(color = colors.monoAmber),
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp)
                        .background(colors.panel)
                        .padding(horizontal = 8.dp),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    LcarsText(
                        text = "> $lastInteracted",
                        style = LcarsTheme.typography.telemetry.copy(color = colors.text),
                    )
                }
                LcarsText(
                    text = "AUTHORIZATION: LEVEL 4 CLEARED",
                    style = LcarsTheme.typography.labelSmall.copy(color = colors.tacticalGreen),
                )
            }
        }
    }
}

// ---------------------------------------------------------------------------------
// 02 // DATA & TELEMETRY STAGE (Data Cascades, Matrices, Meters)
// ---------------------------------------------------------------------------------
@Composable
private fun DataStage() {
    val colors = LcarsTheme.colorScheme
    val spacing = LcarsTheme.dimensions
    var sliderValue by rememberSaveable { mutableIntStateOf(65) }
    var freezeCascade by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(spacing.gapLarge),
    ) {
        // LCARS 24.2 Data Cascade
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            LcarsText(
                text = "LCARS 24.2 TELEMETRY DATA CASCADE",
                style = LcarsTheme.typography.labelSmall.copy(color = colors.lightBlue),
            )
            LcarsButton(
                text = if (freezeCascade) "RESUME FLOW" else "FREEZE CASCADE",
                color = colors.commandSecondary,
                shape = LcarsButtonShape.Pill,
                minWidth = 120.dp,
                minHeight = 32.dp,
                onClick = { freezeCascade = !freezeCascade },
            )
        }

        LcarsDataCascade(
            running = !freezeCascade,
            modifier = Modifier.fillMaxWidth(),
        )

        // Number Matrix & Telemetry
        LcarsText(
            text = "SENSOR NUMBER MATRIX & STATUS",
            style = LcarsTheme.typography.labelSmall.copy(color = colors.monoAmber),
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing.gapStandard),
        ) {
            LcarsNumberMatrix(
                rows = 4,
                columns = 6,
                seed = 1701,
                modifier = Modifier
                    .weight(1.2f)
                    .height(118.dp),
            )
            Column(
                modifier = Modifier.weight(0.8f),
                verticalArrangement = Arrangement.spacedBy(spacing.gapStandard),
            ) {
                LcarsStatusLight(
                    label = "ODN NETWORK",
                    active = true,
                    color = colors.tacticalGreen,
                )
                LcarsStatusLight(
                    label = "SUB-PROCESSOR 04",
                    active = false,
                    color = colors.a7,
                )
                LcarsStatusLight(
                    label = "DEFLECTOR ARRAY",
                    active = true,
                    alerting = true,
                    color = colors.alertRed,
                )
            }
        }

        // Segmented Sliders and Bars
        LcarsText(
            text = "SEGMENTED LEVEL CONTROLS (${sliderValue}%)",
            style = LcarsTheme.typography.labelSmall.copy(color = colors.a4),
        )

        LcarsSegmentedSlider(
            value = sliderValue,
            onValueChange = { sliderValue = it },
            totalSegments = 100,
            color = colors.activeAccent,
            modifier = Modifier.fillMaxWidth(),
        )
        LcarsProgressBar(
            progress = sliderValue / 100f,
            label = "BUFFER CAPACITY",
            color = colors.monoAmber,
            segments = 20,
        )
    }
}

// ---------------------------------------------------------------------------------
// 03 // PROPULSION & MOTION STAGE (Warp Core, Alerts, Cascade)
// ---------------------------------------------------------------------------------
@Composable
private fun MotionStage() {
    val colors = LcarsTheme.colorScheme
    val spacing = LcarsTheme.dimensions
    var warpFactor by rememberSaveable { mutableFloatStateOf(6.2f) }
    var activeAlert by rememberSaveable { mutableStateOf<LcarsAlertLevel?>(null) }
    val cascadeState = rememberLcarsCascadeState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(spacing.gapLarge),
    ) {
        // Alert Synchronizer
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing.gapStandard),
        ) {
            LcarsButton(
                text = "CONDITION NORMAL",
                color = colors.commandPrimary,
                shape = LcarsButtonShape.BlockStart,
                onClick = { activeAlert = null },
                modifier = Modifier.weight(1f),
            )
            LcarsButton(
                text = "YELLOW ALERT",
                color = colors.monoAmber,
                alertLevel = if (activeAlert == LcarsAlertLevel.Warning) LcarsAlertLevel.Warning else null,
                onClick = { activeAlert = LcarsAlertLevel.Warning },
                modifier = Modifier.weight(1f),
            )
            LcarsButton(
                text = "RED ALERT",
                color = colors.alertRed,
                shape = LcarsButtonShape.BlockEnd,
                alertLevel = if (activeAlert == LcarsAlertLevel.Critical) LcarsAlertLevel.Critical else null,
                onClick = { activeAlert = LcarsAlertLevel.Critical },
                modifier = Modifier.weight(1f),
            )
        }

        LcarsAlertBanner(
            message = when (activeAlert) {
                LcarsAlertLevel.Critical -> "RED ALERT: WARP CORE FIELD VARIANCE DETECTED"
                LcarsAlertLevel.Warning -> "YELLOW ALERT: DEFENSE SYSTEMS STANDBY"
                else -> "NOMINAL: ENGINEERING CONDUIT SYNCHRONIZED"
            },
            active = activeAlert != null,
            level = activeAlert ?: LcarsAlertLevel.Normal,
            modifier = Modifier.fillMaxWidth(),
        )

        // Warp Core Intermix Conduit
        LcarsText(
            text = "MAIN ENGINEERING // WARP CORE CONDUIT (WARP ${"%.2f".format(warpFactor)})",
            style = LcarsTheme.typography.labelSmall.copy(color = colors.lightBlue),
        )

        LcarsWarpCoreMeter(
            warpFactor = warpFactor,
            running = true,
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
        )

        LcarsSegmentedSlider(
            value = (warpFactor * 10f).toInt(),
            onValueChange = { warpFactor = it / 10f },
            totalSegments = 99,
            color = colors.monoAmber,
            modifier = Modifier.fillMaxWidth(),
        )

        // Console Boot Cascade Sequence
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            LcarsText(
                text = "CONSOLE POWER-ON CASCADE SEQUENCE",
                style = LcarsTheme.typography.labelSmall.copy(color = colors.a8),
            )
            LcarsButton(
                text = "RE-TRIGGER BOOT",
                color = colors.commandPrimary,
                shape = LcarsButtonShape.Pill,
                minWidth = 140.dp,
                minHeight = 32.dp,
                onClick = { cascadeState.trigger() },
            )
        }

        LcarsCascadeContainer(
            totalSteps = 4,
            state = cascadeState,
            modifier = Modifier
                .fillMaxWidth()
                .background(colors.background)
                .padding(spacing.gapStandard),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(spacing.gapStandard),
            ) {
                Box(modifier = Modifier.weight(1f).height(36.dp).lcarsCascadeItem(1).background(colors.framePrimary))
                Box(modifier = Modifier.weight(1f).height(36.dp).lcarsCascadeItem(2).background(colors.commandPrimary))
                Box(modifier = Modifier.weight(1f).height(36.dp).lcarsCascadeItem(3).background(colors.commandSecondary))
                Box(modifier = Modifier.weight(1f).height(36.dp).lcarsCascadeItem(4).background(colors.activeAccent))
            }
        }
    }
}

// ---------------------------------------------------------------------------------
// 04 // TACTICAL & SENSORS STAGE (Diagnostic Brackets, Star Chart)
// ---------------------------------------------------------------------------------
@Composable
private fun SensorsStage() {
    val colors = LcarsTheme.colorScheme
    val spacing = LcarsTheme.dimensions

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(spacing.gapStandard),
    ) {
        LcarsText(
            text = "TACTICAL LONG-RANGE SENSOR & DIAGNOSTIC BRACKET",
            style = LcarsTheme.typography.labelSmall.copy(color = colors.lightBlue),
        )

        LcarsInspectBracket(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            running = true,
        ) {
            LcarsStarChart(
                mode = LcarsStarChartMode.Navigation,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

// ---------------------------------------------------------------------------------
// 05 // STRUCTURAL PATTERNS STAGE (Scaffolds, Rails, PADD Frame)
// ---------------------------------------------------------------------------------
@Composable
private fun PatternsStage() {
    val colors = LcarsTheme.colorScheme
    val spacing = LcarsTheme.dimensions

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(spacing.gapLarge),
    ) {
        LcarsText(
            text = "SEGMENTED MULTI-TONE BARS (LCARS 24.2 SPEC)",
            style = LcarsTheme.typography.labelSmall.copy(color = colors.lightBlue),
        )

        LcarsSegmentedBar(
            segments = listOf(
                LcarsBarSegment(0.40f, colors.framePrimary, "SYS 01"),
                LcarsBarSegment(0.04f, colors.inactiveAccent, ""),
                LcarsBarSegment(0.17f, colors.commandPrimary, "NAV"),
                LcarsBarSegment(0.35f, colors.commandPrimary, "CORE"),
                LcarsBarSegment(0.04f, colors.frameSecondary, ""),
            ),
            height = 36.dp,
        )

        LcarsText(
            text = "FRAMED COMMAND RAILS & OPTION STRIP",
            style = LcarsTheme.typography.labelSmall.copy(color = colors.monoAmber),
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp),
            horizontalArrangement = Arrangement.spacedBy(spacing.gapStandard),
        ) {
            LcarsFramedCommandRail(
                items = listOf(
                    LcarsFramedRailItem("cmd1", "PRIMARY SENSOR", height = 40.dp),
                    LcarsFramedRailItem("cmd2", "COMM GRID", height = 40.dp),
                    LcarsFramedRailItem("spacer", type = LcarsCommandRailItemType.SpacerBlock, weight = 1f),
                    LcarsFramedRailItem("alert", "ALERT", type = LcarsCommandRailItemType.AlertBlock, height = 40.dp),
                ),
                modifier = Modifier.weight(0.4f),
            )

            Column(
                modifier = Modifier.weight(0.6f),
                verticalArrangement = Arrangement.spacedBy(spacing.gapStandard),
            ) {
                LcarsOptionStrip(
                    items = listOf("SECTOR 001", "SECTOR 002", "SOL SYSTEM"),
                    selectedItem = "SECTOR 001",
                    onSelect = {},
                    label = { it },
                    compact = true,
                ) { item, selected ->
                    LcarsText(
                        text = item,
                        style = LcarsTheme.typography.labelSmall.copy(
                            color = if (selected) colors.monoAmber else colors.lightBlue,
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                    )
                }

                LcarsTelemetryPanel(
                    title = "SYSTEM STATUS",
                    entries = listOf(
                        LcarsTelemetryEntry("CORE", "ONLINE", LcarsTelemetryStatus.Normal),
                        LcarsTelemetryEntry("ODN", "99.4%", LcarsTelemetryStatus.Normal),
                        LcarsTelemetryEntry("DEFLECTOR", "ALERT", LcarsTelemetryStatus.Warning),
                    ),
                    compact = true,
                    layout = LcarsTelemetryLayout.CompactGrid,
                )
            }
        }

        LcarsText(
            text = "STANDARD LCARS ENCLOSING ARCH FRAME (C-FRAME ARCH)",
            style = LcarsTheme.typography.labelSmall.copy(color = colors.monoAmber),
        )

        // Live Mini Standard Arch Frame Showcase
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(190.dp)
                .background(Color(0xFF070A10), RoundedCornerShape(4.dp)),
        ) {
            LcarsStandardFrame(
                title = "TACTICAL WORKSTATION CONSOLE",
                headerCode = "LCARS 47",
                footerCode = "SYS 05",
                statusText = "CONTAINMENT FIELD 100% // READY",
                leftRailWidth = 110.dp,
                headerHeight = 28.dp,
                footerHeight = 24.dp,
                leftRailContent = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .background(colors.commandPrimary, RoundedCornerShape(2.dp))
                            .padding(4.dp),
                        contentAlignment = Alignment.BottomEnd,
                    ) {
                        LcarsText(
                            text = "PRIMARY",
                            style = LcarsTheme.typography.labelSmall.copy(fontSize = 9.sp, color = Color.Black),
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .background(colors.auxiliaryTan, RoundedCornerShape(2.dp))
                            .padding(4.dp),
                        contentAlignment = Alignment.BottomEnd,
                    ) {
                        LcarsText(
                            text = "STANDBY",
                            style = LcarsTheme.typography.labelSmall.copy(fontSize = 9.sp, color = Color.Black),
                        )
                    }
                },
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    LcarsTelemetryPanel(
                        title = "CORE REACTOR FLUX",
                        entries = listOf(
                            LcarsTelemetryEntry("INJECTOR", "SYNC 99.4%", LcarsTelemetryStatus.Normal),
                            LcarsTelemetryEntry("TEMPERATURE", "3,480 K", LcarsTelemetryStatus.Normal),
                        ),
                        compact = true,
                        layout = LcarsTelemetryLayout.CompactGrid,
                        modifier = Modifier.weight(1f),
                    )
                    LcarsTelemetryPanel(
                        title = "TACTICAL CAPACITOR",
                        entries = listOf(
                            LcarsTelemetryEntry("PHASERS", "ONLINE 100%", LcarsTelemetryStatus.Normal),
                            LcarsTelemetryEntry("DEFLECTOR", "NOMINAL", LcarsTelemetryStatus.Normal),
                        ),
                        compact = true,
                        layout = LcarsTelemetryLayout.CompactGrid,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }

        LcarsText(
            text = "STANDARD LCARS FOCUS SCREEN FRAME",
            style = LcarsTheme.typography.labelSmall.copy(color = colors.lightBlue),
        )

        // Live Focus Screen Frame Showcase
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp),
        ) {
            LcarsFocusScreenFrame(
                headerLabel = "MAIN SENSOR ARRAY // 4077",
                footerLabel = "SUBSYSTEM COHESION NOMINAL",
                barHeight = 22.dp,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    LcarsText(
                        text = "FORWARD VIEWPORT OPTICAL SCAN MATRIX ACTIVE",
                        style = LcarsTheme.typography.telemetry.copy(color = colors.monoAmber),
                        modifier = Modifier.weight(1f),
                    )
                    LcarsButton(
                        text = "SCAN SECTOR",
                        onClick = {},
                        color = colors.commandPrimary,
                        shape = LcarsButtonShape.Pill,
                    )
                }
            }
        }

        LcarsText(
            text = "NESTED DIAGNOSTIC BRACKET & SENSOR MONITOR",
            style = LcarsTheme.typography.labelSmall.copy(color = colors.lightBlue),
        )

        LcarsInspectBracket(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            running = false,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(spacing.gapStandard),
            ) {
                LcarsTelemetryPanel(
                    title = "WARP VECTOR MATRIX",
                    entries = listOf(
                        LcarsTelemetryEntry("WARP FACTOR", "W 6.20", LcarsTelemetryStatus.Normal),
                        LcarsTelemetryEntry("PLASMA TEMP", "4,210 K", LcarsTelemetryStatus.Normal),
                        LcarsTelemetryEntry("FIELD COHESION", "99.8%", LcarsTelemetryStatus.Normal),
                    ),
                    compact = true,
                    layout = LcarsTelemetryLayout.CompactGrid,
                    modifier = Modifier.weight(1f),
                )
                LcarsTelemetryPanel(
                    title = "SUBSPACE ARRAY",
                    entries = listOf(
                        LcarsTelemetryEntry("BANDWIDTH", "12.4 TB/S", LcarsTelemetryStatus.Normal),
                        LcarsTelemetryEntry("CARRIER LOCK", "SYNC", LcarsTelemetryStatus.Normal),
                        LcarsTelemetryEntry("LATENCY", "0.14 MS", LcarsTelemetryStatus.Normal),
                    ),
                    compact = true,
                    layout = LcarsTelemetryLayout.CompactGrid,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Preview(widthDp = 900, heightDp = 720, showBackground = true)
@Composable
private fun ComponentShowcaseDemoScreenPreview() {
    DemoLcarsTheme {
        ComponentShowcaseDemoScreen()
    }
}
