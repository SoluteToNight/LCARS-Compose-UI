package com.lcars.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lcars.ui.controls.LcarsButton
import com.lcars.ui.controls.LcarsButtonShape
import com.lcars.ui.foundation.LcarsText
import com.lcars.ui.layout.LcarsBarSegment
import com.lcars.ui.layout.LcarsResponsiveScaffold
import com.lcars.ui.layout.LcarsSegmentedBar
import com.lcars.ui.theme.LcarsPreset
import com.lcars.ui.theme.LcarsTheme

private data class HubCapability(
    val code: String,
    val title: String,
    val detail: String,
)

private data class DemoHubDestination(
    val id: String,
    val code: String,
    val title: String,
    val summary: String,
    val capabilities: List<HubCapability>,
    val formFactor: String,
    val dataRequirement: String,
    val permissionRequirement: String,
    val color: Color,
    val onOpen: () -> Unit,
)

@Composable
fun DemoHubScreen(
    onOpenCatalog: () -> Unit,
    onOpenWeather: () -> Unit,
    onOpenPadd: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DemoLcarsTheme(preset = LcarsPreset.NemesisBlueUltra) {
        val colors = LcarsTheme.colorScheme
        var activeConsole by rememberSaveable { mutableStateOf<String?>(null) }

        val destinations = listOf(
            DemoHubDestination(
                id = "engineering",
                code = "01 / ENG",
                title = "engineering eps monitor",
                summary = "Stage 9 Master Systems Display & EPS energy conduit monitor with 60fps continuous flow.",
                capabilities = listOf(
                    HubCapability("01", "eps conduit flow", "Renders smooth-flowing energy packets across multi-channel conduits."),
                    HubCapability("02", "sensor histogram", "Horizontal multi-color scale with real-time smooth bar interpolation."),
                    HubCapability("03", "keypad rhythm", "Alternating color keypad cluster with technobabble telemetry codes."),
                ),
                formFactor = "LANDSCAPE ONLY",
                dataRequirement = "LOCAL SIMULATION",
                permissionRequirement = "NONE",
                color = colors.monoAmber,
                onOpen = { activeConsole = "engineering" },
            ),
            DemoHubDestination(
                id = "sickbay",
                code = "02 / MED",
                title = "sickbay biomedical scan",
                summary = "Stage 9 Sickbay Bio-Bed 0208 console with double bracket frame and neural telemetry grid.",
                capabilities = listOf(
                    HubCapability("01", "bracket frame", "Enclosed double-elbow layout with integrated sub-runners and caps."),
                    HubCapability("02", "diagnostic matrix", "High-density data stream with smooth-pulsing anomaly core."),
                    HubCapability("03", "physiological scale", "Smooth-scaling anatomical metrics and biological meters."),
                ),
                formFactor = "LANDSCAPE ONLY",
                dataRequirement = "LOCAL SIMULATION",
                permissionRequirement = "NONE",
                color = colors.lightBlue,
                onOpen = { activeConsole = "sickbay" },
            ),
            DemoHubDestination(
                id = "bridge",
                code = "03 / NAV",
                title = "bridge forward nav & scan",
                summary = "Stage 9 Helm/Conn console featuring tactile attitude disc and tactical targeting crosshair.",
                capabilities = listOf(
                    HubCapability("01", "attitude disc", "Four-quadrant circular navigator with central crossarms and touch feedback."),
                    HubCapability("02", "targeting brackets", "Smoothly tracking space crosshair and breathing target brackets."),
                    HubCapability("03", "red alert switch", "Smooth color scheme shift from standard classic to Red Alert mode."),
                ),
                formFactor = "LANDSCAPE ONLY",
                dataRequirement = "LOCAL SIMULATION",
                permissionRequirement = "NONE",
                color = colors.auxiliaryTan,
                onOpen = { activeConsole = "bridge" },
            ),
            DemoHubDestination(
                id = "catalog",
                code = "04 / LIB",
                title = "component catalog",
                summary = "Browse the reusable LCARS API and compare component behavior across visual and motion modes.",
                capabilities = listOf(
                    HubCapability("01", "search and filter", "Find controls, patterns, displays, and PADD components by group or keyword."),
                    HubCapability("02", "theme presets", "Compare Classic Ultra, Nemesis Blue Ultra, and Lower Decks PADD tokens."),
                    HubCapability("03", "motion modes", "Inspect system, reduced, and disabled motion behavior in one catalog."),
                ),
                formFactor = "ANY ORIENTATION",
                dataRequirement = "LOCAL COMPONENTS",
                permissionRequirement = "NONE",
                color = colors.commandPrimary,
                onOpen = onOpenCatalog,
            ),
            DemoHubDestination(
                id = "weather",
                code = "05 / WX",
                title = "weather console",
                summary = "Inspect a production-style LCARS scene driven by live weather, warning, and location-aware data.",
                capabilities = listOf(
                    HubCapability("01", "live weather", "Loads current conditions and forecast data with cached or offline fallback."),
                    HubCapability("02", "warning workflow", "Displays NMC alerts, sector filtering, and a stepped storm simulation."),
                    HubCapability("03", "responsive console", "Adapts the same scene across compact and wide landscape profiles."),
                ),
                formFactor = "LANDSCAPE",
                dataRequirement = "NETWORK / CACHE",
                permissionRequirement = "LOCATION OPTIONAL",
                color = colors.lightBlue,
                onOpen = onOpenWeather,
            ),
        )
        var selectedId by rememberSaveable { mutableStateOf(destinations.first().id) }
        val selected = destinations.first { it.id == selectedId }

        if (activeConsole != null) {
            when (activeConsole) {
                "engineering" -> EngineeringEpsConsoleScreen(onBack = { activeConsole = null })
                "sickbay" -> SickbayBiomedicalConsoleScreen(onBack = { activeConsole = null })
                "bridge" -> BridgeNavTacticalConsoleScreen(onBack = { activeConsole = null })
                else -> { activeConsole = null }
            }
        } else {
            LcarsResponsiveScaffold(
                modifier = modifier
                    .fillMaxSize()
                    .background(colors.background)
                    .windowInsetsPadding(WindowInsets.safeDrawing),
                portrait = {
                    PortraitHub(
                        destinations = destinations,
                        selected = selected,
                        onSelect = { selectedId = it.id },
                    )
                },
                compactLandscape = {
                    LandscapeHub(
                        destinations = destinations,
                        selected = selected,
                        compact = true,
                        onSelect = { selectedId = it.id },
                    )
                },
                wideLandscape = {
                    LandscapeHub(
                        destinations = destinations,
                        selected = selected,
                        compact = false,
                        onSelect = { selectedId = it.id },
                    )
                },
            )
        }
    }
}

