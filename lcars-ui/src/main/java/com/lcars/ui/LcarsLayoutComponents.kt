package com.lcars.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class LcarsDividerGridType {
    Type1,
    Type2,
    Type3,
}

enum class LcarsResponsiveMode {
    Portrait,
    CompactLandscape,
    WideLandscape,
}

@Composable
fun LcarsDividerGrid(
    type: LcarsDividerGridType,
    modifier: Modifier = Modifier,
    topHeight: Dp = 16.dp,
    bottomHeight: Dp = 16.dp,
) {
    val colors = LocalLcarsColors.current
    val gap = LocalLcarsSpacing.current.gapStandard

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(gap),
    ) {
        DividerGridRow(type = type, top = true, height = topHeight, colors = colors)
        DividerGridRow(type = type, top = false, height = bottomHeight, colors = colors)
    }
}

@Composable
fun LcarsInspectBracket(
    modifier: Modifier = Modifier,
    color: Color = LocalLcarsColors.current.a2,
    markerColor: Color = LocalLcarsColors.current.lightBlue,
    running: Boolean = true,
    showSideRails: Boolean = true,
    content: @Composable BoxScope.() -> Unit,
) {
    val colors = LocalLcarsColors.current
    val transition = rememberInfiniteTransition(label = "LcarsInspectBracket")
    val marker by transition.animateFloat(
        initialValue = 0.1f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1600,
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "LcarsInspectBracketMarker",
    )
    val markerPosition = if (running) marker else 0.42f

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.background),
        contentAlignment = Alignment.Center,
    ) {
        val bracketWidth = when {
            maxWidth < 380.dp -> 32.dp
            maxWidth < 720.dp -> 48.dp
            else -> 64.dp
        }
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(LocalLcarsSpacing.current.gapStandard),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            InspectSide(
                side = LcarsCommandRailSide.Start,
                markerPosition = markerPosition,
                color = color,
                markerColor = markerColor,
                width = bracketWidth,
                showSideRails = showSideRails,
                modifier = Modifier.fillMaxHeight(),
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center,
                content = content,
            )
            InspectSide(
                side = LcarsCommandRailSide.End,
                markerPosition = 1f - markerPosition,
                color = color,
                markerColor = markerColor,
                width = bracketWidth,
                showSideRails = showSideRails,
                modifier = Modifier.fillMaxHeight(),
            )
        }
    }
}

@Composable
fun LcarsTargetScanner(
    running: Boolean,
    modifier: Modifier = Modifier,
    color: Color = LocalLcarsColors.current.monoAmber,
    scanDurationMillis: Int = 6000,
) {
    val progress = if (running) {
        val transition = rememberInfiniteTransition(label = "LcarsTargetScanner")
        val animated by transition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = scanDurationMillis.coerceAtLeast(600),
                    easing = LinearEasing,
                ),
                repeatMode = RepeatMode.Restart,
            ),
            label = "LcarsTargetScannerProgress",
        )
        animated
    } else {
        0.35f
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val strokeWidth = 6.dp.toPx()
        repeat(3) { ring ->
            val localProgress = ((progress + ring / 3f) % 1f)
            val width = size.width * (0.12f + localProgress * 0.78f)
            val height = size.height * (0.12f + localProgress * 0.78f)
            val left = (size.width - width) / 2f
            val top = (size.height - height) / 2f
            val alpha = (1f - localProgress).coerceIn(0.16f, 0.9f)
            val right = left + width
            val bottom = top + height
            val segmentLength = (width.coerceAtMost(height) * 0.30f).coerceAtLeast(18.dp.toPx())
            val segmentThickness = strokeWidth

            listOf(
                lcarsScannerCornerPolygon(left, top, segmentLength, segmentThickness, CornerPosition.TopStart),
                lcarsScannerCornerPolygon(right, top, segmentLength, segmentThickness, CornerPosition.TopEnd),
                lcarsScannerCornerPolygon(left, bottom, segmentLength, segmentThickness, CornerPosition.BottomStart),
                lcarsScannerCornerPolygon(right, bottom, segmentLength, segmentThickness, CornerPosition.BottomEnd),
            ).forEach { cornerPath ->
                drawPath(
                    path = cornerPath,
                    color = Color.White.copy(alpha = alpha * 0.05f),
                )
                drawPath(
                    path = cornerPath,
                    color = color.copy(alpha = alpha),
                )
            }
        }
    }
}

