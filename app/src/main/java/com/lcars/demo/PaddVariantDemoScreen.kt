package com.lcars.demo

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lcars.ui.LcarsPhonePaddScaffold
import com.lcars.ui.LcarsPhonePaddTheme
import com.lcars.ui.LcarsPaddControl
import com.lcars.ui.LcarsPaddControlShape
import com.lcars.ui.LcarsPaddDataLines
import com.lcars.ui.LcarsPaddMessage
import com.lcars.ui.LcarsPaddReadoutPanel
import com.lcars.ui.LcarsPaddStatusStrip
import com.lcars.ui.LcarsStyle
import com.lcars.ui.LocalLcarsColors
import com.lcars.ui.LocalLcarsPhonePaddMetrics
import com.lcars.ui.LocalLcarsTypography

@Composable
fun PaddVariantDemoScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var selectedStyleName by rememberSaveable { mutableStateOf(LcarsStyle.StandardPadd.name) }
    var alert by rememberSaveable { mutableStateOf(false) }
    val selectedStyle = LcarsStyle.valueOf(selectedStyleName)

    LcarsPhonePaddTheme(style = selectedStyle) {
        LcarsPhonePaddScaffold(
            title = "systems data 21-0071",
            registry = "uss raven - database 83-s28",
            footerLabel = "standard padd variant",
            modifier = modifier,
            footer = {
                LcarsPaddControl(
                    text = "back",
                    color = LocalLcarsColors.current.a7,
                    shape = LcarsPaddControlShape.LeftCap,
                    modifier = Modifier.weight(0.32f),
                    onClick = onBack,
                )
                LcarsPaddControl(
                    text = if (alert) "clear" else "resist",
                    color = if (alert) LocalLcarsColors.current.alertRed else LocalLcarsColors.current.a1,
                    shape = LcarsPaddControlShape.RightCap,
                    modifier = Modifier.weight(0.68f),
                    onClick = { alert = !alert },
                )
            },
        ) {
            PaddStyleSelector(
                selectedStyle = selectedStyle,
                onStyleSelected = { selectedStyleName = it.name },
            )
            PaddPrimaryReadout(alert = alert)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp),
                horizontalArrangement = Arrangement.spacedBy(LocalLcarsPhonePaddMetrics.current.gap),
            ) {
                LcarsPaddControl(
                    text = "log",
                    color = LocalLcarsColors.current.a5,
                    shape = LcarsPaddControlShape.LeftCap,
                    modifier = Modifier.weight(1f),
                    onClick = {},
                )
                LcarsPaddControl(
                    text = "chart",
                    color = LocalLcarsColors.current.a2,
                    shape = LcarsPaddControlShape.Rectangle,
                    modifier = Modifier.weight(1f),
                    onClick = {},
                )
                LcarsPaddControl(
                    text = "link",
                    color = LocalLcarsColors.current.violet,
                    shape = LcarsPaddControlShape.RightCap,
                    modifier = Modifier.weight(1f),
                    onClick = {},
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(LocalLcarsPhonePaddMetrics.current.gap),
            ) {
                PaddCompactDiagram(
                    modifier = Modifier
                        .weight(0.52f)
                        .fillMaxHeight(),
                )
                Column(
                    modifier = Modifier
                        .weight(0.48f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(LocalLcarsPhonePaddMetrics.current.gap),
                ) {
                    LcarsPaddReadoutPanel(
                        title = "message buffer",
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.42f),
                    ) {
                        LcarsPaddMessage(
                            text = if (alert) "resist" else "ready",
                            alert = alert,
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                    PaddStatusStack(
                        alert = alert,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.58f),
                    )
                }
            }
        }
    }
}