@Composable
private fun LandscapeHub(
    destinations: List<DemoHubDestination>,
    selected: DemoHubDestination,
    compact: Boolean,
    onSelect: (DemoHubDestination) -> Unit,
) {
    val spacing = LcarsTheme.dimensions
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(spacing.gapLarge),
        verticalArrangement = Arrangement.spacedBy(spacing.gapStandard),
    ) {
        HubHeader(compact = compact)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(if (compact) 12.dp else 20.dp),
        ) {
            DestinationIndex(
                destinations = destinations,
                selected = selected,
                compact = compact,
                onSelect = onSelect,
                modifier = Modifier
                    .weight(if (compact) 0.38f else 0.31f)
                    .fillMaxHeight(),
            )
            DestinationDetail(
                destination = selected,
                compact = compact,
                modifier = Modifier
                    .weight(if (compact) 0.62f else 0.69f)
                    .fillMaxHeight(),
            )
        }
        HubFooter(compact = compact)
    }
}

@Composable
private fun PortraitHub(
    destinations: List<DemoHubDestination>,
    selected: DemoHubDestination,
    onSelect: (DemoHubDestination) -> Unit,
) {
    val spacing = LcarsTheme.dimensions
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(spacing.gapLarge),
        verticalArrangement = Arrangement.spacedBy(spacing.gapLarge),
    ) {
        HubHeader(compact = false)
        DestinationIndex(
            destinations = destinations,
            selected = selected,
            compact = true,
            onSelect = onSelect,
            modifier = Modifier.fillMaxWidth(),
        )
        DestinationDetail(
            destination = selected,
            compact = true,
            portrait = true,
            modifier = Modifier.fillMaxWidth(),
        )
        HubFooter(compact = false)
    }
}

@Composable
private fun HubHeader(compact: Boolean) {
    val colors = LcarsTheme.colorScheme
    LcarsSegmentedBar(
        segments = listOf(
            LcarsBarSegment(0.22f, colors.framePrimary, "LCARS COMPOSE UI"),
            LcarsBarSegment(0.10f, colors.commandSecondary),
            LcarsBarSegment(0.50f, colors.commandPrimary, "DEMONSTRATION HUB"),
            LcarsBarSegment(0.18f, colors.activeAccent, "V${BuildConfig.VERSION_NAME}"),
        ),
        height = if (compact) 32.dp else 42.dp,
        labelColor = Color.Black,
    )
}

