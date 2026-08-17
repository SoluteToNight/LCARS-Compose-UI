# LCARS 24.2 to Compose Reference Mapping

The default visual constraints come from the static HTML/CSS under `example/LCARS-24.2`. The Compose implementation translates those rules semantically and proportionally; it does not reproduce the DOM or copy fixed browser dimensions.

## Presets

| Compose preset | Primary source |
| --- | --- |
| `LcarsPreset.ClassicUltra` | `example/LCARS-24.2/assets/classic.css` |
| `LcarsPreset.NemesisBlueUltra` | `example/LCARS-24.2/assets/nemesis-blue.css` |
| `LcarsPreset.LowerDecksPadd` | `example/LCARS-24.2/assets/lower-decks-padd.css` |

## Translation Rules

| HTML/CSS concept | Compose contract |
| --- | --- |
| CSS color variables | Semantic roles in `LcarsColorScheme`; raw numbered colors remain compatibility details |
| Fixed pixel gaps | `LcarsDimensions`; the standard interlock gap is `4.dp` |
| Rounded end caps | `LcarsShapes` or explicit asymmetric Compose shapes |
| Flex/grid proportions | `Row`/`Column` weights and `BoxWithConstraints`, selected by `LcarsSizeClass` |
| Media queries | Compact `< 600.dp`, Medium `< 840.dp`, Expanded `>= 840.dp` |
| Alert flashing | Stepped keyframes controlled by `LcarsMotionMode` |
| Browser audio hooks | Opt-in `LcarsSoundPlayer`; default is `NoOpLcarsSoundPlayer` |
| Uppercase labels | Public `LcarsText` and LCARS controls, without duplicating text rendering in apps |

## Invariants

- Pure black base, flat color geometry, no shadows, gradients, glass, or Material surface styling.
- Dense asymmetric elbows, pills, one-sided rounded blocks, and narrow deliberate gaps.
- Colored controls normally use black, uppercase, bottom/right-aligned labels.
- Disabled controls remain visible and expose disabled semantics.
- Interactive targets keep at least a 48 dp touch area even when the visible geometry is denser.
- Reduced or disabled motion never falls back to smooth breathing fades.

The reference directory is read-only design input. Changes belong in Compose tokens, components, the Catalog demo, and tests.
