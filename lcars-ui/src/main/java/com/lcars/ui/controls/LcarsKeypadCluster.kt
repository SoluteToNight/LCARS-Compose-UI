package com.lcars.ui.controls

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lcars.ui.foundation.LcarsText
import com.lcars.ui.theme.LcarsMotionTokens
import com.lcars.ui.theme.LocalLcarsColors
import com.lcars.ui.theme.LocalLcarsSpacing
import com.lcars.ui.theme.LocalLcarsTypography
import com.lcars.ui.theme.keypadRhythm

data class LcarsKeypadItem(
    val code: String,
    val label: String = "",
    val customColor: Color? = null,
    val onClick: () -> Unit = {},
)

/**
 * Stage 9 High-Density Keypad Cluster.
 * Arranges buttons with alternating rhythmic LCARS colors, pill caps, and right-aligned technobabble shortcodes.
 */
@Composable
fun LcarsKeypadColumn(
    items: List<LcarsKeypadItem>,
    modifier: Modifier = Modifier,
    buttonHeight: Dp = 32.dp,
    gap: Dp = 4.dp,
    colorOffset: Int = 0,
    shape: LcarsButtonShape = LcarsButtonShape.BlockStart,
) {
    val colors = LocalLcarsColors.current

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(gap)
    ) {
        items.forEachIndexed { index, item ->
            val defaultColor = colors.keypadRhythm(index + colorOffset)
            val btnColor = item.customColor ?: defaultColor
            LcarsKeypadButton(
                item = item,
                color = btnColor,
                shape = shape,
                height = buttonHeight,
                onClick = item.onClick,
            )
        }
    }
}

/**
 * Dual column keypad cluster with left pill caps and right rectangle buttons.
 */
@Composable
fun LcarsDualKeypadCluster(
    leftItems: List<LcarsKeypadItem>,
    rightItems: List<LcarsKeypadItem>,
    modifier: Modifier = Modifier,
    buttonHeight: Dp = 32.dp,
    gap: Dp = 4.dp,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(gap)
    ) {
        LcarsKeypadColumn(
            items = leftItems,
            modifier = Modifier.weight(1f),
            buttonHeight = buttonHeight,
            gap = gap,
            colorOffset = 0,
            shape = LcarsButtonShape.BlockStart
        )
        LcarsKeypadColumn(
            items = rightItems,
            modifier = Modifier.weight(1f),
            buttonHeight = buttonHeight,
            gap = gap,
            colorOffset = 2,
            shape = LcarsButtonShape.BlockEnd
        )
    }
}

@Composable
fun LcarsKeypadButton(
    item: LcarsKeypadItem,
    color: Color,
    modifier: Modifier = Modifier,
    shape: LcarsButtonShape = LcarsButtonShape.BlockStart,
    height: Dp = 32.dp,
    onClick: () -> Unit = {},
) {
    val typography = LocalLcarsTypography.current
    val colors = LocalLcarsColors.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val targetColor = if (isPressed) colors.spaceWhite else color
    val animatedColor by animateColorAsState(
        targetValue = targetColor,
        animationSpec = LcarsMotionTokens.ColorTransitionSpec,
        label = "KeypadColorTransition"
    )

    val cornerRadius = height / 2
    val buttonShape = when (shape) {
        LcarsButtonShape.Pill -> RoundedCornerShape(cornerRadius)
        LcarsButtonShape.BlockStart -> RoundedCornerShape(topStart = cornerRadius, bottomStart = cornerRadius)
        LcarsButtonShape.BlockEnd -> RoundedCornerShape(topEnd = cornerRadius, bottomEnd = cornerRadius)
        LcarsButtonShape.Rectangle -> RoundedCornerShape(0.dp)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(buttonShape)
            .background(animatedColor)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (item.label.isNotEmpty()) {
                LcarsText(
                    text = item.label.uppercase(),
                    style = typography.labelSmall.copy(color = Color.Black),
                    maxLines = 1,
                )
            } else {
                Box(modifier = Modifier.width(1.dp))
            }

            LcarsText(
                text = item.code.uppercase(),
                style = typography.labelMedium.copy(color = Color.Black),
                maxLines = 1,
            )
        }
    }
}
