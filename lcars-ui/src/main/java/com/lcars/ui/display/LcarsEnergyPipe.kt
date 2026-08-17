package com.lcars.ui.display

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lcars.ui.foundation.LcarsText
import com.lcars.ui.theme.LocalLcarsColors
import com.lcars.ui.theme.LocalLcarsTypography
import com.lcars.ui.theme.rememberLcarsSmoothFlow

enum class LcarsFlowDirection {
    LeftToRight,
    RightToLeft,
}

/**
 * Stage 9 Plasma Conduit & EPS Energy Pipe.
 * Renders an authentic continuous plasma wave surge with luminescent core and silky-smooth cyclic gradient sweep.
 * Powered by GPU TileMode.Repeated hardware interpolation for 0% stutter and 100% fluid energy surging.
 */
@Composable
fun LcarsEnergyPipe(
    modifier: Modifier = Modifier,
    pipeHeight: Dp = 18.dp,
    baseColor: Color = LocalLcarsColors.current.monoAmber,
    glowColor: Color = Color.White,
    waveCount: Int = 3,
    direction: LcarsFlowDirection = LcarsFlowDirection.LeftToRight,
    durationMillis: Int = 1600,
) {
    val phase by rememberLcarsSmoothFlow(durationMillis = durationMillis)

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(pipeHeight)
    ) {
        val width = size.width
        val height = size.height
        if (width <= 0f || height <= 0f) return@Canvas
        val cornerRadius = CornerRadius(height / 2f, height / 2f)

        // Draw Dark Tube Conduit Background
        drawRoundRect(
            color = baseColor.copy(alpha = 0.15f),
            topLeft = Offset(0f, 0f),
            size = Size(width, height),
            cornerRadius = cornerRadius
        )

        // Draw Conduit Outer Border
        drawRoundRect(
            color = baseColor.copy(alpha = 0.45f),
            topLeft = Offset(0f, 0f),
            size = Size(width, height),
            cornerRadius = cornerRadius,
            style = Stroke(width = 1.dp.toPx())
        )

        // Seamless Hardware-Interpolated Wave Shader
        val waveLength = (width / waveCount.coerceAtLeast(1).toFloat()).coerceAtLeast(1f)
        val startOffset = if (direction == LcarsFlowDirection.LeftToRight) {
            phase * waveLength
        } else {
            (1f - phase) * waveLength
        }

        val brush = Brush.horizontalGradient(
            colors = listOf(
                baseColor.copy(alpha = 0.20f),
                baseColor.copy(alpha = 0.65f),
                glowColor.copy(alpha = 0.90f),
                Color.White,
                glowColor.copy(alpha = 0.90f),
                baseColor.copy(alpha = 0.65f),
                baseColor.copy(alpha = 0.20f),
            ),
            startX = startOffset - waveLength,
            endX = startOffset,
            tileMode = TileMode.Repeated
        )

        drawRoundRect(
            brush = brush,
            topLeft = Offset(1.dp.toPx(), 1.dp.toPx()),
            size = Size(width - 2.dp.toPx(), height - 2.dp.toPx()),
            cornerRadius = CornerRadius((height - 2.dp.toPx()) / 2f, (height - 2.dp.toPx()) / 2f)
        )
    }
}

/**
 * Stage 9 Matter / Anti-Matter Reactant Injector.
 * Complete authentic replica featuring:
 * 1. Left Expansion Nozzle Funnel with internal glow.
 * 2. Upper/Lower Bypass Circulation Conduits.
 * 3. Central Triple Reaction Chambers with sweeping metallic/plasma shimmer waves.
 * 4. Right Output Pill Terminals with live telemetry codes.
 */
