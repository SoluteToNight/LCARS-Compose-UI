package com.lcars.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
            fontSize = (height * 0.86f).toSp(),
            lineHeight = (height * 0.92f).toSp(),
            color = labelColor,
        )
    }
    val shape = RoundedCornerShape(
        topStartPercent = if (startCap) 50 else 0,
        topEndPercent = if (endCap) 50 else 0,
        bottomEndPercent = if (endCap) 50 else 0,
        bottomStartPercent = if (startCap) 50 else 0,
    )
    val alignment = when (labelAlign) {
        LcarsLabelAlign.Start -> Alignment.CenterStart
        LcarsLabelAlign.Center -> Alignment.Center
        LcarsLabelAlign.End -> Alignment.CenterEnd
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(shape)
            .background(color),
        contentAlignment = alignment,
    ) {
        if (!label.isNullOrBlank()) {
            LcarsText(
                text = label,
                modifier = Modifier
                    .background(colors.background)
                    .padding(horizontal = gap),
                style = labelStyle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                minFontSize = (height * 0.58f).let { with(density) { it.toSp() } },
            )
        }
    }
}
