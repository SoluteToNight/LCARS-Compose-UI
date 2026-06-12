# LCARS UI Project Instructions

## Project Goal

This repository is an Android Jetpack Compose implementation of an LCARS
(Library Computer Access and Retrieval System) UI component library inspired by
Star Trek interfaces.

Primary design reference: [LCARS组件库.md](LCARS组件库.md)

Visual/code reference: [example/lcars](example/lcars), a Vite/Vue LCARS project
checked into the repository for geometry, spacing, color, typography, and
interaction references.

## Current Structure

- `lcars-ui/` is the reusable Android Compose library module.
- `app/` is the LCARS demo/starter application that depends on `:lcars-ui`.
- `example/lcars/` is reference material only. Do not port its web framework
  directly; translate the visual/system ideas into idiomatic Compose.
- Publishing is intentionally out of scope for now. Keep `:lcars-ui` usable as
  a local module dependency unless the user explicitly asks for Maven/AAR
  publishing.

## LCARS Design Rules

- Use a pure black base and flat colored geometry. Avoid shadows, gradients,
  elevation, glass effects, and generic Material surface styling unless needed
  only for Android integration.
- Favor asymmetric large-radius geometry: elbows, pills, one-sided rounded
  blocks, hard rectangular bars, and tight interlocking layouts.
- Keep gaps narrow and deliberate. The design document uses `4.dp` as the
  standard Compose gap; the web reference uses similarly tight block gaps.
- Button text should be uppercase, high-density, and usually aligned to the
  right or bottom-right.
- Text inside colored controls should normally be black for LCARS contrast.
- Prefer Canvas or explicit Compose shapes for core LCARS geometry rather than
  relying on default Material component visuals.
- Alert animations should use stepped/keyframe changes, not smooth breathing
  fades.

## Compose Implementation Conventions

- Use Kotlin and Jetpack Compose.
- Keep reusable LCARS APIs small and declarative:
  `LcarsButton`, `LcarsBar`, `LcarsElbow`, layout containers, tokens, and demo
  screens.
- Put global style in LCARS-specific tokens, such as colors, typography, and
  spacing via `CompositionLocal`, rather than depending on dynamic Material
  color.
- Prefer package paths under `com.lcars.ui` until a separate library module is
  introduced.
- Keep previews focused and representative: include both portrait and landscape
  friendly layouts where practical.
- The library must not require unclear-license fonts by default. Keep custom
  fonts app-injected through `LcarsTypography`; the demo may use placeholder
  fonts for local visual testing.

## Reference Mapping

- `example/lcars/src/LCARSButton.vue`: button sizing, one-sided rounding, hidden
  disabled controls, bottom-right label alignment.
- `example/lcars/src/LCARSBar.vue` and `FocusFrameBar.vue`: horizontal LCARS bars
  with rounded caps and embedded labels.
- `example/lcars/src/FocusFrame.vue` and `InspectBracket.vue`: frame/bracket
  layouts, dense panel composition, animated markers, and responsive geometry.
- `example/lcars/src/styles/index.css`: LCARS color variables and global spacing
  values.

## Build And Verification

Use Windows PowerShell from the repository root.

- Build debug APK: `./gradlew.bat assembleDebug`
- Install debug APK on connected device/emulator: `./gradlew.bat installDebug`
- Run unit tests: `./gradlew.bat test`
- Run instrumented tests when a device/emulator is available:
  `./gradlew.bat connectedAndroidTest`

This directory is a Git repository, so standard Git commands can be used for change tracking, commits, and pushing.

## Component Design and Refactoring Conventions

- **API Visibility**: Core visual/text components (like `LcarsText`) must be kept `public` so that client applications (like `:app`) can reuse them without duplicating rendering logic (e.g. uppercase conversion, `TextAutoSize` protection).
- **Refactoring Fallbacks**: When migrating screens to new component APIs, preserve a local delegate/wrapper with a `USE_FALLBACK_TEXT` or similar boolean flag to allow instant rollback during visual regression testing.

## Editing Notes

- Do not modify `example/lcars/` unless the task is explicitly about the
  reference project.
- Do not replace LCARS visuals with stock Material components.
- Keep generated/demo telemetry text and labels plausible but nonessential; the
  reusable component API matters more than hardcoded demo data.
- Avoid broad refactors while the component taxonomy is still forming.