private enum class CornerPosition {
    TopStart,
    TopEnd,
    BottomStart,
    BottomEnd,
}

private fun lcarsScannerCornerPolygon(
    x: Float,
    y: Float,
    length: Float,
    thickness: Float,
    position: CornerPosition,
): Path = Path().apply {
    when (position) {
        CornerPosition.TopStart -> {
            moveTo(x, y)
            lineTo(x + length, y)
            lineTo(x + length, y + thickness)
            lineTo(x + thickness, y + thickness)
            lineTo(x + thickness, y + length)
            lineTo(x, y + length)
        }
        CornerPosition.TopEnd -> {
            moveTo(x, y)
            lineTo(x - length, y)
            lineTo(x - length, y + thickness)
            lineTo(x - thickness, y + thickness)
            lineTo(x - thickness, y + length)
            lineTo(x, y + length)
        }
        CornerPosition.BottomStart -> {
            moveTo(x, y)
            lineTo(x + length, y)
            lineTo(x + length, y - thickness)
            lineTo(x + thickness, y - thickness)
            lineTo(x + thickness, y - length)
            lineTo(x, y - length)
        }
        CornerPosition.BottomEnd -> {
            moveTo(x, y)
            lineTo(x - length, y)
            lineTo(x - length, y - thickness)
            lineTo(x - thickness, y - thickness)
            lineTo(x - thickness, y - length)
            lineTo(x, y - length)
        }
    }
    close()
}

@Composable
fun LcarsResponsiveScaffold(
    modifier: Modifier = Modifier,
    compactLandscapeHeight: Dp = 520.dp,
    compactWidth: Dp = 600.dp,
    portrait: @Composable BoxScope.() -> Unit,
    compactLandscape: @Composable BoxScope.() -> Unit,
    wideLandscape: @Composable BoxScope.() -> Unit,
) {
    BoxWithConstraints(modifier = modifier) {
        when (resolveLcarsResponsiveMode(maxWidth, maxHeight, compactWidth, compactLandscapeHeight)) {
            LcarsResponsiveMode.Portrait -> portrait()
            LcarsResponsiveMode.CompactLandscape -> compactLandscape()
            LcarsResponsiveMode.WideLandscape -> wideLandscape()
        }
    }
}

internal fun resolveLcarsResponsiveMode(
    width: Dp,
    height: Dp,
    compactWidth: Dp = 600.dp,
    compactLandscapeHeight: Dp = 520.dp,
): LcarsResponsiveMode = when {
    height >= width || width < compactWidth -> LcarsResponsiveMode.Portrait
    height < compactLandscapeHeight -> LcarsResponsiveMode.CompactLandscape
    else -> LcarsResponsiveMode.WideLandscape
}

