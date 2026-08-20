package com.lcars.ui.layout

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lcars.ui.foundation.LcarsText
import com.lcars.ui.theme.LcarsTheme
import com.lcars.ui.theme.LocalLcarsColors
import com.lcars.ui.theme.LocalLcarsSoundService
import com.lcars.ui.theme.LocalLcarsTypography
import com.lcars.ui.theme.playClick

/**
 * LCARS Spine label vertical alignment modes.
 *
 * Implements strict LCARS geometric anchor constraints:
 * - [Bottom] (Default): Anchors to bottom-end with 6.dp bottom margin and 12.dp end margin (standard navigation buttons).
 * - [Top]: Anchors to top-end with 6.dp top margin and 12.dp end margin (section headers / viewport markers).
 * - [Center]: Vertically centered with 12.dp end margin (compact status blocks / numeric telemetry).
 */
enum class LcarsSpineAlign {
    Top,
    Center,
    Bottom,
}

/**
 * Authentic LCARS Spine Interactive Button.
 *
 * Enforces hard structural constraints:
 * 1. Geometry: Strictly rectangular (0.dp corners, zero roundings allowed).
 * 2. Typography: Locked to [LcarsTypography.labelSmall] (16.sp), preventing height scaling inflation.
 * 3. Alignment: Rigidly honors [verticalAlign] (Top, Center, Bottom) with fixed 12.dp end-margin.
 * 4. Active state: Smooth luminous illumination.
 */
@Composable
fun LcarsSpineButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = LocalLcarsColors.current.commandPrimary,
    contentColor: Color = Color.Black,
    verticalAlign: LcarsSpineAlign = LcarsSpineAlign.Bottom,
    selected: Boolean = false,
    enabled: Boolean = true,
    showActiveIndicator: Boolean = true,
    height: Dp = 48.dp,
) {
    val soundService = LocalLcarsSoundService.current
    val interactionSource = remember { MutableInteractionSource() }

    val animatedColor by animateColorAsState(
        targetValue = if (selected) Color.White else color,
        animationSpec = tween(durationMillis = 140, easing = FastOutSlowInEasing),
        label = "SpineButtonColor",
    )

    val contentAlignment = when (verticalAlign) {
        LcarsSpineAlign.Top -> Alignment.TopEnd
        LcarsSpineAlign.Center -> Alignment.CenterEnd
        LcarsSpineAlign.Bottom -> Alignment.BottomEnd
    }

    val paddingValues = when (verticalAlign) {
        LcarsSpineAlign.Top -> PaddingValues(start = 10.dp, end = 12.dp, top = 6.dp, bottom = 0.dp)
        LcarsSpineAlign.Center -> PaddingValues(start = 10.dp, end = 12.dp, top = 0.dp, bottom = 0.dp)
        LcarsSpineAlign.Bottom -> PaddingValues(start = 10.dp, end = 12.dp, top = 0.dp, bottom = 6.dp)
    }

    val displayText = if (selected && showActiveIndicator && !text.startsWith("»")) {
        "» $text"
    } else {
        text
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(LcarsTheme.shapes.rectangle) // Hard constraint: 0.dp corners
            .background(animatedColor)
            .clickable(
                enabled = enabled,
                role = Role.Tab,
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    soundService.playClick()
                    onClick()
                },
            )
            .semantics {
                this.selected = selected
                this.contentDescription = text
            }
            .alpha(if (enabled) 1f else 0.38f)
            .padding(paddingValues),
        contentAlignment = contentAlignment,
    ) {
        LcarsText(
            text = displayText,
            style = LocalLcarsTypography.current.labelSmall.copy(
                color = contentColor,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            ),
            maxLines = 1,
            autoFit = true,
            minFontSize = 9.sp,
        )
    }
}

/**
 * Authentic LCARS Spine Static/Filler Block (non-clickable structural segment).
 */
@Composable
fun LcarsSpineBlock(
    modifier: Modifier = Modifier,
    color: Color = LocalLcarsColors.current.framePrimary,
    text: String? = null,
    contentColor: Color = Color.Black,
    verticalAlign: LcarsSpineAlign = LcarsSpineAlign.Bottom,
    height: Dp? = null,
) {
    val contentAlignment = when (verticalAlign) {
        LcarsSpineAlign.Top -> Alignment.TopEnd
        LcarsSpineAlign.Center -> Alignment.CenterEnd
        LcarsSpineAlign.Bottom -> Alignment.BottomEnd
    }

    val paddingValues = when (verticalAlign) {
        LcarsSpineAlign.Top -> PaddingValues(start = 10.dp, end = 12.dp, top = 6.dp, bottom = 0.dp)
        LcarsSpineAlign.Center -> PaddingValues(start = 10.dp, end = 12.dp, top = 0.dp, bottom = 0.dp)
        LcarsSpineAlign.Bottom -> PaddingValues(start = 10.dp, end = 12.dp, top = 0.dp, bottom = 6.dp)
    }

    val baseModifier = modifier
        .fillMaxWidth()
        .clip(LcarsTheme.shapes.rectangle)
        .background(color)

    val finalModifier = if (height != null) baseModifier.height(height) else baseModifier

    Box(
        modifier = if (!text.isNullOrBlank()) finalModifier.padding(paddingValues) else finalModifier,
        contentAlignment = contentAlignment,
    ) {
        if (!text.isNullOrBlank()) {
            LcarsText(
                text = text,
                style = LocalLcarsTypography.current.labelSmall.copy(color = contentColor),
                maxLines = 1,
                autoFit = true,
                minFontSize = 9.sp,
            )
        }
    }
}
