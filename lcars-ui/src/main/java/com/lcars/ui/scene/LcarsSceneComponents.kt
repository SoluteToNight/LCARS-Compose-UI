package com.lcars.ui.scene

import com.lcars.ui.theme.*
import com.lcars.ui.foundation.*
import com.lcars.ui.controls.*
import com.lcars.ui.display.*
import com.lcars.ui.layout.*
import com.lcars.ui.padd.*

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.max
import kotlin.math.min

enum class LcarsStarChartMode {
    Navigation,
    Inspection,
}

@androidx.compose.runtime.Immutable
data class LcarsStar(
    val label: String,
    val x: Float,
    val y: Float,
    val size: Float = 9f,
    val labeled: Boolean = true,
)

@Composable
fun LcarsTransmissionFrame(
    headerLabel: String,
    footerLabel: String,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit = {},
) {
    val colors = LocalLcarsColors.current
    val spacing = LocalLcarsSpacing.current
    val gap = spacing.gapStandard
    val compact = LocalLcarsAdaptiveProfile.current.compact

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
            .padding(gap),
        verticalArrangement = Arrangement.spacedBy(gap),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        LcarsBar(
            color = colors.lightBlue,
            height = spacing.barHeight + if (compact) 6.dp else 10.dp,
            startCap = true,
            endCap = true,
            label = headerLabel,
            labelAlign = LcarsLabelAlign.End,
            labelColor = colors.monoAmber,
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(colors.panel),
            contentAlignment = Alignment.Center,
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val stroke = 16.dp.toPx()
                drawRoundRect(
                    color = colors.monoAmber,
                    topLeft = Offset(0f, 0f),
                    size = Size(size.width, size.height),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(0f, 82.dp.toPx()),
                    style = Stroke(width = stroke),
                )
                drawRect(
                    color = colors.background,
                    topLeft = Offset(0f, size.height * 0.46f),
                    size = Size(stroke * 1.4f, size.height * 0.12f),
                )
                drawRect(
                    color = colors.a7,
                    topLeft = Offset(0f, 0f),
                    size = Size(142.dp.toPx().coerceAtMost(size.width * 0.32f), stroke),
                )
                drawRect(
                    color = colors.a3,
                    topLeft = Offset(0f, size.height - stroke),
                    size = Size(182.dp.toPx().coerceAtMost(size.width * 0.42f), stroke),
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 18.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(gap),
            ) {
                LcarsText(
                    text = lcarsLabel(title),
                    style = LocalLcarsTypography.current.header.copy(
                        color = colors.monoAmber,
                        fontSize = 52.sp,
                        lineHeight = 54.sp,
                        textAlign = TextAlign.Center,
                    ),
                    maxLines = 2,
                )
                LcarsText(
                    text = lcarsLabel(subtitle),
                    style = LocalLcarsTypography.current.telemetry.copy(
                        color = colors.lightBlue,
                        fontSize = 28.sp,
                        lineHeight = 30.sp,
                        textAlign = TextAlign.Center,
                    ),
                    maxLines = 3,
                    softWrap = true,
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = false),
                    contentAlignment = Alignment.Center,
                    content = content,
                )
            }
        }
        LcarsBar(
            color = colors.a7,
            height = spacing.barHeight + if (compact) 2.dp else 4.dp,
            startCap = true,
            endCap = true,
            label = footerLabel,
            labelAlign = LcarsLabelAlign.Start,
            labelColor = colors.lightBlue,
        )
    }
}

@Composable
fun LcarsStarChart(
    mode: LcarsStarChartMode,
    modifier: Modifier = Modifier,
    stars: List<LcarsStar>? = null,
    seed: Int = 1701,
    showCoords: Boolean = true,
    showScanner: Boolean = true,
    running: Boolean = true,
) {
    val colors = LocalLcarsColors.current
    val generatedStars = remember(stars, seed) {
        stars ?: generateLcarsStars(seed = seed)
    }

    val chartContent: @Composable BoxScope.() -> Unit = {
        if (showScanner) {
            LcarsTargetScanner(
                running = running,
                color = colors.a5,
                modifier = Modifier.fillMaxSize(),
            )
        }
        StarChartCanvas(
            stars = generatedStars,
            seed = seed,
            modifier = Modifier.fillMaxSize(),
        )
        if (showCoords) {
            LcarsStarCoords(
                seed = seed,
                running = running,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(
                        start = LocalLcarsSpacing.current.panelPadding + 4.dp,
                        top = LocalLcarsSpacing.current.panelPadding + 6.dp,
                        end = LocalLcarsSpacing.current.panelPadding,
                        bottom = LocalLcarsSpacing.current.panelPadding,
                    ),
            )
        }
    }

    if (mode == LcarsStarChartMode.Inspection) {
        LcarsInspectBracket(
            modifier = modifier.background(colors.background),
            running = running,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colors.panel),
                content = chartContent,
            )
        }
    } else {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(colors.background),
            content = chartContent,
        )
    }
}

