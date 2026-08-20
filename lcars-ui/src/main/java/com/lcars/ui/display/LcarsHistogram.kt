package com.lcars.ui.display

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lcars.ui.foundation.LcarsText
import com.lcars.ui.theme.LocalLcarsColors
import com.lcars.ui.theme.LocalLcarsTypography
import com.lcars.ui.theme.keypadRhythm

@androidx.compose.runtime.Immutable
data class LcarsHistogramBar(
    val code: String,
    val valueFraction: Float, // 0f..1f
    val displayValue: String = "",
    val customColor: Color? = null,
)

/**
 * Stage 9 Multi-Color Horizontal Histogram & Sensor Meter.
 * Renders segmented horizontal telemetry bars with smooth interpolation and top ruler ticks.
 */
@Composable
fun LcarsHistogram(
    bars: List<LcarsHistogramBar>,
    modifier: Modifier = Modifier,
    title: String = "COMPOSITE SENSOR ANALYSIS 4077",
    barHeight: Dp = 20.dp,
    gap: Dp = 6.dp,
) {
    val colors = LocalLcarsColors.current
    val typography = LocalLcarsTypography.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Black)
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(gap)
    ) {
        // Title & Scale Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LcarsText(
                text = title.uppercase(),
                style = typography.titleSmall.copy(color = colors.monoAmber)
            )

            // Ruler numbers
            Row(
                modifier = Modifier.width(220.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf("00", "25", "50", "75", "100").forEach { tick ->
                    LcarsText(
                        text = tick,
                        style = typography.labelSmall.copy(color = colors.auxiliaryTan)
                    )
                }
            }
        }

        // Horizontal Ruler Line
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(colors.auxiliaryTan.copy(alpha = 0.5f))
        )

        // Histogram Bars
        bars.forEachIndexed { index, bar ->
            val animatedFraction by animateFloatAsState(
                targetValue = bar.valueFraction.coerceIn(0f, 1f),
                animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing),
                label = "BarFraction_${bar.code}"
            )
            val barColor = bar.customColor ?: colors.keypadRhythm(index)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(barHeight),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left Code Tag
                Box(
                    modifier = Modifier
                        .width(72.dp)
                        .fillMaxHeight()
                        .background(barColor, RoundedCornerShape(topStart = barHeight / 2, bottomStart = barHeight / 2))
                        .padding(horizontal = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    LcarsText(
                        text = bar.code,
                        style = typography.labelSmall.copy(
                            fontSize = 12.sp,
                            lineHeight = 13.sp,
                            color = Color.Black
                        ),
                        autoFit = true,
                        minFontSize = 8.sp
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                // Bar Fill Track
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(Color(0xFF0F1420), RoundedCornerShape(2.dp))
                ) {
                    // Progress Bar Fill
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(animatedFraction)
                            .fillMaxHeight()
                            .background(barColor, RoundedCornerShape(2.dp))
                    )

                    // Adaptive Value Text (Inside vs Outside Placement)
                    if (bar.displayValue.isNotEmpty()) {
                        val fitsInside = animatedFraction >= 0.28f
                        if (fitsInside) {
                            // Sufficient width: aligned inside the colored bar with high-contrast black text
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(animatedFraction)
                                    .fillMaxHeight()
                                    .padding(end = 6.dp),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                LcarsText(
                                    text = bar.displayValue,
                                    style = typography.labelSmall.copy(
                                        fontSize = 12.sp,
                                        lineHeight = 13.sp,
                                        color = Color.Black
                                    ),
                                    autoFit = true,
                                    minFontSize = 8.sp
                                )
                            }
                        } else {
                            // Short bar: automatically flips outside the bar to the right side with highlighted text
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(start = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Spacer(modifier = Modifier.fillMaxWidth(animatedFraction.coerceAtLeast(0.01f)))
                                Spacer(modifier = Modifier.width(4.dp))
                                LcarsText(
                                    text = bar.displayValue,
                                    style = typography.labelSmall.copy(
                                        fontSize = 12.sp,
                                        lineHeight = 13.sp,
                                        color = barColor
                                    ),
                                    autoFit = true,
                                    minFontSize = 8.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
