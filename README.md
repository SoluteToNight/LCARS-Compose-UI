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
- Demo app with three layout breakpoints: portrait PADD, compact landscape, and wide landscape.

## Component Documentation

- 中文组件说明：[lcars-ui/COMPONENTS.zh-CN.md](lcars-ui/COMPONENTS.zh-CN.md)
- English component guide: [lcars-ui/COMPONENTS.en.md](lcars-ui/COMPONENTS.en.md)

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

## Current Scope

The reusable module is published as an Android AAR through JitPack. Maven Central publishing is intentionally out of scope for now.