@Composable
private fun StarChartCanvas(
    stars: List<LcarsStar>,
    seed: Int,
    modifier: Modifier = Modifier,
) {
    val colors = LocalLcarsColors.current
    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current
    val labelStyle = LocalLcarsTypography.current.labelSmall.copy(
        color = colors.a8,
        fontSize = 18.sp,
        lineHeight = 20.sp,
    )
    val gridLabelStyle = LocalLcarsTypography.current.labelSmall.copy(
        color = colors.a4,
        fontSize = 12.sp,
        lineHeight = 14.sp,
    )
    val gridLabels = remember(seed) { generateGridLabels(seed, 80) }

    val starLayouts = remember(stars, labelStyle, density) {
        stars.map { star ->
            if (star.labeled && star.label.isNotBlank()) {
                textMeasurer.measure(
                    text = lcarsLabel(star.label),
                    style = labelStyle,
                )
            } else {
                null
            }
        }
    }

    val gridLabelLayouts = remember(gridLabels, gridLabelStyle, density) {
        gridLabels.map { label ->
            textMeasurer.measure(
                text = label,
                style = gridLabelStyle,
            )
        }
    }

    val acceptedLabels = remember { mutableListOf<Rect>() }

    Canvas(modifier = modifier.background(Color.Black)) {
        val gridColor = colors.a3.copy(alpha = 0.58f)
        val gridStep = max(88.dp.toPx(), min(size.width, size.height) / 4f)
        var x = 0f
        var labelIndex = 0
        while (x <= size.width + gridStep) {
            drawLine(
                color = gridColor,
                start = Offset(x, 0f),
                end = Offset(x, size.height),
                strokeWidth = 1.dp.toPx(),
            )
            x += gridStep
        }
        var y = 0f
        while (y <= size.height + gridStep) {
            drawLine(
                color = gridColor,
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = 1.dp.toPx(),
            )
            val labelLayout = gridLabelLayouts[labelIndex % gridLabelLayouts.size]
            val labelTop = y + 6.dp.toPx()
            if (labelTop + labelLayout.size.height <= size.height - 6.dp.toPx()) {
                drawRect(
                    color = Color.Black,
                    topLeft = Offset(6.dp.toPx(), labelTop - 1.dp.toPx()),
                    size = Size(labelLayout.size.width.toFloat() + 4.dp.toPx(), labelLayout.size.height.toFloat() + 2.dp.toPx()),
                )
                drawText(
                    textLayoutResult = labelLayout,
                    topLeft = Offset(8.dp.toPx(), labelTop),
                )
            }
            labelIndex += 1
            y += gridStep
        }

        acceptedLabels.clear()
        stars.forEachIndexed { index, star ->
            val center = Offset(
                x = star.x.coerceIn(0f, 1f) * size.width,
                y = star.y.coerceIn(0f, 1f) * size.height,
            )
            val radius = star.size.dp.toPx().coerceAtLeast(1.5.dp.toPx()) / 2f
            val starColor = if (star.labeled) colors.a1 else colors.a5
            drawCircle(
                color = starColor,
                radius = radius,
                center = center,
            )

            val measured = starLayouts[index]
            if (measured != null) {
                val selectSize = 34.dp.toPx()
                val selectMargin = 5.dp.toPx()
                val textW = measured.size.width.toFloat()
                val textH = measured.size.height.toFloat()

                drawRect(
                    color = colors.lightBlue,
                    topLeft = Offset(center.x - selectSize / 2f, center.y - selectSize / 2f),
                    size = Size(7.dp.toPx(), selectSize),
                    style = Stroke(width = 3.dp.toPx()),
                )
                drawRect(
                    color = colors.lightBlue,
                    topLeft = Offset(center.x + selectSize / 2f - 7.dp.toPx(), center.y - selectSize / 2f),
                    size = Size(7.dp.toPx(), selectSize),
                    style = Stroke(width = 3.dp.toPx()),
                )

                // 8-Direction Adaptive Placement without DrawScope object allocations
                val paddingEdge = 6.dp.toPx()
                val minX = paddingEdge
                val maxX = size.width - paddingEdge
                val minY = paddingEdge
                val maxY = size.height - paddingEdge

                var bestLeft = Float.NaN
                var bestTop = Float.NaN

                for (dir in 0..7) {
                    val candLeft: Float
                    val candTop: Float
                    when (dir) {
                        0 -> { // 1. Right (Standard preferred)
                            candLeft = center.x + selectSize / 2f + selectMargin
                            candTop = center.y - textH / 2f
                        }
                        1 -> { // 2. Left
                            candLeft = center.x - selectSize / 2f - selectMargin - textW
                            candTop = center.y - textH / 2f
                        }
                        2 -> { // 3. Bottom
                            candLeft = center.x - textW / 2f
                            candTop = center.y + selectSize / 2f + 4.dp.toPx()
                        }
                        3 -> { // 4. Top
                            candLeft = center.x - textW / 2f
                            candTop = center.y - selectSize / 2f - 4.dp.toPx() - textH
                        }
                        4 -> { // 5. Top-Right
                            candLeft = center.x + selectSize / 2f + 2.dp.toPx()
                            candTop = center.y - selectSize / 2f - textH
                        }
                        5 -> { // 6. Top-Left
                            candLeft = center.x - selectSize / 2f - 2.dp.toPx() - textW
                            candTop = center.y - selectSize / 2f - textH
                        }
                        6 -> { // 7. Bottom-Right
                            candLeft = center.x + selectSize / 2f + 2.dp.toPx()
                            candTop = center.y + selectSize / 2f
                        }
                        else -> { // 8. Bottom-Left
                            candLeft = center.x - selectSize / 2f - 2.dp.toPx() - textW
                            candTop = center.y + selectSize / 2f
                        }
                    }
                    val candRight = candLeft + textW
                    val candBottom = candTop + textH

                    if (candLeft >= minX && candRight <= maxX && candTop >= minY && candBottom <= maxY) {
                        val pad = 6.dp.toPx()
                        val overlaps = acceptedLabels.any { placed ->
                            val pLeft = placed.left - pad
                            val pTop = placed.top - pad
                            val pRight = placed.right + pad
                            val pBottom = placed.bottom + pad
                            !(candRight <= pLeft || candLeft >= pRight || candBottom <= pTop || candTop >= pBottom)
                        }
                        if (!overlaps) {
                            bestLeft = candLeft
                            bestTop = candTop
                            break
                        }
                    }
                }

                if (bestLeft.isNaN()) {
                    val r0Left = center.x + selectSize / 2f + selectMargin
                    val r0Top = center.y - textH / 2f
                    bestLeft = r0Left.coerceIn(paddingEdge, (size.width - textW - paddingEdge).coerceAtLeast(paddingEdge))
                    bestTop = r0Top.coerceIn(paddingEdge, (size.height - textH - paddingEdge).coerceAtLeast(paddingEdge))
                }

                val bestPlacement = Rect(bestLeft, bestTop, bestLeft + textW, bestTop + textH)
                acceptedLabels += bestPlacement
                drawRect(
                    color = Color.Black,
                    topLeft = Offset(bestPlacement.left - 2.dp.toPx(), bestPlacement.top - 1.dp.toPx()),
                    size = Size(textW + 4.dp.toPx(), textH + 2.dp.toPx()),
                )
                drawText(
                    textLayoutResult = measured,
                    topLeft = Offset(bestPlacement.left, bestPlacement.top),
                )
            }
        }
    }
}

