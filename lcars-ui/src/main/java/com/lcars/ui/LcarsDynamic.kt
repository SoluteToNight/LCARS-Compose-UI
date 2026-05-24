package com.lcars.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LcarsScannerSweep(
    running: Boolean,
    modifier: Modifier = Modifier,
    color: Color = LocalLcarsColors.current.violet,
    sweepColor: Color = LocalLcarsColors.current.lightBlue,
    cycleMillis: Int = 1400,
) {
    val colors = LocalLcarsColors.current
    val progress = if (running) {
        val transition = rememberInfiniteTransition(label = "LcarsScanner")
        val animatedProgress by transition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = cycleMillis, easing = LinearEasing),
                repeatMode = RepeatMode.Restart,
            ),
            label = "LcarsScannerProgress",
        )
        animatedProgress
    } else {
        0f
    }

    Box(modifier = modifier.background(colors.panel)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val gridLineColor = color.copy(alpha = 0.34f)
            val rowHeight = size.height / 6f
            repeat(7) { index ->
                val y = index * rowHeight
                drawLine(
                    color = gridLineColor,
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = 1.dp.toPx(),
                )
            }

            if (running) {
                val sweepWidth = 5.dp.toPx()
                val x = size.width * progress
                drawRect(
                    color = sweepColor.copy(alpha = 0.18f),
                    topLeft = Offset((x - sweepWidth * 5f).coerceAtLeast(0f), 0f),
                    size = Size(sweepWidth * 10f, size.height),
                )
                drawRect(
                    color = sweepColor,
                    topLeft = Offset(x.coerceIn(0f, size.width - sweepWidth), 0f),
                    size = Size(sweepWidth, size.height),
                )
            }
        }
    }
}
