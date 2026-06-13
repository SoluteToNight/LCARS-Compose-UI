package com.lcars.ui

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.max
import kotlin.math.roundToInt

enum class LcarsAlertLevel {
    Normal,
    Advisory,
    Warning,
    Critical,
}

@Composable
fun LcarsStatusLight(
    label: String,
    active: Boolean,
    modifier: Modifier = Modifier,
    color: Color = LocalLcarsColors.current.tacticalGreen,
    inactiveColor: Color = LocalLcarsColors.current.a7,
    alerting: Boolean = false,
    size: Dp = 18.dp,
    compact: Boolean = false,
) {
    val colors = LocalLcarsColors.current
    val displayColor = steppedAlertColor(
        baseColor = if (active) color else inactiveColor,
        alertColor = colors.alertRed,
        active = alerting,
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.panel)
            .padding(horizontal = if (compact) 6.dp else 8.dp, vertical = if (compact) 3.dp else 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .clip(RoundedCornerShape(percent = 50))
                .background(displayColor),
        )
        Spacer(modifier = Modifier.width(if (compact) LocalLcarsSpacing.current.gapStandard else LocalLcarsSpacing.current.gapLarge))
        LcarsText(
            text = label,
            style = LocalLcarsTypography.current.labelSmall.copy(color = colors.lightBlue),
            maxLines = 1,
        )
    }
}

@Composable
fun LcarsProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    label: String? = null,
    color: Color = LocalLcarsColors.current.monoAmber,
    trackColor: Color = LocalLcarsColors.current.a7,
    labelColor: Color = LocalLcarsColors.current.lightBlue,
    alerting: Boolean = false,
    height: Dp = 28.dp,
    shape: androidx.compose.ui.graphics.Shape = RoundedCornerShape(percent = 50),
    segments: Int? = null,
) {
    val colors = LocalLcarsColors.current
    val clampedProgress = progress.coerceIn(0f, 1f)
    val fillColor = steppedAlertColor(color, colors.alertRed, alerting)

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(LocalLcarsSpacing.current.gapStandard),
    ) {
        if (label != null) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                LcarsText(
                    text = label,
                    style = LocalLcarsTypography.current.labelSmall.copy(color = labelColor),
                    maxLines = 1,
                )
                LcarsText(
                    text = "${(clampedProgress * 100f).roundToInt()}%",
                    style = LocalLcarsTypography.current.labelSmall.copy(color = labelColor),
                    maxLines = 1,
                )
            }
        }
        if (segments != null && segments > 0) {
            val activeSegments = if (clampedProgress > 0f) {
                (clampedProgress * segments).roundToInt().coerceAtLeast(1)
            } else {
                0
            }
            LcarsSegmentedMeter(
                activeSegments = activeSegments,
                totalSegments = segments,
                color = fillColor,
                inactiveColor = trackColor,
                height = height,
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height)
                    .clip(shape)
                    .background(trackColor),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(clampedProgress)
                        .fillMaxHeight()
                        .background(fillColor),
                )
            }
        }
    }
}

@Composable
fun LcarsAlertBanner(
    message: String,
    active: Boolean,
    modifier: Modifier = Modifier,
    level: LcarsAlertLevel = LcarsAlertLevel.Critical,
) {
    val colors = LocalLcarsColors.current
    val baseColor = when (level) {
        LcarsAlertLevel.Normal -> colors.tacticalGreen
        LcarsAlertLevel.Advisory -> colors.lightBlue
        LcarsAlertLevel.Warning -> colors.monoAmber
        LcarsAlertLevel.Critical -> colors.alertRed
    }
    val bannerColor = steppedAlertColor(
        baseColor = if (active) baseColor else colors.a7,
        alertColor = colors.alertRed,
        active = active && level == LcarsAlertLevel.Critical,
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp)
            .clip(RoundedCornerShape(percent = 50))
            .background(bannerColor)
            .padding(horizontal = 18.dp, vertical = 8.dp),
        contentAlignment = Alignment.BottomEnd,
    ) {
        LcarsText(
            text = message,
            style = LocalLcarsTypography.current.button.copy(color = Color.Black),
            maxLines = 1,
        )
    }
}

@Immutable
data class LcarsDataRow(
    val cells: List<String>,
    val highlighted: Boolean = false,
)

@Composable
fun LcarsDataTable(
    headers: List<String>,
    rows: List<LcarsDataRow>,
    modifier: Modifier = Modifier,
    compact: Boolean = false,
) {
    val colors = LocalLcarsColors.current
    val gap = LocalLcarsSpacing.current.gapStandard

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.panel)
            .padding(gap),
        verticalArrangement = Arrangement.spacedBy(gap),
    ) {
        DataTableLine(
            cells = headers,
            background = colors.a7,
            textColor = Color.Black,
            compact = compact,
        )
        rows.forEachIndexed { index, row ->
            DataTableLine(
                cells = row.cells,
                background = when {
                    row.highlighted -> colors.monoAmber
                    index % 2 == 0 -> Color.Black
                    else -> Color(0xFF111111)
                },
                textColor = if (row.highlighted) Color.Black else colors.lightBlue,
                compact = compact,
            )
        }
    }
}

