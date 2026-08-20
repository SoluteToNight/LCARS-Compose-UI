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
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lcars.ui.controls.LcarsButton
import com.lcars.ui.controls.LcarsButtonShape
import com.lcars.ui.controls.LcarsDialog
import com.lcars.ui.controls.LcarsDirectionalDisc
import com.lcars.ui.controls.LcarsDiscDirection
import com.lcars.ui.controls.LcarsKeypadColumn
import com.lcars.ui.controls.LcarsKeypadItem
import com.lcars.ui.controls.LcarsPillGrid
import com.lcars.ui.controls.LcarsPillItem
import com.lcars.ui.controls.LcarsSegmentedControl
import com.lcars.ui.controls.LcarsStatusItem
import com.lcars.ui.controls.LcarsStatusList
import com.lcars.ui.controls.LcarsToggle
import com.lcars.ui.display.LcarsAlertLevel
import com.lcars.ui.foundation.LcarsText
import com.lcars.ui.layout.LcarsBar
import com.lcars.ui.layout.LcarsBarSegment
import com.lcars.ui.layout.LcarsCutoutBar
import com.lcars.ui.layout.LcarsDividerGrid
import com.lcars.ui.layout.LcarsDividerGridType
import com.lcars.ui.layout.LcarsElbow
import com.lcars.ui.layout.LcarsElbowDirection
import com.lcars.ui.layout.LcarsElbowFrame
import com.lcars.ui.layout.LcarsResponsiveScaffold
import com.lcars.ui.layout.LcarsSegmentedBar
import com.lcars.ui.layout.LcarsSpineAlign
import com.lcars.ui.layout.LcarsSpineBlock
import com.lcars.ui.layout.LcarsSpineButton
import com.lcars.ui.theme.LcarsTheme

private enum class CoreComponentItem(
    val code: String,
    val shortName: String,
    val title: String,
    val apiClass: String,
    val category: String,
) {
    ButtonToggle(
        "01",
        "BTN",
        "BUTTON & TOGGLE",
        "LcarsButton, LcarsToggle",
        "CONTROLS",
    ),
    SegmentedControl(
        "02",
        "SEG",
        "SEGMENTED CONTROL",
        "LcarsSegmentedControl",
        "CONTROLS",
    ),
    Elbow(
        "03",
        "ELB",
        "ELBOW",
        "LcarsElbow, LcarsElbowFrame",
        "LAYOUT",
    ),
    BarDivider(
        "04",
        "BAR",
        "BAR & DIVIDER",
        "LcarsBar, LcarsSegmentedBar, LcarsDividerGrid",
        "LAYOUT",
    ),
    DirectionalDisc(
        "05",
        "DSC",
        "DIRECTIONAL DISC",
        "LcarsDirectionalDisc",
        "CONTROLS",
    ),
    KeypadCluster(
        "06",
        "PAD",
        "KEYPAD CLUSTER",
        "LcarsKeypadColumn, LcarsKeypadItem",
        "CONTROLS",
    ),
    Dialog(
        "07",
        "DLG",
        "DIALOG",
        "LcarsDialog",
        "OVERLAYS",
    ),
}

