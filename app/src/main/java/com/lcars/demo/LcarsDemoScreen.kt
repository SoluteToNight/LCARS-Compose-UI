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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
import com.lcars.ui.LcarsTargetScanner
import com.lcars.ui.LcarsTelemetryEntry
import com.lcars.ui.LcarsTelemetryPanel
import com.lcars.ui.LcarsTelemetryStatus
import com.lcars.ui.LcarsToggle
import com.lcars.ui.LcarsTransmissionFrame
import com.lcars.ui.LocalLcarsColors
import com.lcars.ui.LocalLcarsSpacing
import com.lcars.ui.LocalLcarsTypography

private enum class DemoPage {
    Console,
    Catalog,
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

    LcarsResponsiveScaffold(
        modifier = modifier,
        portrait = {
            LcarsDemoPortrait(
                systemAlert = systemAlert,
                onToggleAlert = { systemAlert = !systemAlert },
                onShowCatalog = { selectedPage = DemoPage.Catalog.name },
                modifier = Modifier.fillMaxSize(),
            )
        },
        compactLandscape = {
            LcarsDemoCompactLandscape(
                systemAlert = systemAlert,
                onToggleAlert = { systemAlert = !systemAlert },
                onShowCatalog = { selectedPage = DemoPage.Catalog.name },
                modifier = Modifier.fillMaxSize(),
            )
        },
        wideLandscape = {
            LcarsDemoLandscape(
                systemAlert = systemAlert,
                onToggleAlert = { systemAlert = !systemAlert },
                onShowCatalog = { selectedPage = DemoPage.Catalog.name },
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
private fun LcarsDemoPortrait(
    systemAlert: Boolean,
    onToggleAlert: () -> Unit,
    onShowCatalog: () -> Unit,
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
