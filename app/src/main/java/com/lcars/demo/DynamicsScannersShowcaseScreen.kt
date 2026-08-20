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
import androidx.compose.runtime.remember
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
import com.lcars.ui.display.LcarsEnergyPipe
import com.lcars.ui.display.LcarsFlowDirection
import com.lcars.ui.display.LcarsReactantInjector
import com.lcars.ui.display.LcarsWarpCoreMeter
import com.lcars.ui.foundation.LcarsText
import com.lcars.ui.layout.LcarsStandardFrame
import com.lcars.ui.layout.LcarsTargetScanner
import com.lcars.ui.scene.LcarsStarChart
import com.lcars.ui.scene.LcarsStarChartMode
import com.lcars.ui.scene.LcarsTransmissionFrame
import com.lcars.ui.theme.LcarsTheme

@Composable
fun DynamicsScannersShowcaseScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LcarsTheme.colorScheme
    val spacing = LcarsTheme.dimensions

    var activeCategory by rememberSaveable { mutableStateOf("ENERGY & PLASMA") }
    var chartMode by rememberSaveable { mutableStateOf(LcarsStarChartMode.Navigation) }
    var pipeDirection by rememberSaveable { mutableStateOf(LcarsFlowDirection.LeftToRight) }

    val categories = listOf("ENERGY & PLASMA", "STAR CHART & RADAR", "TRANSMISSION")

    LcarsStandardFrame(
        title = "DYNAMICS & SCANNERS",
        headerCode = "03 / SCENE",
        footerCode = "SCN 99",
        statusText = "ASTROMETRIC & PROPULSION SENSORS // ACTIVE",
        frameColor = colors.auxiliaryTan,
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
                "ENERGY & PLASMA" -> {
                    // SECTION 1: MATTER REACTANT INJECTOR
                    ShowcaseCard(title = "MATTER / ANTI-MATTER REACTANT INJECTOR") {
                        LcarsReactantInjector(
                            title = "WARP CORE REACTANT INJECTOR",
                            primaryColor = colors.monoAmber,
                            conduitColor = colors.lightBlue,
                            direction = pipeDirection,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }

                    // SECTION 2: EPS PLASMA ENERGY CONDUIT
                    ShowcaseCard(title = "EPS PLASMA CONDUIT PIPE") {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            LcarsEnergyPipe(
                                pipeHeight = 22.dp,
                                baseColor = colors.monoAmber,
                                glowColor = Color.White,
                                direction = pipeDirection,
                                modifier = Modifier.fillMaxWidth(),
                            )
                            LcarsEnergyPipe(
                                pipeHeight = 22.dp,
                                baseColor = colors.lightBlue,
                                glowColor = Color.White,
                                direction = pipeDirection,
                                modifier = Modifier.fillMaxWidth(),
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End,
                            ) {
                                LcarsButton(
                                    text = if (pipeDirection == LcarsFlowDirection.LeftToRight) "DIR: FORWARD" else "DIR: REVERSE",
                                    onClick = {
                                        pipeDirection = if (pipeDirection == LcarsFlowDirection.LeftToRight) {
                                            LcarsFlowDirection.RightToLeft
                                        } else {
                                            LcarsFlowDirection.LeftToRight
                                        }
                                    },
                                    color = colors.commandSecondary,
                                    shape = LcarsButtonShape.Pill,
                                    minHeight = 36.dp,
                                )
                            }
                        }
                    }

                    // SECTION 3: WARP CORE RESONANCE METER
                    ShowcaseCard(title = "WARP CORE RESONANCE CHAMBER") {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp)
                                .background(Color.Black)
                                .padding(8.dp),
                        ) {
                            LcarsWarpCoreMeter(
                                warpFactor = 7.4f,
                                running = true,
                                modifier = Modifier.fillMaxSize(),
                            )
                        }
                    }
                }

                "STAR CHART & RADAR" -> {
                    // SECTION 1: ASTROMETRIC STAR CHART
                    ShowcaseCard(title = "ASTROMETRIC STELLAR CARTOGRAPHY") {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                LcarsText(
                                    text = "MODE: ${chartMode.name.uppercase()}",
                                    style = LcarsTheme.typography.labelSmall.copy(color = colors.monoAmber),
                                )
                                LcarsSegmentedControl(
                                    options = listOf("NAV", "INSPECT"),
                                    selectedOption = if (chartMode == LcarsStarChartMode.Navigation) "NAV" else "INSPECT",
                                    onOptionSelected = {
                                        chartMode = if (it == "NAV") LcarsStarChartMode.Navigation else LcarsStarChartMode.Inspection
                                    },
                                    modifier = Modifier.width(180.dp),
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp),
                            ) {
                                LcarsStarChart(
                                    mode = chartMode,
                                    modifier = Modifier.fillMaxSize(),
                                )
                            }
                        }
                    }

                    // SECTION 2: TARGET TRACKING SCANNER
                    ShowcaseCard(title = "TARGET TRACKING SCANNER BRACKETS") {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .background(Color.Black),
                            contentAlignment = Alignment.Center,
                        ) {
                            LcarsTargetScanner(
                                running = true,
                                color = colors.lightBlue,
                                modifier = Modifier.fillMaxSize(),
                            )
                            LcarsText(
                                text = "LOCK: BEARING 241 MK 12",
                                style = LcarsTheme.typography.labelSmall.copy(color = colors.monoAmber),
                            )
                        }
                    }
                }

                "TRANSMISSION" -> {
                    // SECTION: INCOMING TRANSMISSION FRAME
                    ShowcaseCard(title = "SECURE COMMUNICATION & AUTHORIZATION FRAME") {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp),
                        ) {
                            LcarsTransmissionFrame(
                                headerLabel = "COMM / 08",
                                footerLabel = "SECURE // 24",
                                title = "STARFLEET COMMAND",
                                subtitle = "DIRECTIVE 1014-B",
                                modifier = Modifier.fillMaxSize(),
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxSize().padding(12.dp),
                                    verticalArrangement = Arrangement.SpaceAround,
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                ) {
                                    LcarsText(
                                        text = "PRIORITY TRANSMISSION RECEIVED",
                                        style = LcarsTheme.typography.labelMedium.copy(color = colors.monoAmber),
                                    )
                                    LcarsText(
                                        text = "AUTHENTICATION CODE: SIGMA-994-OMEGA",
                                        style = LcarsTheme.typography.labelSmall.copy(color = colors.lightBlue),
                                    )
                                    LcarsButton(
                                        text = "DECODE MESSAGE",
                                        onClick = {},
                                        color = colors.monoAmber,
                                        shape = LcarsButtonShape.Pill,
                                        minHeight = 36.dp,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
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
