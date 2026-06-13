package com.lcars.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Immutable
data class LcarsPhonePaddMetrics(
    val gap: Dp = 4.dp,
    val outerPadding: Dp = 8.dp,
    val headerHeight: Dp = 44.dp,
    val railWidth: Dp = 38.dp,
    val railBlockHeight: Dp = 42.dp,
    val controlHeight: Dp = 42.dp,
    val stripHeight: Dp = 10.dp,
    val panelPadding: Dp = 8.dp,
)

val LocalLcarsPhonePaddMetrics = staticCompositionLocalOf { LcarsPhonePaddMetrics() }

fun resolveLcarsPhonePaddMetrics(width: Dp, height: Dp): LcarsPhonePaddMetrics = when {
    width < 360.dp || height < 700.dp -> LcarsPhonePaddMetrics(
        outerPadding = 6.dp,
        headerHeight = 38.dp,
        railWidth = 32.dp,
        railBlockHeight = 36.dp,
        controlHeight = 36.dp,
        panelPadding = 6.dp,
    )
    width > 430.dp -> LcarsPhonePaddMetrics(
        outerPadding = 10.dp,
        headerHeight = 48.dp,
        railWidth = 42.dp,
        railBlockHeight = 46.dp,
        controlHeight = 44.dp,
        panelPadding = 10.dp,
    )
    else -> LcarsPhonePaddMetrics()
}

fun lcarsPhonePaddColors(): LcarsColors = LcarsColors(
    monoAmber = Color(0xFFC96A1A),
    auxiliaryTan = Color(0xFFB78D6A),
    lightBlue = Color(0xFF9EA6D7),
    violet = Color(0xFFB575AE),
    tacticalGreen = Color(0xFF5FA438),
    alertRed = Color(0xFFE23B28),
    background = Color.Black,
    panel = Color(0xFF050304),
    text = Color(0xFFE8B089),
    a1 = Color(0xFFE17822),
    a2 = Color(0xFFB85E20),
    a3 = Color(0xFFD39367),
    a4 = Color(0xFFE9BFA0),
    a5 = Color(0xFFD9841E),
    a6 = Color(0xFF8E578F),
    a7 = Color(0xFF6A4F78),
    a8 = Color(0xFFC08ABC),
    a9 = Color(0xFF9B4C7C),
    blue = Color(0xFF777FBC),
    butterscotch = Color(0xFFC96A1A),
    almondCreme = Color(0xFFE9BFA0),
    classicRed = Color(0xFFE23B28),
    tomato = Color(0xFFE65A34),
    gray = Color(0xFF5D5663),
    spaceWhite = Color(0xFFF2D4C3),
)

@Composable
fun LcarsPhonePaddTheme(
    style: LcarsStyle = LcarsStyle.StandardPadd,
    colors: LcarsColors? = null,
    typography: LcarsTypography = LcarsTypography().let { base ->
        base.copy(
            header = base.header.copy(fontSize = 22.sp, lineHeight = 24.sp),
            button = base.button.copy(fontSize = 15.sp, lineHeight = 16.sp),
            telemetry = base.telemetry.copy(fontSize = 14.sp, lineHeight = 16.sp),
            labelSmall = base.labelSmall.copy(fontSize = 11.sp, lineHeight = 12.sp),
        )
    },
    metrics: LcarsPhonePaddMetrics = LcarsPhonePaddMetrics(),
    content: @Composable () -> Unit,
) {
    LcarsTheme(
        style = style,
        colors = colors,
        typography = typography,
        spacing = LcarsSpacing(
            gapStandard = metrics.gap,
            gapLarge = metrics.gap * 2f,
            buttonMinWidth = 76.dp,
            buttonMinHeight = metrics.controlHeight,
            barHeight = 20.dp,
            elbowThickness = 24.dp,
            panelPadding = metrics.panelPadding,
            scaffoldControlWidth = 96.dp,
            commandRailWidth = 104.dp,
            commandRailCompactWidth = 82.dp,
        ),
    ) {
        CompositionLocalProvider(LocalLcarsPhonePaddMetrics provides metrics) {
            content()
        }
    }
}

