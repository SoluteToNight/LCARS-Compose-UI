package com.lcars.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lcars.ui.LcarsAlertBanner
import com.lcars.ui.LcarsBar
import com.lcars.ui.LcarsButton
import com.lcars.ui.LcarsButtonShape
import com.lcars.ui.LcarsCommandRail
import com.lcars.ui.LcarsCommandRailItem
import com.lcars.ui.LcarsCommandRailItemType
import com.lcars.ui.LcarsCommandRailSide
import com.lcars.ui.LcarsDataRow
import com.lcars.ui.LcarsDataTable
import com.lcars.ui.LcarsDialog
import com.lcars.ui.LcarsDividerGrid
import com.lcars.ui.LcarsDividerGridType
import com.lcars.ui.LcarsElbow
import com.lcars.ui.LcarsElbowDirection
import com.lcars.ui.LcarsFramePanel
import com.lcars.ui.LcarsInspectBracket
import com.lcars.ui.LcarsLabelAlign
import com.lcars.ui.LcarsLogConsole
import com.lcars.ui.LcarsLogEntry
import com.lcars.ui.LcarsLogSeverity
import com.lcars.ui.LcarsMainConsole
import com.lcars.ui.LcarsNumberMatrix
import com.lcars.ui.LcarsNumericLabel
import com.lcars.ui.LcarsProgressBar
import com.lcars.ui.LcarsReadoutTicker
import com.lcars.ui.LcarsResponsiveScaffold
import com.lcars.ui.LcarsScannerSweep
import com.lcars.ui.LcarsSegmentedControl
import com.lcars.ui.LcarsSegmentedMeter
import com.lcars.ui.LcarsStarChart
import com.lcars.ui.LcarsStarChartMode
import com.lcars.ui.LcarsStarCoords
import com.lcars.ui.LcarsStatusLight
import com.lcars.ui.LcarsStyle
import com.lcars.ui.LcarsTargetScanner
import com.lcars.ui.LcarsTelemetryEntry
import com.lcars.ui.LcarsTelemetryPanel
import com.lcars.ui.LcarsTelemetryStatus
import com.lcars.ui.LcarsToggle
import com.lcars.ui.LcarsTransmissionFrame
import com.lcars.ui.LcarsAdaptiveTheme
import com.lcars.ui.colors
import com.lcars.ui.LocalLcarsColors
import com.lcars.ui.LocalLcarsSpacing
import com.lcars.ui.LocalLcarsTypography

private enum class DemoPage {
    Console,
    Catalog,
    StyleShowcase,
}

@Composable
fun LcarsDemoScreen(modifier: Modifier = Modifier) {
    var systemAlert by rememberSaveable { mutableStateOf(false) }
    var selectedPage by rememberSaveable { mutableStateOf(DemoPage.Console.name) }

    if (selectedPage == DemoPage.Catalog.name) {
        LcarsComponentCatalogScreen(
            onShowConsole = { selectedPage = DemoPage.Console.name },
            modifier = modifier,
        )
        return
    }
    if (selectedPage == DemoPage.StyleShowcase.name) {
        StyleShowcaseRoute(
            systemAlert = systemAlert,
            onToggleAlert = { systemAlert = !systemAlert },
            onShowConsole = { selectedPage = DemoPage.Console.name },
            onShowCatalog = { selectedPage = DemoPage.Catalog.name },
            modifier = modifier,
        )
        return
    }

    LcarsResponsiveScaffold(
        modifier = modifier,
        portrait = {
            LcarsDemoPortrait(
                systemAlert = systemAlert,
                onToggleAlert = { systemAlert = !systemAlert },
                onShowCatalog = { selectedPage = DemoPage.Catalog.name },
                onShowStyleShowcase = { selectedPage = DemoPage.StyleShowcase.name },
                modifier = Modifier.fillMaxSize(),
            )
        },
        compactLandscape = {
            LcarsDemoCompactLandscape(
                systemAlert = systemAlert,
                onToggleAlert = { systemAlert = !systemAlert },
                onShowCatalog = { selectedPage = DemoPage.Catalog.name },
                onShowStyleShowcase = { selectedPage = DemoPage.StyleShowcase.name },
                modifier = Modifier.fillMaxSize(),
            )
        },
        wideLandscape = {
            LcarsDemoLandscape(
                systemAlert = systemAlert,
                onToggleAlert = { systemAlert = !systemAlert },
                onShowCatalog = { selectedPage = DemoPage.Catalog.name },
                onShowStyleShowcase = { selectedPage = DemoPage.StyleShowcase.name },
                modifier = Modifier.fillMaxSize(),
            )
        },
    )
}

@Composable
private fun LcarsDemoLandscape(
    systemAlert: Boolean,
    onToggleAlert: () -> Unit,
    onShowCatalog: () -> Unit,
    onShowStyleShowcase: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalLcarsColors.current
    val spacing = LocalLcarsSpacing.current
    LcarsMainConsole(
        modifier = modifier,
        leftWingContent = {
            MainCommandRail(
                systemAlert = systemAlert,
                onToggleAlert = onToggleAlert,
                onShowCatalog = onShowCatalog,
                onShowStyleShowcase = onShowStyleShowcase,
                compact = false,
                modifier = Modifier.fillMaxHeight(),
            )
        },
        mainDeckContent = {
            LcarsBar(
                color = colors.lightBlue,
                height = 28.dp,
                startCap = true,
                endCap = true,
                label = "telemetry main deck",
                labelAlign = LcarsLabelAlign.End,
                labelColor = colors.a1,
            )
            LcarsTelemetryPanel(
                title = "primary telemetry matrix",
                alerting = systemAlert,
                entries = telemetryEntries(systemAlert),
            )
            LcarsBar(
                color = colors.violet,
                height = 20.dp,
                startCap = true,
                label = "ui component catalogue",
                labelAlign = LcarsLabelAlign.Start,
                labelColor = colors.a1,
            )
            ComponentShowcase(
                alerting = systemAlert,
                onToggleAlert = onToggleAlert,
            )
            LcarsScannerSweep(
                running = true,
                color = if (systemAlert) colors.alertRed else colors.violet,
                sweepColor = if (systemAlert) colors.monoAmber else colors.lightBlue,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(86.dp),
            )
            LcarsBar(
                color = colors.a7,
                height = 10.dp,
                endCap = true,
            )
            LogPanel(
                alerting = systemAlert,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(top = spacing.gapStandard),
            )
        },
    )
}