@Composable
fun LcarsReadoutTicker(
    values: List<String>,
    modifier: Modifier = Modifier,
    running: Boolean = true,
    cycleMillis: Int = 2400,
    color: Color = LocalLcarsColors.current.tacticalGreen,
) {
    val colors = LocalLcarsColors.current
    val value = if (values.isEmpty()) {
        ""
    } else if (running) {
        val transition = rememberInfiniteTransition(label = "LcarsReadoutTicker")
        val progress by transition.animateFloat(
            initialValue = 0f,
            targetValue = values.size.toFloat(),
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = cycleMillis, easing = LinearEasing),
                repeatMode = RepeatMode.Restart,
            ),
            label = "LcarsReadoutTickerProgress",
        )
        values[progress.toInt().coerceIn(0, values.lastIndex)]
    } else {
        values.first()
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.panel)
            .padding(horizontal = 10.dp, vertical = 8.dp),
    ) {
        LcarsText(
            text = value,
            style = LocalLcarsTypography.current.telemetry.copy(color = color),
            maxLines = 1,
            overflow = TextOverflow.Clip,
        )
    }
}

@Composable
fun LcarsSegmentedMeter(
    activeSegments: Int,
    totalSegments: Int,
    modifier: Modifier = Modifier,
    color: Color = LocalLcarsColors.current.lightBlue,
    inactiveColor: Color = LocalLcarsColors.current.a7,
    height: Dp = 36.dp,
) {
    val safeTotal = totalSegments.coerceAtLeast(1)
    val safeActive = activeSegments.coerceIn(0, safeTotal)
    val gapPx = with(androidx.compose.ui.platform.LocalDensity.current) {
        LocalLcarsSpacing.current.gapStandard.toPx()
    }

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(height),
    ) {
        val segmentWidth = max(0f, (size.width - gapPx * (safeTotal - 1)) / safeTotal)
        repeat(safeTotal) { index ->
            drawRect(
                color = if (index < safeActive) color else inactiveColor,
                topLeft = Offset(index * (segmentWidth + gapPx), 0f),
                size = Size(segmentWidth, size.height),
            )
        }
    }
}

@Composable
fun LcarsSegmentedSlider(
    value: Int,
    onValueChange: (Int) -> Unit,
    totalSegments: Int,
    modifier: Modifier = Modifier,
    color: Color = LocalLcarsColors.current.lightBlue,
    inactiveColor: Color = LocalLcarsColors.current.a7,
    height: Dp = 36.dp,
) {
    val total = totalSegments.coerceAtLeast(1)
    var width by remember { mutableStateOf(1) }
    val soundService = LocalLcarsSoundService.current
    val currentValue by rememberUpdatedState(value)
    val currentOnValueChange by rememberUpdatedState(onValueChange)
    val currentSoundService by rememberUpdatedState(soundService)

    Box(
        modifier = modifier
            .height(height)
            .onGloballyPositioned { coordinates ->
                width = coordinates.size.width.coerceAtLeast(1)
            }
            .pointerInput(total, width) {
                detectTapGestures { offset ->
                    val fraction = (offset.x / width).coerceIn(0f, 1f)
                    val newValue = (fraction * total).roundToInt()
                    if (newValue != currentValue) {
                        currentSoundService.playSliderAdjust()
                        currentOnValueChange(newValue)
                    }
                }
            }
            .pointerInput(total, width) {
                detectHorizontalDragGestures { change, _ ->
                    change.consume()
                    val fraction = (change.position.x / width).coerceIn(0f, 1f)
                    val newValue = (fraction * total).roundToInt()
                    if (newValue != currentValue) {
                        currentSoundService.playSliderAdjust()
                        currentOnValueChange(newValue)
                    }
                }
            }
    ) {
        LcarsSegmentedMeter(
            activeSegments = value,
            totalSegments = total,
            color = color,
            inactiveColor = inactiveColor,
            height = height,
            modifier = Modifier.fillMaxSize()
        )
    }
}


@Composable
private fun DataTableLine(
    cells: List<String>,
    background: Color,
    textColor: Color,
    compact: Boolean,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(background)
            .padding(horizontal = if (compact) 6.dp else 8.dp, vertical = if (compact) 2.dp else 5.dp),
        horizontalArrangement = Arrangement.spacedBy(LocalLcarsSpacing.current.gapStandard),
    ) {
        cells.forEach { cell ->
            LcarsText(
                text = cell,
                modifier = Modifier.weight(1f),
                style = LocalLcarsTypography.current.labelSmall.copy(color = textColor),
                maxLines = 1,
            )
        }
    }
}

@Composable
internal fun steppedAlertColor(
    baseColor: Color,
    alertColor: Color,
    active: Boolean,
): Color {
    if (!active) return baseColor
    val transition = rememberInfiniteTransition(label = "LcarsSteppedAlert")
    val animatedColor by transition.animateColor(
        initialValue = baseColor,
        targetValue = alertColor,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 600
                baseColor at 0
                baseColor at 299
                alertColor at 300
                alertColor at 599
            },
            repeatMode = RepeatMode.Restart,
        ),
        label = "LcarsSteppedAlertColor",
    )
    return animatedColor
}
