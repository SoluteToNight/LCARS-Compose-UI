package com.lcars.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

enum class LcarsCommandRailSide {
    Start,
    End,
}

enum class LcarsCommandRailItemType {
    Command,
    PassiveBlock,
    SpacerBlock,
    AlertBlock,
}

data class LcarsCommandRailItem(
    val id: String,
    val label: String = "",
    val type: LcarsCommandRailItemType = LcarsCommandRailItemType.Command,
    val color: Color? = null,
    val enabled: Boolean = true,
    val weight: Float = 0f,
)

@Composable
fun LcarsCommandRail(
    items: List<LcarsCommandRailItem>,
    modifier: Modifier = Modifier,
    side: LcarsCommandRailSide = LcarsCommandRailSide.Start,
    compact: Boolean? = null,
    onCommandClick: (LcarsCommandRailItem) -> Unit = {},
) {
    val colors = LocalLcarsColors.current
    val spacing = LocalLcarsSpacing.current
    val resolvedCompact = compact ?: LocalLcarsAdaptiveProfile.current.compact
    val gap = spacing.gapStandard
    val railWidth = if (resolvedCompact) spacing.commandRailCompactWidth else spacing.commandRailWidth
    val commandHeight = if (resolvedCompact) {
        (spacing.buttonMinHeight - 6.dp).coerceAtLeast(34.dp)
    } else {
        spacing.buttonMinHeight
    }
    val shape = when (side) {
        LcarsCommandRailSide.Start -> LcarsButtonShape.BlockStart
        LcarsCommandRailSide.End -> LcarsButtonShape.BlockEnd
    }

    Column(
        modifier = modifier.widthIn(min = railWidth),
        verticalArrangement = Arrangement.spacedBy(gap),
    ) {
        items.forEachIndexed { index, item ->
            val color = item.color ?: commandRailColor(index, item.type, colors)
            val itemModifier = Modifier
                .fillMaxWidth()
                .then(if (item.weight > 0f) Modifier.weight(item.weight) else Modifier)

            when (item.type) {
                LcarsCommandRailItemType.Command -> LcarsButton(
                    text = item.label,
                    onClick = { onCommandClick(item) },
                    modifier = itemModifier,
                    color = color,
                    shape = shape,
                    alerting = false,
                    enabled = item.enabled,
                    minWidth = 0.dp,
                    minHeight = commandHeight,
                )
                LcarsCommandRailItemType.AlertBlock -> LcarsButton(
                    text = item.label,
                    onClick = { onCommandClick(item) },
                    modifier = itemModifier,
                    color = color,
                    shape = shape,
                    alerting = true,
                    enabled = item.enabled,
                    minWidth = 0.dp,
                    minHeight = commandHeight,
                )
                LcarsCommandRailItemType.PassiveBlock -> PassiveRailBlock(
                    label = item.label,
                    color = color,
                    modifier = itemModifier.height(if (resolvedCompact) 34.dp else 46.dp),
                    side = side,
                )
                LcarsCommandRailItemType.SpacerBlock -> Spacer(
                    modifier = itemModifier
                        .height(if (resolvedCompact) 42.dp else 68.dp)
                        .clip(railShape(side))
                        .background(color),
                )
            }
        }
    }
}

@Composable
fun LcarsSegmentedControl(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabledOptions: Set<String> = options.toSet(),
    alerting: Boolean = false,
) {
    val colors = LocalLcarsColors.current
    val gap = LocalLcarsSpacing.current.gapStandard

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(gap),
    ) {
        options.forEachIndexed { index, option ->
            val state = resolveLcarsSegmentState(option, selectedOption, enabledOptions)
            val selected = state == LcarsSegmentState.Selected
            val enabled = state != LcarsSegmentState.Disabled
            val baseColor = when {
                selected -> colors.monoAmber
                index % 3 == 0 -> colors.a2
                index % 3 == 1 -> colors.lightBlue
                else -> colors.a7
            }
            val displayColor = steppedAlertColor(
                baseColor = baseColor,
                alertColor = colors.alertRed,
                active = alerting && selected,
            )
            val shape = when (index) {
                0 -> LcarsButtonShape.BlockStart
                options.lastIndex -> LcarsButtonShape.BlockEnd
                else -> LcarsButtonShape.Rectangle
            }

            LcarsButton(
                text = option,
                onClick = { onOptionSelected(option) },
                modifier = Modifier.weight(1f),
                color = displayColor,
                contentColor = Color.Black,
                shape = shape,
                enabled = enabled,
                minWidth = 0.dp,
                minHeight = 44.dp,
            )
        }
    }
}

