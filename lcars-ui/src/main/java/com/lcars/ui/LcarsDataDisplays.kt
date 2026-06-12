package com.lcars.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.absoluteValue

data class LcarsLogEntry(
    val message: String,
    val severity: LcarsLogSeverity = LcarsLogSeverity.Info,
    val code: String? = null,
)

enum class LcarsLogSeverity {
    Info,
    Success,
    Warning,
    Alert,
}

@Composable
fun LcarsLogConsole(
    entries: List<LcarsLogEntry>,
    modifier: Modifier = Modifier,
    maxLines: Int = 6,
    compact: Boolean = false,
    autoScroll: Boolean = true,
) {
    val colors = LocalLcarsColors.current
    val safeMaxLines = maxLines.coerceAtLeast(1)
    val visibleEntries = if (autoScroll) {
        entries.takeLast(safeMaxLines)
    } else {
        entries.take(safeMaxLines)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF050505))
            .padding(if (compact) 6.dp else 8.dp),
        verticalArrangement = Arrangement.spacedBy(if (compact) 1.dp else 2.dp),
    ) {
        visibleEntries.forEach { entry ->
            LcarsText(
                text = entry.rendered(),
                style = LocalLcarsTypography.current.labelSmall.copy(
                    color = logSeverityColor(entry.severity, colors),
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                minFontSize = 8.sp,
            )
        }
    }
}

@Composable
fun LcarsNumberMatrix(
    rows: Int,
    columns: Int,
    modifier: Modifier = Modifier,
    seed: Int = 407,
    running: Boolean = true,
    highlightedRow: Int? = null,
) {
    val colors = LocalLcarsColors.current
    val data = remember(rows, columns, seed) {
        generateLcarsNumberMatrix(
            rows = rows,
            columns = columns,
            seed = seed,
        )
    }
    var animatedRow by remember(seed, rows) { mutableIntStateOf(0) }

    LaunchedEffect(running, rows, seed) {
        if (!running) return@LaunchedEffect
        while (true) {
            delay(220)
            animatedRow = (animatedRow + 1) % rows.coerceAtLeast(1)
        }
    }

    val resolvedHighlight = highlightedRow ?: if (running) animatedRow else null

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.panel)
            .padding(LocalLcarsSpacing.current.gapStandard),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        data.forEachIndexed { rowIndex, row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                row.forEach { cell ->
                    NumberMatrixCell(
                        cell = cell,
                        highlighted = rowIndex == resolvedHighlight,
                        modifier = Modifier.weight(cell.weight),
                    )
                }
            }
        }
    }
}

@Composable
fun LcarsStarCoords(
    modifier: Modifier = Modifier,
    count: Int = 2,
    digits: Int = 6,
    updateIntervalMillis: Int = 200,
    running: Boolean = true,
    seed: Int = 118,
) {
    val colors = LocalLcarsColors.current
    var tick by remember(seed) { mutableIntStateOf(0) }

    LaunchedEffect(running, updateIntervalMillis, seed) {
        if (!running) return@LaunchedEffect
        while (true) {
            delay(updateIntervalMillis.toLong().coerceAtLeast(50L))
            tick += 1
        }
    }

    val coords = remember(count, digits, seed, tick) {
        generateLcarsStarCoords(
            count = count,
            digits = digits,
            seed = seed + tick,
        )
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(0.dp),
    ) {
        coords.forEach { coord ->
            LcarsText(
                text = coord,
                style = LocalLcarsTypography.current.telemetry.copy(color = colors.a2),
                maxLines = 1,
                overflow = TextOverflow.Clip,
            )
        }
    }
}

@Composable
fun LcarsNumericLabel(
    label: String,
    modifier: Modifier = Modifier,
    color: Color = LocalLcarsColors.current.lightBlue,
    height: Dp = 46.dp,
    leftWeight: Float = 0.42f,
    labelWeight: Float = 0.24f,
    rightWeight: Float = 0.34f,
) {
    val colors = LocalLcarsColors.current
    val gap = LocalLcarsSpacing.current.gapStandard
    val safeLeftWeight = leftWeight.coerceAtLeast(0.01f)
    val safeLabelWeight = labelWeight.coerceAtLeast(0.01f)
    val safeRightWeight = rightWeight.coerceAtLeast(0.01f)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(height),
        horizontalArrangement = Arrangement.spacedBy(gap),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .weight(safeLeftWeight)
                .fillMaxHeight()
                .background(color, RoundedCornerShapeCompat.StartPill),
        )
        LcarsText(
            text = label.filter(Char::isDigit).ifBlank { "000" },
            modifier = Modifier.weight(safeLabelWeight),
            style = LocalLcarsTypography.current.header.copy(color = color),
            maxLines = 1,
            overflow = TextOverflow.Clip,
            minFontSize = 12.sp,
        )
        Box(
            modifier = Modifier
                .weight(safeRightWeight)
                .fillMaxHeight()
                .background(color, RoundedCornerShapeCompat.EndPill),
        )
        Box(
            modifier = Modifier
                .width(gap)
                .fillMaxHeight()
                .background(colors.background),
        )
    }
}