@Composable
fun LcarsPhonePaddScaffold(
    title: String,
    modifier: Modifier = Modifier,
    registry: String = "PADD 2370",
    footerLabel: String? = null,
    sideRail: Boolean = true,
    footer: (@Composable RowScope.() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val metrics = resolveLcarsPhonePaddMetrics(maxWidth, maxHeight)

        CompositionLocalProvider(LocalLcarsPhonePaddMetrics provides metrics) {
            val colors = LocalLcarsColors.current
            val padd = LocalLcarsPhonePaddMetrics.current

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colors.background)
                    .padding(padd.outerPadding),
                verticalArrangement = Arrangement.spacedBy(padd.gap),
            ) {
                LcarsPaddHeader(
                    title = title,
                    registry = registry,
                    modifier = Modifier.fillMaxWidth(),
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(padd.gap),
                ) {
                    if (sideRail) {
                        LcarsPaddSideRail(
                            modifier = Modifier
                                .width(padd.railWidth)
                                .fillMaxHeight(),
                        )
                    }
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.spacedBy(padd.gap),
                        content = content,
                    )
                }
                footer?.let { footerContent ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(padd.gap),
                        content = footerContent,
                    )
                }
                if (footerLabel != null) {
                    LcarsPaddStatusStrip(
                        label = footerLabel,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }
}

@Composable
fun LcarsPaddHeader(
    title: String,
    modifier: Modifier = Modifier,
    registry: String = "PADD 2370",
) {
    val colors = LocalLcarsColors.current
    val metrics = LocalLcarsPhonePaddMetrics.current

    Row(
        modifier = modifier.height(metrics.headerHeight),
        horizontalArrangement = Arrangement.spacedBy(metrics.gap),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .width(metrics.railWidth)
                .fillMaxHeight()
                .clip(RoundedCornerShape(topStartPercent = 50, bottomStartPercent = 50))
                .background(colors.violet),
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(metrics.gap),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(colors.panel)
                    .border(1.dp, colors.violet),
                contentAlignment = Alignment.CenterStart,
            ) {
                LcarsText(
                    text = registry,
                    modifier = Modifier.padding(horizontal = metrics.panelPadding),
                    style = LocalLcarsTypography.current.labelSmall.copy(color = colors.a4),
                    maxLines = 1,
                )
            }
            LcarsPaddStatusStrip(
                label = title,
                modifier = Modifier.fillMaxWidth(),
                primaryColor = colors.a1,
                capColor = colors.violet,
            )
        }
    }
}

@Composable
fun LcarsPaddSideRail(
    modifier: Modifier = Modifier,
    blocks: List<String> = listOf("01", "02", "03", "04"),
) {
    val colors = LocalLcarsColors.current
    val metrics = LocalLcarsPhonePaddMetrics.current

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(metrics.gap),
    ) {
        blocks.forEachIndexed { index, label ->
            val color = when (index % 4) {
                0 -> colors.a8
                1 -> colors.a5
                2 -> colors.a2
                else -> colors.gray
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(metrics.railBlockHeight)
                    .clip(
                        RoundedCornerShape(
                            topStartPercent = if (index == 0) 50 else 0,
                            bottomStartPercent = if (index == blocks.lastIndex) 50 else 0,
                        ),
                    )
                    .background(color)
                    .padding(4.dp),
                contentAlignment = Alignment.BottomEnd,
            ) {
                LcarsText(
                    text = label,
                    style = LocalLcarsTypography.current.labelSmall.copy(color = Color.Black),
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(metrics.stripHeight)
                .background(colors.tacticalGreen),
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(metrics.stripHeight)
                .background(colors.tacticalGreen),
        )
    }
}

enum class LcarsPaddControlShape {
    Pill,
    LeftCap,
    RightCap,
    Rectangle,
}

@Composable
fun LcarsPaddControl(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = LocalLcarsColors.current.a1,
    contentColor: Color = Color.Black,
    shape: LcarsPaddControlShape = LcarsPaddControlShape.RightCap,
    enabled: Boolean = true,
) {
    val metrics = LocalLcarsPhonePaddMetrics.current
    val soundService = LocalLcarsSoundService.current
    val controlShape = when (shape) {
        LcarsPaddControlShape.Pill -> RoundedCornerShape(percent = 50)
        LcarsPaddControlShape.LeftCap -> RoundedCornerShape(
            topStartPercent = 50,
            bottomStartPercent = 50,
        )
        LcarsPaddControlShape.RightCap -> RoundedCornerShape(
            topEndPercent = 50,
            bottomEndPercent = 50,
        )
        LcarsPaddControlShape.Rectangle -> RoundedCornerShape(0.dp)
    }

    Box(
        modifier = modifier
            .defaultMinSize(minHeight = metrics.controlHeight)
            .clip(controlShape)
            .background(color)
            .clickable(enabled = enabled, role = Role.Button, onClick = {
                soundService.playClick()
                onClick()
            })
            .alpha(if (enabled) 1f else 0f)
            .padding(horizontal = metrics.panelPadding, vertical = 5.dp),
        contentAlignment = Alignment.BottomEnd,
    ) {
        LcarsText(
            text = text,
            style = LocalLcarsTypography.current.button.copy(color = contentColor),
            minFontSize = 9.sp,
        )
    }
}

@Composable
fun LcarsPaddReadoutPanel(
    title: String,
    modifier: Modifier = Modifier,
    footerLabel: String? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val colors = LocalLcarsColors.current
    val metrics = LocalLcarsPhonePaddMetrics.current

    Column(
        modifier = modifier
            .background(colors.panel)
            .border(1.dp, colors.a7)
            .padding(metrics.panelPadding),
        verticalArrangement = Arrangement.spacedBy(metrics.gap),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(metrics.gap),
        ) {
            Box(
                modifier = Modifier
                    .width(8.dp)
                    .height(8.dp)
                    .clip(RoundedCornerShape(percent = 50))
                    .background(colors.violet),
            )
            LcarsText(
                text = title,
                modifier = Modifier.weight(1f),
                style = LocalLcarsTypography.current.labelSmall.copy(color = colors.a1),
                maxLines = 1,
            )
        }
        content()
        footerLabel?.let {
            LcarsPaddStatusStrip(
                label = it,
                modifier = Modifier.fillMaxWidth(),
                primaryColor = colors.a2,
                capColor = colors.violet,
            )
        }
    }
}

