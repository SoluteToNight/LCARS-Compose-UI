package com.lcars.ui.controls

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lcars.ui.theme.LcarsMotionTokens
import com.lcars.ui.theme.LocalLcarsColors
import kotlin.math.atan2

enum class LcarsDiscDirection {
    North,
    East,
    South,
    West,
    Center,
}

/**
 * Stage 9 Directional Disc / Attitude Navigator.
 * Circular 4-quadrant tactile interface with cross-arms used on Helm/Conn and Ops consoles.
 */
@Composable
fun LcarsDirectionalDisc(
    modifier: Modifier = Modifier,
    size: Dp = 120.dp,
    baseColor: Color = LocalLcarsColors.current.auxiliaryTan,
    crossColor: Color = LocalLcarsColors.current.monoAmber,
    activeColor: Color = LocalLcarsColors.current.spaceWhite,
    onDirectionSelected: (LcarsDiscDirection) -> Unit = {},
) {
    val colors = LocalLcarsColors.current
    var activeDirection by remember { mutableStateOf<LcarsDiscDirection?>(null) }

    val discColor by animateColorAsState(
        targetValue = if (activeDirection != null) activeColor else baseColor,
        animationSpec = LcarsMotionTokens.ColorTransitionSpec,
        label = "DiscColor"
    )

    Box(
        modifier = modifier
            .size(size)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = { offset ->
                        val center = Offset(size.toPx() / 2f, size.toPx() / 2f)
                        val dx = offset.x - center.x
                        val dy = offset.y - center.y
                        val distSq = dx * dx + dy * dy
                        val radius = size.toPx() / 2f

                        if (distSq <= radius * radius) {
                            val angle = (Math.toDegrees(atan2(dy.toDouble(), dx.toDouble())).toFloat() + 360f) % 360f
                            val dir = when (angle) {
                                in 45f..135f -> LcarsDiscDirection.South
                                in 135f..225f -> LcarsDiscDirection.West
                                in 225f..315f -> LcarsDiscDirection.North
                                else -> LcarsDiscDirection.East
                            }
                            activeDirection = dir
                            onDirectionSelected(dir)
                            tryAwaitRelease()
                            activeDirection = null
                        }
                    }
                )
            }
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val canvasSize = this.size.minDimension
            val radius = canvasSize / 2f
            val center = Offset(this.size.width / 2f, this.size.height / 2f)
            val gap = 4.dp.toPx()
            val armThickness = 12.dp.toPx()

            // Outer 4 Quarters
            val quadrantAngles = listOf(
                Pair(45f + 4f, 90f - 8f),   // South
                Pair(135f + 4f, 90f - 8f),  // West
                Pair(225f + 4f, 90f - 8f),  // North
                Pair(315f + 4f, 90f - 8f),  // East
            )

            quadrantAngles.forEach { (startAngle, sweepAngle) ->
                drawArc(
                    color = discColor,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = true,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(radius * 2, radius * 2),
                    style = Fill
                )
            }

            // Black divider cross
            drawLine(
                color = Color.Black,
                start = Offset(center.x - radius, center.y),
                end = Offset(center.x + radius, center.y),
                strokeWidth = gap
            )
            drawLine(
                color = Color.Black,
                start = Offset(center.x, center.y - radius),
                end = Offset(center.x, center.y + radius),
                strokeWidth = gap
            )

            // Inner Central Cross Arms
            drawRect(
                color = crossColor,
                topLeft = Offset(center.x - armThickness / 2f, center.y - radius * 0.7f),
                size = Size(armThickness, radius * 1.4f)
            )
            drawRect(
                color = crossColor,
                topLeft = Offset(center.x - radius * 0.7f, center.y - armThickness / 2f),
                size = Size(radius * 1.4f, armThickness)
            )

            // Black Center Box
            val centerHole = armThickness * 0.9f
            drawRect(
                color = Color.Black,
                topLeft = Offset(center.x - centerHole / 2f, center.y - centerHole / 2f),
                size = Size(centerHole, centerHole)
            )
        }
    }
}
