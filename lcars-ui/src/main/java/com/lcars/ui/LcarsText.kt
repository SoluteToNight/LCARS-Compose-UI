package com.lcars.ui

import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun LcarsText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalLcarsTypography.current.telemetry,
    color: Color = Color.Unspecified,
    textAlign: TextAlign? = null,
    maxLines: Int = 1,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    minFontSize: TextUnit = 10.sp,
    autoFit: Boolean = true,
    softWrap: Boolean = false,
) {
    val mergedStyle = style.merge(
        TextStyle(
            color = color,
            textAlign = textAlign ?: TextAlign.Unspecified
        )
    )
    BasicText(
        text = lcarsLabel(text),
        modifier = modifier,
        style = mergedStyle,
        maxLines = maxLines,
        overflow = overflow,
        softWrap = softWrap,
        autoSize = if (autoFit) {
            TextAutoSize.StepBased(
                minFontSize = minFontSize,
                maxFontSize = mergedStyle.fontSize,
                stepSize = 0.5.sp,
            )
        } else {
            null
        },
    )
}
