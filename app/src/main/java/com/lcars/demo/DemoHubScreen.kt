package com.lcars.demo

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lcars.ui.controls.LcarsButton
import com.lcars.ui.controls.LcarsButtonShape
import com.lcars.ui.foundation.LcarsText
import com.lcars.ui.layout.LcarsBarSegment
import com.lcars.ui.layout.LcarsCutoutBar
import com.lcars.ui.layout.LcarsElbowDirection
import com.lcars.ui.layout.LcarsElbowFrame
import com.lcars.ui.layout.LcarsSpineAlign
import com.lcars.ui.layout.LcarsSpineBlock
import com.lcars.ui.layout.LcarsSpineButton
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
    DemoLcarsTheme(preset = LcarsPreset.ClassicUltra) {
        val colors = LcarsTheme.colorScheme
        var activeConsole by rememberSaveable { mutableStateOf<String?>(null) }

        val destinations = listOf(
            DemoHubDestination(
                id = "core",
                code = "01 / CORE",
                title = "CONTROLS & FRAMES",
                summary = "Interactive inspection for buttons, toggles, segmented controls, directional attitude disc, keypad clusters, elbows, and structural bars.",
                capabilities = listOf(
                    HubCapability("01", "buttons & toggles", "Pill, block cap, and rectangular tactile buttons with stepped alert states."),
                    HubCapability("02", "frames & elbows", "Asymmetric thickness elbows, nested concentric curves, and segmented bars."),
                    HubCapability("03", "keypads & dialogs", "Alternating color rhythm keypads, 4-quadrant attitude disc, and authorization modals."),
                ),
                formFactor = "ANY ORIENTATION",
                dataRequirement = "LOCAL COMPONENTS",
                permissionRequirement = "NONE",
                color = colors.monoAmber,
                onOpen = { activeConsole = "core" },
            ),
            DemoHubDestination(
                id = "data",
                code = "02 / DATA",
                title = "TELEMETRY & DISPLAYS",
                summary = "Real-time telemetry streams, optical subprocessor diagnostic matrix, sensor histograms, log consoles, and 24.2 data cascades.",
                capabilities = listOf(
                    HubCapability("01", "diagnostic matrix", "Hexadecimal data stream with pulsing optical alert core."),
                    HubCapability("02", "histograms & logs", "Multi-color sensor meters and auto-scrolling event log stream."),
                    HubCapability("03", "data cascade & slider", "7-row cascading telemetry stream and tactile segmented slider."),
                ),
                formFactor = "ANY ORIENTATION",
                dataRequirement = "LOCAL SIMULATION",
                permissionRequirement = "NONE",
                color = colors.lightBlue,
                onOpen = { activeConsole = "data" },
            ),
            DemoHubDestination(
                id = "scene",
                code = "03 / SCENE",
                title = "DYNAMICS & SCANNERS",
                summary = "Astrometric star chart, animated target tracking brackets, continuous EPS plasma conduits, and matter reactant injectors.",
                capabilities = listOf(
                    HubCapability("01", "stellar cartography", "Starfield map with zero-allocation label collision resolution."),
                    HubCapability("02", "target tracking", "Expanding animated scanner brackets and targeting crosshairs."),
                    HubCapability("03", "plasma conduits", "Hardware-interpolated flowing EPS energy pipe and reaction chambers."),
                ),
                formFactor = "ANY ORIENTATION",
                dataRequirement = "LOCAL SIMULATION",
                permissionRequirement = "NONE",
                color = colors.auxiliaryTan,
                onOpen = { activeConsole = "scene" },
            ),
            DemoHubDestination(
                id = "theme",
                code = "04 / THEME",
                title = "TOKENS & PALETTES",
                summary = "Compare Classic Ultra, Nemesis Blue Ultra, and Lower Decks PADD tokens, motion modes, and Okuda cadence rhythms.",
                capabilities = listOf(
                    HubCapability("01", "theme presets", "Live switching between 2360s TNG, 2370s Nemesis, and Lower Decks palettes."),
                    HubCapability("02", "cadence rhythm", "Alternating cold/warm color sequence for high-density button columns."),
                    HubCapability("03", "motion accessibility", "Live toggling between System, Reduced, and Off motion modes."),
                ),
                formFactor = "ANY ORIENTATION",
                dataRequirement = "LOCAL TOKENS",
                permissionRequirement = "NONE",
                color = colors.violet,
                onOpen = { activeConsole = "theme" },
            ),
            DemoHubDestination(
                id = "weather",
                code = "05 / WX",
                title = "WEATHER CONSOLE",
                summary = "Inspect a production-style LCARS scene driven by live weather, warning, and location-aware data.",
                capabilities = listOf(
                    HubCapability("01", "live weather", "Loads current conditions and forecast data with fallback."),
                    HubCapability("02", "warning workflow", "Displays NMC alerts, sector filtering, and stepped storm simulation."),
                    HubCapability("03", "responsive console", "Adapts the same scene across compact and wide landscape profiles."),
                ),
                formFactor = "LANDSCAPE",
                dataRequirement = "NETWORK / CACHE",
                permissionRequirement = "LOCATION OPTIONAL",
                color = colors.tacticalGreen,
                onOpen = onOpenWeather,
            ),
            DemoHubDestination(
                id = "padd",
                code = "06 / PAD",
                title = "PADD VARIANT TERMINAL",
                summary = "Portable handheld data terminal featuring compact C-frame and micro-keypad matrix.",
                capabilities = listOf(
                    HubCapability("01", "compact c-frame", "Proportional 1/4 scaled elbow border for handheld screens."),
                    HubCapability("02", "mission log readout", "Mission logs, personnel records, and sensor playback."),
                    HubCapability("03", "tactile thumb keys", "Ergonomic thumb-accessible navigation and trigger keys."),
                ),
                formFactor = "PORTRAIT / PADD",
                dataRequirement = "LOCAL SIMULATION",
                permissionRequirement = "NONE",
                color = colors.commandSecondary,
                onOpen = onOpenPadd,
            ),
        )
        var selectedId by rememberSaveable { mutableStateOf(destinations.first().id) }
        val selected = destinations.first { it.id == selectedId }

        if (activeConsole != null) {
            when (activeConsole) {
                "core" -> CoreControlsFramesShowcaseScreen(onBack = { activeConsole = null })
                "data" -> TelemetryDisplaysShowcaseScreen(onBack = { activeConsole = null })
                "scene" -> DynamicsScannersShowcaseScreen(onBack = { activeConsole = null })
                "theme" -> TokensPalettesShowcaseScreen(onBack = { activeConsole = null })
                else -> { activeConsole = null }
            }
        } else {
            LcarsResponsiveScaffold(
                modifier = modifier
                    .fillMaxSize()
                    .background(colors.background)
                    .windowInsetsPadding(WindowInsets.safeDrawing),
                portrait = {
                    PortraitDashboardHub(
                        destinations = destinations,
                        selected = selected,
                        onSelect = { selectedId = it.id },
                    )
                },
                compactLandscape = {
                    LandscapeDashboardHub(
                        destinations = destinations,
                        selected = selected,
                        compact = true,
                        onSelect = { selectedId = it.id },
                    )
                },
                wideLandscape = {
                    LandscapeDashboardHub(
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

/**
 * Landscape Dashboard built directly upon the Monolithic Unified [LcarsElbowFrame].
 */
@Composable
private fun LandscapeDashboardHub(
    destinations: List<DemoHubDestination>,
    selected: DemoHubDestination,
    compact: Boolean,
    onSelect: (DemoHubDestination) -> Unit,
) {
    val colors = LcarsTheme.colorScheme
    val spacing = LcarsTheme.dimensions

    val spineWidth = if (compact) 120.dp else 160.dp
    val railThickness = if (compact) 24.dp else 30.dp
    val elbowWidth = if (compact) 180.dp else 240.dp
    // Extend the main elbow beam itself downward by one button height (36.dp compact / 48.dp wide)
    val elbowHeight = if (compact) 108.dp else 140.dp
    val outerRadius = if (compact) 54.dp else 72.dp
    val innerRadius = if (compact) 16.dp else 24.dp

    val navButtonBaseColors = listOf(
        colors.commandSecondary, // #FF9966 --butterscotch (First button warm contrast against blue elbow)
        colors.auxiliaryTan,    // #FFCC99 --sunflower
        colors.violet,          // #CC99FF --african-violet
        colors.almondCreme,     // #FFBBAA --almond-creme
        colors.commandSecondary,// #FF9966 --butterscotch
        colors.auxiliaryTan,    // #FFCC99 --sunflower
    )

    LcarsElbowFrame(
        color = colors.framePrimary,
        direction = LcarsElbowDirection.TopLeft,
        spineWidth = spineWidth,
        railThickness = railThickness,
        elbowWidth = elbowWidth,
        elbowHeight = elbowHeight,
        outerRadius = outerRadius,
        innerRadius = innerRadius,
        gap = spacing.gapStandard,
        elbowLabel = "01-4028",
        modifier = Modifier
            .fillMaxSize()
            .padding(spacing.gapLarge),
        spineSlot = {
            // 1. STACK OF COHESIVE NAVIGATION BUTTONS (starts directly below the extended main elbow beam)
            destinations.forEachIndexed { index, destination ->
                val isSelected = destination.id == selected.id
                val baseColor = navButtonBaseColors[index % navButtonBaseColors.size]

                LcarsSpineButton(
                    text = destination.code,
                    onClick = { onSelect(destination) },
                    color = baseColor,
                    selected = isSelected,
                    verticalAlign = LcarsSpineAlign.Bottom,
                    height = if (compact) 36.dp else 48.dp,
                )
            }

            // 2. FLUID VERTICAL FILLER SEGMENT (blue structural spine extension)
            LcarsSpineBlock(
                color = colors.framePrimary,
                modifier = Modifier.weight(1f),
            )

            // 3. BOTTOM ANCHOR BASE PANEL
            LcarsSpineButton(
                text = "DECK 01 / HUB",
                onClick = {},
                color = colors.violet,
                showActiveIndicator = false,
                verticalAlign = LcarsSpineAlign.Bottom,
                height = if (compact) 34.dp else 44.dp,
            )
        },
        railSlot = {
            // TOP HEADER CUTOUT RAIL WITH END CAP (Slot API)
            LcarsCutoutBar(
                color = colors.framePrimary,
                endCap = true,
                height = railThickness,
                gap = spacing.gapStandard,
                modifier = Modifier.fillMaxWidth(),
            ) {
                LcarsText(
                    text = "LCARS DEMOHUB",
                    style = LcarsTheme.typography.header.copy(
                        color = colors.monoAmber,
                        fontSize = (railThickness * 0.72f).let { with(LocalDensity.current) { it.toSp() } },
                    ),
                    maxLines = 1,
                )
            }
        },
    ) {
        // ================= INNER FRAMED VIEWPORT =================
        // Provides deliberate breathing margin away from the elbow inner curve and rails
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = if (compact) 16.dp else 22.dp,
                    top = if (compact) 12.dp else 18.dp,
                    end = if (compact) 8.dp else 12.dp,
                    bottom = if (compact) 8.dp else 12.dp,
                ),
            verticalArrangement = Arrangement.spacedBy(spacing.gapStandard),
        ) {
            // TOP WARNING / SUMMARY BANNER (Okuda Golden-Orange Pill-Banner)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colors.monoAmber, shape = LcarsTheme.shapes.pill)
                    .padding(horizontal = 16.dp, vertical = 5.dp),
                contentAlignment = Alignment.Center,
            ) {
                LcarsText(
                    text = "STATUS NOMINAL: ${selected.title.uppercase()} / READY FOR INITIALIZATION",
                    style = LcarsTheme.typography.labelSmall.copy(color = Color.Black),
                )
            }

            // MAIN SPLIT PANELS: CENTRAL DOSSIER + TELEMETRY DIAGNOSTICS
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(spacing.gapStandard),
            ) {
                DashboardCentralDossier(
                    destination = selected,
                    compact = compact,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                )

                DashboardTelemetrySidePanel(
                    selected = selected,
                    compact = compact,
                    modifier = Modifier
                        .width(if (compact) 180.dp else 220.dp)
                        .fillMaxHeight(),
                )
            }

            // BOTTOM FOOTER STATUS RAIL (Classic Ultra 3-Color Status Bar)
            LcarsSegmentedBar(
                segments = listOf(
                    LcarsBarSegment(0.36f, colors.framePrimary, "SUBSYSTEMS: 06 ONLINE"),
                    LcarsBarSegment(0.34f, colors.commandSecondary, "EPS CONDUITS: 94.2%"),
                    LcarsBarSegment(0.30f, colors.monoAmber, "TELEMETRY LINK: ACTIVE"),
                ),
                height = 22.dp,
                gap = spacing.gapStandard,
                labelColor = Color.Black,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

/**
 * Central Dossier Viewport displaying selected subsystem metrics, capabilities, and launch action.
 */
@Composable
private fun DashboardCentralDossier(
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
        // TOP METRICS RIBBON
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing.gapStandard),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .background(destination.color)
                    .padding(horizontal = 12.dp, vertical = 4.dp),
            ) {
                LcarsText(
                    text = destination.code,
                    style = LcarsTheme.typography.button.copy(color = Color.Black),
                )
            }

            LcarsText(
                text = destination.title,
                style = if (compact) LcarsTheme.typography.titleSmall else LcarsTheme.typography.header,
                modifier = Modifier.weight(1f),
            )

            Box(
                modifier = Modifier
                    .background(colors.panel)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
            ) {
                LcarsText(
                    text = "TARGET ACQUIRED",
                    style = LcarsTheme.typography.labelSmall.copy(color = colors.readoutAccent),
                )
            }
        }

        // SUMMARY DESCRIPTION BOX
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(colors.panel)
                .padding(horizontal = 12.dp, vertical = if (compact) 6.dp else 8.dp),
        ) {
            LcarsText(
                text = destination.summary,
                style = if (compact) LcarsTheme.typography.labelSmall.copy(color = colors.subHighlight)
                else LcarsTheme.typography.telemetry.copy(color = colors.subHighlight),
            )
        }

        // CAPABILITY TILES
        Column(
            modifier = if (!portrait) Modifier.weight(1f) else Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(spacing.gapStandard),
        ) {
            destination.capabilities.forEach { capability ->
                DashboardCapabilityRow(
                    capability = capability,
                    color = destination.color,
                    compact = compact,
                    modifier = if (!portrait) Modifier.weight(1f) else Modifier.fillMaxWidth(),
                )
            }
        }

        // RUNTIME ATTRIBUTE BADGES
        RuntimeProfileBadges(destination = destination, portrait = portrait)

        // PRIMARY LAUNCH BUTTON
        LcarsButton(
            text = "INITIALIZE ${destination.title}",
            onClick = destination.onOpen,
            color = destination.color,
            shape = LcarsButtonShape.BlockEnd,
            minHeight = if (compact) 42.dp else 48.dp,
            modifier = Modifier
                .fillMaxWidth()
                .semantics {
                    contentDescription = "Initialize ${destination.title}"
                },
        )
    }
}