@Composable
fun LcarsReactantInjector(
    title: String = "MATTER REACTANT INJECTOR",
    modifier: Modifier = Modifier,
    primaryColor: Color = LocalLcarsColors.current.monoAmber,
    conduitColor: Color = LocalLcarsColors.current.lightBlue,
    direction: LcarsFlowDirection = LcarsFlowDirection.LeftToRight,
    injectorCodes: List<String> = listOf("678647", "888787", "787858"),
    durationMillis: Int = 1800,
) {
    val colors = LocalLcarsColors.current
    val typography = LocalLcarsTypography.current
    val phase by rememberLcarsSmoothFlow(durationMillis = durationMillis)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(130.dp)
            .background(Color(0xFF04060C), RoundedCornerShape(6.dp))
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LcarsText(
                    text = title.uppercase(),
                    style = typography.labelMedium.copy(color = primaryColor)
                )
                LcarsText(
                    text = "STATUS: PLASMA INJECTION ONLINE",
                    style = typography.labelSmall.copy(color = colors.tacticalGreen)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Main Visual Assembly
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 1. Left Nozzle Funnel & Bypass Pipes (Canvas)
                Canvas(
                    modifier = Modifier
                        .width(110.dp)
                        .fillMaxHeight()
                ) {
                    val w = size.width
                    val h = size.height

                    // Draw Upper & Lower Bypass Loops
                    val pipeStroke = 8.dp.toPx()
                    val loopPath = Path().apply {
                        // Upper bypass loop
                        moveTo(w * 0.4f, h * 0.35f)
                        lineTo(w * 0.4f, h * 0.12f)
                        lineTo(w, h * 0.12f)

                        // Lower bypass loop
                        moveTo(w * 0.4f, h * 0.65f)
                        lineTo(w * 0.4f, h * 0.88f)
                        lineTo(w, h * 0.88f)
                    }

                    drawPath(
                        path = loopPath,
                        color = conduitColor.copy(alpha = 0.35f),
                        style = Stroke(width = pipeStroke)
                    )

                    // Draw Flowing Packets on Bypass
                    val flowFrac = if (direction == LcarsFlowDirection.LeftToRight) phase else (1f - phase)
                    val flowX = (flowFrac * w * 0.6f) + w * 0.4f
                    drawCircle(
                        color = Color.White,
                        radius = 4.dp.toPx(),
                        center = Offset(flowX.coerceIn(w * 0.4f, w), h * 0.12f)
                    )
                    drawCircle(
                        color = Color.White,
                        radius = 4.dp.toPx(),
                        center = Offset(flowX.coerceIn(w * 0.4f, w), h * 0.88f)
                    )

                    // Draw Left Expansion Funnel / Nozzle
                    val nozzlePath = Path().apply {
                        moveTo(0f, h * 0.15f)
                        lineTo(w * 0.38f, h * 0.32f)
                        lineTo(w * 0.38f, h * 0.68f)
                        lineTo(0f, h * 0.85f)
                        close()
                    }

                    // Shimmer gradient across nozzle
                    val nozzleBrush = Brush.horizontalGradient(
                        colors = listOf(
                            conduitColor.copy(alpha = 0.3f),
                            conduitColor,
                            Color.White.copy(alpha = 0.8f),
                            conduitColor
                        ),
                        startX = 0f,
                        endX = w * 0.38f
                    )

                    drawPath(
                        path = nozzlePath,
                        brush = nozzleBrush,
                        style = Fill
                    )
                    drawPath(
                        path = nozzlePath,
                        color = conduitColor,
                        style = Stroke(width = 1.5.dp.toPx())
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

                // 2. Central Triple Reaction Chambers (Canvas with sweeping plasma sheen)
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceAround
                ) {
                    for (i in 0 until 3) {
                        Canvas(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(22.dp)
                        ) {
                            val w = size.width
                            val h = size.height
                            if (w <= 0f || h <= 0f) return@Canvas
                            val r = CornerRadius(h / 3f, h / 3f)

                            // Base Tube
                            drawRoundRect(
                                color = primaryColor.copy(alpha = 0.18f),
                                size = Size(w, h),
                                cornerRadius = r
                            )

                            // Plasma Surge Gradient using TileMode.Repeated
                            val waveLength = w * 0.5f
                            val startOffset = if (direction == LcarsFlowDirection.LeftToRight) {
                                phase * waveLength
                            } else {
                                (1f - phase) * waveLength
                            }

                            val plasmaBrush = Brush.horizontalGradient(
                                colors = listOf(
                                    primaryColor.copy(alpha = 0.25f),
                                    primaryColor.copy(alpha = 0.85f),
                                    Color.White,
                                    primaryColor.copy(alpha = 0.85f),
                                    primaryColor.copy(alpha = 0.25f)
                                ),
                                startX = startOffset - waveLength,
                                endX = startOffset,
                                tileMode = TileMode.Repeated
                            )

                            drawRoundRect(
                                brush = plasmaBrush,
                                size = Size(w, h),
                                cornerRadius = r
                            )

                            // Metallic chamber horizontal segment lines
                            for (seg in 1..4) {
                                val sx = w * (seg / 5f)
                                drawLine(
                                    color = Color.Black.copy(alpha = 0.7f),
                                    start = Offset(sx, 0f),
                                    end = Offset(sx, h),
                                    strokeWidth = 2.dp.toPx()
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.width(6.dp))

                // 3. Right Output Pill Terminals with Telemetry Codes
                Column(
                    modifier = Modifier
                        .width(76.dp)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceAround
                ) {
                    injectorCodes.take(3).forEachIndexed { idx, code ->
                        val capColor = if (idx == 0) primaryColor else if (idx == 1) colors.auxiliaryTan else colors.lightBlue
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(22.dp)
                                .background(
                                    color = capColor,
                                    shape = RoundedCornerShape(topEnd = 11.dp, bottomEnd = 11.dp)
                                )
                                .padding(horizontal = 6.dp),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            LcarsText(
                                text = code,
                                style = typography.labelSmall.copy(color = Color.Black)
                            )
                        }
                    }
                }
            }
        }
    }
}
