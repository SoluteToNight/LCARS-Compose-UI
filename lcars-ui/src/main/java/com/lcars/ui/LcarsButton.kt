package com.lcars.ui

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

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
    color: Color = LocalLcarsColors.current.monoAmber,
    contentColor: Color = Color.Black,
    shape: LcarsButtonShape = LcarsButtonShape.Pill,
    alerting: Boolean = false,
    enabled: Boolean = true,
    minWidth: Dp = LocalLcarsSpacing.current.buttonMinWidth,
    minHeight: Dp = LocalLcarsSpacing.current.buttonMinHeight,
) {
    val colors = LocalLcarsColors.current
    val soundService = LocalLcarsSoundService.current
    val alertColor = if (alerting) {
        val transition = rememberInfiniteTransition(label = "LcarsAlert")
        val animatedColor by transition.animateColor(
            initialValue = color,
            targetValue = colors.alertRed,
            animationSpec = infiniteRepeatable(
                animation = keyframes {
                    durationMillis = 600
                    color at 0
                    color at 299
                    colors.alertRed at 300
                    colors.alertRed at 599
                },
                repeatMode = RepeatMode.Restart,
            ),
            label = "LcarsAlertColor",
        )
        animatedColor
    } else {
        color
    }
    val backgroundColor = if (alerting) alertColor else color
    val buttonShape = when (shape) {
        LcarsButtonShape.Pill -> RoundedCornerShape(percent = 50)
        LcarsButtonShape.BlockStart -> RoundedCornerShape(
            topStartPercent = 50,
            topEndPercent = 0,
            bottomEndPercent = 0,
            bottomStartPercent = 50,
        )
        LcarsButtonShape.BlockEnd -> RoundedCornerShape(
            topStartPercent = 0,
            topEndPercent = 50,
            bottomEndPercent = 50,
            bottomStartPercent = 0,
        )
        LcarsButtonShape.Rectangle -> RoundedCornerShape(0.dp)
    }

    Box(
        modifier = modifier
            .defaultMinSize(minWidth = minWidth, minHeight = minHeight)
            .clip(buttonShape)
            .background(backgroundColor)
            .clickable(enabled = enabled, role = Role.Button, onClick = {
                soundService.playClick()
                onClick()
            })
            .alpha(if (enabled) 1f else 0f)
            .padding(horizontal = 14.dp, vertical = 8.dp),
        contentAlignment = Alignment.BottomEnd,
    ) {
        LcarsText(
            text = text,
            style = LocalLcarsTypography.current.button.copy(color = contentColor),
            maxLines = 1,
        )
    }
}
