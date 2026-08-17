# LCARS Compose Components

This document describes the reusable Jetpack Compose components in the `:lcars-ui` module. The components follow the LCARS visual rules: black base surfaces, flat color geometry, tight spacing, asymmetric rounded blocks, uppercase text, and stepped alert animations.

Chinese version: [COMPONENTS.zh-CN.md](COMPONENTS.zh-CN.md).

## Basic Usage

Place LCARS components under `LcarsTheme`. Import APIs from their responsibility package rather than the old root package.

```kotlin
LcarsTheme(spec = LcarsPreset.ClassicUltra.spec) {
    LcarsFramePanel(title = "sensor deck") {
        LcarsStatusLight(label = "sensor lock", active = true)
        LcarsProgressBar(progress = 0.64f, label = "reactor balance")
    }
}
```

## Theme And Tokens

### `LcarsTheme`

Purpose: provides an immutable LCARS specification without depending on Material color or dynamic color.

Main pieces:

- `LcarsPreset`: exactly three reference-backed presets: `ClassicUltra`, `NemesisBlueUltra`, and `LowerDecksPadd`.
- `LcarsColorScheme`: semantic roles for backgrounds, controls, telemetry, status, and alerts.
- `LcarsTypography`: text styles for headers, buttons, telemetry, and small labels.
- `LcarsDimensions`: standard gaps, touch targets, bars, elbows, rails, and responsive geometry.
- `LcarsShapes`: reusable LCARS geometry contracts.
- `LcarsMotionScheme` / `LcarsMotionMode`: stepped animation timing and system/reduced/off behavior.
- `LcarsSoundPlayer`: optional host-provided sound; the default implementation is silent.

Use for: app roots, previews, and isolated component examples.

Preset application:

```kotlin
LcarsTheme(spec = LcarsPreset.NemesisBlueUltra.spec) {
    AppContent()
}
```

Create a custom spec by copying a preset:

```kotlin
LcarsTheme(spec = LcarsPreset.ClassicUltra.spec.copy(
    colorScheme = customColors,
    dimensions = customDimensions,
)) {
    AppContent()
}
```

Read tokens through `LcarsTheme.colorScheme`, `typography`, `dimensions`, `shapes`, and `motionScheme`. Use `LcarsResponsiveScaffold` or `resolveLcarsSizeClass` for container-based adaptation.

### `LcarsPhonePaddTheme`

Purpose: provides compact phone-PADD typography and spacing using the same theme spec. It defaults to `LcarsPreset.LowerDecksPadd` and accepts any of the three presets.

Use for: isolated phone portrait PADD screens and demos that should not inherit the larger console geometry density.

## Geometry Primitives

### `LcarsButton`

Purpose: LCARS command button with pill, start-rounded, end-rounded, and rectangular shapes. Text is uppercased and aligned toward the bottom-right by default.

Key parameters:

- `text`: button label.
- `onClick`: click callback.
- `color` / `contentColor`: background and text colors.
- `shape`: `Pill`, `BlockStart`, `BlockEnd`, or `Rectangle`.
- `alertLevel`: selects optional stepped alert behavior and semantic severity.
- `enabled`: disabled controls remain visible at reduced opacity and expose disabled semantics.

Use for: commands, mode actions, confirmations, and side control groups.

### `LcarsBar`

Purpose: horizontal LCARS bar with optional rounded caps and an embedded label.

Key parameters:

- `color` / `height`: bar color and height.
- `startCap` / `endCap`: rounded cap controls.
- `label`: embedded label text.
- `labelAlign`: start, center, or end alignment.

Use for: page headers, section headers, status footers, and frame edges.

### `LcarsElbow`

Purpose: draws the classic LCARS large-radius elbow geometry.

Key parameters:

- `direction`: `TopLeft`, `TopRight`, `BottomLeft`, or `BottomRight`.
- `wingWidth` / `wingHeight`: overall size.
- `thickness`: block thickness.
- `text`: short label inside the elbow.

Use for: PADD corners, console wings, and strong visual anchors.

### `LcarsFramePanel`

Purpose: content panel with an LCARS title bar and optional footer bar.

Key parameters:

- `title`: panel title.
- `footerLabel`: optional footer label.
- `content`: panel content slot.

