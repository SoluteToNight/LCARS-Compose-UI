# LCARS Compose Components

This document describes the reusable Jetpack Compose components in the `:lcars-ui` module. The components follow the LCARS visual rules: black base surfaces, flat color geometry, tight spacing, asymmetric rounded blocks, uppercase text, and stepped alert animations.

Chinese version: [COMPONENTS.zh-CN.md](COMPONENTS.zh-CN.md).

## Basic Usage

Place LCARS components under `LcarsTheme`. The theme provides LCARS-specific color, typography, and spacing tokens.

```kotlin
LcarsTheme {
    LcarsFramePanel(title = "sensor deck") {
        LcarsStatusLight(label = "sensor lock", active = true)
        LcarsProgressBar(progress = 0.64f, label = "reactor balance")
    }
}
```

## Theme And Tokens

### `LcarsTheme`

Purpose: provides LCARS color, typography, and spacing values for the component tree without depending on dynamic Material colors.

Main pieces:

- `LcarsColors`: LCARS palette, including amber, light blue, violet, alert red, tactical green, and black surfaces.
- `LcarsTypography`: text styles for headers, buttons, telemetry, and small labels.
- `LcarsSpacing`: standard gap, button minimum size, bar height, elbow thickness, and related layout values.

Use for: app roots, previews, and isolated component examples.

### `LcarsAdaptiveTheme`

Purpose: adds portrait/compact-landscape adaptive tokens on top of `LcarsTheme`. It resolves `LcarsResponsiveMode` from the container size and provides `LocalLcarsAdaptiveProfile`.

Behavior:

- Wide landscape keeps the default sizes.
- Portrait and compact landscape compress geometry, spacing, button heights, and command rail widths.
- Text keeps the default 1x logical resolution and existing `sp` sizes; the adaptive theme does not additionally scale, shrink, or resample typography.

Use for: app roots, demo roots, and screens that should automatically switch LCARS sizing tokens across portrait and landscape.

## Geometry Primitives

### `LcarsButton`

Purpose: LCARS command button with pill, start-rounded, end-rounded, and rectangular shapes. Text is uppercased and aligned toward the bottom-right by default.

Key parameters:

- `text`: button label.
- `onClick`: click callback.
- `color` / `contentColor`: background and text colors.
- `shape`: `Pill`, `BlockStart`, `BlockEnd`, or `Rectangle`.
- `alerting`: enables stepped alert flashing.
- `enabled`: disabled controls are hidden, matching the LCARS reference behavior.

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