@Composable
private fun LcarsDemoCompactLandscape(
    systemAlert: Boolean,
    onToggleAlert: () -> Unit,
    onShowCatalog: () -> Unit,
    onShowStyleShowcase: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalLcarsColors.current
    val spacing = LocalLcarsSpacing.current
    BoxWithConstraints(
        modifier = modifier
            .background(colors.background)
            .padding(spacing.gapStandard),
    ) {
        val lowHeight = maxHeight < 420.dp
        val railWidth = if (lowHeight) 136.dp else 150.dp
        val commandHeight = if (lowHeight) 34.dp else 40.dp
        val deckBarHeight = if (lowHeight) 18.dp else 22.dp
        val bottomBarHeight = if (lowHeight) 6.dp else 8.dp
        val visibleTelemetry = telemetryEntries(systemAlert).let { entries ->
            if (lowHeight) entries.take(4) else entries
        }

        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(spacing.gapStandard),
        ) {
            MainCommandRail(
                systemAlert = systemAlert,
                onToggleAlert = onToggleAlert,
                onShowCatalog = onShowCatalog,
                onShowStyleShowcase = onShowStyleShowcase,
                compact = true,
                modifier = Modifier
                    .width(railWidth)
                    .fillMaxHeight(),
            )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(spacing.gapStandard),
        ) {
            LcarsBar(
                color = colors.lightBlue,
                height = deckBarHeight,
                startCap = true,
                endCap = true,
                label = "telemetry deck",
                labelAlign = LcarsLabelAlign.End,
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(spacing.gapStandard),
            ) {
                LcarsTelemetryPanel(
                    title = "primary telemetry matrix",
                    alerting = systemAlert,
                    entries = visibleTelemetry,
                    singleColumnBelow = 0.dp,
                    modifier = Modifier
                        .weight(0.62f)
                        .fillMaxHeight(),
                )
                Column(
                    modifier = Modifier
                        .weight(0.38f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(spacing.gapStandard),
                ) {
                    LcarsButton(
                        text = when {
                            systemAlert -> "danger active"
                            lowHeight -> "trigger flash"
                            else -> "trigger system flash"
                        },
                        color = colors.monoAmber,
                        shape = LcarsButtonShape.Pill,
                        alerting = systemAlert,
                        minWidth = 0.dp,
                        minHeight = commandHeight,
                        modifier = Modifier.fillMaxWidth(),
                        onClick = onToggleAlert,
                    )
                    LcarsScannerSweep(
                        running = true,
                        color = if (systemAlert) colors.alertRed else colors.violet,
                        sweepColor = if (systemAlert) colors.monoAmber else colors.lightBlue,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                    )
                    LogPanel(
                        alerting = systemAlert,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = if (lowHeight) 52.dp else 68.dp),
                        maxLines = if (lowHeight) 2 else 4,
                    )
                }
            }
            LcarsBar(
                color = colors.a7,
                height = bottomBarHeight,
                endCap = true,
            )
        }
        }
    }
}

@Composable
private fun StyleShowcaseRoute(
    systemAlert: Boolean,
    onToggleAlert: () -> Unit,
    onShowConsole: () -> Unit,
    onShowCatalog: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var selectedStyleName by rememberSaveable { mutableStateOf(LcarsStyle.ClassicUltra.name) }
    val selectedStyle = LcarsStyle.valueOf(selectedStyleName)

    LcarsAdaptiveTheme(style = selectedStyle) {
        LcarsStyleShowcaseScreen(
            selectedStyle = selectedStyle,
            onStyleSelected = { selectedStyleName = it.name },
            systemAlert = systemAlert,
            onToggleAlert = onToggleAlert,
            onShowConsole = onShowConsole,
            onShowCatalog = onShowCatalog,
            modifier = modifier,
        )
    }
}

@Composable
private fun LcarsStyleShowcaseScreen(
    selectedStyle: LcarsStyle,
    onStyleSelected: (LcarsStyle) -> Unit,
    systemAlert: Boolean,
    onToggleAlert: () -> Unit,
    onShowConsole: () -> Unit,
    onShowCatalog: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalLcarsColors.current
    val spacing = LocalLcarsSpacing.current
    val scrollState = rememberScrollState()
    val spec = selectedStyle.showcaseSpec()

    Column(
        modifier = modifier
            .background(colors.background)
            .verticalScroll(scrollState)
            .padding(spacing.gapStandard),
        verticalArrangement = Arrangement.spacedBy(spacing.gapLarge),
    ) {
        LcarsStyleShowcaseHeader(
            spec = spec,
            systemAlert = systemAlert,
            onToggleAlert = onToggleAlert,
            onShowConsole = onShowConsole,
            onShowCatalog = onShowCatalog,
        )
        LcarsStyleSelector(
            selectedStyle = selectedStyle,
            onStyleSelected = onStyleSelected,
        )
        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            val stacked = maxWidth < 760.dp
            if (stacked) {
                Column(verticalArrangement = Arrangement.spacedBy(spacing.gapLarge)) {
                    LcarsStyleGeometryPanel(spec, onToggleAlert = onToggleAlert, onShowCatalog = onShowCatalog)
                    LcarsStyleEffectsPanel(spec, systemAlert = systemAlert)
                    LcarsStyleColorPanel(spec)
                    LcarsStyleStatusPanel(spec, systemAlert = systemAlert)
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(spacing.gapLarge),
                ) {
                    Column(
                        modifier = Modifier.weight(0.46f),
                        verticalArrangement = Arrangement.spacedBy(spacing.gapLarge),
                    ) {
                        LcarsStyleGeometryPanel(spec, onToggleAlert = onToggleAlert, onShowCatalog = onShowCatalog)
                        LcarsStyleStatusPanel(spec, systemAlert = systemAlert)
                    }
                    Column(
                        modifier = Modifier.weight(0.54f),
                        verticalArrangement = Arrangement.spacedBy(spacing.gapLarge),
                    ) {
                        LcarsStyleEffectsPanel(spec, systemAlert = systemAlert)
                        LcarsStyleColorPanel(spec)
                    }
                }
            }
        }
        LcarsBar(
            color = colors.butterscotch,
            height = 12.dp,
            endCap = true,
            label = spec.reference,
            labelAlign = LcarsLabelAlign.End,
            labelColor = colors.a1,
        )
    }
}

