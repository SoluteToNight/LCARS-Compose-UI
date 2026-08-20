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
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
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

private val QuadrantStartAngles = floatArrayOf(49f, 139f, 229f, 319f)
private const val QuadrantSweepAngle = 82f

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
    var activeDirection by remember { mutableStateOf<LcarsDiscDirection?>(null) }
    val currentOnDirectionSelected by rememberUpdatedState(onDirectionSelected)

    val discColor by animateColorAsState(
        targetValue = if (activeDirection != null) activeColor else baseColor,
        animationSpec = LcarsMotionTokens.ColorTransitionSpec,
        label = "DiscColor",
    )

    Box(
        modifier = modifier
            .size(size)
            .semantics(mergeDescendants = true) {
                role = Role.Button
                contentDescription = "Directional Navigator Disc"
            }
            .pointerInput(Unit) {
                val inputWidth = this.size.width.toFloat()
                val inputHeight = this.size.height.toFloat()
                val centerX = inputWidth / 2f
                val centerY = inputHeight / 2f
                val radius = minOf(centerX, centerY)

                detectTapGestures(
                    onPress = { offset ->
                        val dx = offset.x - centerX
                        val dy = offset.y - centerY
                        val distSq = dx * dx + dy * dy

                        if (distSq <= radius * radius) {
                            val angle = (Math.toDegrees(atan2(dy.toDouble(), dx.toDouble())).toFloat() + 360f) % 360f
                            val dir = if (angle >= 45f && angle < 135f) {
                                LcarsDiscDirection.South
                            } else if (angle >= 135f && angle < 225f) {
                                LcarsDiscDirection.West
                            } else if (angle >= 225f && angle < 315f) {
                                LcarsDiscDirection.North
                            } else {
                                LcarsDiscDirection.East
                            }
                            activeDirection = dir
                            currentOnDirectionSelected(dir)
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

            // Outer 4 Quarters without List/Pair allocations
            for (startAngle in QuadrantStartAngles) {
                drawArc(
                    color = discColor,
                    startAngle = startAngle,
                    sweepAngle = QuadrantSweepAngle,
                    useCenter = true,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(radius * 2f, radius * 2f),
                    style = Fill,
                )
            }

            // Black divider cross
            drawLine(
                color = Color.Black,
                start = Offset(center.x - radius, center.y),
                end = Offset(center.x + radius, center.y),
                strokeWidth = gap,
            )
            drawLine(
                color = Color.Black,
                start = Offset(center.x, center.y - radius),
                end = Offset(center.x, center.y + radius),
                strokeWidth = gap,
            )

            // Inner Central Cross Arms
            drawRect(
                color = crossColor,
                topLeft = Offset(center.x - armThickness / 2f, center.y - radius * 0.7f),
                size = Size(armThickness, radius * 1.4f),
            )
            drawRect(
                color = crossColor,
                topLeft = Offset(center.x - radius * 0.7f, center.y - armThickness / 2f),
                size = Size(radius * 1.4f, armThickness),
            )

            // Black Center Box
            val centerHole = armThickness * 0.9f
            drawRect(
                color = Color.Black,
                topLeft = Offset(center.x - centerHole / 2f, center.y - centerHole / 2f),
                size = Size(centerHole, centerHole),
            )
        }
    }
}