@Composable
fun CoreControlsFramesShowcaseScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LcarsTheme.colorScheme

    var selectedItem by rememberSaveable { mutableStateOf(CoreComponentItem.ButtonToggle) }
    var showDialog by rememberSaveable { mutableStateOf(false) }
    var selectedOption by rememberSaveable { mutableStateOf("OPTION A") }
    var toggleState by rememberSaveable { mutableStateOf(true) }
    var alertToggleState by rememberSaveable { mutableStateOf(false) }
    var discDirection by remember { mutableStateOf(LcarsDiscDirection.Center) }
    var lastClickedButton by rememberSaveable { mutableStateOf("NONE") }

    LcarsResponsiveScaffold(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
            .windowInsetsPadding(WindowInsets.safeDrawing),
        portrait = {
            PortraitCoreShowcase(
                selectedItem = selectedItem,
                onSelectItem = { selectedItem = it },
                onBack = onBack,
                lastClickedButton = lastClickedButton,
                onButtonClicked = { lastClickedButton = it },
                onSetShowDialog = { showDialog = it },
                selectedOption = selectedOption,
                onSelectOption = { selectedOption = it },
                toggleState = toggleState,
                onToggleStateChange = { toggleState = it },
                alertToggleState = alertToggleState,
                onAlertToggleChange = { alertToggleState = it },
                discDirection = discDirection,
                onDiscDirectionChange = { discDirection = it },
            )
        },
        compactLandscape = {
            LandscapeCoreShowcase(
                compact = true,
                selectedItem = selectedItem,
                onSelectItem = { selectedItem = it },
                onBack = onBack,
                lastClickedButton = lastClickedButton,
                onButtonClicked = { lastClickedButton = it },
                onSetShowDialog = { showDialog = it },
                selectedOption = selectedOption,
                onSelectOption = { selectedOption = it },
                toggleState = toggleState,
                onToggleStateChange = { toggleState = it },
                alertToggleState = alertToggleState,
                onAlertToggleChange = { alertToggleState = it },
                discDirection = discDirection,
                onDiscDirectionChange = { discDirection = it },
            )
        },
        wideLandscape = {
            LandscapeCoreShowcase(
                compact = false,
                selectedItem = selectedItem,
                onSelectItem = { selectedItem = it },
                onBack = onBack,
                lastClickedButton = lastClickedButton,
                onButtonClicked = { lastClickedButton = it },
                onSetShowDialog = { showDialog = it },
                selectedOption = selectedOption,
                onSelectOption = { selectedOption = it },
                toggleState = toggleState,
                onToggleStateChange = { toggleState = it },
                alertToggleState = alertToggleState,
                onAlertToggleChange = { alertToggleState = it },
                discDirection = discDirection,
                onDiscDirectionChange = { discDirection = it },
            )
        },
    )

    if (showDialog) {
        LcarsDialog(
            title = "CONFIRMATION DIALOG",
            confirmLabel = "CONFIRM",
            dismissLabel = "DISMISS",
            level = LcarsAlertLevel.Warning,
            onConfirm = {
                showDialog = false
                lastClickedButton = "DIALOG CONFIRMED"
            },
            onDismiss = {
                showDialog = false
                lastClickedButton = "DIALOG DISMISSED"
            },
            content = {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    LcarsText(
                        text = "CONTENT SLOT // COMPOSABLE PAYLOAD",
                        style = LcarsTheme.typography.telemetry.copy(color = colors.lightBlue),
                    )
                    LcarsText(
                        text = "PARAMETER VALUE: NOMINAL // LEVEL: WARNING",
                        style = LcarsTheme.typography.labelSmall.copy(color = colors.monoAmber),
                    )
                }
            },
        )
    }
}

