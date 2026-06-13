package com.lcars.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.unit.dp

@Composable
fun LcarsAppScaffold(
    title: String,
    modifier: Modifier = Modifier,
    footerLabel: String? = null,
    controls: @Composable ColumnScope.() -> Unit = {},
    content: @Composable ColumnScope.() -> Unit,
) {
    val colors = LocalLcarsColors.current
    val gap = LocalLcarsSpacing.current.gapStandard

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
            .padding(gap),
        verticalArrangement = Arrangement.spacedBy(gap),
    ) {
        LcarsBar(
            color = colors.lightBlue,
            height = LocalLcarsSpacing.current.barHeight + 4.dp,
            startCap = true,
            endCap = true,
            label = title,
            labelAlign = LcarsLabelAlign.End,
            labelColor = colors.a1,
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(gap),
        ) {
            Column(
                modifier = Modifier.width(LocalLcarsSpacing.current.scaffoldControlWidth),
                verticalArrangement = Arrangement.spacedBy(gap),
                content = controls,
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(gap),
                content = content,
            )
        }
        if (footerLabel != null) {
            LcarsBar(
                color = colors.butterscotch,
                height = (LocalLcarsSpacing.current.barHeight / 2).coerceAtLeast(8.dp),
                endCap = true,
                label = footerLabel,
                labelAlign = LcarsLabelAlign.End,
                labelColor = colors.a1,
            )
        }
    }
}

@Composable
fun LcarsPaddScaffold(
    title: String,
    modifier: Modifier = Modifier,
    footerLabel: String? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val colors = LocalLcarsColors.current
    val gap = LocalLcarsSpacing.current.gapStandard

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
            .padding(gap),
        verticalArrangement = Arrangement.spacedBy(gap),
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            LcarsElbow(
                text = "padd",
                color = colors.monoAmber,
                direction = LcarsElbowDirection.TopLeft,
                wingWidth = 112.dp,
                wingHeight = 60.dp,
                thickness = LocalLcarsSpacing.current.elbowThickness.coerceAtMost(32.dp),
            )
            Spacer(modifier = Modifier.width(gap))
            LcarsBar(
                color = colors.lightBlue,
                height = LocalLcarsSpacing.current.barHeight + 2.dp,
                endCap = true,
                label = title,
                labelAlign = LcarsLabelAlign.End,
                labelColor = colors.a1,
                modifier = Modifier.weight(1f),
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(gap),
            content = content,
        )
        if (footerLabel != null) {
            LcarsBar(
                color = colors.a7,
                height = LocalLcarsSpacing.current.barHeight,
                endCap = true,
                label = footerLabel,
                labelAlign = LcarsLabelAlign.End,
                labelColor = colors.a1,
            )
        }
    }
}

@Composable
fun LcarsConsoleScaffold(
    modifier: Modifier = Modifier,
    leftWingContent: @Composable ColumnScope.() -> Unit,
    mainDeckContent: @Composable ColumnScope.() -> Unit,
) {
    LcarsMainConsole(
        modifier = modifier,
        leftWingContent = leftWingContent,
        mainDeckContent = mainDeckContent,
    )
}

@Composable
fun LcarsFramePanel(
    title: String,
    modifier: Modifier = Modifier,
    footerLabel: String? = null,
    titleAlign: LcarsLabelAlign = LcarsLabelAlign.Start,
    content: @Composable ColumnScope.() -> Unit,
) {
    val colors = LocalLcarsColors.current
    val gap = LocalLcarsSpacing.current.gapStandard

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clipToBounds()
            .background(colors.background)
            .padding(gap),
        verticalArrangement = Arrangement.spacedBy(gap),
    ) {
        LcarsBar(
            color = colors.violet,
            height = LocalLcarsSpacing.current.barHeight.coerceAtMost(20.dp),
            startCap = true,
            label = title,
            labelAlign = titleAlign,
            labelColor = colors.a1,
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(gap),
            content = content,
        )
        if (footerLabel != null) {
            LcarsBar(
                color = colors.butterscotch,
                height = (LocalLcarsSpacing.current.barHeight / 3).coerceAtLeast(6.dp),
                endCap = true,
                label = footerLabel,
                labelAlign = LcarsLabelAlign.End,
                labelColor = colors.a1,
            )
        }
    }
}

@Composable
internal fun LcarsPanelSurface(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(LocalLcarsColors.current.panel),
        content = { content() },
    )
}
