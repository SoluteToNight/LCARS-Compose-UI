# lcars-ui

[中文](README_zh.md)

`lcars-ui` is a reusable LCARS Compose UI module. It provides theme primitives, geometric controls, dynamic readouts, data panels, and application layout scaffolds.

## Installation

```kotlin
dependencies {
    implementation("com.github.SoluteToNight:LCARS-Compose-UI:<version>")
}
```

The artifact is published through JitPack as the release AAR of this module.

## Component Documentation

- 中文组件说明：[COMPONENTS.zh-CN.md](COMPONENTS.zh-CN.md)
- English component guide: [COMPONENTS.en.md](COMPONENTS.en.md)

## Theme

Use `LcarsTheme` or `LcarsAdaptiveTheme` at the root of your Compose content. They provide `LocalLcarsColors`, `LocalLcarsTypography`, and `LocalLcarsSpacing`.

The simplest way to apply a visual style is to pass `LcarsStyle`:

```kotlin
LcarsAdaptiveTheme(style = LcarsStyle.NemesisBlueUltra) {
    LcarsConsoleScaffold(
        leftWingContent = { /* commands */ },
        mainDeckContent = { /* readouts */ },
    )
}
```

Available styles:

- `LcarsStyle.ClassicUltra`
- `LcarsStyle.LowerDecks`
- `LcarsStyle.LowerDecksPadd`
- `LcarsStyle.NemesisBlueUltra`

Style tokens set the default color and spacing values for all LCARS components under the theme. You can still override them explicitly:

```kotlin
LcarsTheme(
    style = LcarsStyle.LowerDecks,
    colors = customColors,
    spacing = customSpacing,
) {
    AppContent()
}
```

The default font is Antonio, a narrow open-source display family distributed under the SIL Open Font License. Apps can inject another licensed LCARS-like font by passing a custom `LcarsTypography`.

## Core Components

- `LcarsButton`: pill, start-rounded, end-rounded, and rectangle command controls.
- `LcarsBar`: horizontal LCARS bar with optional caps and embedded label.
- `LcarsElbow`: four-direction Canvas elbow geometry.
- `LcarsFramePanel`: framed content section with LCARS title/footer bars.
- `LcarsTelemetryPanel`: responsive telemetry readout grid.
- `LcarsDataTable`: dense status table.

## Dynamic Components

- `LcarsAlertBanner`
- `LcarsStatusLight`
- `LcarsProgressBar`
- `LcarsSegmentedMeter`
- `LcarsScannerSweep`
- `LcarsReadoutTicker`

Alert effects use stepped keyframes rather than smooth fades.

## Extended Components

- `LcarsCommandRail`
- `LcarsSegmentedControl`
- `LcarsToggle`
- `LcarsDialog`
- `LcarsLogConsole`
- `LcarsNumberMatrix`
- `LcarsStarCoords`
- `LcarsNumericLabel`
- `LcarsDividerGrid`
- `LcarsInspectBracket`
- `LcarsTargetScanner`
- `LcarsResponsiveScaffold`
- `LcarsTransmissionFrame`
- `LcarsStarChart`

## Layout Scaffolds

- `LcarsAppScaffold`: generic app shell with top bar, control rail, content deck, optional footer.
- `LcarsPaddScaffold`: portrait-friendly PADD shell.
- `LcarsConsoleScaffold`: landscape console shell over `LcarsMainConsole`.

## Example

```kotlin
LcarsFramePanel(title = "dynamic states") {
    LcarsStatusLight(label = "sensor lock", active = true)
    LcarsProgressBar(progress = 0.64f, label = "reactor balance")
    LcarsAlertBanner(message = "critical alert active", active = true)
}
```