internal fun generateLcarsStars(
    seed: Int,
    backgroundCount: Int = 72,
    labeledCount: Int = 10,
): List<LcarsStar> {
    val random = StableLcarsRandom(seed)
    val labels = listOf(
        "alcor",
        "antares",
        "betazed",
        "deneb",
        "el-adrel",
        "forcas",
        "galorndon",
        "mintaka",
        "rigel",
        "sol",
        "tiburon",
        "vega",
        "wolf 359",
        "zakdorn",
    )
    val background = List(backgroundCount.coerceAtLeast(0)) {
        LcarsStar(
            label = "",
            x = random.nextFloat(),
            y = random.nextFloat(),
            size = (1 + random.nextInt(8)).toFloat(),
            labeled = false,
        )
    }
    val labeled = mutableListOf<LcarsStar>()
    for (index in 0 until labeledCount.coerceAtLeast(0)) {
        var bestX = 0.1f
        var bestY = 0.1f
        var maxMinDist = -1f
        // Try candidate positions to maximize distance from other labeled stars
        for (attempt in 0..15) {
            val candX = 0.06f + random.nextFloat() * 0.76f
            val candY = 0.08f + random.nextFloat() * 0.76f
            val minDist = labeled.minOfOrNull { (it.x - candX) * (it.x - candX) + (it.y - candY) * (it.y - candY) } ?: Float.MAX_VALUE
            if (minDist > maxMinDist) {
                maxMinDist = minDist
                bestX = candX
                bestY = candY
            }
            if (minDist > 0.03f) { // Safe distance threshold
                break
            }
        }
        labeled += LcarsStar(
            label = labels[index % labels.size],
            x = bestX,
            y = bestY,
            size = (8 + random.nextInt(5)).toFloat(),
            labeled = true,
        )
    }
    return background + labeled
}

private fun generateGridLabels(seed: Int, count: Int): List<String> {
    val random = StableLcarsRandom(seed * 31 + 9)
    return List(count.coerceAtLeast(1)) {
        makeStableDigits(4, signed = true, random = random)
    }
}
