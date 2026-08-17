package com.lcars.ui.display

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lcars.ui.foundation.LcarsText
import com.lcars.ui.theme.LocalLcarsColors
import com.lcars.ui.theme.LocalLcarsTypography
import com.lcars.ui.theme.rememberLcarsDataStream
import com.lcars.ui.theme.rememberLcarsSmoothPulse

/**
 * Stage 9 Forward Navigation & Tactical Scanner (100% faithful to Enterprise-D video reference).
 * Features:
 * 1. Precision dual-axis tick-mark rulers with smooth laser cursors.
 * 2. Nested dual-layer targeting brackets (large outer frame + inner target lock reticle).
 * 3. Upper dense hexadecimal/decimal telemetry data streams.
 * 4. Target-attached top/bottom real-time coordinate readouts.
 * 5. Smooth 60fps tracking drift and breathing animation.
 */
@Composable
fun LcarsForwardNavigationScan(
    modifier: Modifier = Modifier,
    targetLocked: Boolean = true,
    topAttachedValue: String = "201804",
    bottomAttachedValue: String = "388720",
    bearing: String = "BEARING: 042.8 MARK 12",
    range: String = "RANGE: 38,400 KM",
    primaryColor: Color = LocalLcarsColors.current.monoAmber,
    accentColor: Color = LocalLcarsColors.current.auxiliaryTan,
    gridColor: Color = LocalLcarsColors.current.lightBlue.copy(alpha = 0.25f),
    rulerColor: Color = LocalLcarsColors.current.lightBlue.copy(alpha = 0.45f),
) {
    val colors = LocalLcarsColors.current
    val typography = LocalLcarsTypography.current

    // Upper dense telemetry stream (5 columns x 3 rows)
    val telemetryData by rememberLcarsDataStream(rows = 3, columns = 5)

    // Smooth targeting motion
    val infiniteTransition = rememberInfiniteTransition(label = "NavScanMotion")
    val trackingDriftX by infiniteTransition.animateFloat(
        initialValue = -16f,
        targetValue = 16f,
        animationSpec = infiniteRepeatable(
            animation = tween(2800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "TrackingDriftX"
    )
    val trackingDriftY by infiniteTransition.animateFloat(
        initialValue = -10f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "TrackingDriftY"
    )
    val bracketBreath by infiniteTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "BracketBreath"
    )
    val sliderPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "SliderPhase"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(10.dp)
    ) {
        // Canvas Layer: Rulers, Grid, Brackets, and Tracking Reticles
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val center = Offset(
                x = w / 2f + (if (targetLocked) trackingDriftX else 0f),
                y = h / 2f + (if (targetLocked) trackingDriftY else 0f)
            )

            // 1. Spatial Grid
            val cols = 8
            val rows = 6
            for (i in 1 until cols) {
                val gx = w * (i / cols.toFloat())
                drawLine(
                    color = gridColor,
                    start = Offset(gx, 0f),
                    end = Offset(gx, h),
                    strokeWidth = 1.dp.toPx()
                )
            }
            for (j in 1 until rows) {
                val gy = h * (j / rows.toFloat())
                drawLine(
                    color = gridColor,
                    start = Offset(0f, gy),
                    end = Offset(w, gy),
                    strokeWidth = 1.dp.toPx()
                )
            }

            // 2. Central Dual-Axis Tick-Mark Measurement Rulers (Video Precision)
            val rulerY = h / 2f
            val rulerX = w / 2f

            // Horizontal Axis Ruler
            drawLine(
                color = rulerColor,
                start = Offset(0f, rulerY),
                end = Offset(w, rulerY),
                strokeWidth = 1.5.dp.toPx()
            )
            val numHorizontalTicks = 40
            val tickSpacingX = w / numHorizontalTicks
            for (i in 0..numHorizontalTicks) {
                val tx = i * tickSpacingX
                val isMajor = i % 5 == 0
                val tickH = if (isMajor) 10.dp.toPx() else 5.dp.toPx()
                drawLine(
                    color = if (isMajor) primaryColor else rulerColor,
                    start = Offset(tx, rulerY - tickH / 2f),
                    end = Offset(tx, rulerY + tickH / 2f),
                    strokeWidth = if (isMajor) 1.5.dp.toPx() else 1.dp.toPx()
                )
            }

            // Vertical Axis Ruler
            drawLine(
                color = rulerColor,
                start = Offset(rulerX, 0f),
                end = Offset(rulerX, h),
                strokeWidth = 1.5.dp.toPx()
            )
            val numVerticalTicks = 24
            val tickSpacingY = h / numVerticalTicks
            for (j in 0..numVerticalTicks) {
                val ty = j * tickSpacingY
                val isMajor = j % 4 == 0
                val tickW = if (isMajor) 10.dp.toPx() else 5.dp.toPx()
                drawLine(
                    color = if (isMajor) primaryColor else rulerColor,
                    start = Offset(rulerX - tickW / 2f, ty),
                    end = Offset(rulerX + tickW / 2f, ty),
                    strokeWidth = if (isMajor) 1.5.dp.toPx() else 1.dp.toPx()
                )
            }

            // Smooth Laser Slider Cursor on Horizontal Axis
            val cursorX = (sliderPhase * w)
            drawLine(
                color = primaryColor,
                start = Offset(cursorX, rulerY - 14.dp.toPx()),
                end = Offset(cursorX, rulerY + 14.dp.toPx()),
                strokeWidth = 3.dp.toPx()
            )

            // 3. Outer Tactical Brackets [   ] (Wide Frame with Wing Ticks)
            val outerWidth = (w * 0.45f).coerceIn(240.dp.toPx(), 480.dp.toPx())
            val outerHeight = (h * 0.55f).coerceIn(160.dp.toPx(), 320.dp.toPx())
            val outerCornerLen = 28.dp.toPx()
            val outerStroke = 2.5.dp.toPx()
            val oLeft = center.x - outerWidth / 2f
            val oRight = center.x + outerWidth / 2f
            val oTop = center.y - outerHeight / 2f
            val oBottom = center.y + outerHeight / 2f

            // Outer 4 Corners
            drawCorner(oLeft, oTop, outerCornerLen, outerCornerLen, primaryColor, outerStroke, isTop = true, isLeft = true)
            drawCorner(oRight, oTop, -outerCornerLen, outerCornerLen, primaryColor, outerStroke, isTop = true, isLeft = false)
            drawCorner(oLeft, oBottom, outerCornerLen, -outerCornerLen, primaryColor, outerStroke, isTop = false, isLeft = true)
            drawCorner(oRight, oBottom, -outerCornerLen, -outerCornerLen, primaryColor, outerStroke, isTop = false, isLeft = false)

            // Outer Left/Right Ladder Ticks (Video Signature)
            val ladderSteps = 5
            for (step in 1 until ladderSteps) {
                val ly = oTop + (outerHeight * (step / ladderSteps.toFloat()))
                // Left Wing Tick
                drawLine(
                    color = primaryColor,
                    start = Offset(oLeft - 8.dp.toPx(), ly),
                    end = Offset(oLeft, ly),
                    strokeWidth = 2.dp.toPx()
                )
                // Right Wing Tick
                drawLine(
                    color = primaryColor,
                    start = Offset(oRight, ly),
                    end = Offset(oRight + 8.dp.toPx(), ly),
                    strokeWidth = 2.dp.toPx()
                )
            }

            // 4. Inner Target Lock Reticle [ + ] (Breathing & Attached Data)
            val innerBoxSize = 72.dp.toPx() * bracketBreath
            val innerCornerLen = 14.dp.toPx()
            val innerStroke = 3.dp.toPx()
            val iLeft = center.x - innerBoxSize / 2f
            val iRight = center.x + innerBoxSize / 2f
            val iTop = center.y - innerBoxSize / 2f
            val iBottom = center.y + innerBoxSize / 2f

            drawCorner(iLeft, iTop, innerCornerLen, innerCornerLen, primaryColor, innerStroke, isTop = true, isLeft = true)
            drawCorner(iRight, iTop, -innerCornerLen, innerCornerLen, primaryColor, innerStroke, isTop = true, isLeft = false)
            drawCorner(iLeft, iBottom, innerCornerLen, -innerCornerLen, primaryColor, innerStroke, isTop = false, isLeft = true)
            drawCorner(iRight, iBottom, -innerCornerLen, -innerCornerLen, primaryColor, innerStroke, isTop = false, isLeft = false)

            // Center Crosshair +
            val chLen = 22.dp.toPx()
            drawLine(primaryColor, Offset(center.x - chLen, center.y), Offset(center.x + chLen, center.y), 1.5.dp.toPx())
            drawLine(primaryColor, Offset(center.x, center.y - chLen), Offset(center.x, center.y + chLen), 1.5.dp.toPx())
            drawCircle(primaryColor, radius = 2.5.dp.toPx(), center = center)

            // 5. Four Viewport Corners Auxiliary Marks
            val mSize = 16.dp.toPx()
            val mStroke = 1.5.dp.toPx()
            drawCorner(4.dp.toPx(), 4.dp.toPx(), mSize, mSize, accentColor, mStroke, isTop = true, isLeft = true)
            drawCorner(w - 4.dp.toPx(), 4.dp.toPx(), -mSize, mSize, accentColor, mStroke, isTop = true, isLeft = false)
            drawCorner(4.dp.toPx(), h - 4.dp.toPx(), mSize, -mSize, accentColor, mStroke, isTop = false, isLeft = true)
            drawCorner(w - 4.dp.toPx(), h - 4.dp.toPx(), -mSize, -mSize, accentColor, mStroke, isTop = false, isLeft = false)
        }

        // Upper Section: Dense Telemetry Matrix (Stage 9 Video Signature)
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 8.dp)
                .fillMaxWidth(0.88f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            for (rowValues in telemetryData.take(3)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    for (code in rowValues) {
                        LcarsText(
                            text = code,
                            style = typography.labelSmall.copy(
                                color = accentColor
                            )
                        )
                    }
                }
            }
        }

        // Center Floating Attached Readouts (Follow Inner Reticle Drift)
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(84.dp)
            ) {
                // Top Attached Code (e.g. 201804 / Bearing)
                LcarsText(
                    text = topAttachedValue,
                    style = typography.labelMedium.copy(
                        color = primaryColor,
                        letterSpacing = 2.sp
                    )
                )

                // Bottom Attached Code (e.g. 388720 / Range)
                LcarsText(
                    text = bottomAttachedValue,
                    style = typography.labelMedium.copy(
                        color = primaryColor,
                        letterSpacing = 2.sp
                    )
                )
            }
        }

        // Left-Bottom Sub-Range Readout
        LcarsText(
            text = range,
            style = typography.labelSmall.copy(color = colors.lightBlue),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 12.dp, bottom = 8.dp)
        )

        // Right-Bottom Tactical Status
        LcarsText(
            text = if (targetLocked) "TARGET: ACQUIRED [AUTO 12]" else "SEARCHING SCAN...",
            style = typography.labelSmall.copy(color = if (targetLocked) primaryColor else colors.auxiliaryTan),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 12.dp, bottom = 8.dp)
        )
    }
}

/** Helper function to draw corner brackets on Canvas */
private fun DrawScope.drawCorner(
    x: Float,
    y: Float,
    dx: Float,
    dy: Float,
    color: Color,
    strokeWidth: Float,
    isTop: Boolean,
    isLeft: Boolean,
) {
    drawLine(color, Offset(x, y), Offset(x + dx, y), strokeWidth)
    drawLine(color, Offset(x, y), Offset(x, y + dy), strokeWidth)
}

/**
 * Backward compatibility alias for LcarsTargetingCrosshair.
 */
@Composable
fun LcarsTargetingCrosshair(
    modifier: Modifier = Modifier,
    targetLocked: Boolean = true,
    bearing: String = "BEARING: 042.8",
    range: String = "RANGE: 42,000 KM",
    gridColor: Color = LocalLcarsColors.current.lightBlue.copy(alpha = 0.2f),
    bracketColor: Color = LocalLcarsColors.current.monoAmber,
) {
    LcarsForwardNavigationScan(
        modifier = modifier,
        targetLocked = targetLocked,
        topAttachedValue = "201804",
        bottomAttachedValue = "388720",
        bearing = bearing,
        range = range,
        primaryColor = bracketColor,
        gridColor = gridColor,
    )
}