internal data class LcarsNumberMatrixCell(
    val value: String,
    val widthUnits: Int,
    val visible: Boolean,
) {
    val weight: Float get() = widthUnits.coerceAtLeast(1).toFloat()
}

internal fun generateLcarsNumberMatrix(
    rows: Int,
    columns: Int,
    seed: Int,
): List<List<LcarsNumberMatrixCell>> {
    val safeRows = rows.coerceAtLeast(1)
    val safeColumns = columns.coerceAtLeast(1)
    val widthTemplate = listOf(6, 4, 3, 10, 2, 5, 4, 2, 4, 8, 5, 2, 3, 6, 5, 7, 5, 3, 1)
    val random = StableLcarsRandom(seed)

    return List(safeRows) { row ->
        List(safeColumns) { column ->
            val width = widthTemplate[(column + random.nextInt(widthTemplate.size)) % widthTemplate.size]
            val visible = if (width == 1) {
                random.nextInt(3) != 0
            } else {
                random.nextInt(100) >= 2
            }
            val value = if (width == 1) {
                "."
            } else {
                makeStableDigits(
                    digits = width.coerceAtMost(12),
                    signed = false,
                    random = random,
                )
            }
            LcarsNumberMatrixCell(
                value = value,
                widthUnits = width.coerceAtLeast(1),
                visible = visible || row == 0,
            )
        }
    }
}

internal fun generateLcarsStarCoords(
    count: Int,
    digits: Int,
    seed: Int,
): List<String> {
    val random = StableLcarsRandom(seed)
    return List(count.coerceAtLeast(1)) {
        makeStableDigits(
            digits = digits.coerceAtLeast(1),
            signed = true,
            random = random,
        )
    }
}

internal fun logSeverityColor(
    severity: LcarsLogSeverity,
    colors: LcarsColors,
): Color = when (severity) {
    LcarsLogSeverity.Info -> colors.lightBlue
    LcarsLogSeverity.Success -> colors.tacticalGreen
    LcarsLogSeverity.Warning -> colors.monoAmber
    LcarsLogSeverity.Alert -> colors.alertRed
}

private fun LcarsLogEntry.rendered(): String {
    val prefix = code?.takeIf { it.isNotBlank() }?.let { "$it " }.orEmpty()
    return "$prefix$message"
}

@Composable
private fun NumberMatrixCell(
    cell: LcarsNumberMatrixCell,
    highlighted: Boolean,
    modifier: Modifier = Modifier,
) {
    val colors = LocalLcarsColors.current
    val color = if (highlighted) colors.a8 else colors.a9

    if (cell.value == ".") {
        Canvas(
            modifier = modifier
                .height(16.dp)
                .alphaIf(cell.visible),
        ) {
            drawOval(
                color = color,
                topLeft = Offset(size.width - 18.dp.toPx(), size.height * 0.18f),
                size = Size(18.dp.toPx(), size.height * 0.64f),
            )
        }
    } else {
        LcarsText(
            text = cell.value,
            modifier = modifier.alphaIf(cell.visible),
            style = LocalLcarsTypography.current.labelSmall.copy(color = color),
            maxLines = 1,
            overflow = TextOverflow.Clip,
            minFontSize = 8.sp,
        )
    }
}

private fun Modifier.alphaIf(visible: Boolean): Modifier =
    then(if (visible) Modifier else Modifier.alpha(0f))

internal class StableLcarsRandom(seed: Int) {
    private var state = if (seed == 0) 0x6D2B79F5 else seed

    fun nextInt(bound: Int): Int {
        if (bound <= 1) return 0
        state = state * 1664525 + 1013904223
        return (state ushr 1).absoluteValue % bound
    }

    fun nextFloat(): Float = nextInt(Int.MAX_VALUE) / Int.MAX_VALUE.toFloat()
}

internal fun makeStableDigits(
    digits: Int,
    signed: Boolean,
    random: StableLcarsRandom,
): String {
    val safeDigits = digits.coerceAtLeast(1)
    val body = buildString {
        repeat(safeDigits) {
            append(random.nextInt(10))
        }
    }
    return if (signed && random.nextInt(2) == 0) "-$body" else body
}

private object RoundedCornerShapeCompat {
    val StartPill = androidx.compose.foundation.shape.RoundedCornerShape(
        topStartPercent = 50,
        topEndPercent = 0,
        bottomEndPercent = 0,
        bottomStartPercent = 50,
    )
    val EndPill = androidx.compose.foundation.shape.RoundedCornerShape(
        topStartPercent = 0,
        topEndPercent = 50,
        bottomEndPercent = 50,
        bottomStartPercent = 0,
    )
}