@Composable
private fun LcarsStyleShowcaseHeader(
    spec: StyleShowcaseSpec,
    systemAlert: Boolean,
    onToggleAlert: () -> Unit,
    onShowConsole: () -> Unit,
    onShowCatalog: () -> Unit,
) {
    val colors = LocalLcarsColors.current
    val spacing = LocalLcarsSpacing.current

    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val compact = maxWidth < 680.dp
        if (compact) {
            Column(verticalArrangement = Arrangement.spacedBy(spacing.gapStandard)) {
                LcarsBar(
                    color = colors.lightBlue,
                    height = 28.dp,
                    startCap = true,
                    endCap = true,
                    label = spec.title,
                    labelAlign = LcarsLabelAlign.End,
                    labelColor = colors.a1,
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(spacing.gapStandard),
                ) {
                    LcarsButton(
                        text = "home",
                        color = colors.classicRed,
                        shape = LcarsButtonShape.BlockStart,
                        minWidth = 0.dp,
                        minHeight = 48.dp,
                        modifier = Modifier.weight(1f),
                        onClick = onShowConsole,
                    )
                    LcarsButton(
                        text = if (systemAlert) "reset" else "alert",
                        color = colors.butterscotch,
                        shape = LcarsButtonShape.Rectangle,
                        alerting = systemAlert,
                        minWidth = 0.dp,
                        minHeight = 48.dp,
                        modifier = Modifier.weight(1f),
                        onClick = onToggleAlert,
                    )
                    LcarsButton(
                        text = "catalog",
                        color = colors.violet,
                        shape = LcarsButtonShape.BlockEnd,
                        minWidth = 0.dp,
                        minHeight = 48.dp,
                        modifier = Modifier.weight(1f),
                        onClick = onShowCatalog,
                    )
                }
                BasicText(
                    text = spec.banner,
                    modifier = Modifier.fillMaxWidth(),
                    style = LocalLcarsTypography.current.header.copy(
                        color = colors.a1,
                        textAlign = TextAlign.End,
                        fontSize = 42.sp,
                        lineHeight = 44.sp,
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(172.dp),
                horizontalArrangement = Arrangement.spacedBy(spacing.gapStandard),
            ) {
                Column(
                    modifier = Modifier
                        .width(176.dp)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(spacing.gapStandard),
                ) {
                    LcarsButton(
                        text = "home",
                        color = colors.classicRed,
                        shape = LcarsButtonShape.BlockStart,
                        minWidth = 0.dp,
                        minHeight = 52.dp,
                        modifier = Modifier.fillMaxWidth(),
                        onClick = onShowConsole,
                    )
                    LcarsButton(
                        text = if (systemAlert) "reset alert" else "set alert",
                        color = colors.butterscotch,
                        shape = LcarsButtonShape.Rectangle,
                        alerting = systemAlert,
                        minWidth = 0.dp,
                        minHeight = 52.dp,
                        modifier = Modifier.fillMaxWidth(),
                        onClick = onToggleAlert,
                    )
                    LcarsButton(
                        text = "catalog",
                        color = colors.violet,
                        shape = LcarsButtonShape.BlockStart,
                        minWidth = 0.dp,
                        minHeight = 52.dp,
                        modifier = Modifier.fillMaxWidth(),
                        onClick = onShowCatalog,
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Bottom,
                ) {
                    BasicText(
                        text = spec.banner,
                        modifier = Modifier.fillMaxWidth(),
                        style = LocalLcarsTypography.current.header.copy(
                            color = colors.a1,
                            textAlign = TextAlign.End,
                            fontSize = 54.sp,
                            lineHeight = 56.sp,
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    LcarsDataCascade(
                        alerting = systemAlert,
                        maxColumns = 6,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(84.dp),
                    )
                    LcarsBar(
                        color = colors.lightBlue,
                        height = 28.dp,
                        startCap = true,
                        endCap = true,
                        label = spec.title,
                        labelAlign = LcarsLabelAlign.End,
                        labelColor = colors.a1,
                    )
                }
            }
        }
    }
}

@Composable
private fun LcarsStyleSelector(
    selectedStyle: LcarsStyle,
    onStyleSelected: (LcarsStyle) -> Unit,
) {
    val colors = LocalLcarsColors.current
    val spacing = LocalLcarsSpacing.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(spacing.gapStandard),
    ) {
        allShowcaseStyles.forEach { style ->
            val active = style == selectedStyle
            LcarsButton(
                text = style.shortLabel(),
                color = if (active) colors.a1 else style.colors().lightBlue,
                shape = if (active) LcarsButtonShape.Pill else LcarsButtonShape.Rectangle,
                minWidth = 0.dp,
                minHeight = 42.dp,
                modifier = Modifier.weight(1f),
                onClick = { onStyleSelected(style) },
            )
        }
    }
}

@Composable
private fun LcarsStyleGeometryPanel(
    spec: StyleShowcaseSpec,
    onToggleAlert: () -> Unit,
    onShowCatalog: () -> Unit,
) {
    val colors = LocalLcarsColors.current
    val spacing = LocalLcarsSpacing.current

    LcarsFramePanel(title = "geometry language", footerLabel = spec.reference) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing.gapStandard),
        ) {
            LcarsButton(
                text = spec.primaryButton,
                color = colors.classicRed,
                shape = LcarsButtonShape.BlockStart,
                minWidth = 0.dp,
                modifier = Modifier.weight(1f),
                onClick = {},
            )
            LcarsButton(
                text = spec.secondaryButton,
                color = colors.butterscotch,
                shape = LcarsButtonShape.Rectangle,
                minWidth = 0.dp,
                modifier = Modifier.weight(1f),
                onClick = {},
            )
            LcarsButton(
                text = spec.tertiaryButton,
                color = colors.lightBlue,
                shape = LcarsButtonShape.BlockEnd,
                minWidth = 0.dp,
                modifier = Modifier.weight(1f),
                onClick = {},
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing.gapStandard),
        ) {
            LcarsButton(
                text = spec.actionButton,
                color = colors.a1,
                shape = LcarsButtonShape.BlockStart,
                minWidth = 0.dp,
                minHeight = 48.dp,
                modifier = Modifier.weight(1f),
                onClick = onToggleAlert,
            )
            LcarsButton(
                text = "catalog",
                color = colors.almondCreme,
                shape = LcarsButtonShape.BlockEnd,
                minWidth = 0.dp,
                minHeight = 48.dp,
                modifier = Modifier.weight(1f),
                onClick = onShowCatalog,
            )
        }
        LcarsDividerGrid(
            type = LcarsDividerGridType.Type2,
            topHeight = 14.dp,
            bottomHeight = 14.dp,
        )
    }
}

