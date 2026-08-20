package com.lcars.ui.controls

import com.lcars.ui.theme.*
import com.lcars.ui.foundation.*
import com.lcars.ui.display.*
import com.lcars.ui.layout.*
import com.lcars.ui.scene.*
import com.lcars.ui.padd.*

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
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
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class LcarsButtonShape {
    Pill,
    BlockStart,
    BlockEnd,
    Rectangle,
}

@Composable
fun LcarsButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = LocalLcarsColors.current.commandPrimary,
    contentColor: Color = Color.Black,
    shape: LcarsButtonShape = LcarsButtonShape.Pill,
    alignment: Alignment = Alignment.BottomEnd,
    alertLevel: LcarsAlertLevel? = null,
    enabled: Boolean = true,
    role: Role = Role.Button,
    selected: Boolean? = null,
    minWidth: Dp = LocalLcarsSpacing.current.buttonMinWidth,
    minHeight: Dp = LocalLcarsSpacing.current.buttonMinHeight,
) {
    val colors = LocalLcarsColors.current
    val soundService = LocalLcarsSoundService.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val alerting = alertLevel != null
    val targetAlertColor = alertLevel?.color(colors) ?: color
    val alertPeriodMillis = when (alertLevel) {
        LcarsAlertLevel.Critical -> 600
        LcarsAlertLevel.Warning -> 800
        LcarsAlertLevel.Advisory -> 1200
        LcarsAlertLevel.Normal, null -> 600
    }
    val halfPeriodMillis = alertPeriodMillis / 2

    val alertColor = if (alerting && LcarsTheme.motionMode == LcarsMotionMode.System) {
        val transition = rememberInfiniteTransition(label = "LcarsAlert")
        val animatedColor by transition.animateColor(
            initialValue = color,
            targetValue = targetAlertColor,
            animationSpec = infiniteRepeatable(
                animation = keyframes {
                    durationMillis = alertPeriodMillis
                    color at 0
                    color at (halfPeriodMillis - 1)
                    targetAlertColor at halfPeriodMillis
                    targetAlertColor at (alertPeriodMillis - 1)
                },
                repeatMode = RepeatMode.Restart,
            ),
            label = "LcarsAlertColor",
        )
        animatedColor
    } else if (alerting) {
        targetAlertColor
    } else {
        color
    }

    val baseBgColor = if (alerting) alertColor else color
    val tactileColor = if (baseBgColor == colors.spaceWhite) {
        colors.a4
    } else {
        colors.spaceWhite
    }

    val resolvedBgColor = if (isPressed && enabled && LcarsTheme.motionMode != LcarsMotionMode.Off) {
        tactileColor
    } else {
        baseBgColor
    }

    val buttonShape = when (shape) {
        LcarsButtonShape.Pill -> LcarsTheme.shapes.pill
        LcarsButtonShape.BlockStart -> LcarsTheme.shapes.startCap
        LcarsButtonShape.BlockEnd -> LcarsTheme.shapes.endCap
        LcarsButtonShape.Rectangle -> LcarsTheme.shapes.rectangle
    }

    // 1. Horizontal padding with Shape-dependent arc compensation:
    // Left arc (Pill, BlockStart) requires 16dp to avoid round edge cut; square left requires 10dp.
    // Right arc (Pill, BlockEnd) requires 16dp to avoid round edge cut; square right requires 10dp.
    val startPadding = when (shape) {
        LcarsButtonShape.Pill, LcarsButtonShape.BlockStart -> 16.dp
        LcarsButtonShape.BlockEnd, LcarsButtonShape.Rectangle -> 10.dp
    }
    val endPadding = when (shape) {
        LcarsButtonShape.Pill, LcarsButtonShape.BlockEnd -> 16.dp
        LcarsButtonShape.BlockStart, LcarsButtonShape.Rectangle -> 10.dp
    }

    // 2. Vertical padding respecting vertical alignment:
    val topPadding = when (alignment) {
        Alignment.TopStart, Alignment.TopCenter, Alignment.TopEnd -> 6.dp
        Alignment.CenterStart, Alignment.Center, Alignment.CenterEnd -> 0.dp
        else -> 0.dp // BottomStart, BottomCenter, BottomEnd
    }
    val bottomPadding = when (alignment) {
        Alignment.TopStart, Alignment.TopCenter, Alignment.TopEnd -> 0.dp
        Alignment.CenterStart, Alignment.Center, Alignment.CenterEnd -> 0.dp
        else -> 6.dp // BottomStart, BottomCenter, BottomEnd
    }

    Box(
        modifier = modifier
            .defaultMinSize(minWidth = minWidth, minHeight = maxOf(minHeight, 48.dp))
            .clip(buttonShape)
            .background(resolvedBgColor)
            .clickable(
                enabled = enabled,
                role = role,
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    soundService.playClick()
                    onClick()
                },
            )
            .semantics {
                selected?.let { this.selected = it }
            }
            .alpha(if (enabled) 1f else 0.38f)
            .padding(
                start = startPadding,
                end = endPadding,
                top = topPadding,
                bottom = bottomPadding,
            ),
        contentAlignment = alignment,
    ) {
        LcarsText(
            text = text,
            style = LocalLcarsTypography.current.button.copy(color = contentColor),
            maxLines = 1,
            autoFit = true,
            minFontSize = 10.sp,
        )
    }
}

internal fun LcarsAlertLevel.color(colors: LcarsColorScheme): Color = when (this) {
    LcarsAlertLevel.Normal -> colors.activeAccent
    LcarsAlertLevel.Advisory -> colors.lightBlue
    LcarsAlertLevel.Warning -> colors.monoAmber
    LcarsAlertLevel.Critical -> colors.alertRed
}
