package com.lcars.ui.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.BoxScope
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
import com.lcars.ui.foundation.LcarsText
import com.lcars.ui.theme.LocalLcarsColors
import com.lcars.ui.theme.LocalLcarsSpacing
import com.lcars.ui.theme.LocalLcarsTypography

/**
 * Stage 9 Double Bracket / Elbow Enclosure Frame.
 * Designed for landscape workstation consoles (Sickbay, Engineering, Bridge).
 * Features top/bottom elbows, horizontal runners, left sidebar slots, and central viewport.
 */
@Composable
fun LcarsBracketFrame(
    title: String,
    modifier: Modifier = Modifier,
    frameColor: Color = LocalLcarsColors.current.framePrimary,
    accentColor: Color = LocalLcarsColors.current.monoAmber,
    leftRailWidth: Dp = 140.dp,
    barHeight: Dp = 24.dp,
    dividerGap: Dp = 4.dp,
    cornerThickness: Dp = 32.dp,
    topEndCapText: String = "",
    bottomEndCapText: String = "",
    leftSidebarContent: @Composable ColumnScope.() -> Unit = {},
    rightSidebarContent: (@Composable ColumnScope.() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit,
) {
    val colors = LocalLcarsColors.current
    val typography = LocalLcarsTypography.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(8.dp)
    ) {
        // TOP FRAME BAR & ELBOW
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(cornerThickness),
            verticalAlignment = Alignment.Top,
        ) {
            // Top-Left Elbow Head
            Box(
                modifier = Modifier
                    .width(leftRailWidth)
                    .fillMaxHeight()
                    .background(
                        color = frameColor,
                        shape = RoundedCornerShape(topStart = cornerThickness)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                LcarsText(
                    text = "LCARS 47",
                    style = typography.labelSmall.copy(color = Color.Black)
                )
            }

            Spacer(modifier = Modifier.width(dividerGap))

            // Top Horizontal Bar (Title Area)
            Row(
                modifier = Modifier
                    .weight(1f)
                    .height(barHeight)
                    .background(frameColor)
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                LcarsText(
                    text = title.uppercase(),
                    style = typography.titleSmall.copy(color = Color.Black)
                )

                if (topEndCapText.isNotEmpty()) {
                    LcarsText(
                        text = topEndCapText.uppercase(),
                        style = typography.labelSmall.copy(color = Color.Black)
                    )
                }
            }

            Spacer(modifier = Modifier.width(dividerGap))

            // Top-Right Cap
            Box(
                modifier = Modifier
                    .width(36.dp)
                    .height(barHeight)
                    .background(
                        color = accentColor,
                        shape = RoundedCornerShape(topEnd = barHeight / 2, bottomEnd = barHeight / 2)
                    )
            )
        }

        Spacer(modifier = Modifier.height(dividerGap))

        // CENTER BODY (Left Sidebar + Content + Optional Right Sidebar)
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            // Left Spine Column
            Column(
                modifier = Modifier
                    .width(leftRailWidth)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(dividerGap)
            ) {
                leftSidebarContent()
            }

            Spacer(modifier = Modifier.width(dividerGap))

            // Main Central Viewport
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(Color.Black)
            ) {
                content()
            }

            if (rightSidebarContent != null) {
                Spacer(modifier = Modifier.width(dividerGap))
                Column(
                    modifier = Modifier
                        .width(120.dp)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(dividerGap)
                ) {
                    rightSidebarContent()
                }
            }
        }

        Spacer(modifier = Modifier.height(dividerGap))

        // BOTTOM FRAME BAR & INVERTED ELBOW
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(cornerThickness),
            verticalAlignment = Alignment.Bottom,
        ) {
            // Bottom-Left Elbow
            Box(
                modifier = Modifier
                    .width(leftRailWidth)
                    .fillMaxHeight()
                    .background(
                        color = frameColor,
                        shape = RoundedCornerShape(bottomStart = cornerThickness)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                contentAlignment = Alignment.TopEnd
            ) {
                LcarsText(
                    text = "SYS 02",
                    style = typography.labelSmall.copy(color = Color.Black)
                )
            }

            Spacer(modifier = Modifier.width(dividerGap))

            // Bottom Horizontal Bar
            Row(
                modifier = Modifier
                    .weight(1f)
                    .height(barHeight)
                    .background(frameColor)
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                if (bottomEndCapText.isNotEmpty()) {
                    LcarsText(
                        text = bottomEndCapText.uppercase(),
                        style = typography.labelSmall.copy(color = Color.Black)
                    )
                } else {
                    Spacer(modifier = Modifier.width(1.dp))
                }

                LcarsText(
                    text = "ENTERPRISE-D • DECK 1",
                    style = typography.labelSmall.copy(color = Color.Black)
                )
            }

            Spacer(modifier = Modifier.width(dividerGap))

            // Bottom-Right Cap
            Box(
                modifier = Modifier
                    .width(36.dp)
                    .height(barHeight)
                    .background(
                        color = accentColor,
                        shape = RoundedCornerShape(topEnd = barHeight / 2, bottomEnd = barHeight / 2)
                    )
            )
        }
    }
}