@Composable
private fun LandscapeCoreShowcase(
    compact: Boolean,
    selectedItem: CoreComponentItem,
    onSelectItem: (CoreComponentItem) -> Unit,
    onBack: () -> Unit,
    lastClickedButton: String,
    onButtonClicked: (String) -> Unit,
    onSetShowDialog: (Boolean) -> Unit,
    selectedOption: String,
    onSelectOption: (String) -> Unit,
    toggleState: Boolean,
    onToggleStateChange: (Boolean) -> Unit,
    alertToggleState: Boolean,
    onAlertToggleChange: (Boolean) -> Unit,
    discDirection: LcarsDiscDirection,
    onDiscDirectionChange: (LcarsDiscDirection) -> Unit,
) {
    val colors = LcarsTheme.colorScheme
    val spacing = LcarsTheme.dimensions

    val spineWidth = if (compact) 120.dp else 160.dp
    val railThickness = if (compact) 24.dp else 30.dp
    val elbowWidth = if (compact) 180.dp else 240.dp
    val elbowHeight = if (compact) 90.dp else 116.dp
    val outerRadius = if (compact) 54.dp else 72.dp
    val innerRadius = if (compact) 16.dp else 24.dp

    val navButtonBaseColors = listOf(
        colors.commandSecondary,
        colors.auxiliaryTan,
        colors.violet,
        colors.almondCreme,
        colors.commandSecondary,
        colors.auxiliaryTan,
        colors.violet,
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
        elbowLabel = "01-CTRL",
        modifier = Modifier
            .fillMaxSize()
            .padding(spacing.gapLarge),
        spineSlot = {
            // 7 COMPONENT ITEMS: Clean numerical sequence using hardcoded LcarsSpineButton
            CoreComponentItem.entries.forEachIndexed { index, item ->
                val isSelected = item == selectedItem
                val baseColor = navButtonBaseColors[index % navButtonBaseColors.size]

                LcarsSpineButton(
                    text = "${item.code} / ${item.shortName}",
                    onClick = { onSelectItem(item) },
                    color = baseColor,
                    selected = isSelected,
                    verticalAlign = LcarsSpineAlign.Bottom,
                    height = if (compact) 36.dp else 48.dp,
                )
            }

            // STRUCTURAL FILLER SEGMENT
            LcarsSpineBlock(
                color = colors.framePrimary,
                modifier = Modifier.weight(1f),
            )

            // BOTTOM RETURN BUTTON BASE ANCHOR
            LcarsSpineButton(
                text = "« RETURN HUB",
                onClick = onBack,
                color = colors.alertRed,
                showActiveIndicator = false,
                verticalAlign = LcarsSpineAlign.Bottom,
                height = if (compact) 34.dp else 44.dp,
            )
        },
        railSlot = {
            LcarsCutoutBar(
                color = colors.framePrimary,
                endCap = true,
                height = railThickness,
                gap = spacing.gapStandard,
                modifier = Modifier.fillMaxWidth(),
            ) {
                LcarsText(
                    text = "CONTROLS & FRAMES // ${selectedItem.title}",
                    style = LcarsTheme.typography.header.copy(
                        color = colors.monoAmber,
                        fontSize = (railThickness * 0.70f).let { with(LocalDensity.current) { it.toSp() } },
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
                    start = if (compact) 16.dp else 22.dp,
                    top = if (compact) 12.dp else 18.dp,
                    end = if (compact) 8.dp else 12.dp,
                    bottom = if (compact) 8.dp else 12.dp,
                ),
            verticalArrangement = Arrangement.spacedBy(spacing.gapStandard),
        ) {
            // TOP STATUS RIBBON
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colors.monoAmber, shape = LcarsTheme.shapes.pill)
                    .padding(horizontal = 14.dp, vertical = 4.dp),
                contentAlignment = Alignment.Center,
            ) {
                LcarsText(
                    text = "INSPECTION ACTIVE: ${selectedItem.title} // LAST ACTION: $lastClickedButton",
                    style = LcarsTheme.typography.labelSmall.copy(color = Color.Black, fontSize = 11.sp),
                )
            }

            // MAIN 70/30 SPLIT DASHBOARD: INTERACTIVE STAGE (70%) + INSPECTOR PANEL (30%)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(spacing.gapStandard),
            ) {
                // LEFT 70%: SCROLLABLE INTERACTIVE STAGE
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(spacing.gapStandard),
                ) {
                    CoreComponentStageContent(
                        selectedItem = selectedItem,
                        onButtonClicked = onButtonClicked,
                        onSetShowDialog = onSetShowDialog,
                        selectedOption = selectedOption,
                        onSelectOption = onSelectOption,
                        toggleState = toggleState,
                        onToggleStateChange = onToggleStateChange,
                        alertToggleState = alertToggleState,
                        onAlertToggleChange = onAlertToggleChange,
                        discDirection = discDirection,
                        onDiscDirectionChange = onDiscDirectionChange,
                    )
                }

                // RIGHT 30%: SIMPLE COMPONENT INSPECTION & REVIEW PANEL
                ComponentReviewPanel(
                    selectedItem = selectedItem,
                    lastClickedButton = lastClickedButton,
                    selectedOption = selectedOption,
                    toggleState = toggleState,
                    alertToggleState = alertToggleState,
                    discDirection = discDirection,
                    compact = compact,
                    modifier = Modifier
                        .width(if (compact) 240.dp else 300.dp)
                        .fillMaxHeight(),
                )
            }

            // BOTTOM FOOTER BAR
            LcarsSegmentedBar(
                segments = listOf(
                    LcarsBarSegment(0.36f, colors.framePrimary, "CONTROLS: 07 ONLINE"),
                    LcarsBarSegment(0.34f, colors.commandSecondary, "ACTIVE: ${selectedItem.code}"),
                    LcarsBarSegment(0.30f, colors.monoAmber, "INSPECTION: NOMINAL"),
                ),
                height = if (compact) 18.dp else 22.dp,
                gap = spacing.gapStandard,
                labelColor = Color.Black,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun ComponentReviewPanel(
    selectedItem: CoreComponentItem,
    lastClickedButton: String,
    selectedOption: String,
    toggleState: Boolean,
    alertToggleState: Boolean,
    discDirection: LcarsDiscDirection,
    compact: Boolean,
    modifier: Modifier = Modifier,
) {
    val colors = LcarsTheme.colorScheme
    val spacing = LcarsTheme.dimensions

    Column(
        modifier = modifier
            .background(colors.panel)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        // PANEL HEADER
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(colors.commandSecondary)
                .padding(horizontal = 10.dp, vertical = 6.dp),
            contentAlignment = Alignment.CenterStart,
        ) {
            LcarsText(
                text = "SPEC & REVIEW // ${selectedItem.code}",
                style = LcarsTheme.typography.labelMedium.copy(
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                ),
            )
        }

        // COMPONENT IDENTITY
        ReviewField(label = "API CLASS", value = selectedItem.apiClass)
        ReviewField(label = "CATEGORY", value = selectedItem.category)

        // LIVE STATE VALUE
        val liveState = when (selectedItem) {
            CoreComponentItem.ButtonToggle -> "TOGGLE: ${if (toggleState) "ACTIVE" else "STANDBY"}"
            CoreComponentItem.SegmentedControl -> "OPTION: $selectedOption"
            CoreComponentItem.DirectionalDisc -> "BEARING: $discDirection"
            CoreComponentItem.Elbow -> "GEOMETRY: 4-DIR"
            CoreComponentItem.BarDivider -> "SEGMENTS: 3-PART"
            CoreComponentItem.KeypadCluster -> "KEYS: 04 CADENCE"
            CoreComponentItem.Dialog -> "OVERLAY: READY"
        }
        ReviewField(label = "ACTIVE STATE", value = liveState, highlight = true)

        // LAST USER EVENT
        ReviewField(label = "LAST ACTION", value = lastClickedButton)

        // LCARS TOKENS
        ReviewField(label = "STANDARD GAP", value = "4.dp (FIXED)")
        ReviewField(label = "SHAPE RULE", value = "RECTANGLE / PILL")

        Spacer(modifier = Modifier.weight(1f))

        // STATUS BADGE
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(colors.monoAmber)
                .padding(horizontal = 10.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center,
        ) {
            LcarsText(
                text = "STATUS: VERIFIED // OK",
                style = LcarsTheme.typography.labelMedium.copy(
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                ),
            )
        }
    }
}

@Composable
private fun ReviewField(
    label: String,
    value: String,
    highlight: Boolean = false,
) {
    val colors = LcarsTheme.colorScheme
    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
        LcarsText(
            text = label,
            style = LcarsTheme.typography.labelSmall.copy(
                color = colors.subHighlight,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
            ),
        )
        LcarsText(
            text = value,
            style = LcarsTheme.typography.telemetry.copy(
                color = if (highlight) colors.monoAmber else colors.text,
                fontSize = 15.sp,
                fontWeight = if (highlight) FontWeight.Bold else FontWeight.Normal,
            ),
            maxLines = 2,
        )
    }
}