@Composable
private fun PaddStyleSelector(
    selectedStyle: LcarsStyle,
    onStyleSelected: (LcarsStyle) -> Unit,
) {
    val colors = LocalLcarsColors.current
    val gap = LocalLcarsPhonePaddMetrics.current.gap
    val styles = listOf(
        LcarsStyle.StandardPadd,
        LcarsStyle.ClassicUltra,
        LcarsStyle.LowerDecks,
        LcarsStyle.LowerDecksPadd,
        LcarsStyle.NemesisBlueUltra,
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(gap),
    ) {
        styles.forEach { style ->
            val selected = style == selectedStyle
            LcarsPaddControl(
                text = style.paddDemoLabel(),
                color = if (selected) colors.a1 else colors.gray,
                shape = if (style == styles.first()) {
                    LcarsPaddControlShape.LeftCap
                } else if (style == styles.last()) {
                    LcarsPaddControlShape.RightCap
                } else {
                    LcarsPaddControlShape.Rectangle
                },
                modifier = Modifier.weight(1f),
                onClick = { onStyleSelected(style) },
            )
        }
    }
}

@Composable
private fun PaddPrimaryReadout(alert: Boolean) {
    LcarsPaddReadoutPanel(
        title = "hansen family",
        footerLabel = if (alert) "message override" else "personal research log",
        modifier = Modifier.fillMaxWidth(),
    ) {
        LcarsPaddDataLines(
            lines = if (alert) {
                listOf(
                    "collective carrier detected",
                    "defensive instruction fragment isolated",
                    "message priority elevated",
                    "stand by for manual acknowledgement",
                )
            } else {
                listOf(
                    "i can't get this datacore to behave for another second",
                    "field notes recovered from subspace buffer",
                    "cycle sync locked at stardate 40840.211",
                    "mobile interface reduced to command strips and readout panes",
                )
            },
            maxLines = 4,
        )
    }
}

@Composable
private fun PaddCompactDiagram(modifier: Modifier = Modifier) {
    val colors = LocalLcarsColors.current
    val typography = LocalLcarsTypography.current
    val gap = LocalLcarsPhonePaddMetrics.current.gap
    val scrollState = rememberScrollState()

    LcarsPaddReadoutPanel(
        title = "kreetassan diagram",
        modifier = modifier,
        footerLabel = "scan grid 04",
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(gap)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(gap),
            ) {
                repeat(9) { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(gap),
                    ) {
                        repeat(3) { column ->
                            val active = (row + column) % 3 == 0
                            BasicText(
                                text = "${row + 1}${column + 4}${(row * 7 + column * 3) % 10}",
                                modifier = Modifier.weight(1f),
                                style = typography.labelSmall.copy(
                                    color = if (active) colors.a1 else colors.a4,
                                    textAlign = TextAlign.End,
                                    fontSize = 12.sp,
                                    lineHeight = 13.sp,
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Clip,
                            )
                        }
                    }
                }
            }
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(0.72f)
                    .height(2.dp)
                    .background(colors.violet),
            )
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxHeight(0.72f)
                    .width(2.dp)
                    .background(colors.violet),
            )
        }
    }
}

@Composable
private fun PaddStatusStack(
    alert: Boolean,
    modifier: Modifier = Modifier,
) {
    val colors = LocalLcarsColors.current
    val gap = LocalLcarsPhonePaddMetrics.current.gap

    LcarsPaddReadoutPanel(
        title = "subsystems",
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(gap),
        ) {
            repeat(5) { index ->
                LcarsPaddStatusStrip(
                    label = when (index) {
                        0 -> "power ${if (alert) "alert" else "nominal"}"
                        1 -> "sensor ${if (alert) "lock" else "ready"}"
                        2 -> "archive 83-s28"
                        3 -> "stardate 40840"
                        else -> "bus 21-0071"
                    },
                    primaryColor = when {
                        alert && index == 0 -> colors.alertRed
                        index % 2 == 0 -> colors.a1
                        else -> colors.a2
                    },
                    capColor = if (index == 1) colors.violet else colors.a7,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

private fun LcarsStyle.paddDemoLabel(): String = when (this) {
    LcarsStyle.StandardPadd -> "std"
    LcarsStyle.ClassicUltra -> "classic"
    LcarsStyle.LowerDecks -> "decks"
    LcarsStyle.LowerDecksPadd -> "ld"
    LcarsStyle.NemesisBlueUltra -> "nem"
}

@Preview(widthDp = 390, heightDp = 820, showBackground = true)
@Composable
private fun PaddVariantDemoPreview() {
    PaddVariantDemoScreen(onBack = {}, modifier = Modifier.fillMaxSize())
}