@Composable
private fun DestinationIndex(
    destinations: List<DemoHubDestination>,
    selected: DemoHubDestination,
    compact: Boolean,
    onSelect: (DemoHubDestination) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LcarsTheme.colorScheme
    val spacing = LcarsTheme.dimensions
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(spacing.gapStandard),
    ) {
        LcarsText(
            text = "DEMONSTRATION INDEX",
            style = LcarsTheme.typography.telemetry.copy(color = colors.readoutAccent),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = if (compact) 2.dp else 8.dp),
        )
        destinations.forEach { destination ->
            val isSelected = destination.id == selected.id
            LcarsButton(
                text = if (isSelected) {
                    "active / ${destination.code} / ${destination.title}"
                } else {
                    "${destination.code} / ${destination.title}"
                },
                onClick = { onSelect(destination) },
                color = if (isSelected) colors.activeAccent else destination.color,
                shape = LcarsButtonShape.BlockStart,
                role = Role.Tab,
                selected = isSelected,
                minHeight = if (compact) 52.dp else 68.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics {
                        contentDescription = "Select ${destination.title}"
                    },
            )
        }
        if (!compact) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(spacing.gapStandard),
            ) {
                WorkflowBlock(
                    code = "01",
                    label = "SELECT A DEMO",
                    color = colors.framePrimary,
                    modifier = Modifier.weight(1f),
                )
                WorkflowBlock(
                    code = "02",
                    label = "REVIEW CAPABILITIES",
                    color = colors.commandInactive,
                    modifier = Modifier.weight(1f),
                )
                WorkflowBlock(
                    code = "03",
                    label = "OPEN TERMINAL",
                    color = colors.commandSecondary,
                    modifier = Modifier.weight(1f),
                )
            }
        }
        LcarsSegmentedBar(
            segments = listOf(
                LcarsBarSegment(0.70f, colors.commandInactive, "SELECT THEN REVIEW"),
                LcarsBarSegment(0.30f, colors.commandSecondary, "READY"),
            ),
            height = if (compact) 24.dp else 30.dp,
            labelColor = Color.Black,
        )
    }
}

@Composable
private fun WorkflowBlock(
    code: String,
    label: String,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color)
            .padding(12.dp),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Bottom,
    ) {
        LcarsText(
            text = label,
            style = LcarsTheme.typography.labelSmall.copy(
                fontSize = 13.sp,
                lineHeight = 14.sp,
                color = Color.Black,
            ),
            autoFit = true,
            minFontSize = 9.sp,
            modifier = Modifier.weight(1f, fill = false),
        )
        LcarsText(
            text = code,
            style = LcarsTheme.typography.titleSmall.copy(
                fontSize = 20.sp,
                lineHeight = 22.sp,
                color = Color.Black,
            ),
        )
    }
}

@Composable
private fun DestinationDetail(
    destination: DemoHubDestination,
    compact: Boolean,
    modifier: Modifier = Modifier,
    portrait: Boolean = false,
) {
    val colors = LcarsTheme.colorScheme
    val spacing = LcarsTheme.dimensions
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(spacing.gapStandard),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                LcarsText(
                    text = destination.code,
                    style = LcarsTheme.typography.labelSmall.copy(color = destination.color),
                )
                LcarsText(
                    text = destination.title,
                    style = LcarsTheme.typography.header.copy(color = colors.text),
                )
            }
            LcarsText(
                text = "SELECTED",
                style = LcarsTheme.typography.labelSmall.copy(color = colors.activeAccent),
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(colors.panel)
                .padding(horizontal = 12.dp, vertical = if (compact) 8.dp else 12.dp),
        ) {
            LcarsText(
                text = destination.summary,
                style = if (compact) {
                    LcarsTheme.typography.labelSmall.copy(color = colors.subHighlight)
                } else {
                    LcarsTheme.typography.telemetry.copy(color = colors.subHighlight)
                },
            )
        }
        LcarsText(
            text = "WHAT THIS DEMO PROVES",
            style = LcarsTheme.typography.labelSmall.copy(color = colors.readoutAccent),
            modifier = Modifier.padding(top = if (compact) 0.dp else 4.dp),
        )
        Column(
            modifier = if (!portrait) Modifier.weight(1f) else Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(spacing.gapStandard),
        ) {
            destination.capabilities.forEach { capability ->
                CapabilityRow(
                    capability = capability,
                    color = destination.color,
                    compact = compact,
                    modifier = if (!portrait) {
                        Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    } else {
                        Modifier
                            .fillMaxWidth()
                            .height(78.dp)
                    },
                )
            }
        }
        RuntimeProfile(destination = destination, portrait = portrait)
        LcarsButton(
            text = "open ${destination.title}",
            onClick = destination.onOpen,
            color = destination.color,
            shape = LcarsButtonShape.BlockEnd,
            minHeight = if (compact) 52.dp else 64.dp,
            modifier = Modifier
                .fillMaxWidth()
                .semantics {
                    contentDescription = "Open ${destination.title}"
                },
        )
    }
}