@Composable
fun LcarsPaddStatusStrip(
    modifier: Modifier = Modifier,
    label: String? = null,
    primaryColor: Color = LocalLcarsColors.current.a1,
    capColor: Color = LocalLcarsColors.current.violet,
) {
    val colors = LocalLcarsColors.current
    val metrics = LocalLcarsPhonePaddMetrics.current

    Row(
        modifier = modifier.height(metrics.stripHeight),
        horizontalArrangement = Arrangement.spacedBy(metrics.gap),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .width(metrics.stripHeight)
                .fillMaxHeight()
                .clip(RoundedCornerShape(percent = 50))
                .background(capColor),
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(primaryColor),
            contentAlignment = Alignment.CenterEnd,
        ) {
            if (!label.isNullOrBlank()) {
                LcarsText(
                    text = label,
                    modifier = Modifier
                        .background(colors.background)
                        .padding(horizontal = metrics.gap),
                    style = LocalLcarsTypography.current.labelSmall.copy(
                        color = colors.a4,
                        textAlign = TextAlign.End,
                    ),
                    maxLines = 1,
                    minFontSize = 8.sp,
                )
            }
        }
        Box(
            modifier = Modifier
                .width(metrics.stripHeight)
                .fillMaxHeight()
                .clip(RoundedCornerShape(percent = 50))
                .background(capColor),
        )
    }
}

@Composable
fun LcarsPaddDataLines(
    lines: List<String>,
    modifier: Modifier = Modifier,
    color: Color = LocalLcarsColors.current.text,
    maxLines: Int = lines.size,
) {
    val typography = LocalLcarsTypography.current
    val metrics = LocalLcarsPhonePaddMetrics.current

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy((metrics.gap / 2).coerceAtLeast(1.dp)),
    ) {
        lines.take(maxLines).forEach {
            BasicText(
                text = lcarsLabel(it),
                modifier = Modifier.fillMaxWidth(),
                style = typography.telemetry.copy(
                    color = color,
                    textAlign = TextAlign.Start,
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
fun LcarsPaddMessage(
    text: String,
    modifier: Modifier = Modifier,
    alert: Boolean = false,
) {
    val colors = LocalLcarsColors.current
    val borderColor = if (alert) colors.alertRed else colors.a1
    val textColor = if (alert) colors.alertRed else colors.a1

    Box(
        modifier = modifier
            .background(colors.panel)
            .border(2.dp, borderColor)
            .padding(12.dp),
        contentAlignment = Alignment.Center,
    ) {
        BasicText(
            text = lcarsLabel(text),
            style = LocalLcarsTypography.current.header.copy(
                color = textColor,
                textAlign = TextAlign.Center,
                fontSize = 34.sp,
                lineHeight = 36.sp,
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
