package com.lcars.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Immutable
data class LcarsBarSegment(
    val weight: Float,
    val color: Color,
    val label: String? = null,
)

@Composable
fun LcarsSegmentedBar(
    segments: List<LcarsBarSegment>,
    modifier: Modifier = Modifier,
    height: Dp = LocalLcarsSpacing.current.barHeight,
    gap: Dp = LocalLcarsSpacing.current.gapStandard,
    labelColor: Color = Color.Black,
) {
    val safeSegments = segments.ifEmpty {
        listOf(LcarsBarSegment(weight = 1f, color = LocalLcarsColors.current.framePrimary))
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(height),
        horizontalArrangement = Arrangement.spacedBy(gap),
    ) {
        safeSegments.forEach { segment ->
            Box(
                modifier = Modifier
                    .weight(segment.weight.coerceAtLeast(0.01f))
                    .fillMaxHeight()
                    .background(segment.color)
                    .padding(horizontal = gap, vertical = (gap / 2f).coerceAtLeast(1.dp)),
                contentAlignment = Alignment.BottomEnd,
            ) {
                if (!segment.label.isNullOrBlank()) {
                    LcarsText(
                        text = segment.label,
                        style = LocalLcarsTypography.current.labelSmall.copy(color = labelColor),
                        maxLines = 1,
                        minFontSize = 8.sp,
                    )
                }
            }
        }
    }
}

@Composable
fun LcarsConsoleFrame(
    modifier: Modifier = Modifier,
    compact: Boolean? = null,
    frameColor: Color = LocalLcarsColors.current.framePrimary,
    railWidth: Dp? = null,
    topBarHeight: Dp? = null,
    contentStartPadding: Dp? = null,
    contentTopPadding: Dp? = null,
    leftRail: @Composable BoxScope.() -> Unit,
    topBar: @Composable RowScope.() -> Unit,
    content: @Composable BoxScope.() -> Unit,
) {
    val colors = LocalLcarsColors.current
    val spacing = LocalLcarsSpacing.current
    val resolvedCompact = compact ?: LocalLcarsAdaptiveProfile.current.compact
    val resolvedRailWidth = railWidth ?: if (resolvedCompact) 154.dp else 224.dp
    val resolvedTopBarHeight = topBarHeight ?: if (resolvedCompact) 18.dp else 28.dp
    val resolvedContentStart = contentStartPadding ?: if (resolvedCompact) 22.dp else 32.dp
    val resolvedContentTop = contentTopPadding ?: if (resolvedCompact) 22.dp else 28.dp
    val cornerSize = if (resolvedCompact) 32.dp else 44.dp

    Row(
        modifier = modifier
            .background(colors.background)
            .padding(spacing.gapLarge),
    ) {
        Box(
            modifier = Modifier
                .width(resolvedRailWidth)
                .fillMaxHeight(),
            content = leftRail,
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .drawWithCache {
                    val top = resolvedTopBarHeight.toPx()
                    val corner = cornerSize.toPx()
                    val colorBridge = Path().apply {
                        moveTo(0f, top)
                        lineTo(corner, top)
                        lineTo(0f, top + corner)
                        close()
                    }
                    val maskRadius = corner * 0.62f
                    val mask = Path().apply {
                        moveTo(0f, top + maskRadius)
                        quadraticTo(0f, top, maskRadius, top)
                        lineTo(corner, top)
                        lineTo(corner, top + corner)
                        lineTo(0f, top + corner)
                        close()
                    }
                    onDrawBehind {
                        drawPath(path = colorBridge, color = frameColor)
                        drawPath(path = mask, color = colors.background)
                    }
                },
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(spacing.gapStandard),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(resolvedTopBarHeight),
                    horizontalArrangement = Arrangement.spacedBy(spacing.gapStandard),
                    content = topBar,
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(start = resolvedContentStart, top = resolvedContentTop),
                    content = content,
                )
            }
        }
    }
}

@Immutable
data class LcarsFramedRailItem(
    val id: String,
    val label: String = "",
    val type: LcarsCommandRailItemType = LcarsCommandRailItemType.Command,
    val color: Color? = null,
    val height: Dp? = null,
    val weight: Float = 0f,
    val enabled: Boolean = true,
)