@Composable
private fun CapabilityRow(
    capability: HubCapability,
    color: Color,
    compact: Boolean,
    modifier: Modifier = Modifier,
) {
    val colors = LcarsTheme.colorScheme
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Box(
            modifier = Modifier
                .width(if (compact) 48.dp else 64.dp)
                .fillMaxHeight()
                .background(color),
            contentAlignment = Alignment.BottomEnd,
        ) {
            LcarsText(
                text = capability.code,
                style = LcarsTheme.typography.button.copy(color = Color.Black),
                modifier = Modifier.padding(8.dp),
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(colors.panel)
                .padding(horizontal = 10.dp, vertical = if (compact) 6.dp else 10.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            LcarsText(
                text = capability.title,
                style = LcarsTheme.typography.telemetry.copy(color = colors.text),
            )
            if (!compact) {
                LcarsText(
                    text = capability.detail,
                    style = LcarsTheme.typography.labelSmall.copy(color = colors.subHighlight),
                )
            }
        }
    }
}

@Composable
private fun RuntimeProfile(
    destination: DemoHubDestination,
    portrait: Boolean,
) {
    val items = listOf(
        "FORM FACTOR" to destination.formFactor,
        "DATA" to destination.dataRequirement,
        "PERMISSIONS" to destination.permissionRequirement,
    )
    val colors = LcarsTheme.colorScheme
    val spacing = LcarsTheme.dimensions
    if (portrait) {
        Column(verticalArrangement = Arrangement.spacedBy(spacing.gapStandard)) {
            items.forEach { (label, value) -> RuntimeItem(label, value, Modifier.fillMaxWidth()) }
        }
    } else {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing.gapStandard),
        ) {
            items.forEach { (label, value) ->
                RuntimeItem(label, value, Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun RuntimeItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    val colors = LcarsTheme.colorScheme
    Column(
        modifier = modifier
            .background(colors.panel)
            .padding(horizontal = 8.dp, vertical = 6.dp),
    ) {
        LcarsText(
            text = label,
            style = LcarsTheme.typography.labelSmall.copy(color = colors.commandSecondary),
        )
        LcarsText(
            text = value,
            style = LcarsTheme.typography.labelSmall.copy(color = colors.text),
        )
    }
}

@Composable
private fun HubFooter(compact: Boolean) {
    val colors = LcarsTheme.colorScheme
    LcarsSegmentedBar(
        segments = listOf(
            LcarsBarSegment(0.42f, colors.commandSecondary, "ANDROID / JETPACK COMPOSE"),
            LcarsBarSegment(0.40f, colors.framePrimary, "05 DEMONSTRATIONS"),
            LcarsBarSegment(0.18f, colors.activeAccent, "SYSTEM READY"),
        ),
        height = if (compact) 22.dp else 28.dp,
        labelColor = Color.Black,
    )
}

@Preview(widthDp = 390, heightDp = 820, showBackground = true)
@Composable
private fun DemoHubPortraitPreview() {
    DemoHubScreen(onOpenCatalog = {}, onOpenWeather = {}, onOpenPadd = {})
}

@Preview(widthDp = 844, heightDp = 390, showBackground = true)
@Composable
private fun DemoHubCompactLandscapePreview() {
    DemoHubScreen(onOpenCatalog = {}, onOpenWeather = {}, onOpenPadd = {})
}

@Preview(widthDp = 1280, heightDp = 720, showBackground = true)
@Composable
private fun DemoHubWideLandscapePreview() {
    DemoHubScreen(onOpenCatalog = {}, onOpenWeather = {}, onOpenPadd = {})
}