Use for: catalog sections, status panels, and data panels.

## Pattern Components

These components sit between atoms and full business scenes. They extract repeated LCARS compositions from demos without binding to weather, NMEA, or other domain data.

### `LcarsSegmentedBar`

Purpose: weighted multi-segment LCARS color bar with optional labels inside each segment.

Key parameters:

- `segments`: list of `LcarsBarSegment`, each with `weight`, `color`, and optional `label`.
- `height`: bar height.
- `gap`: gap between segments.
- `labelColor`: segment label color.

Use for: console top frame bars, status color strips, and PADD variant sample bars.

### `LcarsConsoleFrame`

Purpose: abstracts a typical console shell: left rail, top bar, main content area, and L-shaped corner bridge.

Key parameters:

- `leftRail`: left slot.
- `topBar`: top bar slot.
- `content`: main content slot.
- `compact`, `railWidth`, `topBarHeight`: size overrides.
- `frameColor`: primary frame color.

Use for: weather panels, ship consoles, and wide landscape dashboard shells.

### `LcarsFramedCommandRail`

Purpose: command rail with a rounded top frame base, suitable for replacing hand-written side frame button groups in demos.

Key parameters:

- `items`: list of `LcarsFramedRailItem`.
- `side`: rounding direction.
- `topInset` / `topCornerRadius`: top elbow geometry.
- `header` / `footer`: optional slots.
- `onCommandClick`: command callback.

Use for: main navigation, mode switching, alert controls, and side frames with large filler blocks.

### `LcarsOptionStrip`

Purpose: customizable LCARS option strip with built-in selected border and bottom label rail.

Key parameters:

- `items`: option data.
- `selectedItem`: selected item, nullable.
- `onSelect`: selection callback.
- `label`: bottom label mapper.
- `itemContent`: custom content for each option block.

Use for: forecast day strips, PADD variant selectors, and icon/thumbnail option groups.

## Controls

### `LcarsCommandRail`

Purpose: standard LCARS command rail that combines command buttons, passive blocks, spacer blocks, and alert blocks.

Key parameters:

- `items`: list of `LcarsCommandRailItem`.
- `side`: start or end side, which controls one-sided rounding.
- `compact`: uses a tighter size.
- `onCommandClick`: callback for command items.

`LcarsCommandRailItemType`:

- `Command`: clickable command.
- `PassiveBlock`: informational geometry block.
- `SpacerBlock`: solid filler block.
- `AlertBlock`: command block with stepped alert flashing.

Use for: left/right command bars, PADD quick commands, and fixed navigation rails.

### `LcarsSegmentedControl`

Purpose: LCARS mode selector with mutually exclusive geometric segments.

Key parameters:

- `options`: option labels.
- `selectedOption`: current selection.
- `onOptionSelected`: selection callback.
- `enabledOptions`: enabled option set.
- `alerting`: flashes the selected segment.

Use for: `NAV / COMM / SENSOR`, view modes, and workflow state switching.

### `LcarsToggle`

Purpose: binary LCARS state control. It is built from LCARS geometry rather than a platform switch.

Key parameters:

- `checked`: current state.
- `onCheckedChange`: state change callback.
- `checkedLabel` / `uncheckedLabel`: state labels.
- `alerting`: enables alert flashing.

Use for: online/standby, armed/disarmed, locked/unlocked, authorized/unauthorized.

### `LcarsDialog`

Purpose: LCARS-styled confirmation, warning, or authorization dialog. It uses the Android dialog container but avoids Material visual styling.

Key parameters:

- `title` / `message`: dialog title and body.
- `confirmLabel` / `dismissLabel`: action labels.
- `level`: `Normal`, `Advisory`, `Warning`, or `Critical`.
- `onConfirm` / `onDismiss`: action callbacks.

Use for: destructive confirmations, authorization prompts, and warning messages.

## Data And Status Displays

### `LcarsTelemetryPanel`

Purpose: responsive telemetry grid with labels, values, and status colors.

Key parameters:

- `title`: panel title.
- `entries`: list of `LcarsTelemetryEntry`.
- `alerting`: changes normal-state rendering during alerts.
- `singleColumnBelow`: width breakpoint for single-column layout.
- `compact`: uses tighter typography and padding.
- `layout`: `Grid`, `CompactGrid`, or `Inline`. `Inline` is for compressed horizontal readout strips.

Use for: coordinates, sensor status, system metrics, and device state.

### `LcarsDataTable`

Purpose: dense status table with headers and highlighted rows.

Key parameters:

- `headers`: header labels.
- `rows`: list of `LcarsDataRow`.

Use for: subsystem status, diagnostic codes, and compact task lists.

### `LcarsLogConsole`

Purpose: log/event stream panel with severity colors. It displays a fixed number of lines and avoids nested vertical scrolling issues inside scrollable pages.

Key parameters:

- `entries`: list of `LcarsLogEntry`.
- `maxLines`: number of visible rows.
- `compact`: tighter padding.
- `autoScroll`: when `true`, shows the latest entries; when `false`, shows the first entries.

Use for: event logs, diagnostics, communication streams, and background task status.

### `LcarsNumberMatrix`

Purpose: stable pseudo-random numeric matrix with optional running row highlight.

Key parameters:

- `rows` / `columns`: matrix size.
- `seed`: random seed. The same seed produces the same output.
- `running`: animates the highlighted row.
- `highlightedRow`: manually selects the highlighted row.

Use for: LCARS data texture, scanner panels, and diagnostic matrices.

### `LcarsStarCoords`

Purpose: periodically refreshed star coordinate readout.

Key parameters:

- `count`: number of coordinate lines.
- `digits`: digit count.
- `updateIntervalMillis`: refresh interval.
- `running`: enables automatic updates.
- `seed`: initial random seed.

Use for: navigation charts, scanner screens, and coordinate overlays.

### `LcarsNumericLabel`

Purpose: numeric label with start and end pill blocks, inspired by the LCARS reference numeric labels.

Key parameters:

- `label`: numeric text.
- `color`: geometry and number color.
- `height`: label height.

Use for: area numbers, panel IDs, and device identifiers.

## Dynamic Visuals

### `LcarsAlertBanner`

Purpose: alert banner. Critical alerts use stepped flashing.

Use for: global alerts, status summaries, and danger notices.

### `LcarsStatusLight`

Purpose: status light with a label. Supports active/inactive and alerting states.

Use for: sensor lock, connection state, subsystem online state.

### `LcarsProgressBar`

Purpose: flat LCARS progress bar with optional label and percentage.

Use for: energy balance, task progress, loading state.

### `LcarsSegmentedMeter`

Purpose: meter made of rectangular segments.

Use for: power level, signal strength, capacity readout.

### `LcarsScannerSweep`

Purpose: horizontal scanner sweep with grid lines.

Use for: scan areas, radar/sensor readouts, and dynamic background panels.

### `LcarsTargetScanner`

Purpose: expanding target-selection brackets that simulate scan or lock workflows.

Key parameters:

- `running`: plays the scanner animation.
- `color`: bracket color.
- `scanDurationMillis`: scan cycle duration.

Use for: target lock, sensor search, and star chart overlays.

### `LcarsReadoutTicker`

Purpose: single-line readout that cycles through values.

Use for: status broadcasts, polling messages, and telemetry summaries.

## Layout And Framing

### `LcarsAppScaffold`

Purpose: generic app shell with a top title bar, left control column, content deck, and optional footer.

Use for: tablet or desktop-sized LCARS pages.

### `LcarsPaddScaffold`

Purpose: PADD-style page shell for portrait and narrow layouts.

Use for: phone portrait screens, handheld interfaces, and single-page tools.

### `LcarsPhonePaddScaffold`

Purpose: phone portrait PADD shell with a compact header, optional side rail, content deck, optional footer controls, and status strip. It is tuned for handheld PADD references with fewer decorative geometry blocks than the larger console layouts.

Key parameters:

- `title`: primary top status-strip label.
- `registry`: compact header registry label.
- `footerLabel`: optional bottom status-strip label.
- `sideRail`: enables or hides the compact left rail.
- `footer`: slot for bottom command controls.
- `content`: vertical content deck.

Use for: phone portrait PADD variants, mobile task screens, compact logs, and handheld data readouts.

