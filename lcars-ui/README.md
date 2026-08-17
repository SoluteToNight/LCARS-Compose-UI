# lcars-ui

[中文](README_zh.md)

`lcars-ui` is a Jetpack Compose LCARS component library. The v1 API keeps one Android AAR while separating theme, foundation, controls, displays, layouts, scenes, and PADD components into focused packages.

## Installation

```kotlin
dependencies {
    implementation(project(":lcars-ui"))
}
```

## Package Map

| Package | Responsibility |
| --- | --- |
| `com.lcars.ui.theme` | Theme specs, three presets, semantic colors, dimensions, shapes, motion, sound |
| `com.lcars.ui.foundation` | Public text and low-level text helpers |
| `com.lcars.ui.controls` | Buttons, toggles, segmented controls, dialogs, command rails |
| `com.lcars.ui.display` | Status, progress, telemetry, tables, logs, meters, dynamic readouts |
| `com.lcars.ui.layout` | Bars, elbows, frames, responsive scaffolds, console layouts |
| `com.lcars.ui.scene` | Star charts, transmission and other complete scene patterns |
| `com.lcars.ui.padd` | Handheld PADD theme, scaffold, controls, and readouts |

Preview-only examples live in `com.lcars.ui.preview`; applications should not depend on that package.

## Theme

Use one of the three reference-backed presets:

```kotlin
import com.lcars.ui.theme.LcarsPreset
import com.lcars.ui.theme.LcarsTheme

LcarsTheme(spec = LcarsPreset.NemesisBlueUltra.spec) {
    AppContent()
}
```

- `LcarsPreset.ClassicUltra`
- `LcarsPreset.NemesisBlueUltra`
- `LcarsPreset.LowerDecksPadd`

Read semantic tokens with `LcarsTheme.colorScheme`, `LcarsTheme.typography`, `LcarsTheme.dimensions`, `LcarsTheme.shapes`, and `LcarsTheme.motionScheme`. Pass `LcarsMotionMode.Reduced` or `Off` when the host app needs stricter motion behavior. Sound is opt-in through `LcarsSoundPlayer`; the default player is silent.

## Example

```kotlin
import com.lcars.ui.display.LcarsAlertBanner
import com.lcars.ui.display.LcarsProgressBar
import com.lcars.ui.layout.LcarsFramePanel

LcarsFramePanel(title = "dynamic states") {
    LcarsProgressBar(progress = 0.64f, label = "reactor balance")
    LcarsAlertBanner(message = "critical alert active", active = true)
}
```

For handheld layouts, use `LcarsPhonePaddTheme(preset = LcarsPreset.LowerDecksPadd)` and `LcarsPhonePaddScaffold` from `com.lcars.ui.padd`.

## Documentation

- [English component guide](COMPONENTS.en.md)
- [中文组件说明](COMPONENTS.zh-CN.md)
- [LCARS 24.2 CSS to Compose mapping](REFERENCE_MAPPING.md)

