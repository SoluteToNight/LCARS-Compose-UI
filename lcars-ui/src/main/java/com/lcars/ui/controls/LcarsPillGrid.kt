package com.lcars.ui.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lcars.ui.foundation.LcarsText
import com.lcars.ui.theme.LcarsTheme
import com.lcars.ui.theme.LocalLcarsColors
import com.lcars.ui.theme.LocalLcarsTypography

/**
 * An item descriptor in an [LcarsPillGrid].
 *
 * @param text The button label. If null or blank, renders an empty asymmetric spacer slot.
 * @param onClick Event callback when clicked.
 * @param color The background color of the pill button.
 * @param shape Shape of the pill button (defaulting to [LcarsButtonShape.BlockStart] for left column, [LcarsButtonShape.BlockEnd] for right column).
 * @param enabled Whether the button is interactive.
 */
data class LcarsPillItem(
    val text: String? = null,
    val onClick: () -> Unit = {},
    val color: Color = Color.Unspecified,
    val shape: LcarsButtonShape? = null,
    val enabled: Boolean = true,
)

/**
 * Authentic LCARS 2-Column Asymmetrical Pill Grid (.pillbox / .pillbox-2).
 *
 * Implements the classic 2-column paired pill layout:
 * - Left column defaults to [LcarsButtonShape.BlockStart] (rounded left cap, square right).
 * - Right column defaults to [LcarsButtonShape.BlockEnd] (square left, rounded right cap) or [LcarsButtonShape.Rectangle].
 * - Supports empty slots to create asymmetrical visual breathing spaces.
 */
@Composable
fun LcarsPillGrid(
    items: List<LcarsPillItem>,
    modifier: Modifier = Modifier,
    buttonHeight: Dp = 56.dp,
    gap: Dp = 8.dp,
) {
    val colors = LocalLcarsColors.current

    // Group items into pairs of 2
    val pairs = items.chunked(2)

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(gap),
    ) {
        pairs.forEach { pair ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(gap),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Left Column Item
                val leftItem = pair.getOrNull(0)
                if (leftItem != null && !leftItem.text.isNullOrBlank()) {
                    val resolvedColor = if (leftItem.color != Color.Unspecified) leftItem.color else colors.commandPrimary
                    val resolvedShape = leftItem.shape ?: LcarsButtonShape.BlockStart
                    LcarsButton(
                        text = leftItem.text,
                        onClick = leftItem.onClick,
                        color = resolvedColor,
                        shape = resolvedShape,
                        enabled = leftItem.enabled,
                        minHeight = buttonHeight,
                        modifier = Modifier
                            .weight(1f)
                            .height(buttonHeight),
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(buttonHeight),
                    )
                }

                // Right Column Item
                val rightItem = pair.getOrNull(1)
                if (rightItem != null && !rightItem.text.isNullOrBlank()) {
                    val resolvedColor = if (rightItem.color != Color.Unspecified) rightItem.color else colors.commandSecondary
                    val resolvedShape = rightItem.shape ?: LcarsButtonShape.BlockEnd
                    LcarsButton(
                        text = rightItem.text,
                        onClick = rightItem.onClick,
                        color = resolvedColor,
                        shape = resolvedShape,
                        enabled = rightItem.enabled,
                        minHeight = buttonHeight,
                        modifier = Modifier
                            .weight(1f)
                            .height(buttonHeight),
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(buttonHeight),
                    )
                }
            }
        }
    }
}

/**
 * A telemetry/status entry in [LcarsStatusList].
 */
data class LcarsStatusItem(
    val text: String,
    val bulletColor: Color = Color.Unspecified,
    val textColor: Color = Color.Unspecified,
)

/**
 * Authentic LCARS Status Bullet List (.lcars-list-2).
 *
 * Displays an uppercase list with authentic oval/pill bullet indicators on the left.
 */
@Composable
fun LcarsStatusList(
    items: List<LcarsStatusItem>,
    modifier: Modifier = Modifier,
    defaultColor: Color = LocalLcarsColors.current.monoAmber,
    gap: Dp = 8.dp,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(gap),
    ) {
        items.forEach { item ->
            val bulletColor = if (item.bulletColor != Color.Unspecified) item.bulletColor else defaultColor
            val textColor = if (item.textColor != Color.Unspecified) item.textColor else defaultColor

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                // Classic LCARS horizontal oval bullet (20dp x 12dp)
                Box(
                    modifier = Modifier
                        .size(width = 20.dp, height = 12.dp)
                        .clip(LcarsTheme.shapes.pill)
                        .background(bulletColor),
                )

                LcarsText(
                    text = item.text,
                    style = LocalLcarsTypography.current.labelSmall.copy(
                        color = textColor,
                        fontSize = 14.sp,
                    ),
                    maxLines = 1,
                    autoFit = true,
                )
            }
        }
    }
}
