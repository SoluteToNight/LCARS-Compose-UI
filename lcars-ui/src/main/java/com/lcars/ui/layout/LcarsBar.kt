package com.lcars.ui.layout

import com.lcars.ui.theme.*
import com.lcars.ui.foundation.*
import com.lcars.ui.controls.*
import com.lcars.ui.display.*
import com.lcars.ui.scene.*
import com.lcars.ui.padd.*

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class LcarsLabelAlign {
    Start,
    Center,
    End,
}

@Composable
fun LcarsBar(
    modifier: Modifier = Modifier,
    color: Color = LocalLcarsColors.current.lightBlue,
    height: Dp = LocalLcarsSpacing.current.barHeight,
    startCap: Boolean = false,
    endCap: Boolean = false,
    label: String? = null,
    labelAlign: LcarsLabelAlign = LcarsLabelAlign.End,
    labelColor: Color = LocalLcarsColors.current.monoAmber,
) {
    val colors = LocalLcarsColors.current
    val gap = LocalLcarsSpacing.current.gapStandard
    val density = LocalDensity.current
    val labelStyle = with(density) {
        LocalLcarsTypography.current.header.copy(
            fontSize = (height * 0.80f).toSp(),
            lineHeight = (height * 0.86f).toSp(),
            color = labelColor,
        )
    }
    val labelOpticalOffset = -(height * 0.055f)
    val shape = RoundedCornerShape(
        topStartPercent = if (startCap) 50 else 0,
        topEndPercent = if (endCap) 50 else 0,
        bottomEndPercent = if (endCap) 50 else 0,
        bottomStartPercent = if (startCap) 50 else 0,
    )
    fun segmentShape(start: Boolean, end: Boolean) = RoundedCornerShape(
        topStartPercent = if (start) 50 else 0,
        topEndPercent = if (end) 50 else 0,
        bottomEndPercent = if (end) 50 else 0,
        bottomStartPercent = if (start) 50 else 0,
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .background(colors.background),
    ) {
        if (label.isNullOrBlank()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(shape)
                    .background(color),
            )
        } else {
            @Composable
            fun LabelCutout(modifier: Modifier = Modifier) {
                Box(
                    modifier = modifier
                        .background(colors.background)
                        .padding(horizontal = gap),
                    contentAlignment = Alignment.Center,
                ) {
                    LcarsText(
                        text = label,
                        modifier = Modifier.offset(y = labelOpticalOffset),
                        style = labelStyle,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        minFontSize = (height * 0.58f).let { with(density) { it.toSp() } },
                    )
                }
            }

            @Composable
            fun ColorSegment(
                start: Boolean,
                end: Boolean,
                modifier: Modifier = Modifier,
            ) {
                Box(
                    modifier = modifier
                        .fillMaxHeight()
                        .clip(segmentShape(start = start, end = end))
                        .background(color),
                )
            }

            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                when (labelAlign) {
                    LcarsLabelAlign.Start -> {
                        if (startCap) {
                            ColorSegment(
                                start = true,
                                end = false,
                                modifier = Modifier.width(height),
                            )
                        }
                        LabelCutout()
                        ColorSegment(
                            start = false,
                            end = endCap,
                            modifier = Modifier.weight(1f),
                        )
                    }
                    LcarsLabelAlign.Center -> {
                        ColorSegment(
                            start = startCap,
                            end = false,
                            modifier = Modifier.weight(1f),
                        )
                        LabelCutout()
                        ColorSegment(
                            start = false,
                            end = endCap,
                            modifier = Modifier.weight(1f),
                        )
                    }
                    LcarsLabelAlign.End -> {
                        ColorSegment(
                            start = startCap,
                            end = false,
                            modifier = Modifier.weight(1f),
                        )
                        LabelCutout()
                        if (endCap) {
                            ColorSegment(
                                start = false,
                                end = true,
                                modifier = Modifier.width(height),
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * LCARS Cutout Horizontal Rail (Slot API).
 *
 * Renders a segmented horizontal beam with a clean cutout gap for arbitrary Compose content,
 * featuring asymmetric or symmetric cap shapes (such as a rounded right end-cap).
 *
 * Architecture:
 * - Start Segment: Solid color block, optionally with [startCap] (start pill cap).
 * - Content Slot: Centered or custom-aligned cutout on pure black background.
 * - End Segment: Solid color block, optionally with [endCap] (end pill cap).
 */
@Composable
fun LcarsCutoutBar(
    modifier: Modifier = Modifier,
    color: Color = LocalLcarsColors.current.framePrimary,
    height: Dp = LocalLcarsSpacing.current.barHeight,
    startWeight: Float = 1f,
    endWeight: Float? = null,
    endWidth: Dp? = null,
    startCap: Boolean = false,
    endCap: Boolean = true,
    gap: Dp = LocalLcarsSpacing.current.gapStandard,
    content: @Composable () -> Unit,
) {
    val colors = LocalLcarsColors.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .background(colors.background),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(gap),
    ) {
        // 1. Start Segment (e.g. continuing from elbow)
        Box(
            modifier = Modifier
                .weight(startWeight.coerceAtLeast(0.01f))
                .fillMaxHeight()
                .background(
                    color = color,
                    shape = if (startCap) LcarsTheme.shapes.startCap else LcarsTheme.shapes.rectangle,
                ),
        )

        // 2. Center Cutout Content Slot (Caller controls arbitrary Compose UI)
        Box(
            modifier = Modifier.padding(horizontal = (gap / 2f).coerceAtLeast(2.dp)),
            contentAlignment = Alignment.Center,
        ) {
            content()
        }

        // 3. End Segment (e.g. endCap pill cap)
        val endModifier = when {
            endWidth != null -> Modifier.width(endWidth)
            endWeight != null -> Modifier.weight(endWeight.coerceAtLeast(0.01f))
            endCap -> Modifier.width(height * 1.5f)
            else -> Modifier.weight(1f)
        }

        Box(
            modifier = endModifier
                .fillMaxHeight()
                .background(
                    color = color,
                    shape = if (endCap) LcarsTheme.shapes.endCap else LcarsTheme.shapes.rectangle,
                ),
        )
    }
}

