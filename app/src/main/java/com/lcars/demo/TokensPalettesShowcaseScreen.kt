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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lcars.ui.controls.LcarsButton
import com.lcars.ui.controls.LcarsButtonShape
import com.lcars.ui.controls.LcarsSegmentedControl
import com.lcars.ui.foundation.LcarsText
import com.lcars.ui.layout.LcarsBar
import com.lcars.ui.layout.LcarsStandardFrame
import com.lcars.ui.theme.LcarsMotionMode
import com.lcars.ui.theme.LcarsPreset
import com.lcars.ui.theme.LcarsTheme
import com.lcars.ui.theme.keypadRhythm

@Composable
fun TokensPalettesShowcaseScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var activePreset by rememberSaveable { mutableStateOf(LcarsPreset.ClassicUltra) }
    var activeMotion by rememberSaveable { mutableStateOf(LcarsMotionMode.System) }
    var activeCategory by rememberSaveable { mutableStateOf("PRESETS") }

    DemoLcarsTheme(preset = activePreset) {
        val colors = LcarsTheme.colorScheme
        val typography = LcarsTheme.typography
        val spacing = LcarsTheme.dimensions

        val categories = listOf("PRESETS", "PALETTE & CADENCE", "TYPOGRAPHY")

        LcarsStandardFrame(
            title = "TOKENS & PALETTES",
            headerCode = "04 / THEME",
            footerCode = "TKN 01",
            statusText = "STYLE TOKENS & COLOR CADENCE // ACTIVE",
            frameColor = colors.violet,
            accentColor = colors.monoAmber,
            leftRailWidth = 140.dp,
            modifier = modifier,
            leftRailContent = {
                LcarsButton(
                    text = "RETURN",
                    onClick = onBack,
                    color = colors.alertRed,
                    shape = LcarsButtonShape.BlockStart,
                    modifier = Modifier.fillMaxWidth(),
                    minHeight = 44.dp,
                )
                Spacer(modifier = Modifier.height(spacing.gapStandard))
                categories.forEach { category ->
                    val isSelected = category == activeCategory
                    LcarsButton(
                        text = category,
                        onClick = { activeCategory = category },
                        color = if (isSelected) Color.White else colors.commandSecondary,
                        shape = LcarsButtonShape.Rectangle,
                        selected = isSelected,
                        modifier = Modifier.fillMaxWidth(),
                        minHeight = 40.dp,
                    )
                    Spacer(modifier = Modifier.height(spacing.gapStandard))
                }
            },
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                when (activeCategory) {
                    "PRESETS" -> {
                        // SECTION 1: THEME PRESET SWITCHER
                        ShowcaseCard(title = "ERA THEME PRESETS") {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                LcarsSegmentedControl(
                                    options = listOf("CLASSIC", "NEMESIS", "LOWER DECKS"),
                                    selectedOption = when (activePreset) {
                                        LcarsPreset.ClassicUltra -> "CLASSIC"
                                        LcarsPreset.NemesisBlueUltra -> "NEMESIS"
                                        LcarsPreset.LowerDecksPadd -> "LOWER DECKS"
                                    },
                                    onOptionSelected = {
                                        activePreset = when (it) {
                                            "CLASSIC" -> LcarsPreset.ClassicUltra
                                            "NEMESIS" -> LcarsPreset.NemesisBlueUltra
                                            else -> LcarsPreset.LowerDecksPadd
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                )
                                LcarsText(
                                    text = "ACTIVE PRESET: ${activePreset.name.uppercase()}",
                                    style = typography.labelSmall.copy(color = colors.monoAmber),
                                )
                            }
                        }

                        // SECTION 2: MOTION MODE
                        ShowcaseCard(title = "MOTION & ACCESSIBILITY MODES") {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                LcarsSegmentedControl(
                                    options = listOf("SYSTEM", "REDUCED", "OFF"),
                                    selectedOption = when (activeMotion) {
                                        LcarsMotionMode.System -> "SYSTEM"
                                        LcarsMotionMode.Reduced -> "REDUCED"
                                        LcarsMotionMode.Off -> "OFF"
                                    },
                                    onOptionSelected = {
                                        activeMotion = when (it) {
                                            "SYSTEM" -> LcarsMotionMode.System
                                            "REDUCED" -> LcarsMotionMode.Reduced
                                            else -> LcarsMotionMode.Off
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                )
                                LcarsText(
                                    text = "MOTION BEHAVIOR: Keyframed stepped animations vs static accessible fallback.",
                                    style = typography.telemetry.copy(color = colors.text),
                                )
                            }
                        }
                    }

                    "PALETTE & CADENCE" -> {
                        // SECTION 1: OKUDA CADENCE RHYTHM
                        ShowcaseCard(title = "OKUDA ALTERNATING CADENCE RHYTHM") {
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                LcarsText(
                                    text = "High-contrast alternating cold/warm sequence for button columns and matrices:",
                                    style = typography.telemetry.copy(color = colors.text),
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                ) {
                                    for (i in 0 until 6) {
                                        val rhythmColor = colors.keypadRhythm(i)
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(36.dp)
                                                .background(rhythmColor),
                                            contentAlignment = Alignment.Center,
                                        ) {
                                            LcarsText(
                                                text = "0$i",
                                                style = typography.labelSmall.copy(color = Color.Black),
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // SECTION 2: CORE COLOR SWATCHES
                        ShowcaseCard(title = "CORE COLOR TOKENS") {
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                val swatchList = listOf(
                                    "AMBER" to colors.monoAmber,
                                    "TAN" to colors.auxiliaryTan,
                                    "BLUE" to colors.lightBlue,
                                    "VIOLET" to colors.violet,
                                    "GREEN" to colors.tacticalGreen,
                                    "ALERT" to colors.alertRed,
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                ) {
                                    swatchList.take(3).forEach { (name, color) ->
                                        ColorSwatchItem(name = name, color = color, modifier = Modifier.weight(1f))
                                    }
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                ) {
                                    swatchList.drop(3).forEach { (name, color) ->
                                        ColorSwatchItem(name = name, color = color, modifier = Modifier.weight(1f))
                                    }
                                }
                            }
                        }
                    }

                    "TYPOGRAPHY" -> {
                        // SECTION: TYPOGRAPHY STYLES
                        ShowcaseCard(title = "TYPOGRAPHY SCALE") {
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                LcarsText(text = "HEADER (34SP) // PRIMARY DISPLAY", style = typography.header.copy(color = colors.monoAmber))
                                LcarsText(text = "TITLE SMALL (20SP) // SUBSYSTEM DIAGNOSTICS", style = typography.titleSmall.copy(color = colors.lightBlue))
                                LcarsText(text = "BUTTON (22SP BOLD) // ENGAGE COMMAND", style = typography.button.copy(color = colors.auxiliaryTan))
                                LcarsText(text = "TELEMETRY (24SP) // 4891.22 MHZ", style = typography.telemetry.copy(color = colors.text))
                                LcarsText(text = "LABEL SMALL (16SP) // DECK 04 EPS STATUS", style = typography.labelSmall.copy(color = colors.tacticalGreen))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ColorSwatchItem(
    name: String,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .height(44.dp)
            .background(color)
            .padding(6.dp),
        contentAlignment = Alignment.BottomEnd,
    ) {
        LcarsText(
            text = name,
            style = LcarsTheme.typography.labelSmall.copy(color = Color.Black, fontSize = 11.sp),
        )
    }
}

@Composable
private fun ShowcaseCard(
    title: String,
    content: @Composable () -> Unit,
) {
    val colors = LcarsTheme.colorScheme
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.panel)
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        LcarsText(
            text = title,
            style = LcarsTheme.typography.labelSmall.copy(color = colors.monoAmber, fontSize = 12.sp),
        )
        content()
    }
}