@Composable
private fun DividerGridRow(
    type: LcarsDividerGridType,
    top: Boolean,
    height: Dp,
    colors: LcarsColors,
) {
    val gap = LocalLcarsSpacing.current.gapStandard
    val specs = dividerGridSpecs(type, top, colors)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(height),
        horizontalArrangement = Arrangement.spacedBy(gap),
    ) {
        specs.forEach { spec ->
            Box(
                modifier = Modifier
                    .weight(spec.weight)
                    .fillMaxHeight()
                    .background(spec.color),
            ) {
                if (spec.cutout != DividerCutout.None) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val cutoutWidth = size.width * spec.cutout.widthFraction
                        val cutoutHeight = size.height * 0.45f
                        val x = if (spec.cutout.fromEnd) size.width - cutoutWidth else 0f
                        val y = if (top) size.height - cutoutHeight else 0f
                        drawRoundRect(
                            color = colors.background,
                            topLeft = Offset(x, y),
                            size = Size(cutoutWidth, cutoutHeight),
                            cornerRadius = CornerRadius(3.dp.toPx(), 3.dp.toPx()),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun InspectSide(
    side: LcarsCommandRailSide,
    markerPosition: Float,
    color: Color,
    markerColor: Color,
    width: Dp,
    showSideRails: Boolean,
    modifier: Modifier = Modifier,
) {
    val colors = LocalLcarsColors.current
    Box(
        modifier = modifier.width(width),
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val frameStroke = 8.dp.toPx().coerceAtMost(w * 0.24f)
            val corner = w * 0.64f
            val path = Path().apply {
                if (side == LcarsCommandRailSide.Start) {
                    moveTo(w, 0f)
                    lineTo(corner, 0f)
                    quadraticTo(0f, 0f, 0f, corner)
                    lineTo(0f, h - corner)
                    quadraticTo(0f, h, corner, h)
                    lineTo(w, h)
                    lineTo(w, h - 8.dp.toPx())
                    lineTo(corner, h - 8.dp.toPx())
                    quadraticTo(8.dp.toPx(), h - 8.dp.toPx(), 8.dp.toPx(), h - corner)
                    lineTo(8.dp.toPx(), corner)
                    quadraticTo(8.dp.toPx(), 8.dp.toPx(), corner, 8.dp.toPx())
                    lineTo(w, 8.dp.toPx())
                } else {
                    moveTo(0f, 0f)
                    lineTo(w - corner, 0f)
                    quadraticTo(w, 0f, w, corner)
                    lineTo(w, h - corner)
                    quadraticTo(w, h, w - corner, h)
                    lineTo(0f, h)
                    lineTo(0f, h - 8.dp.toPx())
                    lineTo(w - corner, h - 8.dp.toPx())
                    quadraticTo(w - 8.dp.toPx(), h - 8.dp.toPx(), w - 8.dp.toPx(), h - corner)
                    lineTo(w - 8.dp.toPx(), corner)
                    quadraticTo(w - 8.dp.toPx(), 8.dp.toPx(), w - corner, 8.dp.toPx())
                    lineTo(0f, 8.dp.toPx())
                }
                close()
            }
            drawPath(path, color)

            if (showSideRails) {
                val railClearance = 7.dp.toPx().coerceAtMost(w * 0.16f)
                val railWidth = w * 0.34f
                val lightWidth = railWidth * 0.46f
                val darkWidth = railWidth * 0.32f
                val innerGap = railWidth * 0.16f
                val railInset = frameStroke + railClearance
                val lightLeft = if (side == LcarsCommandRailSide.Start) {
                    railInset
                } else {
                    w - railInset - lightWidth
                }
                val darkLeft = if (side == LcarsCommandRailSide.Start) {
                    lightLeft + lightWidth + innerGap
                } else {
                    lightLeft - innerGap - darkWidth
                }
                val gutterPadding = 5.dp.toPx()
                val gutterLeft = (lightLeft.coerceAtMost(darkLeft) - gutterPadding).coerceAtLeast(0f)
                val gutterRight = (lightLeft.coerceAtLeast(darkLeft) + lightWidth.coerceAtLeast(darkWidth) + gutterPadding)
                    .coerceAtMost(w)
                drawRect(
                    color = colors.background,
                    topLeft = Offset(gutterLeft, h * 0.08f),
                    size = Size(gutterRight - gutterLeft, h * 0.84f),
                )
                drawRect(
                    color = colors.a3,
                    topLeft = Offset(lightLeft, h * 0.12f),
                    size = Size(lightWidth, h * 0.76f),
                )
                drawRect(
                    color = colors.a7,
                    topLeft = Offset(darkLeft, h * 0.18f),
                    size = Size(darkWidth, h * 0.64f),
                )
            }
            val markerY = h * markerPosition.coerceIn(0.08f, 0.92f)
            drawOval(
                color = markerColor,
                topLeft = Offset(
                    x = if (side == LcarsCommandRailSide.Start) w * 0.42f else w * 0.18f,
                    y = markerY - 7.dp.toPx(),
                ),
                size = Size(w * 0.5f, 14.dp.toPx()),
            )
            val tickStart = 0.14f
            val tickEnd = 0.86f
            val tickCount = 13
            val tickStep = (tickEnd - tickStart) / (tickCount - 1)
            val markerRange = tickStep * 3.2f
            repeat(tickCount) { index ->
                val tickPosition = tickStart + index * tickStep
                val y = h * tickPosition
                val markerDistance = kotlin.math.abs(tickPosition - markerPosition.coerceIn(0.08f, 0.92f))
                val highlight = (1f - markerDistance / markerRange).coerceIn(0f, 1f)
                val length = w * (0.16f + highlight * 0.42f)
                val edgePadding = frameStroke + 5.dp.toPx()
                val edgeX = if (side == LcarsCommandRailSide.Start) edgePadding else w - edgePadding
                val innerX = if (side == LcarsCommandRailSide.Start) {
                    edgeX + length
                } else {
                    edgeX - length
                }
                val alpha = 0.055f + highlight * 0.945f
                val glow = highlight * highlight
                val strokeWidth = 1.dp.toPx() + highlight * 2.dp.toPx()
                if (glow > 0f) {
                    drawLine(
                        color = markerColor.copy(alpha = glow * 0.34f),
                        start = Offset(edgeX, y),
                        end = Offset(innerX, y),
                        strokeWidth = strokeWidth + 4.dp.toPx(),
                    )
                }
                drawLine(
                    color = markerColor.copy(alpha = alpha),
                    start = Offset(edgeX, y),
                    end = Offset(innerX, y),
                    strokeWidth = strokeWidth,
                )
                if (glow > 0.08f) {
                    drawLine(
                        color = markerColor.copy(alpha = glow * 0.62f),
                        start = Offset(edgeX, y),
                        end = Offset(innerX, y),
                        strokeWidth = 0.6.dp.toPx() + highlight * 0.9.dp.toPx(),
                    )
                }
            }
        }
    }
}

private data class DividerSpec(
    val weight: Float,
    val color: Color,
    val cutout: DividerCutout = DividerCutout.None,
)

private data class DividerCutout(
    val widthFraction: Float,
    val fromEnd: Boolean,
) {
    companion object {
        val None = DividerCutout(0f, false)
    }
}

private fun dividerGridSpecs(
    type: LcarsDividerGridType,
    top: Boolean,
    colors: LcarsColors,
): List<DividerSpec> = when (type) {
    LcarsDividerGridType.Type1 -> if (top) {
        listOf(
            DividerSpec(0.16f, colors.a2),
            DividerSpec(0.44f, colors.a3),
            DividerSpec(0.28f, colors.a2),
            DividerSpec(0.12f, colors.a2),
        )
    } else {
        listOf(
            DividerSpec(0.16f, colors.a7),
            DividerSpec(0.44f, colors.a3, DividerCutout(1f, false)),
            DividerSpec(0.28f, colors.a7),
            DividerSpec(0.12f, colors.a3),
        )
    }
    LcarsDividerGridType.Type2 -> if (top) {
        listOf(
            DividerSpec(0.15f, colors.a2),
            DividerSpec(0.70f, colors.a1, DividerCutout(0.42f, false)),
            DividerSpec(0.15f, colors.a2),
        )
    } else {
        listOf(
            DividerSpec(0.15f, colors.a7),
            DividerSpec(0.70f, colors.a1, DividerCutout(0.42f, false)),
            DividerSpec(0.15f, colors.a7),
        )
    }
    LcarsDividerGridType.Type3 -> if (top) {
        listOf(
            DividerSpec(0.18f, colors.a7),
            DividerSpec(0.32f, colors.a8, DividerCutout(0.46f, true)),
            DividerSpec(0.32f, colors.a7),
            DividerSpec(0.18f, colors.a8),
        )
    } else {
        listOf(
            DividerSpec(0.18f, colors.a8),
            DividerSpec(0.32f, colors.a9, DividerCutout(0.46f, true)),
            DividerSpec(0.32f, colors.a6),
            DividerSpec(0.18f, colors.a6),
        )
    }
}
