package com.lcars.ui.foundation

import com.lcars.ui.theme.*
import com.lcars.ui.controls.*
import com.lcars.ui.display.*
import com.lcars.ui.layout.*
import com.lcars.ui.scene.*
import com.lcars.ui.padd.*

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

import androidx.compose.ui.text.style.TextGeometricTransform

@Composable
fun LcarsText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalLcarsTypography.current.telemetry,
    color: Color = Color.Unspecified,
    textAlign: TextAlign? = null,
    maxLines: Int = 1,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    minFontSize: TextUnit = 9.sp,
    autoFit: Boolean = true,
    softWrap: Boolean = false,
) {
    val containsChinese = text.any { it.code in 0x4E00..0x9FFF }
    val baseStyle = if (containsChinese) {
        style.copy(textGeometricTransform = TextGeometricTransform(scaleX = 0.7f))
    } else {
        style
    }
    val mergedStyle = baseStyle.merge(
        TextStyle(
            color = color,
            textAlign = textAlign ?: TextAlign.Unspecified
        )
    )

    // Compute safe max and min font sizes for TextAutoSize
    val safeMaxFontSize = if (mergedStyle.fontSize != TextUnit.Unspecified) {
        mergedStyle.fontSize
    } else {
        16.sp
    }
    val safeMinFontSize = if (minFontSize != TextUnit.Unspecified && minFontSize.value < safeMaxFontSize.value) {
        minFontSize
    } else {
        (safeMaxFontSize.value * 0.6f).coerceAtLeast(8f).sp
    }

    BasicText(
        text = lcarsLabel(text),
        modifier = modifier,
        style = mergedStyle,
        maxLines = maxLines,
        overflow = overflow,
        softWrap = softWrap,
        autoSize = if (autoFit && safeMaxFontSize.value > safeMinFontSize.value) {
            TextAutoSize.StepBased(
                minFontSize = safeMinFontSize,
                maxFontSize = safeMaxFontSize,
                stepSize = 0.5.sp,
            )
        } else {
            null
        },
    )
}
