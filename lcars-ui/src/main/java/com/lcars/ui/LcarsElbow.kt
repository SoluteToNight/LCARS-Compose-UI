package com.lcars.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.min

enum class LcarsElbowDirection {
    TopLeft,
    BottomLeft,
    TopRight,
    BottomRight,
}

@Composable
fun LcarsElbow(
    text: String,
    color: Color,
    direction: LcarsElbowDirection,
    modifier: Modifier = Modifier,
    thickness: Dp = LocalLcarsSpacing.current.elbowThickness,
    wingWidth: Dp = 132.dp,
    wingHeight: Dp = 72.dp,
    innerRadius: Dp = 4.dp,
) {
    val textAlignment = when (direction) {
        LcarsElbowDirection.TopLeft -> Alignment.TopEnd
        LcarsElbowDirection.TopRight -> Alignment.TopStart
        LcarsElbowDirection.BottomLeft -> Alignment.BottomEnd
        LcarsElbowDirection.BottomRight -> Alignment.BottomStart
    }

    Box(
        modifier = modifier
            .size(wingWidth, wingHeight)
            .drawWithCache {
                val t = thickness.toPx().coerceAtMost(min(size.width, size.height) * 0.72f)
                val innerR = innerRadius.toPx().coerceAtMost(t / 2f)
                val path = lcarsElbowPath(direction, size.width, size.height, t, innerR)
                onDrawBehind {
                    drawPath(
                        path = path,
                        color = color,
                    )
                }
            }
    ) {
        LcarsText(
            text = text,
            modifier = Modifier
                .align(textAlignment)
                .padding(horizontal = 8.dp, vertical = 5.dp),
            style = LocalLcarsTypography.current.labelSmall.copy(color = Color.Black),
            maxLines = 1,
        )
    }
}

private fun lcarsElbowPath(
    direction: LcarsElbowDirection,
    width: Float,
    height: Float,
    thickness: Float,
    innerRadius: Float,
): Path {
    val t = thickness
    val r = thickness
    val i = innerRadius
    return Path().apply {
        when (direction) {
            LcarsElbowDirection.TopLeft -> {
                moveTo(0f, height)
                lineTo(0f, r)
                arcTo(Rect(0f, 0f, r * 2f, r * 2f), 180f, 90f, false)
                lineTo(width, 0f)
                lineTo(width, t)
                lineTo(t + i, t)
                arcTo(Rect(t, t, t + i * 2f, t + i * 2f), 270f, -90f, false)
                lineTo(t, height)
            }
            LcarsElbowDirection.TopRight -> {
                moveTo(width, height)
                lineTo(width, r)
                arcTo(Rect(width - r * 2f, 0f, width, r * 2f), 0f, -90f, false)
                lineTo(0f, 0f)
                lineTo(0f, t)
                lineTo(width - t - i, t)
                arcTo(Rect(width - t - i * 2f, t, width - t, t + i * 2f), 270f, 90f, false)
                lineTo(width - t, height)
            }
            LcarsElbowDirection.BottomLeft -> {
                moveTo(0f, 0f)
                lineTo(0f, height - r)
                arcTo(Rect(0f, height - r * 2f, r * 2f, height), 180f, -90f, false)
                lineTo(width, height)
                lineTo(width, height - t)
                lineTo(t + i, height - t)
                arcTo(Rect(t, height - t - i * 2f, t + i * 2f, height - t), 90f, 90f, false)
                lineTo(t, 0f)
            }
            LcarsElbowDirection.BottomRight -> {
                moveTo(width, 0f)
                lineTo(width, height - r)
                arcTo(Rect(width - r * 2f, height - r * 2f, width, height), 0f, 90f, false)
                lineTo(0f, height)
                lineTo(0f, height - t)
                lineTo(width - t - i, height - t)
                arcTo(Rect(width - t - i * 2f, height - t - i * 2f, width - t, height - t), 90f, -90f, false)
                lineTo(width - t, 0f)
            }
        }
        close()
    }
}
