package com.lcars.ui.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lcars.ui.foundation.LcarsText
import com.lcars.ui.theme.LocalLcarsColors
import com.lcars.ui.theme.LocalLcarsSpacing
import com.lcars.ui.theme.LocalLcarsTypography

/**
 * Standard LCARS Arch / C-Frame Layout Component.
 * The signature Star Trek interface enclosure featuring:
 * - Top-Left large elbow arch with system identification code
 * - Horizontal header beam with title and right end-cap
 * - Vertical command pillar / sidebar area
 * - Bottom-Left large elbow arch with operational telemetry
 * - Horizontal footer beam with status and deck metrics
 * - Central viewport content area
 */
@Composable
fun LcarsStandardFrame(
    title: String,
    modifier: Modifier = Modifier,
    headerCode: String = "LCARS 47",
    footerCode: String = "SYS 01",
    statusText: String = "SYSTEM NORMAL // ALL DECKS READY",
    frameColor: Color = LocalLcarsColors.current.framePrimary,
    accentColor: Color = LocalLcarsColors.current.monoAmber,
    leftRailWidth: Dp = 150.dp,
    headerHeight: Dp = 32.dp,
    footerHeight: Dp = 28.dp,
    gap: Dp = LocalLcarsSpacing.current.gapStandard,
    leftRailContent: (@Composable ColumnScope.() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit,
) {
    val colors = LocalLcarsColors.current
    val typography = LocalLcarsTypography.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
            .padding(gap),
        verticalArrangement = Arrangement.spacedBy(gap),
    ) {
        // TOP ARCH / HEADER ROW
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(headerHeight),
            horizontalArrangement = Arrangement.spacedBy(gap),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Top-Left Elbow Arch
            Box(
                modifier = Modifier
                    .width(leftRailWidth)
                    .fillMaxHeight()
                    .background(
                        color = frameColor,
                        shape = RoundedCornerShape(topStart = headerHeight),
                    )
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                contentAlignment = Alignment.BottomEnd,
            ) {
                LcarsText(
                    text = headerCode.uppercase(),
                    style = typography.labelSmall.copy(
                        fontSize = 11.sp,
                        lineHeight = 12.sp,
                        color = Color.Black,
                    ),
                )
            }

            // Top Header Bar
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(frameColor)
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                LcarsText(
                    text = title.uppercase(),
                    style = typography.titleSmall.copy(
                        fontSize = 14.sp,
                        lineHeight = 16.sp,
                        color = Color.Black,
                    ),
                )
                LcarsText(
                    text = "PRIMARY DISPLAY",
                    style = typography.labelSmall.copy(
                        fontSize = 10.sp,
                        color = Color.Black,
                    ),
                )
            }

            // Top Right Pill Cap
            Box(
                modifier = Modifier
                    .width(36.dp)
                    .fillMaxHeight()
                    .background(
                        color = accentColor,
                        shape = RoundedCornerShape(topEnd = headerHeight / 2, bottomEnd = headerHeight / 2),
                    ),
            )
        }

        // MIDDLE VIEWPORT & SIDEBAR ROW
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(gap),
        ) {
            if (leftRailContent != null) {
                Column(
                    modifier = Modifier
                        .width(leftRailWidth)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(gap),
                    content = leftRailContent,
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                content = content,
            )
        }

        // BOTTOM ARCH / FOOTER ROW
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(footerHeight),
            horizontalArrangement = Arrangement.spacedBy(gap),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Bottom-Left Elbow Arch
            Box(
                modifier = Modifier
                    .width(leftRailWidth)
                    .fillMaxHeight()
                    .background(
                        color = frameColor,
                        shape = RoundedCornerShape(bottomStart = footerHeight),
                    )
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                contentAlignment = Alignment.TopEnd,
            ) {
                LcarsText(
                    text = footerCode.uppercase(),
                    style = typography.labelSmall.copy(
                        fontSize = 11.sp,
                        lineHeight = 12.sp,
                        color = Color.Black,
                    ),
                )
            }

            // Bottom Footer Bar
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(frameColor)
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                LcarsText(
                    text = statusText.uppercase(),
                    style = typography.labelSmall.copy(
                        fontSize = 10.sp,
                        color = Color.Black,
                    ),
                )
                LcarsText(
                    text = "FUNCTION VERIFIED",
                    style = typography.labelSmall.copy(
                        fontSize = 10.sp,
                        color = Color.Black,
                    ),
                )
            }

            // Bottom Right Pill Cap
            Box(
                modifier = Modifier
                    .width(48.dp)
                    .fillMaxHeight()
                    .background(
                        color = colors.commandPrimary,
                        shape = RoundedCornerShape(topEnd = footerHeight / 2, bottomEnd = footerHeight / 2),
                    ),
            )
        }
    }
}

