# LCARS UI

[中文](README_zh.md)

[![](https://jitpack.io/v/SoluteToNight/LCARS-Compose-UI.svg)](https://jitpack.io/#SoluteToNight/LCARS-Compose-UI)

LCARS UI is an Android Jetpack Compose component library and demo template for building interfaces inspired by the Star Trek LCARS style.

## Project Structure

- `:lcars-ui`: reusable LCARS Compose components, theme tokens, dynamic widgets, and layout scaffolds.
- `:app`: demo/starter Android app showing a responsive master console and component catalog.
- `example/`: optional local-only visual reference files. This directory is not tracked by Git.

## Installation

Add JitPack to `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

Add the LCARS UI dependency:

```kotlin
dependencies {
    implementation("com.github.SoluteToNight:LCARS-Compose-UI:<version>")
}
```

Use a GitHub release tag, a short commit hash, or `master-SNAPSHOT` as the version.

## Run

```powershell
./gradlew.bat assembleDebug
./gradlew.bat installDebug
```

The launcher opens a responsive Demo Hub with three terminals. Android Studio also provides direct demo targets when shared run configurations are loaded:

- `LCARS Demo`: launches the Demo Hub.
- `Component Catalog`: launches the searchable component catalog directly.
- `Weather System`: launches the atmospheric/weather console only.
- `PADD Variant`: launches the isolated phone portrait PADD variant demo through `PaddVariantActivity`.

Unit and compile checks:

```powershell
./gradlew.bat test
./gradlew.bat :app:compileDebugAndroidTestKotlin
```

## What Is Included

- LCARS theme tokens: colors, typography, spacing.
- Geometry primitives: buttons, bars, elbows, frame panels.
- Dynamic components: alert banner, status light, progress bar, segmented meter, scanner sweep, readout ticker.
- Data/display components: telemetry panel, data table, log console, number matrix, star coordinates, and related readouts.
- Layout templates: app, PADD, console, and responsive scaffolds.
- Phone PADD variant components: compact PADD scaffold, side rail, status strips, readout panels, and controls for handheld portrait layouts.
- Responsive Demo Hub with portrait, compact landscape, and wide landscape layouts.
- Independent component catalog, weather console, and PADD variant activities available from the Hub or direct run configurations.

## Component Documentation

- 中文组件说明：[lcars-ui/COMPONENTS.zh-CN.md](lcars-ui/COMPONENTS.zh-CN.md)
- English component guide: [lcars-ui/COMPONENTS.en.md](lcars-ui/COMPONENTS.en.md)
- Reference CSS mapping: [lcars-ui/REFERENCE_MAPPING.md](lcars-ui/REFERENCE_MAPPING.md)

The v1 AAR uses responsibility packages: `theme`, `foundation`, `controls`, `display`, `layout`, `scene`, and `padd` under `com.lcars.ui`. The old root-package component API is intentionally removed.

## Font Policy

The library bundles Antonio as its default LCARS-like display font. Antonio is distributed under the SIL Open Font License and is stored in `lcars-ui/src/main/res/font/antonio_variable.ttf`; its license is included under `lcars-ui/src/main/assets/fonts/antonio/OFL.txt`.

Apps can still inject a different licensed font by passing a custom `LcarsTypography` to `LcarsTheme`.

## Acknowledgements

Visual reference and LCARS interaction ideas were informed by [louh/lcars](https://github.com/louh/lcars), [joernweissenborn/lcars](https://github.com/joernweissenborn/lcars), and [The LCARS Website Template](https://www.thelcars.com/). Reference files are used only as design guidance and are not redistributed here.

## Basic Usage

```kotlin
LcarsTheme {
    LcarsPaddScaffold(title = "sensor deck") {
        LcarsAlertBanner(message = "system nominal", active = false)
        LcarsTelemetryPanel(
            title = "primary telemetry",
            entries = listOf(
                LcarsTelemetryEntry("lat", "30.542314 n"),
                LcarsTelemetryEntry("fix", "high precision", LcarsTelemetryStatus.Normal),
            ),
        )
    }
}
```

Phone PADD variant:

```kotlin
LcarsPhonePaddTheme(preset = LcarsPreset.LowerDecksPadd) {
    LcarsPhonePaddScaffold(
        title = "systems data 21-0071",
        registry = "uss raven - database 83-s28",
    ) {
        LcarsPaddReadoutPanel(title = "hansen family") {
            LcarsPaddDataLines(
                lines = listOf("mobile interface ready", "archive link nominal"),
            )
        }
    }
}
```

## Current Scope

The reusable module is published as an Android AAR through JitPack. Maven Central publishing is intentionally out of scope for now.