@Composable
fun LcarsToggle(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    checkedLabel: String = "online",
    uncheckedLabel: String = "standby",
    alerting: Boolean = false,
) {
    val colors = LocalLcarsColors.current
    val gap = LocalLcarsSpacing.current.gapStandard
    val activeColor = steppedAlertColor(
        baseColor = if (checked) colors.tacticalGreen else colors.monoAmber,
        alertColor = colors.alertRed,
        active = alerting,
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(percent = 50))
            .background(colors.panel)
            .clickable(role = Role.Switch) { onCheckedChange(!checked) }
            .semantics(mergeDescendants = true) {}
            .padding(gap),
        horizontalArrangement = Arrangement.spacedBy(gap),
    ) {
        Box(
            modifier = Modifier
                .weight(if (checked) 0.36f else 0.64f)
                .height(40.dp)
                .clip(RoundedCornerShape(percent = 50))
                .background(if (checked) colors.a7 else activeColor),
            contentAlignment = Alignment.BottomEnd,
        ) {
            LcarsText(
                text = uncheckedLabel,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                style = LocalLcarsTypography.current.labelSmall.copy(color = Color.Black),
                maxLines = 1,
            )
        }
        Box(
            modifier = Modifier
                .weight(if (checked) 0.64f else 0.36f)
                .height(40.dp)
                .clip(RoundedCornerShape(percent = 50))
                .background(if (checked) activeColor else colors.a7),
            contentAlignment = Alignment.BottomEnd,
        ) {
            LcarsText(
                text = checkedLabel,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                style = LocalLcarsTypography.current.labelSmall.copy(color = Color.Black),
                maxLines = 1,
            )
        }
    }
}

@Composable
fun LcarsDialog(
    title: String,
    message: String,
    confirmLabel: String,
    dismissLabel: String,
    level: LcarsAlertLevel = LcarsAlertLevel.Warning,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalLcarsColors.current
    val accent = when (level) {
        LcarsAlertLevel.Normal -> colors.tacticalGreen
        LcarsAlertLevel.Advisory -> colors.lightBlue
        LcarsAlertLevel.Warning -> colors.monoAmber
        LcarsAlertLevel.Critical -> colors.alertRed
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = true),
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(colors.background)
                .border(4.dp, accent)
                .padding(LocalLcarsSpacing.current.gapStandard),
            verticalArrangement = Arrangement.spacedBy(LocalLcarsSpacing.current.gapStandard),
        ) {
            LcarsBar(
                color = accent,
                height = 30.dp,
                startCap = true,
                endCap = true,
                label = title,
                labelAlign = LcarsLabelAlign.End,
                labelColor = colors.background,
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colors.panel)
                    .padding(12.dp),
            ) {
                LcarsText(
                    text = message,
                    style = LocalLcarsTypography.current.telemetry.copy(color = colors.lightBlue),
                    maxLines = 4,
                    autoFit = false,
                    overflow = TextOverflow.Ellipsis,
                    softWrap = true,
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(LocalLcarsSpacing.current.gapStandard),
            ) {
                LcarsButton(
                    text = dismissLabel,
                    color = colors.a7,
                    shape = LcarsButtonShape.BlockStart,
                    minWidth = 0.dp,
                    modifier = Modifier.weight(1f),
                    onClick = onDismiss,
                )
                LcarsButton(
                    text = confirmLabel,
                    color = accent,
                    shape = LcarsButtonShape.BlockEnd,
                    minWidth = 0.dp,
                    modifier = Modifier.weight(1f),
                    onClick = onConfirm,
                )
            }
        }
    }
}

private fun commandRailColor(
    index: Int,
    type: LcarsCommandRailItemType,
    colors: LcarsColors,
): Color = when (type) {
    LcarsCommandRailItemType.AlertBlock -> colors.alertRed
    LcarsCommandRailItemType.SpacerBlock -> if (index % 2 == 0) colors.a8 else colors.a7
    LcarsCommandRailItemType.PassiveBlock -> if (index % 2 == 0) colors.violet else colors.almondCreme
    LcarsCommandRailItemType.Command -> when (index % 5) {
        0 -> colors.classicRed
        1 -> colors.lightBlue
        2 -> colors.butterscotch
        3 -> colors.almondCreme
        else -> colors.a7
    }
}

@Composable
private fun PassiveRailBlock(
    label: String,
    color: Color,
    modifier: Modifier,
    side: LcarsCommandRailSide,
) {
    Box(
        modifier = modifier
            .clip(railShape(side))
            .background(color)
            .alpha(if (label.isBlank()) 0.92f else 1f)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.BottomEnd,
    ) {
        if (label.isNotBlank()) {
            LcarsText(
                text = label,
                style = LocalLcarsTypography.current.labelSmall.copy(color = Color.Black),
                maxLines = 1,
            )
        }
    }
}

private fun railShape(side: LcarsCommandRailSide): RoundedCornerShape = when (side) {
    LcarsCommandRailSide.Start -> RoundedCornerShape(
        topStartPercent = 50,
        topEndPercent = 0,
        bottomEndPercent = 0,
        bottomStartPercent = 50,
    )
    LcarsCommandRailSide.End -> RoundedCornerShape(
        topStartPercent = 0,
        topEndPercent = 50,
        bottomEndPercent = 50,
        bottomStartPercent = 0,
    )
}

internal enum class LcarsSegmentState {
    Selected,
    Enabled,
    Disabled,
}

internal fun resolveLcarsSegmentState(
    option: String,
    selectedOption: String,
    enabledOptions: Set<String>,
): LcarsSegmentState = when {
    option !in enabledOptions -> LcarsSegmentState.Disabled
    option == selectedOption -> LcarsSegmentState.Selected
    else -> LcarsSegmentState.Enabled
}

internal fun resolveLcarsToggleLabel(
    checked: Boolean,
    checkedLabel: String,
    uncheckedLabel: String,
): String = if (checked) checkedLabel else uncheckedLabel
