package com.lcars.ui.display

import com.lcars.ui.theme.*
import com.lcars.ui.foundation.*
import com.lcars.ui.controls.*
import com.lcars.ui.layout.*
import com.lcars.ui.scene.*
import com.lcars.ui.padd.*

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
import androidx.compose.foundation.progressSemantics
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.setProgress
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            .semantics {
                stateDescription = if (active) "active" else "inactive"
            }
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
        modifier = modifier
            .fillMaxWidth()
            .progressSemantics(clampedProgress),
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
            .semantics {
                if (active) liveRegion = LiveRegionMode.Assertive
                stateDescription = if (active) level.name else "inactive"
            }
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
            .height(height)
            .semantics {
                progressBarRangeInfo = ProgressBarRangeInfo(
                    current = safeActive.toFloat(),
                    range = 0f..safeTotal.toFloat(),
                    steps = (safeTotal - 1).coerceAtLeast(0),
                )
            },
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
    enabled: Boolean = true,
    onValueChangeFinished: (() -> Unit)? = null,
) {
    val total = totalSegments.coerceAtLeast(1)
    val soundService = LocalLcarsSoundService.current
    val currentValue by rememberUpdatedState(value)
    val currentOnValueChange by rememberUpdatedState(onValueChange)
    val currentSoundService by rememberUpdatedState(soundService)
    val safeValue = value.coerceIn(0, total)

    fun updateValue(newValue: Int) {
        val safeNewValue = newValue.coerceIn(0, total)
        if (safeNewValue != currentValue) {
            currentSoundService.playSliderAdjust()
            currentOnValueChange(safeNewValue)
        }
    }

    Box(
        modifier = modifier
            .defaultMinSize(minHeight = 48.dp)
            .semantics {
                progressBarRangeInfo = ProgressBarRangeInfo(
                    current = safeValue.toFloat(),
                    range = 0f..total.toFloat(),
                    steps = (total - 1).coerceAtLeast(0),
                )
                if (enabled) {
                    setProgress { target ->
                        updateValue(target.roundToInt())
                        true
                    }
                } else {
                    disabled()
                }
            }
            .then(if (enabled) Modifier.pointerInput(total) {
                detectTapGestures { offset ->
                    val trackWidth = size.width.coerceAtLeast(1)
                    val fraction = (offset.x / trackWidth).coerceIn(0f, 1f)
                    updateValue((fraction * total).roundToInt())
                    onValueChangeFinished?.invoke()
                }
            } else Modifier)
            .then(if (enabled) Modifier.pointerInput(total) {
                detectHorizontalDragGestures(onDragEnd = {
                    onValueChangeFinished?.invoke()
                }) { change, _ ->
                    change.consume()
                    val trackWidth = size.width.coerceAtLeast(1)
                    val fraction = (change.position.x / trackWidth).coerceIn(0f, 1f)
                    updateValue((fraction * total).roundToInt())
                }
            } else Modifier)
    ) {
        LcarsSegmentedMeter(
            activeSegments = safeValue,
            totalSegments = total,
            color = color,
            inactiveColor = inactiveColor,
            height = height,
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .align(Alignment.Center),
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

/**
 * LCARS 24.2 Data Cascade Component.
 *
 * Implements the 7-row telemetry data cascade animation from LCARS 24.2.
 * Digits rhythmically alternate between cascade accent (Orange #FF8800), light highlight (White #F5F6FA),
 * and dim states across 6000ms cycles, with full support for frozen/static mode.
 */
@Composable
fun LcarsDataCascade(
    modifier: Modifier = Modifier,
    columns: List<List<String>> = defaultLcarsDataCascadeColumns,
    running: Boolean = true,
    accentColor: Color = LocalLcarsColors.current.inactiveAccent,
    highlightColor: Color = LocalLcarsColors.current.spaceWhite,
    cycleMillis: Int = 6000,
) {
    val colors = LocalLcarsColors.current
    val typography = LocalLcarsTypography.current
    val motionActive = running && LcarsTheme.motionMode == LcarsMotionMode.System

    val transition = rememberInfiniteTransition(label = "LcarsDataCascade")
    val phaseState = if (motionActive) {
        transition.animateFloat(
            initialValue = 0f,
            targetValue = 100f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = cycleMillis, easing = LinearEasing),
                repeatMode = RepeatMode.Restart,
            ),
            label = "DataCascadePhase",
        )
    } else {
        null
    }

    val textMeasurer = rememberTextMeasurer()
    val textStyle = remember(typography) {
        typography.labelSmall.copy(
            fontSize = 13.sp,
            lineHeight = 15.sp,
        )
    }

    val measuredColumns = remember(columns, textStyle) {
        columns.map { rows ->
            rows.map { text ->
                textMeasurer.measure(text = lcarsLabel(text), style = textStyle)
            }
        }
    }

    val density = LocalDensity.current
    val (totalWidthDp, totalHeightDp) = remember(measuredColumns, density) {
        with(density) {
            val colGapPx = 12.dp.toPx()
            val rowGapPx = 2.dp.toPx()
            var totalW = 0f
            var maxH = 0f
            measuredColumns.forEachIndexed { idx, rows ->
                var colW = 0f
                var colH = 0f
                rows.forEachIndexed { rIdx, m ->
                    if (m.size.width.toFloat() > colW) colW = m.size.width.toFloat()
                    colH += m.size.height.toFloat() + (if (rIdx > 0) rowGapPx else 0f)
                }
                totalW += colW + (if (idx > 0) colGapPx else 0f)
                if (colH > maxH) maxH = colH
            }
            (totalW.toDp() + 16.dp) to (maxH.toDp() + 12.dp)
        }
    }

    Canvas(
        modifier = modifier
            .size(totalWidthDp, totalHeightDp)
            .background(colors.panel)
            .padding(horizontal = 8.dp, vertical = 6.dp)
    ) {
        val colGap = 12.dp.toPx()
        val rowGap = 2.dp.toPx()
        val currentPhase = phaseState?.value ?: 50f

        var currentX = 0f
        measuredColumns.forEachIndexed { colIndex, rows ->
            var currentY = 0f
            var maxColWidth = 0f

            rows.forEachIndexed { rowIndex, measuredResult ->
                val textColor = if (!motionActive) {
                    if (rowIndex < 4) accentColor else highlightColor
                } else {
                    resolveDataCascadeColor(
                        phase = (currentPhase + colIndex * 15f + rowIndex * 12f) % 100f,
                        rowIndex = rowIndex,
                        accentColor = accentColor,
                        highlightColor = highlightColor,
                        dimColor = colors.background,
                    )
                }

                drawText(
                    textLayoutResult = measuredResult,
                    topLeft = Offset(currentX, currentY),
                    color = textColor,
                )

                val itemHeight = measuredResult.size.height.toFloat()
                val itemWidth = measuredResult.size.width.toFloat()
                if (itemWidth > maxColWidth) maxColWidth = itemWidth
                currentY += itemHeight + rowGap
            }
            currentX += maxColWidth + colGap
        }
    }
}

private fun resolveDataCascadeColor(
    phase: Float,
    rowIndex: Int,
    accentColor: Color,
    highlightColor: Color,
    dimColor: Color,
): Color = when (rowIndex % 3) {
    0 -> when {
        phase < 28f -> highlightColor
        phase < 48f -> accentColor
        phase < 55f -> highlightColor
        phase < 82f -> accentColor
        else -> highlightColor
    }
    1 -> when {
        phase < 35f -> accentColor
        phase < 45f -> highlightColor
        phase < 70f -> accentColor
        phase < 78f -> highlightColor
        else -> accentColor
    }
    else -> when {
        phase < 27f -> dimColor
        phase < 41f -> highlightColor
        phase < 54f -> accentColor
        phase < 58f -> highlightColor
        phase < 72f -> accentColor
        phase < 76f -> highlightColor
        else -> accentColor
    }
}

val defaultLcarsDataCascadeColumns: List<List<String>> = listOf(
    listOf("93", "1853", "24109", "47", "0082", "593", "712"),
    listOf("408", "9912", "047", "1129", "834", "6601", "29"),
    listOf("1701", "34", "8820", "519", "7704", "198", "443"),
)

@Composable
fun steppedAlertColor(
    baseColor: Color,
    alertColor: Color,
    active: Boolean,
    periodMillis: Int = 600,
): Color {
    if (!active) return baseColor
    if (LcarsTheme.motionMode != LcarsMotionMode.System) return alertColor
    val halfPeriod = (periodMillis / 2).coerceAtLeast(1)
    val transition = rememberInfiniteTransition(label = "LcarsSteppedAlert")
    val animatedColor by transition.animateColor(
        initialValue = baseColor,
        targetValue = alertColor,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = periodMillis
                baseColor at 0
                baseColor at (halfPeriod - 1)
                alertColor at halfPeriod
                alertColor at (periodMillis - 1)
            },
            repeatMode = RepeatMode.Restart,
        ),
        label = "LcarsSteppedAlertColor",
    )
    return animatedColor
}