@Composable
fun LcarsFramedCommandRail(
    items: List<LcarsFramedRailItem>,
    modifier: Modifier = Modifier,
    side: LcarsCommandRailSide = LcarsCommandRailSide.Start,
    compact: Boolean? = null,
    frameColor: Color = LocalLcarsColors.current.framePrimary,
    topInset: Dp? = null,
    topCornerRadius: Dp? = null,
    header: (@Composable BoxScope.() -> Unit)? = null,
    footer: (@Composable BoxScope.() -> Unit)? = null,
    onCommandClick: (LcarsFramedRailItem) -> Unit = {},
) {
    val colors = LocalLcarsColors.current
    val spacing = LocalLcarsSpacing.current
    val resolvedCompact = compact ?: LocalLcarsAdaptiveProfile.current.compact
    val inset = topInset ?: if (resolvedCompact) 136.dp else 166.dp
    val corner = topCornerRadius ?: if (resolvedCompact) 64.dp else 98.dp

    Box(
        modifier = modifier
            .widthIn(min = if (resolvedCompact) spacing.commandRailCompactWidth else spacing.commandRailWidth)
            .drawWithCache {
                val h = (inset - spacing.gapStandard).toPx()
                val r = corner.toPx()
                val body = Path().apply {
                    moveTo(0f, h)
                    lineTo(0f, r)
                    quadraticTo(0f, 0f, r, 0f)
                    lineTo(size.width, 0f)
                    lineTo(size.width, h)
                    close()
                }
                onDrawBehind {
                    drawPath(path = body, color = frameColor)
                }
            },
    ) {
        header?.let {
            Box(modifier = Modifier.fillMaxWidth().height(inset), content = it)
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = inset),
            verticalArrangement = Arrangement.spacedBy(spacing.gapStandard),
        ) {
            items.forEachIndexed { index, item ->
                val color = item.color ?: framedRailColor(index, item.type, colors)
                val itemModifier = Modifier
                    .fillMaxWidth()
                    .then(if (item.weight > 0f) Modifier.weight(item.weight) else Modifier)
                    .then(if (item.weight <= 0f && item.height != null) Modifier.height(item.height) else Modifier)
                LcarsFramedRailBlock(
                    item = item,
                    color = color,
                    side = side,
                    modifier = itemModifier,
                    onClick = { onCommandClick(item) },
                )
            }
            footer?.let {
                Box(modifier = Modifier.fillMaxWidth(), content = it)
            }
        }
    }
}

@Composable
private fun LcarsFramedRailBlock(
    item: LcarsFramedRailItem,
    color: Color,
    side: LcarsCommandRailSide,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val soundService = LocalLcarsSoundService.current
    val shape = railShapeForSide(side)
    val backgroundColor = if (item.type == LcarsCommandRailItemType.AlertBlock) {
        steppedAlertColor(color, LocalLcarsColors.current.alertRed, active = true)
    } else {
        color
    }

    Box(
        modifier = modifier
            .clip(if (item.type == LcarsCommandRailItemType.SpacerBlock) RoundedCornerShape(0.dp) else shape)
            .background(backgroundColor)
            .then(
                if (item.type == LcarsCommandRailItemType.Command || item.type == LcarsCommandRailItemType.AlertBlock) {
                    Modifier.clickable(enabled = item.enabled, role = Role.Button) {
                        soundService.playClick()
                        onClick()
                    }
                } else {
                    Modifier
                },
            )
            .alpha(if (item.enabled) 1f else 0f)
            .padding(end = 14.dp, bottom = 8.dp),
        contentAlignment = Alignment.BottomEnd,
    ) {
        if (item.label.isNotBlank()) {
            LcarsText(
                text = item.label,
                style = LocalLcarsTypography.current.labelSmall.copy(color = Color.Black),
                maxLines = 1,
                minFontSize = 8.sp,
            )
        }
    }
}

@Composable
fun <T> LcarsOptionStrip(
    items: List<T>,
    selectedItem: T?,
    onSelect: (T) -> Unit,
    label: (T) -> String,
    modifier: Modifier = Modifier,
    compact: Boolean = false,
    itemContent: @Composable BoxScope.(item: T, selected: Boolean) -> Unit,
) {
    val colors = LocalLcarsColors.current
    val gap = LocalLcarsSpacing.current.gapStandard
    val typography = LocalLcarsTypography.current

    Column(
        modifier = modifier.height(if (compact) 48.dp else 64.dp),
        verticalArrangement = Arrangement.spacedBy(gap),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(gap),
        ) {
            items.forEach { item ->
                val selected = item == selectedItem
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(if (selected) colors.panel else Color(0xFF050505))
                        .clickable(role = Role.Button) { onSelect(item) },
                ) {
                    itemContent(item, selected)
                    if (selected) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            drawRect(
                                color = colors.monoAmber,
                                style = Stroke(width = if (compact) 2.dp.toPx() else 3.dp.toPx()),
                                size = Size(size.width, size.height),
                            )
                            drawRect(
                                color = colors.activeAccent,
                                size = Size(
                                    width = if (compact) 3.dp.toPx() else 4.dp.toPx(),
                                    height = size.height,
                                ),
                            )
                        }
                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(if (compact) 10.dp else 12.dp),
            horizontalArrangement = Arrangement.spacedBy(gap),
        ) {
            items.forEach { item ->
                val selected = item == selectedItem
                LcarsText(
                    text = label(item),
                    style = typography.labelSmall.copy(
                        color = if (selected) colors.monoAmber else colors.readoutAccent,
                        fontSize = if (compact) 8.sp else 10.sp,
                        lineHeight = if (compact) 9.sp else 11.sp,
                        textAlign = TextAlign.Center,
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(Color(0xFF050505)),
                    maxLines = 1,
                    minFontSize = 6.sp,
                )
            }
        }
    }
}

private fun framedRailColor(
    index: Int,
    type: LcarsCommandRailItemType,
    colors: LcarsColors,
): Color = when (type) {
    LcarsCommandRailItemType.AlertBlock -> colors.alertRed
    LcarsCommandRailItemType.SpacerBlock -> colors.framePrimary
    LcarsCommandRailItemType.PassiveBlock -> if (index % 2 == 0) colors.frameSecondary else colors.almondCreme
    LcarsCommandRailItemType.Command -> when (index % 5) {
        0 -> colors.commandPrimary
        1 -> colors.commandSecondary
        2 -> colors.auxiliaryTan
        3 -> colors.commandInactive
        else -> colors.a7
    }
}

private fun railShapeForSide(side: LcarsCommandRailSide): RoundedCornerShape = when (side) {
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
