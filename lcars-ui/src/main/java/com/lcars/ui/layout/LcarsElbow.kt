package com.lcars.ui.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isSpecified
import com.lcars.ui.foundation.LcarsText
import com.lcars.ui.theme.*
import kotlin.math.min

enum class LcarsElbowDirection {
    TopLeft,
    BottomLeft,
    TopRight,
    BottomRight,
}

/**
 * Foundational LCARS L-shaped corner piece (LcarsElbow).
 *
 * Low-level geometric drawing primitive. Renders the LCARS asymmetric elbow curve.
 *
 * Supports:
 * - Asymmetric arm thickness (horizontalThickness vs verticalThickness)
 * - Independent outer corner curve and inner corner radius
 * - Native concentric nested double-elbow rendering (nestedColor)
 * - Responsive sizing (fixed wingWidth/wingHeight or flexible fill via Modifier)
 * - Multi-slot typography, badges, and inner content
 */
@Composable
fun LcarsElbow(
    color: Color,
    direction: LcarsElbowDirection,
    modifier: Modifier = Modifier,
    text: String = "",
    thickness: Dp = 40.dp,
    horizontalThickness: Dp = thickness,
    verticalThickness: Dp = thickness,
    wingWidth: Dp = 132.dp,
    wingHeight: Dp = 72.dp,
    outerRadius: Dp = verticalThickness,
    innerRadius: Dp = 16.dp,
    nestedColor: Color? = null,
    nestedHorizontalThickness: Dp = horizontalThickness,
    nestedVerticalThickness: Dp = verticalThickness,
    nestedGap: Dp = 4.dp,
    nestedInnerRadius: Dp = 4.dp,
    labelSlot: (@Composable BoxScope.() -> Unit)? = null,
    badgeSlot: (@Composable BoxScope.() -> Unit)? = null,
    content: (@Composable BoxScope.() -> Unit)? = null,
) {
    val sizeModifier = when {
        wingWidth.isSpecified && wingHeight.isSpecified && wingWidth > 0.dp && wingHeight > 0.dp ->
            Modifier.size(wingWidth, wingHeight)
        wingWidth.isSpecified && wingWidth > 0.dp ->
            Modifier.width(wingWidth)
        wingHeight.isSpecified && wingHeight > 0.dp ->
            Modifier.height(wingHeight)
        else -> Modifier
    }

    val textAlignment = when (direction) {
        LcarsElbowDirection.TopLeft -> Alignment.TopEnd
        LcarsElbowDirection.TopRight -> Alignment.TopStart
        LcarsElbowDirection.BottomLeft -> Alignment.BottomEnd
        LcarsElbowDirection.BottomRight -> Alignment.BottomStart
    }

    Box(
        modifier = modifier
            .then(sizeModifier)
            .drawWithCache {
                val w = size.width
                val h = size.height

                val ht = horizontalThickness.toPx().coerceAtMost(h)
                val vt = verticalThickness.toPx().coerceAtMost(w)
                val outR = outerRadius.toPx().coerceAtMost(min(w, h)).coerceAtLeast(min(ht, vt))
                val inR = innerRadius.toPx().coerceAtMost(min(ht, vt))

                val mainPath = lcarsElbowPath(direction, w, h, ht, vt, outR, inR)

                val nestedPath = if (nestedColor != null && nestedColor != Color.Transparent) {
                    val gap = nestedGap.toPx()
                    val nht = nestedHorizontalThickness.toPx().coerceAtMost(h)
                    val nvt = nestedVerticalThickness.toPx().coerceAtMost(w)
                    val ninR = nestedInnerRadius.toPx().coerceAtMost(min(nht, nvt) / 2f)
                    lcarsNestedElbowPath(direction, w, h, ht, vt, nht, nvt, gap, inR, ninR)
                } else null

                onDrawBehind {
                    drawPath(
                        path = mainPath,
                        color = color,
                    )
                    if (nestedPath != null && nestedColor != null) {
                        drawPath(
                            path = nestedPath,
                            color = nestedColor,
                        )
                    }
                }
            }
    ) {
        if (labelSlot != null) {
            labelSlot()
        } else if (text.isNotBlank()) {
            LcarsText(
                text = text,
                modifier = Modifier
                    .align(textAlignment)
                    .padding(
                        end = if (direction == LcarsElbowDirection.TopLeft || direction == LcarsElbowDirection.BottomLeft) 10.dp else 4.dp,
                        start = if (direction == LcarsElbowDirection.TopRight || direction == LcarsElbowDirection.BottomRight) 10.dp else 4.dp,
                        top = 5.dp,
                    ),
                style = LocalLcarsTypography.current.labelSmall.copy(color = Color.Black),
                maxLines = 1,
            )
        }

        if (badgeSlot != null) {
            badgeSlot()
        }

        if (content != null) {
            content()
        }
    }
}