/**
 * Standard LCARS Focus Screen / Viewport Frame.
 * Encloses a central display screen with classic dual-ended capped bars and high-density telemetry.
 */
@Composable
fun LcarsFocusScreenFrame(
    headerLabel: String,
    footerLabel: String,
    modifier: Modifier = Modifier,
    frameColor: Color = LocalLcarsColors.current.framePrimary,
    accentColor: Color = LocalLcarsColors.current.auxiliaryTan,
    barHeight: Dp = 24.dp,
    gap: Dp = LocalLcarsSpacing.current.gapStandard,
    content: @Composable BoxScope.() -> Unit,
) {
    val colors = LocalLcarsColors.current
    val typography = LocalLcarsTypography.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
            .padding(gap),
        verticalArrangement = Arrangement.spacedBy(gap),
    ) {
        // TOP FRAME BAR
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(barHeight),
            horizontalArrangement = Arrangement.spacedBy(gap),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .width(32.dp)
                    .fillMaxHeight()
                    .background(
                        color = accentColor,
                        shape = RoundedCornerShape(topStart = barHeight / 2, bottomStart = barHeight / 2),
                    ),
            )
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(frameColor)
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                LcarsText(
                    text = headerLabel.uppercase(),
                    style = typography.titleSmall.copy(
                        fontSize = 13.sp,
                        color = Color.Black,
                    ),
                )
                LcarsText(
                    text = "AUTO-LINK ACTIVE",
                    style = typography.labelSmall.copy(
                        fontSize = 10.sp,
                        color = Color.Black,
                    ),
                )
            }
            Box(
                modifier = Modifier
                    .width(48.dp)
                    .fillMaxHeight()
                    .background(
                        color = colors.commandPrimary,
                        shape = RoundedCornerShape(topEnd = barHeight / 2, bottomEnd = barHeight / 2),
                    ),
            )
        }

        // CENTRAL SCREEN VIEWPORT
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            content = content,
        )

        // BOTTOM FRAME BAR
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(barHeight),
            horizontalArrangement = Arrangement.spacedBy(gap),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .width(48.dp)
                    .fillMaxHeight()
                    .background(
                        color = colors.commandPrimary,
                        shape = RoundedCornerShape(topStart = barHeight / 2, bottomStart = barHeight / 2),
                    ),
            )
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(frameColor)
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                LcarsText(
                    text = footerLabel.uppercase(),
                    style = typography.labelSmall.copy(
                        fontSize = 10.sp,
                        color = Color.Black,
                    ),
                )
                LcarsText(
                    text = "DISPLAY SYNC 60HZ",
                    style = typography.labelSmall.copy(
                        fontSize = 10.sp,
                        color = Color.Black,
                    ),
                )
            }
            Box(
                modifier = Modifier
                    .width(32.dp)
                    .fillMaxHeight()
                    .background(
                        color = accentColor,
                        shape = RoundedCornerShape(topEnd = barHeight / 2, bottomEnd = barHeight / 2),
                    ),
            )
        }
    }
}