@Composable
private fun PortraitCoreShowcase(
    selectedItem: CoreComponentItem,
    onSelectItem: (CoreComponentItem) -> Unit,
    onBack: () -> Unit,
    lastClickedButton: String,
    onButtonClicked: (String) -> Unit,
    onSetShowDialog: (Boolean) -> Unit,
    selectedOption: String,
    onSelectOption: (String) -> Unit,
    toggleState: Boolean,
    onToggleStateChange: (Boolean) -> Unit,
    alertToggleState: Boolean,
    onAlertToggleChange: (Boolean) -> Unit,
    discDirection: LcarsDiscDirection,
    onDiscDirectionChange: (LcarsDiscDirection) -> Unit,
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
                    text = "CONTROLS & FRAMES",
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
            // RETURN BUTTON
            LcarsButton(
                text = "« RETURN TO DEMOHUB",
                onClick = onBack,
                color = colors.alertRed,
                shape = LcarsButtonShape.Rectangle,
                minHeight = 36.dp,
                modifier = Modifier.fillMaxWidth(),
            )

            // COMPONENT SELECTION BUTTONS STRIP
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(spacing.gapStandard),
                ) {
                    CoreComponentItem.entries.take(4).forEach { item ->
                        val isSelected = item == selectedItem
                        LcarsButton(
                            text = item.code,
                            onClick = { onSelectItem(item) },
                            color = if (isSelected) Color.White else colors.commandSecondary,
                            shape = LcarsButtonShape.Rectangle,
                            role = Role.Tab,
                            selected = isSelected,
                            minHeight = 34.dp,
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(spacing.gapStandard),
                ) {
                    CoreComponentItem.entries.drop(4).forEach { item ->
                        val isSelected = item == selectedItem
                        LcarsButton(
                            text = item.code,
                            onClick = { onSelectItem(item) },
                            color = if (isSelected) Color.White else colors.auxiliaryTan,
                            shape = LcarsButtonShape.Rectangle,
                            role = Role.Tab,
                            selected = isSelected,
                            minHeight = 34.dp,
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            }

            // STATUS BANNER
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colors.monoAmber, shape = LcarsTheme.shapes.pill)
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                contentAlignment = Alignment.Center,
            ) {
                LcarsText(
                    text = "${itemCodeLabel(selectedItem)} // ${selectedItem.title}",
                    style = LcarsTheme.typography.labelSmall.copy(color = Color.Black, fontSize = 11.sp),
                )
            }

            // MAIN VIEWPORT STAGE CONTENT (in Column, no overlap)
            CoreComponentStageContent(
                selectedItem = selectedItem,
                onButtonClicked = onButtonClicked,
                onSetShowDialog = onSetShowDialog,
                selectedOption = selectedOption,
                onSelectOption = onSelectOption,
                toggleState = toggleState,
                onToggleStateChange = onToggleStateChange,
                alertToggleState = alertToggleState,
                onAlertToggleChange = onAlertToggleChange,
                discDirection = discDirection,
                onDiscDirectionChange = onDiscDirectionChange,
            )

            // FOOTER BAR
            LcarsSegmentedBar(
                segments = listOf(
                    LcarsBarSegment(0.65f, colors.framePrimary, "STATUS: NOMINAL"),
                    LcarsBarSegment(0.35f, colors.monoAmber, "READY"),
                ),
                height = 24.dp,
                gap = spacing.gapStandard,
                labelColor = Color.Black,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

private fun itemCodeLabel(item: CoreComponentItem): String = "${item.code} / ${item.shortName}"

@Composable
private fun CoreComponentStageContent(
    selectedItem: CoreComponentItem,
    onButtonClicked: (String) -> Unit,
    onSetShowDialog: (Boolean) -> Unit,
    selectedOption: String,
    onSelectOption: (String) -> Unit,
    toggleState: Boolean,
    onToggleStateChange: (Boolean) -> Unit,
    alertToggleState: Boolean,
    onAlertToggleChange: (Boolean) -> Unit,
    discDirection: LcarsDiscDirection,
    onDiscDirectionChange: (LcarsDiscDirection) -> Unit,
) {
    val colors = LcarsTheme.colorScheme
    val spacing = LcarsTheme.dimensions

    when (selectedItem) {
        CoreComponentItem.ButtonToggle -> {
            ComponentStageCard(title = "01 // LCARS BUTTON SHAPES") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    LcarsButton(
                        text = "PILL",
                        onClick = { onButtonClicked("PILL") },
                        shape = LcarsButtonShape.Pill,
                        color = colors.monoAmber,
                        modifier = Modifier.weight(1f),
                    )
                    LcarsButton(
                        text = "BLOCK START",
                        onClick = { onButtonClicked("BLOCK START") },
                        shape = LcarsButtonShape.BlockStart,
                        color = colors.auxiliaryTan,
                        modifier = Modifier.weight(1f),
                    )
                    LcarsButton(
                        text = "BLOCK END",
                        onClick = { onButtonClicked("BLOCK END") },
                        shape = LcarsButtonShape.BlockEnd,
                        color = colors.lightBlue,
                        modifier = Modifier.weight(1f),
                    )
                    LcarsButton(
                        text = "RECTANGLE",
                        onClick = { onButtonClicked("RECTANGLE") },
                        shape = LcarsButtonShape.Rectangle,
                        color = colors.violet,
                        modifier = Modifier.weight(1f),
                    )
                }
            }

            ComponentStageCard(title = "02 // LCARS BUTTON ALERT LEVELS") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    LcarsButton(
                        text = "NORMAL",
                        onClick = { onButtonClicked("ALERT NORMAL") },
                        color = colors.commandPrimary,
                        alertLevel = LcarsAlertLevel.Normal,
                        modifier = Modifier.weight(1f),
                    )
                    LcarsButton(
                        text = "ADVISORY",
                        onClick = { onButtonClicked("ALERT ADVISORY") },
                        color = colors.commandPrimary,
                        alertLevel = LcarsAlertLevel.Advisory,
                        modifier = Modifier.weight(1f),
                    )
                    LcarsButton(
                        text = "WARNING",
                        onClick = { onButtonClicked("ALERT WARNING") },
                        color = colors.commandPrimary,
                        alertLevel = LcarsAlertLevel.Warning,
                        modifier = Modifier.weight(1f),
                    )
                    LcarsButton(
                        text = "CRITICAL",
                        onClick = { onButtonClicked("ALERT CRITICAL") },
                        color = colors.commandPrimary,
                        alertLevel = LcarsAlertLevel.Critical,
                        modifier = Modifier.weight(1f),
                    )
                }
            }

            ComponentStageCard(title = "03 // 2-COLUMN PAIRED PILL GRID (.PILLBOX)") {
                LcarsPillGrid(
                    items = listOf(
                        LcarsPillItem("J-001", { onButtonClicked("PILL: J-001") }, colors.alertRed, LcarsButtonShape.BlockStart),
                        LcarsPillItem("R-002", { onButtonClicked("PILL: R-002") }, colors.monoAmber, LcarsButtonShape.Rectangle),
                        LcarsPillItem("R-003", { onButtonClicked("PILL: R-003") }, colors.framePrimary, LcarsButtonShape.BlockStart),
                        LcarsPillItem("I-004", { onButtonClicked("PILL: I-004") }, colors.commandSecondary, LcarsButtonShape.Rectangle),
                        LcarsPillItem("C-005", { onButtonClicked("PILL: C-005") }, colors.monoAmber, LcarsButtonShape.BlockStart),
                        LcarsPillItem("A-006", { onButtonClicked("PILL: A-006") }, colors.violet, LcarsButtonShape.Rectangle),
                    ),
                    buttonHeight = 52.dp,
                    gap = 8.dp,
                )
            }

            ComponentStageCard(title = "04 // TELEMETRY STATUS LIST (.LCARS-LIST-2)") {
                LcarsStatusList(
                    items = listOf(
                        LcarsStatusItem("SUBSPACE LINK: ESTABLISHED", colors.monoAmber, colors.monoAmber),
                        LcarsStatusItem("STARFLEET DATABASE: CONNECTED", colors.monoAmber, colors.monoAmber),
                        LcarsStatusItem("QUANTUM MEMORY FIELD: STABLE", colors.monoAmber, colors.monoAmber),
                        LcarsStatusItem("OPTICAL DATA NETWORK: REROUTING", colors.commandSecondary, colors.commandSecondary),
                    ),
                )
            }

            ComponentStageCard(title = "05 // ASYMMETRICAL PILL GRID WITH SPACER (.PILLBOX-2)") {
                LcarsPillGrid(
                    items = listOf(
                        LcarsPillItem("F12-22", { onButtonClicked("PILL: F12-22") }, colors.violet, LcarsButtonShape.BlockStart),
                        LcarsPillItem("G24-22", { onButtonClicked("PILL: G24-22") }, colors.violet, LcarsButtonShape.BlockEnd),
                        LcarsPillItem(null), // Asymmetric empty spacer slot
                        LcarsPillItem("H-07AM", { onButtonClicked("PILL: H-07AM") }, colors.lightBlue, LcarsButtonShape.BlockEnd),
                        LcarsPillItem("I50-72", { onButtonClicked("PILL: I50-72") }, colors.monoAmber, LcarsButtonShape.BlockStart),
                        LcarsPillItem("J5369", { onButtonClicked("PILL: J5369") }, colors.commandSecondary, LcarsButtonShape.BlockEnd),
                    ),
                    buttonHeight = 52.dp,
                    gap = 8.dp,
                )
            }

            ComponentStageCard(title = "06 // LCARS TOGGLE SWITCHES") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    LcarsToggle(
                        checked = toggleState,
                        onCheckedChange = {
                            onToggleStateChange(it)
                            onButtonClicked(if (it) "TOGGLE: ACTIVE" else "TOGGLE: STANDBY")
                        },
                        checkedLabel = "TOGGLE: ACTIVE",
                        uncheckedLabel = "TOGGLE: STANDBY",
                        activeColor = colors.monoAmber,
                        modifier = Modifier.weight(1f),
                    )
                    LcarsToggle(
                        checked = alertToggleState,
                        onCheckedChange = {
                            onAlertToggleChange(it)
                            onButtonClicked(if (it) "ALERT TOGGLE: ON" else "ALERT TOGGLE: OFF")
                        },
                        checkedLabel = "ALERT: ONLINE",
                        uncheckedLabel = "ALERT: STANDBY",
                        activeColor = colors.alertRed,
                        alerting = alertToggleState,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }

        CoreComponentItem.SegmentedControl -> {
            ComponentStageCard(title = "01 // LCARS SEGMENTED CONTROL (4-OPTION)") {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    LcarsText(
                        text = "ACTIVE SELECTION: $selectedOption",
                        style = LcarsTheme.typography.labelSmall.copy(color = colors.monoAmber),
                    )
                    LcarsSegmentedControl(
                        options = listOf("OPTION A", "OPTION B", "OPTION C", "OPTION D"),
                        selectedOption = selectedOption,
                        onOptionSelected = {
                            onSelectOption(it)
                            onButtonClicked("SEGMENT: $it")
                        },
                        selectedColor = colors.monoAmber,
                        unselectedColor = colors.commandSecondary,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }

            ComponentStageCard(title = "02 // 3-TAB SEGMENTED BAR WITH ALERTING") {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    LcarsSegmentedControl(
                        options = listOf("TAB 01", "TAB 02", "TAB 03"),
                        selectedOption = if (selectedOption == "OPTION A" || selectedOption == "OPTION B") "TAB 01" else "TAB 02",
                        onOptionSelected = { onButtonClicked("TAB: $it") },
                        selectedColor = colors.lightBlue,
                        unselectedColor = colors.auxiliaryTan,
                        alerting = alertToggleState,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }

        CoreComponentItem.Elbow -> {
            ComponentStageCard(title = "01 // 4-DIRECTIONAL ELBOW GEOMETRY") {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        LcarsElbow(
                            text = "TOP LEFT",
                            color = colors.framePrimary,
                            nestedColor = colors.violet,
                            direction = LcarsElbowDirection.TopLeft,
                            wingWidth = 140.dp,
                            wingHeight = 64.dp,
                            thickness = 34.dp,
                            modifier = Modifier.weight(1f),
                        )
                        LcarsElbow(
                            text = "TOP RIGHT",
                            color = colors.auxiliaryTan,
                            direction = LcarsElbowDirection.TopRight,
                            wingWidth = 140.dp,
                            wingHeight = 64.dp,
                            thickness = 34.dp,
                            modifier = Modifier.weight(1f),
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        LcarsElbow(
                            text = "BOTTOM LEFT",
                            color = colors.lightBlue,
                            direction = LcarsElbowDirection.BottomLeft,
                            wingWidth = 140.dp,
                            wingHeight = 64.dp,
                            thickness = 34.dp,
                            modifier = Modifier.weight(1f),
                        )
                        LcarsElbow(
                            text = "BOTTOM RIGHT",
                            color = colors.monoAmber,
                            direction = LcarsElbowDirection.BottomRight,
                            wingWidth = 140.dp,
                            wingHeight = 64.dp,
                            thickness = 34.dp,
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            }

            ComponentStageCard(title = "02 // SPINE VERTICAL ALIGNMENT (TOP / CENTER / BOTTOM)") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(spacing.gapStandard),
                    ) {
                        LcarsText(
                            text = "TOP-END (HEADER/MARKER)",
                            style = LcarsTheme.typography.labelSmall.copy(color = colors.monoAmber, fontSize = 11.sp),
                        )
                        LcarsSpineButton(
                            text = "01 / TOP ANCHOR",
                            onClick = { onButtonClicked("SPINE: TOP ANCHOR") },
                            color = colors.commandPrimary,
                            verticalAlign = LcarsSpineAlign.Top,
                            height = 68.dp,
                        )
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(spacing.gapStandard),
                    ) {
                        LcarsText(
                            text = "CENTER-END (STATUS/READOUT)",
                            style = LcarsTheme.typography.labelSmall.copy(color = colors.monoAmber, fontSize = 11.sp),
                        )
                        LcarsSpineButton(
                            text = "02 / CENTERED",
                            onClick = { onButtonClicked("SPINE: CENTERED") },
                            color = colors.commandSecondary,
                            verticalAlign = LcarsSpineAlign.Center,
                            height = 68.dp,
                        )
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(spacing.gapStandard),
                    ) {
                        LcarsText(
                            text = "BOTTOM-END (STANDARD NAV)",
                            style = LcarsTheme.typography.labelSmall.copy(color = colors.monoAmber, fontSize = 11.sp),
                        )
                        LcarsSpineButton(
                            text = "03 / BOTTOM NAV",
                            onClick = { onButtonClicked("SPINE: BOTTOM NAV") },
                            color = colors.auxiliaryTan,
                            verticalAlign = LcarsSpineAlign.Bottom,
                            height = 68.dp,
                        )
                    }
                }
            }
        }

        CoreComponentItem.BarDivider -> {
            ComponentStageCard(title = "01 // SOLID & SEGMENTED RATIO BARS") {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    LcarsBar(
                        color = colors.monoAmber,
                        height = 22.dp,
                        startCap = true,
                        endCap = true,
                        label = "SOLID BAR (START + END CAPS)",
                        labelColor = Color.Black,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    LcarsSegmentedBar(
                        segments = listOf(
                            LcarsBarSegment(0.45f, colors.framePrimary, "SEGMENT 01 (45%)"),
                            LcarsBarSegment(0.35f, colors.lightBlue, "SEGMENT 02 (35%)"),
                            LcarsBarSegment(0.20f, colors.auxiliaryTan, "SEGMENT 03 (20%)"),
                        ),
                        height = 22.dp,
                        gap = spacing.gapStandard,
                        labelColor = Color.Black,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }

            ComponentStageCard(title = "02 // LCARS DIVIDER GRIDS") {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    LcarsText(text = "DIVIDER GRID: TYPE 1", style = LcarsTheme.typography.labelSmall.copy(color = colors.subHighlight))
                    LcarsDividerGrid(type = LcarsDividerGridType.Type1, modifier = Modifier.fillMaxWidth())
                    LcarsText(text = "DIVIDER GRID: TYPE 2", style = LcarsTheme.typography.labelSmall.copy(color = colors.subHighlight))
                    LcarsDividerGrid(type = LcarsDividerGridType.Type2, modifier = Modifier.fillMaxWidth())
                }
            }
        }

        CoreComponentItem.DirectionalDisc -> {
            ComponentStageCard(title = "01 // 4-QUADRANT DIRECTIONAL ATTITUDE DISC") {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    LcarsText(
                        text = "CURRENT BEARING: $discDirection",
                        style = LcarsTheme.typography.labelMedium.copy(color = colors.monoAmber),
                    )
                    LcarsDirectionalDisc(
                        size = 160.dp,
                        baseColor = colors.auxiliaryTan,
                        crossColor = colors.monoAmber,
                        activeColor = Color.White,
                        onDirectionSelected = {
                            onDiscDirectionChange(it)
                            onButtonClicked("DISC: $it")
                        },
                    )
                }
            }
        }

        CoreComponentItem.KeypadCluster -> {
            ComponentStageCard(title = "01 // KEYPAD CLUSTER (CADENCE RHYTHM)") {
                val keypadItems = listOf(
                    LcarsKeypadItem("01", "KEY 01") { onButtonClicked("KEYPAD: 01") },
                    LcarsKeypadItem("02", "KEY 02") { onButtonClicked("KEYPAD: 02") },
                    LcarsKeypadItem("03", "KEY 03") { onButtonClicked("KEYPAD: 03") },
                    LcarsKeypadItem("04", "KEY 04") { onButtonClicked("KEYPAD: 04") },
                )
                LcarsKeypadColumn(
                    items = keypadItems,
                    buttonHeight = 36.dp,
                    gap = spacing.gapStandard,
                    shape = LcarsButtonShape.BlockStart,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }

        CoreComponentItem.Dialog -> {
            ComponentStageCard(title = "01 // LCARS DIALOG MODAL TRIGGER") {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    LcarsText(
                        text = "LCARS Dialog features upper color identification bars, action buttons, and customizable slot content.",
                        style = LcarsTheme.typography.telemetry.copy(color = colors.text),
                    )
                    LcarsButton(
                        text = "OPEN DIALOG MODAL",
                        onClick = {
                            onSetShowDialog(true)
                            onButtonClicked("OPEN DIALOG")
                        },
                        color = colors.monoAmber,
                        shape = LcarsButtonShape.Pill,
                        modifier = Modifier.fillMaxWidth(),
                        minHeight = 44.dp,
                    )
                }
            }
        }
    }
}

@Composable
private fun ComponentStageCard(
    title: String,
    content: @Composable () -> Unit,
) {
    val colors = LcarsTheme.colorScheme
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.panel)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        LcarsText(
            text = title,
            style = LcarsTheme.typography.labelSmall.copy(
                color = colors.monoAmber,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
            ),
        )
        content()
    }
}