### Phone PADD Primitives

Purpose: PADD-specific building blocks for composing handheld layouts without changing the existing LCARS primitives.

Components:

- `LcarsPaddHeader`: compact black readout header plus orange status strip.
- `LcarsPaddSideRail`: sparse vertical PADD rail with a few colored blocks.
- `LcarsPaddStatusStrip`: thin orange/violet status strip with optional embedded label.
- `LcarsPaddControl`: compact command block with pill, left-cap, right-cap, and rectangle shapes.
- `LcarsPaddReadoutPanel`: black bordered panel for dense text/data.
- `LcarsPaddDataLines`: uppercase single-line telemetry/log text.
- `LcarsPaddMessage`: centered large message panel, including alert presentation.

Example:

```kotlin
LcarsPhonePaddTheme(preset = LcarsPreset.LowerDecksPadd) {
    LcarsPhonePaddScaffold(
        title = "systems data 21-0071",
        registry = "uss raven - database 83-s28",
    ) {
        LcarsPaddReadoutPanel(title = "hansen family") {
            LcarsPaddDataLines(
                lines = listOf("archive link nominal", "message buffer ready"),
            )
        }
    }
}
```

### `LcarsConsoleScaffold` / `LcarsMainConsole`

Purpose: wide console layout with a left wing and main deck.

Use for: landscape consoles, demo main screens, and operator views.

### `LcarsResponsiveScaffold`

Purpose: selects one of three layout slots based on available size: portrait, compact landscape, or wide landscape.

Key parameters:

- `portrait`: portrait/narrow slot.
- `compactLandscape`: low-height landscape slot.
- `wideLandscape`: wide landscape slot.
- `compactWidth` / `compactLandscapeHeight`: breakpoint values.

Use for: pages that must adapt across phone portrait, foldable landscape, and tablet/desktop landscape.

### `LcarsDividerGrid`

Purpose: multi-segment LCARS divider grid, richer than a single `LcarsBar`.

Key parameters:

- `type`: `Type1`, `Type2`, or `Type3`.
- `topHeight` / `bottomHeight`: row heights.

Use for: content separators, title decoration, and complex structural lines.

### `LcarsInspectBracket`

Purpose: inspection frame with side scales, corner brackets, moving markers, and a central content slot.

Key parameters:

- `color`: main frame color.
- `running`: enables marker animation.
- `content`: inspected object or scan content.

Use for: target inspection, object details, and star chart inspection mode.

## Full-Screen And Advanced Scenes

### `LcarsTransmissionFrame`

Purpose: full-screen communication or authorization template with top/bottom LCARS bars, central title, subtitle, and content slot.

Key parameters:

- `headerLabel` / `footerLabel`: top and bottom labels.
- `title` / `subtitle`: central title and supporting text.
- `content`: optional center content.

Use for: incoming transmissions, authorization screens, lock-screen style prompts, and full-screen status pages.

### `LcarsStarChart`

Purpose: Canvas star chart that renders stars, grid lines, star labels, coordinate readouts, and target scanner overlay.

Key parameters:

- `mode`: `Navigation` or `Inspection`.
- `stars`: optional custom star list.
- `seed`: default generated star seed.
- `showCoords`: shows coordinate readout.
- `showScanner`: shows target scanner overlay.
- `running`: controls the internal scanner, coordinate refresh, and inspection-frame animation. Set it to `false` in long catalog pages to reduce recomposition work.

`Inspection` mode wraps the chart in `LcarsInspectBracket`, making it suitable for target-inspection pages.

### `LcarsStar`

Purpose: data model for `LcarsStarChart`.

Fields:

- `label`: star name.
- `x` / `y`: relative coordinates, usually in `0f..1f`.
- `size`: star point size.
- `labeled`: whether to draw the selection bracket and label.

## Design Notes

- Keep the default base surface pure black.
- Text inside colored controls should usually be black.
- Avoid shadows, gradients, glass effects, and Material surface styling.
- Do not nest vertically scrollable components inside scrollable pages. `LcarsLogConsole` is intentionally fixed-line.
- Prefer stepped, scanning, and flashing animations over soft breathing fades.
- `seed` parameters are for stable previews and tests. The same seed should produce the same generated data.
