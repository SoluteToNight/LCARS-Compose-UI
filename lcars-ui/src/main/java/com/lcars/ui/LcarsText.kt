package com.lcars.ui

import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
internal fun LcarsText(
    text: String,
    style: TextStyle,
    modifier: Modifier = Modifier,
    maxLines: Int = 1,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    minFontSize: TextUnit = 10.sp,
    autoFit: Boolean = true,
    softWrap: Boolean = false,
) {
    BasicText(
        text = lcarsLabel(text),
        modifier = modifier,
        style = style,
        maxLines = maxLines,
        overflow = overflow,
        softWrap = softWrap,
        autoSize = if (autoFit) {
            TextAutoSize.StepBased(
                minFontSize = minFontSize,
                maxFontSize = style.fontSize,
                stepSize = 0.5.sp,
            )
        } else {
            null
        },
    )
}
