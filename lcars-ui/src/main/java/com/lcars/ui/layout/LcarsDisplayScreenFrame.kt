package com.lcars.ui.layout

import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lcars.ui.foundation.LcarsText
import com.lcars.ui.theme.LocalLcarsColors
import com.lcars.ui.theme.LocalLcarsSpacing
import com.lcars.ui.theme.LocalLcarsTypography

/**
 * Authentic Star Trek Master Display Screen Frame (as seen in Sickbay, Briefing Room, Science Station).
 *
 * Core signature geometry:
 * 1. Left compound Dual-Elbow Arch (Top Elbow curved into upper runner, Lower Elbow curved into lower runner).
 * 2. Double horizontal runner bars traversing across the middle-upper screen with colored accent chips.
 * 3. Upper deck telemetry area (Large gold header, 2x2 control pills, dense number matrix).
 * 4. Lower main graphic display viewport (Enclosed display reading photos, 3D meshes, and technical schematics).
 */
@Composable
fun LcarsDisplayScreenFrame(
    title: String,
    modifier: Modifier = Modifier,
    headerCode: String = "LCARS 23295",
    subHeaderCode: String = "PATIENT",
    topRightPills: (@Composable () -> Unit)? = null,
    topTelemetryContent: (@Composable () -> Unit)? = null,
    leftRailWidth: Dp = 160.dp,
    topDeckHeight: Dp = 110.dp,
    runnerHeight: Dp = 14.dp,
    runnerGap: Dp = 4.dp,
    archColor: Color = LocalLcarsColors.current.frameSecondary,     // Lavender / Light Purple
    topRunnerColor: Color = LocalLcarsColors.current.lightBlue,     // Pale Blue
    bottomRunnerColor: Color = LocalLcarsColors.current.monoAmber,  // Amber / Gold
    leftRailContent: @Composable ColumnScope.() -> Unit,
    content: @Composable BoxScope.() -> Unit,
) {
    val colors = LocalLcarsColors.current
    val typography = LocalLcarsTypography.current
    val spacing = LocalLcarsSpacing.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
            .padding(spacing.gapStandard),
    ) {
        // ==========================================
        // 1. TOP SECTION (Left Arch Top + Top Deck)
        // ==========================================
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(topDeckHeight),
            horizontalArrangement = Arrangement.spacedBy(spacing.gapStandard),
        ) {
            // Top-Left Elbow Pillar Block
            Column(
                modifier = Modifier
                    .width(leftRailWidth)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(spacing.gapStandard),
            ) {
                // Topmost arch block
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp)
                        .background(archColor)
                        .padding(horizontal = 8.dp, vertical = 2.dp),
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
                // Sub-header block
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(28.dp)
                        .background(colors.auxiliaryTan)
                        .padding(horizontal = 8.dp, vertical = 2.dp),
                    contentAlignment = Alignment.BottomEnd,
                ) {
                    LcarsText(
                        text = subHeaderCode.uppercase(),
                        style = typography.labelSmall.copy(
                            fontSize = 10.sp,
                            lineHeight = 11.sp,
                            color = Color.Black,
                        ),
                    )
                }
                // Curved arch drop transitioning into upper runner
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                ) {
                    val w = size.width
                    val h = size.height
                    val r = h.coerceAtMost(w)
                    val path = Path().apply {
                        moveTo(0f, 0f)
                        lineTo(w, 0f)
                        lineTo(w, h)
                        lineTo(w - r, h)
                        arcTo(
                            rect = Rect(w - r * 2, h - r * 2, w, h),
                            startAngleDegrees = 90f,
                            sweepAngleDegrees = 90f,
                            forceMoveTo = false,
                        )
                        lineTo(0f, 0f)
                        close()
                    }
                    drawPath(path = path, color = archColor)
                }
            }

            // Top-Right Deck (Title + Control Pills + Dense Number Matrix)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                // Header row: Title + Right Control Pills
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top,
                ) {
                    LcarsText(
                        text = title.uppercase(),
                        style = typography.header.copy(
                            fontSize = 24.sp,
                            letterSpacing = 1.sp,
                            color = colors.monoAmber,
                        ),
                        modifier = Modifier.padding(top = 2.dp),
                    )

                    if (topRightPills != null) {
                        topRightPills()
                    }
                }

                // Middle/Lower Telemetry area
                if (topTelemetryContent != null) {
                    topTelemetryContent()
                }
            }
        }

        // =========================================================================
        // 2. DOUBLE HORIZONTAL RUNNER BARS (The Signature LCARS Display Screen Beam)
        // =========================================================================
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = spacing.gapStandard),
            verticalArrangement = Arrangement.spacedBy(runnerGap),
        ) {
            // Upper Runner Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(runnerHeight),
                horizontalArrangement = Arrangement.spacedBy(spacing.gapStandard),
            ) {
                // Left Arch connection cap
                Box(
                    modifier = Modifier
                        .width(leftRailWidth)
                        .fillMaxHeight()
                        .background(
                            color = archColor,
                            shape = RoundedCornerShape(bottomStart = runnerHeight),
                        ),
                )
                // Upper Runner segmented tracks
                Box(
                    modifier = Modifier
                        .weight(0.45f)
                        .fillMaxHeight()
                        .background(topRunnerColor),
                )
                Box(
                    modifier = Modifier
                        .width(24.dp)
                        .fillMaxHeight()
                        .background(colors.monoAmber),
                )
                Box(
                    modifier = Modifier
                        .weight(0.35f)
                        .fillMaxHeight()
                        .background(topRunnerColor),
                )
                Box(
                    modifier = Modifier
                        .width(18.dp)
                        .fillMaxHeight()
                        .background(colors.commandSecondary),
                )
                Box(
                    modifier = Modifier
                        .weight(0.20f)
                        .fillMaxHeight()
                        .background(
                            color = colors.frameSecondary,
                            shape = RoundedCornerShape(topEnd = runnerHeight / 2, bottomEnd = runnerHeight / 2),
                        ),
                )
            }

            // Lower Runner Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(runnerHeight),
                horizontalArrangement = Arrangement.spacedBy(spacing.gapStandard),
            ) {
                // Left Arch lower curve joint
                Box(
                    modifier = Modifier
                        .width(leftRailWidth)
                        .fillMaxHeight()
                        .background(
                            color = bottomRunnerColor,
                            shape = RoundedCornerShape(topStart = runnerHeight),
                        ),
                )
                // Lower Runner segmented tracks
                Box(
                    modifier = Modifier
                        .weight(0.30f)
                        .fillMaxHeight()
                        .background(bottomRunnerColor),
                )
                Box(
                    modifier = Modifier
                        .width(18.dp)
                        .fillMaxHeight()
                        .background(colors.auxiliaryTan),
                )
                Box(
                    modifier = Modifier
                        .weight(0.50f)
                        .fillMaxHeight()
                        .background(bottomRunnerColor),
                )
                Box(
                    modifier = Modifier
                        .width(36.dp)
                        .fillMaxHeight()
                        .background(colors.alertRed),
                )
                Box(
                    modifier = Modifier
                        .weight(0.20f)
                        .fillMaxHeight()
                        .background(
                            color = colors.framePrimary,
                            shape = RoundedCornerShape(topEnd = runnerHeight / 2, bottomEnd = runnerHeight / 2),
                        ),
                )
            }
        }

        // =========================================================================
        // 3. MAIN LOWER DECK (Left Vertical Control Column + Main Graphic Viewport)
        // =========================================================================
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(spacing.gapStandard),
        ) {
            // Left Command & Selector Column
            Column(
                modifier = Modifier
                    .width(leftRailWidth)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(spacing.gapStandard),
                content = leftRailContent,
            )

            // Central Main Graphic & Schematics Viewport
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                content = content,
            )
        }
    }
}
