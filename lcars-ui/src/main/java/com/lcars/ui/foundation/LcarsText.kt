package com.lcars.ui.foundation

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextGeometricTransform
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isSpecified
import androidx.compose.ui.unit.sp
import com.lcars.ui.theme.LocalLcarsColors
import com.lcars.ui.theme.LocalLcarsTypography

@Composable
fun LcarsText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = TextStyle.Default,
    color: Color = Color.Unspecified,
    textAlign: TextAlign? = null,
    maxLines: Int = 1,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    minFontSize: TextUnit = 8.sp,
    heightRatio: Float = 0.80f,
    autoFit: Boolean = true,
    softWrap: Boolean = false,
) {
    val typography = LocalLcarsTypography.current
    val defaultFontFamily = typography.defaultFont
    val themeTextColor = LocalLcarsColors.current.text

    val containsChinese = remember(text) { text.any { it.code in 0x4E00..0x9FFF } }
    val baseStyle = remember(style, defaultFontFamily, containsChinese) {
        val styledWithFont = if (style.fontFamily == null) {
            style.copy(fontFamily = defaultFontFamily)
        } else {
            style
        }
        if (containsChinese) {
            styledWithFont.copy(textGeometricTransform = TextGeometricTransform(scaleX = 0.7f))
        } else {
            styledWithFont
        }
    }

    val resolvedColor = remember(color, style.color, themeTextColor) {
        when {
            color.isSpecified -> color
            style.color.isSpecified -> style.color
            else -> themeTextColor
        }
    }

    BoxWithConstraints(modifier = modifier) {
        val density = LocalDensity.current
        val isExplicitFontSize = baseStyle.fontSize.isSpecified && baseStyle.fontSize != TextUnit.Unspecified

        val safeMaxFontSize = if (isExplicitFontSize) {
            baseStyle.fontSize
        } else {
            if (maxHeight != Dp.Infinity && maxHeight > 0.dp) {
                with(density) {
                    val rawSp = (maxHeight * heightRatio).toSp().value
                    val minVal = if (minFontSize.isSpecified) minFontSize.value else 8f
                    rawSp.coerceIn(minVal, 64f).sp
                }
            } else {
                16.sp
            }
        }

        val safeMinFontSize = if (minFontSize.isSpecified && minFontSize != TextUnit.Unspecified && minFontSize.value < safeMaxFontSize.value) {
            minFontSize
        } else {
            (safeMaxFontSize.value * 0.6f).coerceAtLeast(8f).sp
        }

        val resolvedLineHeight = if (baseStyle.lineHeight.isSpecified && baseStyle.lineHeight != TextUnit.Unspecified) {
            baseStyle.lineHeight
        } else {
            (safeMaxFontSize.value * 1.1f).sp
        }

        val mergedStyle = baseStyle.copy(
            color = resolvedColor,
            textAlign = textAlign ?: baseStyle.textAlign ?: TextAlign.Unspecified,
            fontSize = safeMaxFontSize,
            lineHeight = resolvedLineHeight,
        )

        BasicText(
            text = lcarsLabel(text),
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
}