@Composable
private fun LcarsStyleEffectsPanel(
    spec: StyleShowcaseSpec,
    systemAlert: Boolean,
) {
    val colors = LocalLcarsColors.current
    val spacing = LocalLcarsSpacing.current

    LcarsFramePanel(title = "data and motion", footerLabel = spec.motionLabel) {
        LcarsDataCascade(
            alerting = systemAlert,
            maxColumns = 8,
            modifier = Modifier
                .fillMaxWidth()
                .height(96.dp),
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing.gapStandard),
        ) {
            LcarsScannerSweep(
                running = true,
                color = if (systemAlert) colors.alertRed else colors.violet,
                sweepColor = if (systemAlert) colors.monoAmber else colors.lightBlue,
                modifier = Modifier
                    .weight(0.58f)
                    .height(112.dp),
            )
            LcarsNumberMatrix(
                rows = 5,
                columns = 6,
                running = true,
                modifier = Modifier
                    .weight(0.42f)
                    .height(112.dp),
            )
        }
    }
}

@Composable
private fun LcarsStyleColorPanel(spec: StyleShowcaseSpec) {
    val spacing = LocalLcarsSpacing.current

    LcarsFramePanel(title = "style palette", footerLabel = "flat black base") {
        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            val columns = if (maxWidth < 420.dp) 2 else 3
            Column(verticalArrangement = Arrangement.spacedBy(spacing.gapStandard)) {
                spec.swatches.chunked(columns).forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(spacing.gapStandard),
                    ) {
                        row.forEach { swatch ->
                            LcarsStyleSwatch(
                                swatch = swatch,
                                modifier = Modifier.weight(1f),
                            )
                        }
                        repeat(columns - row.size) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LcarsStyleSwatch(
    swatch: ClassicSwatch,
    modifier: Modifier = Modifier,
) {
    BasicText(
        text = swatch.label.uppercase(),
        modifier = modifier
            .height(54.dp)
            .background(swatch.color)
            .padding(end = 10.dp, bottom = 8.dp),
        style = LocalLcarsTypography.current.labelSmall.copy(
            color = Color.Black,
            textAlign = TextAlign.End,
        ),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
private fun LcarsStyleStatusPanel(
    spec: StyleShowcaseSpec,
    systemAlert: Boolean,
) {
    val colors = LocalLcarsColors.current
    val spacing = LocalLcarsSpacing.current

    LcarsFramePanel(title = "status strips", footerLabel = spec.statusLabel) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(148.dp),
            horizontalArrangement = Arrangement.spacedBy(spacing.gapStandard),
        ) {
            LcarsStyleSidePanel(spec.statusOne, colors.classicRed, Modifier.weight(0.9f))
            LcarsStyleSidePanel(spec.statusTwo, colors.lightBlue, Modifier.weight(1.1f))
            LcarsStyleSidePanel(
                if (systemAlert) "13-alert" else "13-318",
                colors.almondCreme,
                Modifier.weight(1f),
            )
        }
        LcarsStyleStatusList(systemAlert = systemAlert)
    }
}

@Composable
private fun LcarsStyleStatusList(systemAlert: Boolean) {
    val colors = LocalLcarsColors.current
    val typography = LocalLcarsTypography.current
    val rows = listOf(
        "subspace link: established",
        "starfleet database: connected",
        "quantum memory field: stable",
        if (systemAlert) "optical data network: rerouting" else "optical data network: nominal",
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        rows.forEachIndexed { index, row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .width(24.dp)
                        .height(14.dp)
                        .background(if (index == 3) colors.almondCreme else colors.a1, androidx.compose.foundation.shape.CircleShape),
                )
                BasicText(
                    text = row.uppercase(),
                    style = typography.labelSmall.copy(
                        color = if (index == 3) colors.almondCreme else colors.a1,
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun LcarsStyleSidePanel(
    label: String,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .background(color)
            .padding(end = 10.dp, bottom = 10.dp),
        contentAlignment = Alignment.BottomEnd,
    ) {
        BasicText(
            text = label.uppercase(),
            style = LocalLcarsTypography.current.labelSmall.copy(
                color = Color.Black,
                textAlign = TextAlign.End,
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun LcarsDataCascade(
    alerting: Boolean,
    modifier: Modifier = Modifier,
    maxColumns: Int = 8,
) {
    val colors = LocalLcarsColors.current
    val typography = LocalLcarsTypography.current
    val data = rememberLcarsCascadeData().take(maxColumns.coerceAtLeast(1))

    Row(
        modifier = modifier.padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
        verticalAlignment = Alignment.Bottom,
    ) {
        data.forEachIndexed { columnIndex, column ->
            Column(
                modifier = Modifier.weight(1f, fill = false),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(0.dp),
            ) {
                column.forEachIndexed { rowIndex, value ->
                    val highlighted = alerting && (columnIndex + rowIndex) % 7 == 0
                    BasicText(
                        text = value.uppercase(),
                        style = typography.labelSmall.copy(
                            color = if (highlighted) colors.spaceWhite else colors.a1,
                            textAlign = TextAlign.End,
                            fontSize = 13.sp,
                            lineHeight = 14.sp,
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Clip,
                    )
                }
            }
        }
    }
}

private data class ClassicSwatch(
    val label: String,
    val color: Color,
)

private val allShowcaseStyles = listOf(
    LcarsStyle.ClassicUltra,
    LcarsStyle.LowerDecks,
    LcarsStyle.LowerDecksPadd,
    LcarsStyle.NemesisBlueUltra,
)

private data class StyleShowcaseSpec(
    val title: String,
    val banner: String,
    val reference: String,
    val primaryButton: String,
    val secondaryButton: String,
    val tertiaryButton: String,
    val actionButton: String,
    val motionLabel: String,
    val statusLabel: String,
    val statusOne: String,
    val statusTwo: String,
    val swatches: List<ClassicSwatch>,
)

private fun LcarsStyle.showcaseSpec(): StyleShowcaseSpec {
    val colors = colors()
    return when (this) {
        LcarsStyle.ClassicUltra -> StyleShowcaseSpec(
            title = "classic ultra style",
            banner = "LCARS \u2022 47988",
            reference = "classic-ultra.html",
            primaryButton = "j-001",
            secondaryButton = "r-002",
            tertiaryButton = "r-003",
            actionButton = "i50-72",
            motionLabel = "cascade / scanner",
            statusLabel = "section two sample",
            statusOne = "11-1524",
            statusTwo = "12-0730",
            swatches = listOf(
                ClassicSwatch("red", colors.classicRed),
                ClassicSwatch("bluey", colors.lightBlue),
                ClassicSwatch("violet", colors.violet),
                ClassicSwatch("orange", colors.a1),
                ClassicSwatch("butterscotch", colors.butterscotch),
                ClassicSwatch("almond", colors.almondCreme),
            ),
        )
        LcarsStyle.LowerDecks -> StyleShowcaseSpec(
            title = "lower decks style",
            banner = "LCARS 2380",
            reference = "lower-decks.html",
            primaryButton = "ops-01",
            secondaryButton = "beta-02",
            tertiaryButton = "nav-03",
            actionButton = "alert",
            motionLabel = "warm cascade / scanner",
            statusLabel = "orange frame sample",
            statusOne = "ld-2380",
            statusTwo = "deck-04",
            swatches = listOf(
                ClassicSwatch("sunset", colors.classicRed),
                ClassicSwatch("orange", colors.a1),
                ClassicSwatch("daybreak", colors.a5),
                ClassicSwatch("harvest", colors.a2),
                ClassicSwatch("honey", colors.a3),
                ClassicSwatch("butter", colors.almondCreme),
            ),
        )
        LcarsStyle.LowerDecksPadd -> StyleShowcaseSpec(
            title = "lower decks padd",
            banner = "PADD \u2022 2380",
            reference = "lower-decks-padd.html",
            primaryButton = "padd",
            secondaryButton = "scan",
            tertiaryButton = "comm",
            actionButton = "sync",
            motionLabel = "cold padd readout",
            statusLabel = "compact blue strips",
            statusOne = "padd-01",
            statusTwo = "link-24",
            swatches = listOf(
                ClassicSwatch("alpha", colors.lightBlue),
                ClassicSwatch("arctic", colors.a2),
                ClassicSwatch("snow", colors.a3),
                ClassicSwatch("radio", colors.a1),
                ClassicSwatch("cloud", colors.a7),
                ClassicSwatch("rain", colors.a6),
            ),
        )
        LcarsStyle.NemesisBlueUltra -> StyleShowcaseSpec(
            title = "nemesis blue ultra",
            banner = "LCARS \u2022 1701-E",
            reference = "nemesis-blue-ultra.html",
            primaryButton = "j-001",
            secondaryButton = "r-002",
            tertiaryButton = "r-003",
            actionButton = "i50-72",
            motionLabel = "blue ultra cascade",
            statusLabel = "ultra status sample",
            statusOne = "11-1524",
            statusTwo = "12-0730",
            swatches = listOf(
                ClassicSwatch("evening", colors.a1),
                ClassicSwatch("cool", colors.lightBlue),
                ClassicSwatch("ghost", colors.a3),
                ClassicSwatch("honey", colors.butterscotch),
                ClassicSwatch("gray", colors.gray),
                ClassicSwatch("moon", colors.almondCreme),
            ),
        )
    }
}

private fun LcarsStyle.shortLabel(): String = when (this) {
    LcarsStyle.ClassicUltra -> "classic"
    LcarsStyle.LowerDecks -> "decks"
    LcarsStyle.LowerDecksPadd -> "padd"
    LcarsStyle.NemesisBlueUltra -> "nemesis"
}

@Composable
private fun rememberLcarsCascadeData(): List<List<String>> = androidx.compose.runtime.remember {
    listOf(
        listOf("93", "1853", "24109", "7", "7024", "322"),
        listOf("21509", "68417", "80", "2048", "319825", "46233"),
        listOf("585101", "25403", "31219", "752", "0604", "21048"),
        listOf("2107853", "12201972", "24487255", "30412", "98", "4024161"),
        listOf("33", "56", "04", "69", "41", "15"),
        listOf("0223", "688", "28471", "21366", "8654", "31"),
        listOf("633", "51166", "41699", "6188", "15033", "21094"),
        listOf("406822", "81205", "91007", "38357", "110", "2041"),
    )
}

@Composable
private fun LcarsDemoPortrait(
    systemAlert: Boolean,
    onToggleAlert: () -> Unit,
    onShowCatalog: () -> Unit,
    onShowStyleShowcase: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalLcarsColors.current
    val spacing = LocalLcarsSpacing.current
    val primaryColor = if (systemAlert) colors.alertRed else colors.monoAmber
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .background(colors.background)
            .padding(spacing.gapStandard)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(spacing.gapStandard),
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            LcarsElbow(
                text = "nav-407",
                color = primaryColor,
                direction = LcarsElbowDirection.TopLeft,
                wingWidth = 118.dp,
                wingHeight = 64.dp,
                thickness = 34.dp,
            )
            Spacer(modifier = Modifier.width(spacing.gapStandard))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(spacing.gapStandard),
            ) {
                LcarsBar(
                    color = colors.lightBlue,
                    height = 26.dp,
                    endCap = true,
                    label = "main deck",
                    labelAlign = LcarsLabelAlign.End,
                )
                LcarsBar(
                    color = colors.a7,
                    height = 14.dp,
                    endCap = true,
                    label = "padd mode",
                    labelAlign = LcarsLabelAlign.End,
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing.gapStandard),
        ) {
            LcarsButton(
                text = "sys scan",
                shape = LcarsButtonShape.Pill,
                color = colors.a5,
                minWidth = 0.dp,
                minHeight = 48.dp,
                modifier = Modifier.weight(1f),
                onClick = {},
            )
            LcarsButton(
                text = "stardate",
                shape = LcarsButtonShape.BlockStart,
                color = colors.a2,
                minWidth = 0.dp,
                minHeight = 48.dp,
                modifier = Modifier.weight(1f),
                onClick = {},
            )
        }

        LcarsButton(
            text = if (systemAlert) "danger: alert active" else "trigger system flash",
            color = colors.monoAmber,
            shape = LcarsButtonShape.Pill,
            alerting = systemAlert,
            minWidth = 0.dp,
            minHeight = 48.dp,
            modifier = Modifier.fillMaxWidth(),
            onClick = onToggleAlert,
        )

        LcarsButton(
            text = "catalog",
            color = colors.lightBlue,
            shape = LcarsButtonShape.BlockEnd,
            minWidth = 0.dp,
            minHeight = 48.dp,
            modifier = Modifier.fillMaxWidth(),
            onClick = onShowCatalog,
        )

        LcarsButton(
            text = "classic ultra",
            color = colors.violet,
            shape = LcarsButtonShape.BlockEnd,
            minWidth = 0.dp,
            minHeight = 48.dp,
            modifier = Modifier.fillMaxWidth(),
            onClick = onShowStyleShowcase,
        )

        LcarsTelemetryPanel(
            title = "primary telemetry matrix",
            alerting = systemAlert,
            entries = telemetryEntries(systemAlert),
        )

        LcarsBar(
            color = colors.violet,
            height = 20.dp,
            startCap = true,
            label = "component catalogue",
            labelAlign = LcarsLabelAlign.Start,
        )

        CompactComponentGrid()

        LcarsScannerSweep(
            running = true,
            color = if (systemAlert) colors.alertRed else colors.violet,
            sweepColor = if (systemAlert) colors.monoAmber else colors.lightBlue,
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp),
        )

        LogPanel(
            alerting = systemAlert,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 92.dp),
        )

        Row(modifier = Modifier.fillMaxWidth()) {
            LcarsElbow(
                text = "term-01",
                color = primaryColor,
                direction = LcarsElbowDirection.BottomLeft,
                wingWidth = 118.dp,
                wingHeight = 64.dp,
                thickness = 34.dp,
            )
            Spacer(modifier = Modifier.width(spacing.gapStandard))
            LcarsBar(
                color = colors.a7,
                height = 24.dp,
                endCap = true,
                label = "status active",
                labelAlign = LcarsLabelAlign.End,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun LcarsComponentCatalogScreen(
    onShowConsole: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalLcarsColors.current
    val spacing = LocalLcarsSpacing.current
    var alerting by rememberSaveable { mutableStateOf(false) }
    var selectedMode by rememberSaveable { mutableStateOf("NAV") }
    var securityArmed by rememberSaveable { mutableStateOf(false) }
    var dialogOpen by rememberSaveable { mutableStateOf(false) }
    var dialogResult by rememberSaveable { mutableStateOf("dialog idle") }
    val catalogMotion = true

    if (dialogOpen) {
        LcarsDialog(
            title = "authorization",
            message = "Command channel requires bridge authorization before subspace relay handoff.",
            confirmLabel = "authorize",
            dismissLabel = "cancel",
            level = if (alerting) com.lcars.ui.LcarsAlertLevel.Critical else com.lcars.ui.LcarsAlertLevel.Warning,
            onConfirm = {
                dialogResult = "dialog confirmed"
                dialogOpen = false
            },
            onDismiss = {
                dialogResult = "dialog dismissed"
                dialogOpen = false
            },
        )
    }

    LazyColumn(
        modifier = modifier
            .background(colors.background)
            .padding(spacing.gapStandard),
        verticalArrangement = Arrangement.spacedBy(spacing.gapStandard),
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(spacing.gapStandard),
            ) {
                LcarsButton(
                    text = "console",
                    color = colors.a5,
                    shape = LcarsButtonShape.BlockStart,
                    minWidth = 0.dp,
                    modifier = Modifier.weight(0.34f),
                    onClick = onShowConsole,
                )
                LcarsBar(
                    color = colors.lightBlue,
                    height = 28.dp,
                    endCap = true,
                    label = "component catalog",
                    labelAlign = LcarsLabelAlign.End,
                    modifier = Modifier.weight(0.66f),
                )
            }
        }

        item { LcarsFramePanel(title = "app controls", footerLabel = "interactive api") {
            LcarsCommandRail(
                items = listOf(
                    LcarsCommandRailItem("scan", "scan", color = colors.a5),
                    LcarsCommandRailItem("hold", "hold", type = LcarsCommandRailItemType.PassiveBlock, color = colors.a7),
                    LcarsCommandRailItem("alert", "alert", type = LcarsCommandRailItemType.AlertBlock, color = colors.monoAmber),
                ),
                compact = true,
                modifier = Modifier.fillMaxWidth(),
                onCommandClick = {
                    if (it.id == "alert") alerting = !alerting
                },
            )
            LcarsSegmentedControl(
                options = listOf("NAV", "COMM", "SENSOR"),
                selectedOption = selectedMode,
                onOptionSelected = { selectedMode = it },
                alerting = alerting,
            )
            LcarsToggle(
                checked = securityArmed,
                onCheckedChange = { securityArmed = it },
                checkedLabel = "armed",
                uncheckedLabel = "standby",
                alerting = alerting && securityArmed,
            )
            BasicText(
                text = "MODE $selectedMode // SECURITY ${if (securityArmed) "ARMED" else "STANDBY"} // $dialogResult".uppercase(),
                style = LocalLcarsTypography.current.labelSmall.copy(color = colors.lightBlue),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            LcarsButton(
                text = "open dialog",
                color = colors.auxiliaryTan,
                shape = LcarsButtonShape.BlockEnd,
                modifier = Modifier.fillMaxWidth(),
                onClick = { dialogOpen = true },
            )
        } }

        item { LcarsFramePanel(title = "dynamic states", footerLabel = "alert bus") {
            LcarsAlertBanner(
                message = if (alerting) "critical alert active" else "system nominal",
                active = alerting,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(spacing.gapStandard),
            ) {
                LcarsStatusLight(
                    label = "sensor lock",
                    active = true,
                    modifier = Modifier.weight(1f),
                )
                LcarsStatusLight(
                    label = "alert relay",
                    active = alerting,
                    alerting = alerting,
                    modifier = Modifier.weight(1f),
                )
            }
            LcarsButton(
                text = if (alerting) "clear alert" else "trigger alert",
                alerting = alerting,
                modifier = Modifier.fillMaxWidth(),
                onClick = { alerting = !alerting },
            )
        } }

        item { LcarsFramePanel(title = "readouts") {
            LcarsProgressBar(
                progress = if (alerting) 0.92f else 0.64f,
                label = "reactor balance",
                alerting = alerting,
            )
            LcarsSegmentedMeter(
                activeSegments = if (alerting) 9 else 6,
                totalSegments = 12,
            )
            LcarsReadoutTicker(
                values = listOf(
                    "SP3 FLOW CAPTURED",
                    "GNSS MATRIX LOCKED",
                    "LCARS GRID NOMINAL",
                    "PADD TEMPLATE READY",
                ),
                running = catalogMotion,
            )
        } }

        item { LcarsFramePanel(title = "data displays") {
            LcarsNumericLabel(
                label = "407",
                color = colors.lightBlue,
                modifier = Modifier.fillMaxWidth(),
            )
            LcarsNumberMatrix(
                rows = 5,
                columns = 8,
                seed = if (alerting) 911 else 407,
                running = catalogMotion,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(112.dp),
            )
            LcarsStarCoords(
                count = 2,
                digits = 6,
                seed = 1701,
                running = catalogMotion,
            )
            LcarsLogConsole(
                entries = demoLogEntries(alerting),
                maxLines = 4,
                compact = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(96.dp),
            )
        } }

        item { LcarsFramePanel(title = "telemetry table") {
            LcarsDataTable(
                headers = listOf("subsystem", "state", "code"),
                rows = listOf(
                    LcarsDataRow(listOf("nav", "locked", "407")),
                    LcarsDataRow(listOf("comm", "ready", "118")),
                    LcarsDataRow(listOf("sensor", if (alerting) "variance" else "nominal", "224"), highlighted = alerting),
                ),
            )
        } }

        item { LcarsFramePanel(title = "layout and visual systems") {
            LcarsDividerGrid(
                type = if (alerting) LcarsDividerGridType.Type3 else LcarsDividerGridType.Type2,
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(colors.panel),
            ) {
                LcarsTargetScanner(
                    running = catalogMotion,
                    color = if (alerting) colors.alertRed else colors.monoAmber,
                    modifier = Modifier.fillMaxSize(),
                )
            }
            LcarsInspectBracket(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                running = catalogMotion,
            ) {
                LcarsNumberMatrix(
                    rows = 4,
                    columns = 6,
                    seed = 118,
                    running = false,
                    highlightedRow = 2,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        } }

        item { LcarsFramePanel(title = "star chart") {
            LcarsStarChart(
                mode = LcarsStarChartMode.Navigation,
                seed = 1701,
                showCoords = true,
                showScanner = true,
                running = catalogMotion,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
            )
            LcarsStarChart(
                mode = LcarsStarChartMode.Inspection,
                seed = 407,
                showCoords = false,
                showScanner = true,
                running = catalogMotion,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
            )
        } }

        item { LcarsFramePanel(title = "transmission frame") {
            LcarsTransmissionFrame(
                headerLabel = "subspace comm net 407",
                footerLabel = "main bridge",
                title = "incoming transmission",
                subtitle = "command authorization required",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp),
            ) {
                LcarsSegmentedMeter(
                    activeSegments = 8,
                    totalSegments = 12,
                    modifier = Modifier.fillMaxWidth(0.72f),
                )
            }
        } }

        item { LcarsFramePanel(title = "layout templates", footerLabel = "scaffold api") {
            BasicText(
                text = "LCARSAPPSCAFFOLD // LCARSPADDSCAFFOLD // LCARSCONSOLEScaffold".uppercase(),
                style = LocalLcarsTypography.current.labelSmall.copy(color = colors.lightBlue),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            LcarsBar(
                color = colors.a7,
                height = 14.dp,
                startCap = true,
                endCap = true,
                label = "template primitives",
                labelAlign = LcarsLabelAlign.Center,
            )
        } }
    }
}

@Composable
private fun ComponentShowcase(
    alerting: Boolean,
    onToggleAlert: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalLcarsColors.current
    val gap = LocalLcarsSpacing.current.gapStandard

    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        if (maxWidth < 520.dp) {
            Column(verticalArrangement = Arrangement.spacedBy(gap)) {
                LcarsButton(
                    text = "pill shape",
                    color = colors.monoAmber,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {},
                )
                LcarsButton(
                    text = "block left",
                    shape = LcarsButtonShape.BlockStart,
                    color = colors.lightBlue,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {},
                )
                LcarsButton(
                    text = "pure rect",
                    shape = LcarsButtonShape.Rectangle,
                    color = colors.auxiliaryTan,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {},
                )
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(gap),
            ) {
                LcarsButton(
                    text = "pill shape",
                    color = colors.monoAmber,
                    modifier = Modifier.weight(1f),
                    onClick = {},
                )
                LcarsButton(
                    text = "block left",
                    shape = LcarsButtonShape.BlockStart,
                    color = colors.lightBlue,
                    modifier = Modifier.weight(1f),
                    onClick = {},
                )
                LcarsButton(
                    text = "pure rect",
                    shape = LcarsButtonShape.Rectangle,
                    color = colors.auxiliaryTan,
                    modifier = Modifier.weight(1f),
                    onClick = {},
                )
            }
        }
    }

    LcarsButton(
        text = if (alerting) "danger: alert active" else "trigger system flash",
        color = colors.monoAmber,
        shape = LcarsButtonShape.Pill,
        alerting = alerting,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = gap),
        onClick = onToggleAlert,
    )
}

@Composable
private fun CompactComponentGrid(modifier: Modifier = Modifier) {
    val colors = LocalLcarsColors.current
    val gap = LocalLcarsSpacing.current.gapStandard

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(gap),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(gap),
        ) {
            LcarsButton(
                text = "pill",
                color = colors.monoAmber,
                minWidth = 0.dp,
                minHeight = 46.dp,
                modifier = Modifier.weight(1f),
                onClick = {},
            )
            LcarsButton(
                text = "block",
                shape = LcarsButtonShape.BlockStart,
                color = colors.lightBlue,
                minWidth = 0.dp,
                minHeight = 46.dp,
                modifier = Modifier.weight(1f),
                onClick = {},
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(gap),
        ) {
            LcarsButton(
                text = "rect",
                shape = LcarsButtonShape.Rectangle,
                color = colors.auxiliaryTan,
                minWidth = 0.dp,
                minHeight = 46.dp,
                modifier = Modifier.weight(1f),
                onClick = {},
            )
            LcarsButton(
                text = "hidden",
                shape = LcarsButtonShape.BlockEnd,
                color = colors.a8,
                enabled = false,
                minWidth = 0.dp,
                minHeight = 46.dp,
                modifier = Modifier.weight(1f),
                onClick = {},
            )
        }
    }
}

@Composable
private fun MainCommandRail(
    systemAlert: Boolean,
    onToggleAlert: () -> Unit,
    onShowCatalog: () -> Unit,
    onShowStyleShowcase: () -> Unit,
    compact: Boolean,
    modifier: Modifier = Modifier,
) {
    val colors = LocalLcarsColors.current
    val spacing = LocalLcarsSpacing.current
    val primaryColor = if (systemAlert) colors.alertRed else colors.monoAmber
    val railWidth = if (compact) 136.dp else 154.dp
    val elbowHeight = if (compact) 48.dp else 64.dp
    val elbowThickness = if (compact) 28.dp else 34.dp

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(spacing.gapStandard),
    ) {
        LcarsElbow(
            text = "nav-407",
            color = primaryColor,
            direction = LcarsElbowDirection.TopLeft,
            wingWidth = railWidth,
            wingHeight = elbowHeight,
            thickness = elbowThickness,
        )
        LcarsCommandRail(
            items = listOf(
                LcarsCommandRailItem("scan", if (compact) "scan" else "sys scan", color = colors.a5),
                LcarsCommandRailItem("stardate", "stardate", color = colors.a2),
                LcarsCommandRailItem(
                    id = "alert",
                    label = if (systemAlert) {
                        if (compact) "reset" else "srv-reset"
                    } else {
                        if (compact) "alert" else "set-alert"
                    },
                    type = if (systemAlert) LcarsCommandRailItemType.AlertBlock else LcarsCommandRailItemType.Command,
                    color = colors.auxiliaryTan,
                ),
                LcarsCommandRailItem("catalog", "catalog", color = colors.lightBlue),
                LcarsCommandRailItem("style", if (compact) "style" else "classic", color = colors.violet),
                LcarsCommandRailItem(
                    id = "wing-bus",
                    label = if (compact) "bus" else "wing bus",
                    type = LcarsCommandRailItemType.PassiveBlock,
                    color = if (systemAlert) colors.alertRed else colors.violet,
                ),
                LcarsCommandRailItem(
                    id = "spacer",
                    type = LcarsCommandRailItemType.SpacerBlock,
                    color = if (systemAlert) colors.alertRed else colors.a8,
                    weight = 1f,
                ),
            ),
            compact = compact,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            onCommandClick = { item ->
                when (item.id) {
                    "alert" -> onToggleAlert()
                    "catalog" -> onShowCatalog()
                    "style" -> onShowStyleShowcase()
                }
            },
        )
        LcarsElbow(
            text = "term-01",
            color = primaryColor,
            direction = LcarsElbowDirection.BottomLeft,
            wingWidth = railWidth,
            wingHeight = elbowHeight,
            thickness = elbowThickness,
        )
    }
}

private fun telemetryEntries(systemAlert: Boolean): List<LcarsTelemetryEntry> = listOf(
    LcarsTelemetryEntry("lat", "30.542314 n"),
    LcarsTelemetryEntry("lon", "114.367852 e"),
    LcarsTelemetryEntry("sat visible", "24"),
    LcarsTelemetryEntry(
        label = "fix status",
        value = if (systemAlert) "variance alert" else "high precision",
        status = LcarsTelemetryStatus.Normal,
    ),
    LcarsTelemetryEntry("dilution", "0.42 hdop", LcarsTelemetryStatus.Warning),
    LcarsTelemetryEntry("epoch", "407.13.22", LcarsTelemetryStatus.Neutral),
)

private fun demoLogEntries(alerting: Boolean): List<LcarsLogEntry> = buildList {
    add(LcarsLogEntry("sensor fusion active", LcarsLogSeverity.Info, "log-con"))
    add(LcarsLogEntry("sp3 flow captured", LcarsLogSeverity.Success, "nav"))
    add(LcarsLogEntry("lcars grid nominal", LcarsLogSeverity.Success, "ui"))
    if (alerting) {
        add(LcarsLogEntry("variance detected on subspace bus", LcarsLogSeverity.Alert, "alert"))
    } else {
        add(LcarsLogEntry("no variance detected", LcarsLogSeverity.Info, "diag"))
    }
    add(LcarsLogEntry("system ready", if (alerting) LcarsLogSeverity.Warning else LcarsLogSeverity.Success, "sys"))
}

@Composable
private fun LogPanel(
    alerting: Boolean,
    modifier: Modifier = Modifier,
    maxLines: Int = 4,
) {
    LcarsLogConsole(
        entries = demoLogEntries(alerting),
        maxLines = maxLines,
        compact = maxLines <= 2,
        autoScroll = false,
        modifier = modifier,
    )
}

@Preview(widthDp = 1280, heightDp = 720, showBackground = true)
@Composable
private fun LcarsDemoWideLandscapePreview() {
    DemoLcarsTheme {
        LcarsDemoScreen(modifier = Modifier.fillMaxSize())
    }
}

@Preview(widthDp = 844, heightDp = 390, showBackground = true)
@Composable
private fun LcarsDemoCompactLandscapePreview() {
    DemoLcarsTheme {
        LcarsDemoScreen(modifier = Modifier.fillMaxSize())
    }
}

@Preview(widthDp = 390, heightDp = 820, showBackground = true)
@Composable
private fun LcarsDemoPortraitPreview() {
    DemoLcarsTheme {
        LcarsDemoScreen(modifier = Modifier.fillMaxSize())
    }
}
