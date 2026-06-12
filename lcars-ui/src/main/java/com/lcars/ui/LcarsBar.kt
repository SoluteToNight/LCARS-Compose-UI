package com.lcars.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp

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
                    }
                }
            }
        }
    }
}
