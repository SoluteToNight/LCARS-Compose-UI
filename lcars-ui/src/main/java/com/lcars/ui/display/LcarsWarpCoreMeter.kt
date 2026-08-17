package com.lcars.ui.display

import com.lcars.ui.theme.*
import com.lcars.ui.foundation.*
import com.lcars.ui.controls.*
import com.lcars.ui.layout.*
import com.lcars.ui.scene.*
import com.lcars.ui.padd.*

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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.sin

/**
 * Star Trek Main Engineering Warp Core Intermix Conduit & Reaction Chamber.
 *
 * Implements the iconic vertical matter/antimatter flow and 16-channel plasma resonance conduit
 * defined in LCARS 24.2 engineering schematics.
 *
 * @param warpFactor Current warp factor from 0.0f (offline) to 9.975f (maximum warp).
 * @param running Whether the reactor simulation is active.
 * @param modifier Composable layout modifier.
 * @param matterColor Color of the upper matter injection stream.
 * @param antimatterColor Color of the lower antimatter injection stream.
 * @param coreColor Color of the central Dilithium crystal intermix chamber.
 */
@Composable
fun LcarsWarpCoreMeter(
    warpFactor: Float,
    modifier: Modifier = Modifier,
    running: Boolean = true,
    matterColor: Color = LocalLcarsColors.current.lightBlue,
    antimatterColor: Color = LocalLcarsColors.current.violet,
    coreColor: Color = LocalLcarsColors.current.alertRed,
    accentColor: Color = LocalLcarsColors.current.monoAmber,
) {
    val colors = LocalLcarsColors.current
    val clampedWarp = warpFactor.coerceIn(0f, 9.975f)
    val isOnline = running && clampedWarp > 0f

    // Calculate pulse cycle duration based on warp factor (Warp 1 ~ 1200ms, Warp 9.9 ~ 200ms)
    val cycleMillis = if (clampedWarp > 0f) {
        (1200f / (1f + clampedWarp * 0.45f)).toInt().coerceIn(160, 1400)
    } else {
        1200
    }

    val transition = rememberInfiniteTransition(label = "LcarsWarpCore")
    val pulseProgress by if (isOnline && LcarsTheme.motionMode == LcarsMotionMode.System) {
        transition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = cycleMillis, easing = LinearEasing),
                repeatMode = RepeatMode.Restart,
            ),
            label = "WarpPulseProgress",
        )
    } else {
        transition.animateFloat(
            initialValue = 0.5f,
            targetValue = 0.5f,
            animationSpec = infiniteRepeatable(animation = tween(1000)),
            label = "WarpPulseProgressStatic",
        )
    }

    // Chamber core stepped flash
    val coreIntensity by if (isOnline && LcarsTheme.motionMode == LcarsMotionMode.System) {
        transition.animateFloat(
            initialValue = 0.3f,
            targetValue = 1.0f,
            animationSpec = infiniteRepeatable(
                animation = keyframes {
                    durationMillis = cycleMillis
                    0.3f at 0
                    0.3f at (cycleMillis / 2 - 1)
                    1.0f at (cycleMillis / 2)
                    1.0f at (cycleMillis - 1)
                },
                repeatMode = RepeatMode.Restart,
            ),
            label = "CoreIntensity",
        )
    } else {
        transition.animateFloat(
            initialValue = 0.6f,
            targetValue = 0.6f,
            animationSpec = infiniteRepeatable(animation = tween(1000)),
            label = "CoreIntensityStatic",
        )
    }

    Box(
        modifier = modifier
            .background(colors.panel)
            .padding(LocalLcarsSpacing.current.gapStandard),
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Left Status Column
            Column(
                modifier = Modifier
                    .width(72.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                LcarsText(
                    text = "M/AM INTERMIX",
                    style = LocalLcarsTypography.current.labelSmall.copy(color = colors.lightBlue, fontSize = 11.sp),
                    maxLines = 1,
                )
                LcarsText(
                    text = if (isOnline) "WARP ${"%.2f".format(clampedWarp)}" else "STANDBY",
                    style = LocalLcarsTypography.current.telemetry.copy(
                        color = if (isOnline) accentColor else colors.commandInactive,
                        fontSize = 15.sp,
                    ),
                    maxLines = 1,
                )
                LcarsText(
                    text = "OUTPUT 1.21 GW",
                    style = LocalLcarsTypography.current.labelSmall.copy(color = colors.a7, fontSize = 10.sp),
                    maxLines = 1,
                )
            }

            // Central Vertical Warp Core Column (Conduit Canvas)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center,
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height
                    val chamberHeight = (h * 0.16f).coerceIn(24.dp.toPx(), 60.dp.toPx())
                    val topStreamHeight = (h - chamberHeight) / 2f
                    val bottomStreamTop = topStreamHeight + chamberHeight

                    val segmentCount = 10
                    val segGap = 3.dp.toPx()
                    val segHeight = (topStreamHeight - (segmentCount - 1) * segGap) / segmentCount

                    // 1. Draw Upper Matter Stream (cascading downwards to chamber)
                    repeat(segmentCount) { i ->
                        val y = i * (segHeight + segGap)
                        val flowPhase = if (isOnline) ((pulseProgress + (segmentCount - 1 - i) / segmentCount.toFloat()) % 1f) else 0.5f
                        val active = flowPhase > 0.45f
                        val segColor = if (active) matterColor else matterColor.copy(alpha = 0.22f)

                        drawRect(
                            color = segColor,
                            topLeft = Offset(w * 0.15f, y),
                            size = Size(w * 0.70f, segHeight),
                        )
                    }

                    // 2. Draw Central Dilithium Reaction Chamber
                    val chamberTop = topStreamHeight
                    val chamberColor = if (isOnline) {
                        coreColor.copy(alpha = coreIntensity.coerceIn(0.35f, 1.0f))
                    } else {
                        colors.commandInactive
                    }
                    drawRoundRect(
                        color = chamberColor,
                        topLeft = Offset(w * 0.05f, chamberTop),
                        size = Size(w * 0.90f, chamberHeight),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(6.dp.toPx()),
                    )
                    // Reaction Chamber Core Pulse Ring
                    drawRoundRect(
                        color = colors.spaceWhite.copy(alpha = if (isOnline) coreIntensity else 0.2f),
                        topLeft = Offset(w * 0.25f, chamberTop + chamberHeight * 0.2f),
                        size = Size(w * 0.50f, chamberHeight * 0.6f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(4.dp.toPx()),
                        style = Stroke(width = 2.dp.toPx()),
                    )

                    // 3. Draw Lower Antimatter Stream (cascading upwards to chamber)
                    repeat(segmentCount) { i ->
                        val y = bottomStreamTop + i * (segHeight + segGap)
                        val flowPhase = if (isOnline) ((pulseProgress + i / segmentCount.toFloat()) % 1f) else 0.5f
                        val active = flowPhase > 0.45f
                        val segColor = if (active) antimatterColor else antimatterColor.copy(alpha = 0.22f)

                        drawRect(
                            color = segColor,
                            topLeft = Offset(w * 0.15f, y),
                            size = Size(w * 0.70f, segHeight),
                        )
                    }
                }
            }

            // Right Plasma Resonance Lines (16 Channels from LCARS 24.2)
            PlasmaResonanceBank(
                warpFactor = clampedWarp,
                pulseProgress = pulseProgress,
                running = isOnline,
                modifier = Modifier
                    .width(100.dp)
                    .fillMaxHeight(),
            )
        }
    }
}

@Composable
private fun PlasmaResonanceBank(
    warpFactor: Float,
    pulseProgress: Float,
    running: Boolean,
    modifier: Modifier = Modifier,
) {
    val colors = LocalLcarsColors.current

    Canvas(modifier = modifier) {
        val totalLines = 16
        val barGap = 2.dp.toPx()
        val barWidth = (size.width - (totalLines - 1) * barGap) / totalLines
        val maxH = size.height

        repeat(totalLines) { index ->
            val phaseOffset = (index.toFloat() / totalLines.toFloat()) * 3.14159f * 2f
            val wave = if (running) {
                ((sin(pulseProgress * 6.28318f + phaseOffset) + 1f) / 2f).coerceIn(0.12f, 1f)
            } else {
                0.3f
            }
            val barH = maxH * (0.20f + wave * 0.75f)
            val barTop = (maxH - barH) / 2f
            val x = index * (barWidth + barGap)

            val barColor = when {
                index in 7..8 -> colors.alertRed
                index in 4..11 -> colors.monoAmber
                else -> colors.lightBlue
            }

            drawRect(
                color = barColor,
                topLeft = Offset(x, barTop),
                size = Size(barWidth, barH),
            )
        }
    }
}