@Composable
private fun DashboardCapabilityRow(
    capability: HubCapability,
    color: Color,
    compact: Boolean,
    modifier: Modifier = Modifier,
) {
    val colors = LcarsTheme.colorScheme
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Box(
            modifier = Modifier
                .width(if (compact) 36.dp else 46.dp)
                .fillMaxHeight()
                .background(color),
            contentAlignment = Alignment.BottomEnd,
        ) {
            LcarsText(
                text = capability.code,
                style = LcarsTheme.typography.button.copy(color = Color.Black),
                modifier = Modifier.padding(4.dp),
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(colors.panel)
                .padding(horizontal = 8.dp, vertical = if (compact) 3.dp else 6.dp),
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
private fun RuntimeProfileBadges(
    destination: DemoHubDestination,
    portrait: Boolean,
) {
    val colors = LcarsTheme.colorScheme
    val spacing = LcarsTheme.dimensions
    val badges = listOf(
        "FORM" to destination.formFactor,
        "DATA" to destination.dataRequirement,
        "ACCESS" to destination.permissionRequirement,
    )

    if (portrait) {
        Column(verticalArrangement = Arrangement.spacedBy(spacing.gapStandard)) {
            badges.forEach { (label, value) ->
                RuntimeBadgeItem(label, value, Modifier.fillMaxWidth())
            }
        }
    } else {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing.gapStandard),
        ) {
            badges.forEach { (label, value) ->
                RuntimeBadgeItem(label, value, Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun RuntimeBadgeItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    val colors = LcarsTheme.colorScheme
    Column(
        modifier = modifier
            .background(colors.panel)
            .padding(horizontal = 8.dp, vertical = 4.dp),
    ) {
        LcarsText(
            text = label,
            style = LcarsTheme.typography.labelSmall.copy(color = colors.commandSecondary, fontSize = 10.sp),
        )
        LcarsText(
            text = value,
            style = LcarsTheme.typography.labelSmall.copy(color = colors.text, fontSize = 11.sp),
        )
    }
}

/**
 * Right-side Telemetry Panel with Technobabble Diagnostics and Real-time Phosphor Green Multi-bar Chart.
 */
@Composable
private fun DashboardTelemetrySidePanel(
    selected: DemoHubDestination,
    compact: Boolean,
    modifier: Modifier = Modifier,
) {
    val colors = LcarsTheme.colorScheme
    val spacing = LcarsTheme.dimensions

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(spacing.gapStandard),
    ) {
        // TELEMETRY HEADER
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(colors.commandSecondary)
                .padding(horizontal = 8.dp, vertical = 4.dp),
        ) {
            LcarsText(
                text = "TELEMETRY 72001",
                style = LcarsTheme.typography.labelSmall.copy(color = Color.Black),
            )
        }

        // STATION DIAGNOSTICS NUMBER MATRIX
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(colors.panel)
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                LcarsText(text = "98851225", style = LcarsTheme.typography.labelSmall.copy(color = colors.monoAmber))
                LcarsText(text = "56 144", style = LcarsTheme.typography.labelSmall.copy(color = colors.lightBlue))
                LcarsText(text = "0879805", style = LcarsTheme.typography.labelSmall.copy(color = colors.text))
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                LcarsText(text = "07 85142", style = LcarsTheme.typography.labelSmall.copy(color = colors.text))
                LcarsText(text = "06 469148", style = LcarsTheme.typography.labelSmall.copy(color = colors.tacticalGreen))
                LcarsText(text = "380370", style = LcarsTheme.typography.labelSmall.copy(color = colors.monoAmber))
            }
        }

        // PHOSPHOR GREEN SPECTRUM BARS
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(colors.panel)
                .padding(8.dp),
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                LcarsText(
                    text = "FREQUENCY SCAN",
                    style = LcarsTheme.typography.labelSmall.copy(color = colors.tacticalGreen, fontSize = 10.sp),
                )
                // Multi-bar green spectrum
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(if (compact) 48.dp else 64.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom,
                ) {
                    val heights = listOf(0.4f, 0.75f, 0.55f, 0.9f, 0.35f, 0.8f, 0.6f, 1.0f, 0.45f, 0.7f)
                    heights.forEach { hFraction ->
                        Box(
                            modifier = Modifier
                                .width(if (compact) 6.dp else 8.dp)
                                .fillMaxHeight(hFraction)
                                .background(colors.tacticalGreen),
                        )
                    }
                }
                LcarsText(
                    text = "HZ: 434.908 // STABLE",
                    style = LcarsTheme.typography.labelSmall.copy(color = colors.subHighlight, fontSize = 9.sp),
                )
            }
        }

        // QUICK ENGAGE BUTTON (Pill shape)
        LcarsButton(
            text = "QUICK ENGAGE",
            onClick = selected.onOpen,
            color = colors.monoAmber,
            shape = LcarsButtonShape.Pill,
            minHeight = if (compact) 34.dp else 38.dp,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

/**
 * Portrait Dashboard Hub built with [LcarsElbowFrame].
 */
@Composable
private fun PortraitDashboardHub(
    destinations: List<DemoHubDestination>,
    selected: DemoHubDestination,
    onSelect: (DemoHubDestination) -> Unit,
) {
    val colors = LcarsTheme.colorScheme
    val spacing = LcarsTheme.dimensions

    LcarsElbowFrame(
        color = colors.framePrimary,
        direction = LcarsElbowDirection.TopLeft,
        spineWidth = 54.dp,
        railThickness = 32.dp,
        elbowWidth = 140.dp,
        elbowHeight = 64.dp,
        outerRadius = 48.dp,
        innerRadius = 16.dp,
        gap = spacing.gapStandard,
        elbowLabel = "01",
        modifier = Modifier
            .fillMaxSize()
            .padding(spacing.gapStandard),
        railSlot = {
            LcarsCutoutBar(
                color = colors.framePrimary,
                endCap = true,
                height = 32.dp,
                gap = spacing.gapStandard,
                modifier = Modifier.fillMaxWidth(),
            ) {
                LcarsText(
                    text = "LCARS DEMOHUB",
                    style = LcarsTheme.typography.header.copy(
                        color = colors.monoAmber,
                        fontSize = 16.sp,
                    ),
                    maxLines = 1,
                )
            }
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 12.dp,
                    top = 12.dp,
                    end = 8.dp,
                    bottom = 8.dp,
                )
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(spacing.gapStandard),
        ) {
            // NAVIGATION BUTTON STRIP
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(spacing.gapStandard),
            ) {
                LcarsText(
                    text = "SUBSYSTEM SELECTION",
                    style = LcarsTheme.typography.labelSmall.copy(color = colors.monoAmber),
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(spacing.gapStandard),
                ) {
                    val portraitRow1Colors = listOf(colors.commandSecondary, colors.auxiliaryTan, colors.violet)
                    destinations.take(3).forEachIndexed { index, destination ->
                        val isSelected = destination.id == selected.id
                        val baseColor = portraitRow1Colors[index % portraitRow1Colors.size]
                        LcarsButton(
                            text = destination.code,
                            onClick = { onSelect(destination) },
                            color = if (isSelected) Color.White else baseColor,
                            shape = LcarsButtonShape.Rectangle,
                            role = Role.Tab,
                            selected = isSelected,
                            minHeight = 38.dp,
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(spacing.gapStandard),
                ) {
                    val portraitRow2Colors = listOf(colors.almondCreme, colors.commandSecondary, colors.auxiliaryTan)
                    destinations.drop(3).forEachIndexed { index, destination ->
                        val isSelected = destination.id == selected.id
                        val baseColor = portraitRow2Colors[index % portraitRow2Colors.size]
                        LcarsButton(
                            text = destination.code,
                            onClick = { onSelect(destination) },
                            color = if (isSelected) Color.White else baseColor,
                            shape = LcarsButtonShape.Rectangle,
                            role = Role.Tab,
                            selected = isSelected,
                            minHeight = 38.dp,
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            }

            // CENTRAL DOSSIER
            DashboardCentralDossier(
                destination = selected,
                compact = true,
                portrait = true,
                modifier = Modifier.fillMaxWidth(),
            )

            // FOOTER BAR
            LcarsSegmentedBar(
                segments = listOf(
                    LcarsBarSegment(0.65f, colors.framePrimary, "SYS STATUS: NOMINAL"),
                    LcarsBarSegment(0.35f, colors.monoAmber, "READY"),
                ),
                height = 28.dp,
                gap = spacing.gapStandard,
                labelColor = Color.Black,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
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