/**
 * Authentic LCARS Elbow Frame Container (LcarsElbowFrame).
 *
 * Employs a monolithic unified [LcarsElbow] vector path at the corner, guaranteeing
 * zero gaps, zero seam disconnects, and continuous curved transition between spine and rail.
 *
 * Architecture:
 * - Corner: Unified monolithic [LcarsElbow] (outer radius + inner radius).
 * - Top Rail: [railSlot] positioned along the top horizontal rail with standard LCARS gap.
 * - Left Spine: [spineSlot] positioned along the vertical spine with standard LCARS gap.
 * - Framed Viewport: [content] automatically bounded within the inner corner.
 */
@Composable
fun LcarsElbowFrame(
    color: Color,
    modifier: Modifier = Modifier,
    direction: LcarsElbowDirection = LcarsElbowDirection.TopLeft,
    spineWidth: Dp = 140.dp,
    railThickness: Dp = 30.dp,
    elbowWidth: Dp = 220.dp,
    elbowHeight: Dp = 100.dp,
    outerRadius: Dp = 70.dp,
    innerRadius: Dp = 24.dp,
    gap: Dp = 4.dp,
    elbowLabel: String = "",
    backgroundColor: Color = LocalLcarsColors.current.background,
    spineSlot: (@Composable ColumnScope.() -> Unit)? = null,
    railSlot: (@Composable BoxScope.() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit,
) {
    when (direction) {
        LcarsElbowDirection.TopLeft -> {
            Box(
                modifier = modifier
                    .background(backgroundColor)
                    .fillMaxSize(),
            ) {
                // 1. TOP-LEFT UNIFIED ELBOW
                LcarsElbow(
                    color = color,
                    direction = LcarsElbowDirection.TopLeft,
                    horizontalThickness = railThickness,
                    verticalThickness = spineWidth,
                    wingWidth = elbowWidth,
                    wingHeight = elbowHeight,
                    outerRadius = outerRadius,
                    innerRadius = innerRadius,
                    text = elbowLabel,
                    modifier = Modifier.align(Alignment.TopStart),
                )

                // 2. TOP HORIZONTAL RAIL (after elbow width)
                if (railSlot != null) {
                    Box(
                        modifier = Modifier
                            .padding(start = elbowWidth + gap)
                            .fillMaxWidth()
                            .height(railThickness)
                            .align(Alignment.TopStart),
                    ) {
                        railSlot()
                    }
                }

                // 3. LEFT SPINE BUTTON COLUMN (below elbow height)
                if (spineSlot != null) {
                    Column(
                        modifier = Modifier
                            .padding(top = elbowHeight + gap)
                            .width(spineWidth)
                            .fillMaxHeight()
                            .align(Alignment.TopStart),
                        verticalArrangement = Arrangement.spacedBy(gap),
                    ) {
                        spineSlot()
                    }
                }

                // 4. INNER FRAMED VIEWPORT CONTENT
                Box(
                    modifier = Modifier
                        .padding(start = spineWidth + gap, top = railThickness + gap)
                        .fillMaxSize(),
                ) {
                    content()
                }
            }
        }

        LcarsElbowDirection.BottomLeft -> {
            Box(
                modifier = modifier
                    .background(backgroundColor)
                    .fillMaxSize(),
            ) {
                // 1. BOTTOM-LEFT UNIFIED ELBOW
                LcarsElbow(
                    color = color,
                    direction = LcarsElbowDirection.BottomLeft,
                    horizontalThickness = railThickness,
                    verticalThickness = spineWidth,
                    wingWidth = elbowWidth,
                    wingHeight = elbowHeight,
                    outerRadius = outerRadius,
                    innerRadius = innerRadius,
                    text = elbowLabel,
                    modifier = Modifier.align(Alignment.BottomStart),
                )

                // 2. BOTTOM HORIZONTAL RAIL (after elbow width)
                if (railSlot != null) {
                    Box(
                        modifier = Modifier
                            .padding(start = elbowWidth + gap)
                            .fillMaxWidth()
                            .height(railThickness)
                            .align(Alignment.BottomStart),
                    ) {
                        railSlot()
                    }
                }

                // 3. LEFT SPINE BUTTON COLUMN (above elbow height)
                if (spineSlot != null) {
                    Column(
                        modifier = Modifier
                            .padding(bottom = elbowHeight + gap)
                            .width(spineWidth)
                            .fillMaxHeight()
                            .align(Alignment.TopStart),
                        verticalArrangement = Arrangement.spacedBy(gap),
                    ) {
                        spineSlot()
                    }
                }

                // 4. INNER FRAMED VIEWPORT CONTENT
                Box(
                    modifier = Modifier
                        .padding(start = spineWidth + gap, bottom = railThickness + gap)
                        .fillMaxSize(),
                ) {
                    content()
                }
            }
        }

        LcarsElbowDirection.TopRight -> {
            Box(
                modifier = modifier
                    .background(backgroundColor)
                    .fillMaxSize(),
            ) {
                // 1. TOP-RIGHT UNIFIED ELBOW
                LcarsElbow(
                    color = color,
                    direction = LcarsElbowDirection.TopRight,
                    horizontalThickness = railThickness,
                    verticalThickness = spineWidth,
                    wingWidth = elbowWidth,
                    wingHeight = elbowHeight,
                    outerRadius = outerRadius,
                    innerRadius = innerRadius,
                    text = elbowLabel,
                    modifier = Modifier.align(Alignment.TopEnd),
                )

                // 2. TOP HORIZONTAL RAIL (before elbow width)
                if (railSlot != null) {
                    Box(
                        modifier = Modifier
                            .padding(end = elbowWidth + gap)
                            .fillMaxWidth()
                            .height(railThickness)
                            .align(Alignment.TopStart),
                    ) {
                        railSlot()
                    }
                }

                // 3. RIGHT SPINE BUTTON COLUMN (below elbow height)
                if (spineSlot != null) {
                    Column(
                        modifier = Modifier
                            .padding(top = elbowHeight + gap)
                            .width(spineWidth)
                            .fillMaxHeight()
                            .align(Alignment.TopEnd),
                        verticalArrangement = Arrangement.spacedBy(gap),
                    ) {
                        spineSlot()
                    }
                }

                // 4. INNER FRAMED VIEWPORT CONTENT
                Box(
                    modifier = Modifier
                        .padding(end = spineWidth + gap, top = railThickness + gap)
                        .fillMaxSize(),
                ) {
                    content()
                }
            }
        }

        LcarsElbowDirection.BottomRight -> {
            Box(
                modifier = modifier
                    .background(backgroundColor)
                    .fillMaxSize(),
            ) {
                // 1. BOTTOM-RIGHT UNIFIED ELBOW
                LcarsElbow(
                    color = color,
                    direction = LcarsElbowDirection.BottomRight,
                    horizontalThickness = railThickness,
                    verticalThickness = spineWidth,
                    wingWidth = elbowWidth,
                    wingHeight = elbowHeight,
                    outerRadius = outerRadius,
                    innerRadius = innerRadius,
                    text = elbowLabel,
                    modifier = Modifier.align(Alignment.BottomEnd),
                )

                // 2. BOTTOM HORIZONTAL RAIL (before elbow width)
                if (railSlot != null) {
                    Box(
                        modifier = Modifier
                            .padding(end = elbowWidth + gap)
                            .fillMaxWidth()
                            .height(railThickness)
                            .align(Alignment.BottomStart),
                    ) {
                        railSlot()
                    }
                }

                // 3. RIGHT SPINE BUTTON COLUMN (above elbow height)
                if (spineSlot != null) {
                    Column(
                        modifier = Modifier
                            .padding(bottom = elbowHeight + gap)
                            .width(spineWidth)
                            .fillMaxHeight()
                            .align(Alignment.TopEnd),
                        verticalArrangement = Arrangement.spacedBy(gap),
                    ) {
                        spineSlot()
                    }
                }

                // 4. INNER FRAMED VIEWPORT CONTENT
                Box(
                    modifier = Modifier
                        .padding(end = spineWidth + gap, bottom = railThickness + gap)
                        .fillMaxSize(),
                ) {
                    content()
                }
            }
        }
    }
}

/**
 * Computes the outer LCARS elbow polygon path with asymmetric arm thicknesses.
 */
internal fun lcarsElbowPath(
    direction: LcarsElbowDirection,
    width: Float,
    height: Float,
    horizontalThickness: Float,
    verticalThickness: Float,
    outerRadius: Float,
    innerRadius: Float,
): Path {
    val ht = horizontalThickness
    val vt = verticalThickness
    val r = outerRadius
    val i = innerRadius

    return Path().apply {
        when (direction) {
            LcarsElbowDirection.TopLeft -> {
                moveTo(0f, height)
                lineTo(0f, r)
                arcTo(Rect(0f, 0f, r * 2f, r * 2f), 180f, 90f, false)
                lineTo(width, 0f)
                lineTo(width, ht)
                lineTo(vt + i, ht)
                arcTo(Rect(vt, ht, vt + i * 2f, ht + i * 2f), 270f, -90f, false)
                lineTo(vt, height)
            }
            LcarsElbowDirection.TopRight -> {
                moveTo(width, height)
                lineTo(width, r)
                arcTo(Rect(width - r * 2f, 0f, width, r * 2f), 0f, -90f, false)
                lineTo(0f, 0f)
                lineTo(0f, ht)
                lineTo(width - vt - i, ht)
                arcTo(Rect(width - vt - i * 2f, ht, width - vt, ht + i * 2f), 270f, 90f, false)
                lineTo(width - vt, height)
            }
            LcarsElbowDirection.BottomLeft -> {
                moveTo(0f, 0f)
                lineTo(0f, height - r)
                arcTo(Rect(0f, height - r * 2f, r * 2f, height), 180f, -90f, false)
                lineTo(width, height)
                lineTo(width, height - ht)
                lineTo(vt + i, height - ht)
                arcTo(Rect(vt, height - ht - i * 2f, vt + i * 2f, height - ht), 90f, 90f, false)
                lineTo(vt, 0f)
            }
            LcarsElbowDirection.BottomRight -> {
                moveTo(width, 0f)
                lineTo(width, height - r)
                arcTo(Rect(width - r * 2f, height - r * 2f, width, height), 0f, 90f, false)
                lineTo(0f, height)
                lineTo(0f, height - ht)
                lineTo(width - vt - i, height - ht)
                arcTo(Rect(width - vt - i * 2f, height - ht - i * 2f, width - vt, height - ht), 90f, -90f, false)
                lineTo(width - vt, 0f)
            }
        }
        close()
    }
}

/**
 * Computes the concentric nested inner elbow path.
 */
internal fun lcarsNestedElbowPath(
    direction: LcarsElbowDirection,
    width: Float,
    height: Float,
    outerHt: Float,
    outerVt: Float,
    nestedHt: Float,
    nestedVt: Float,
    gap: Float,
    outerInR: Float,
    nestedInR: Float,
): Path {
    val i = nestedInR

    return Path().apply {
        when (direction) {
            LcarsElbowDirection.TopLeft -> {
                val startX = outerVt + gap
                val startY = outerHt + gap
                val endX = (startX + nestedVt).coerceAtMost(width)
                val endY = (startY + nestedHt).coerceAtMost(height)

                moveTo(startX, height)
                lineTo(startX, startY + outerInR)
                arcTo(Rect(startX, startY, startX + outerInR * 2f, startY + outerInR * 2f), 180f, 90f, false)
                lineTo(width, startY)
                lineTo(width, endY)
                lineTo(endX + i, endY)
                arcTo(Rect(endX, endY, endX + i * 2f, endY + i * 2f), 270f, -90f, false)
                lineTo(endX, height)
            }
            LcarsElbowDirection.TopRight -> {
                val startX = width - outerVt - gap
                val startY = outerHt + gap
                val endX = (startX - nestedVt).coerceAtLeast(0f)
                val endY = (startY + nestedHt).coerceAtMost(height)

                moveTo(startX, height)
                lineTo(startX, startY + outerInR)
                arcTo(Rect(startX - outerInR * 2f, startY, startX, startY + outerInR * 2f), 0f, -90f, false)
                lineTo(0f, startY)
                lineTo(0f, endY)
                lineTo(endX - i, endY)
                arcTo(Rect(endX - i * 2f, endY, endX, endY + i * 2f), 270f, 90f, false)
                lineTo(endX, height)
            }
            LcarsElbowDirection.BottomLeft -> {
                val startX = outerVt + gap
                val startY = height - outerHt - gap
                val endX = (startX + nestedVt).coerceAtMost(width)
                val endY = (startY - nestedHt).coerceAtLeast(0f)

                moveTo(startX, 0f)
                lineTo(startX, startY - outerInR)
                arcTo(Rect(startX, startY - outerInR * 2f, startX + outerInR * 2f, startY), 180f, -90f, false)
                lineTo(width, startY)
                lineTo(width, endY)
                lineTo(endX + i, endY)
                arcTo(Rect(endX, endY - i * 2f, endX + i * 2f, endY), 90f, 90f, false)
                lineTo(endX, 0f)
            }
            LcarsElbowDirection.BottomRight -> {
                val startX = width - outerVt - gap
                val startY = height - outerHt - gap
                val endX = (startX - nestedVt).coerceAtLeast(0f)
                val endY = (startY - nestedHt).coerceAtLeast(0f)

                moveTo(startX, 0f)
                lineTo(startX, startY - outerInR)
                arcTo(Rect(startX - outerInR * 2f, startY - outerInR * 2f, startX, startY), 0f, 90f, false)
                lineTo(0f, startY)
                lineTo(0f, endY)
                lineTo(endX - i, endY)
                arcTo(Rect(endX - i * 2f, endY - i * 2f, endX, endY), 90f, -90f, false)
                lineTo(endX, 0f)
            }
        }
        close()
    }
}
