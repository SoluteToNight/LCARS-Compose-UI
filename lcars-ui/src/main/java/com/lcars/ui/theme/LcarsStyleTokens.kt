package com.lcars.ui.theme

import com.lcars.ui.R
import com.lcars.ui.foundation.*
import com.lcars.ui.controls.*
import com.lcars.ui.display.*
import com.lcars.ui.layout.*
import com.lcars.ui.scene.*
import com.lcars.ui.padd.*

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

enum class LcarsPreset {
    NemesisBlueUltra,
    ClassicUltra,
    LowerDecksPadd,
}

val LcarsPreset.spec: LcarsThemeSpec
    get() = when (this) {
        LcarsPreset.NemesisBlueUltra -> LcarsPresets.NemesisBlueUltra
        LcarsPreset.ClassicUltra -> LcarsPresets.ClassicUltra
        LcarsPreset.LowerDecksPadd -> LcarsPresets.LowerDecksPadd
    }

object LcarsPresets {
    val NemesisBlueUltra: LcarsThemeSpec = LcarsThemeSpec(
        name = "nemesis-blue-ultra",
        colorScheme = LcarsColorScheme(
            monoAmber = Color(0xFFFF9900),       // Standard LCARS Yellow Alert #FF9900
            auxiliaryTan = Color(0xFFCCAA88),    // --wheat #ca8
            lightBlue = Color(0xFF6699FF),       // --cool #69f
            violet = Color(0xFF9966CC),          // --grape #96c
            tacticalGreen = Color(0xFF88BBFF),   // --ghost #8bf (cool Nemesis status tone)
            alertRed = Color(0xFFEE3333),        // Standard LCARS Red Alert #EE3333
            background = Color.Black,
            panel = Color(0xFF040408),
            text = Color(0xFFEBF0FF),            // --moonbeam #ebf0ff
            onColor = Color.Black,
            a1 = Color(0xFF2233FF),              // --midnight #23f
            a2 = Color(0xFF6699FF),              // --cool #69f
            a3 = Color(0xFF88BBFF),              // --ghost #8bf
            a4 = Color(0xFFEBF0FF),              // --moonbeam #ebf0ff
            a5 = Color(0xFFFF8833),              // --tangerine #f83
            a6 = Color(0xFF9966CC),              // --grape #96c
            a7 = Color(0xFF2266FF),              // --evening #26f
            a8 = Color(0xFF52526A),              // --galaxy-gray #52526a
            a9 = Color(0xFFCC6666),              // --roseblush #c66
            blue = Color(0xFF2233FF),            // --midnight
            butterscotch = Color(0xFFFFCC99),    // --honey
            almondCreme = Color(0xFFEBF0FF),     // --moonbeam
            classicRed = Color(0xFFCC2233),      // --cardinal
            tomato = Color(0xFFFF7744),          // --pumpkinshade #f74
            gray = Color(0xFF52526A),            // --galaxy-gray
            spaceWhite = Color(0xFFEBF0FF),      // --moonbeam
            framePrimary = Color(0xFF2266FF),    // --evening #26f (Primary frame / elbows)
            frameSecondary = Color(0xFF6699FF),  // --cool #69f (Secondary frame / buttons)
            commandPrimary = Color(0xFF6699FF),  // --cool #69f (Primary command buttons)
            commandSecondary = Color(0xFFCCAA88),// --wheat #ca8 (Secondary command buttons)
            commandInactive = Color(0xFF52526A), // --galaxy-gray
            activeAccent = Color(0xFF88BBFF),    // --ghost #8bf (Ghost blue active highlight)
            inactiveAccent = Color(0xFF52526A),  // --galaxy-gray
            subHighlight = Color(0xFFFFCC99),    // --honey #fc9
            meterInactive = Color(0xFF52526A),   // --galaxy-gray
            sensorBar = Color(0xFF2266FF),       // --evening #26f
            readoutAccent = Color(0xFF88BBFF),   // --ghost #8bf
        ),
        dimensions = LcarsDimensions(
            barHeight = 28.dp,
            leftFrameWidth = 240.dp,
            navigationWidth = 240.dp,
            frameOuterRadius = 160.dp,
            frameInnerRadius = 60.dp,
            dividerHeight = 8.dp,
            barPrimaryFraction = 0.40f,
            barSecondaryFraction = 0.04f,
            barTertiaryFraction = 0.17f,
            barEndFraction = 0.04f,
        ),
    )

    val ClassicUltra: LcarsThemeSpec = LcarsThemeSpec(
        name = "classic-ultra",
        colorScheme = LcarsColorScheme(
            monoAmber = Color(0xFFFF9900), // --golden-orange: #f90
            auxiliaryTan = Color(0xFFFFCC99), // --sunflower: #fc9
            lightBlue = Color(0xFF8899FF), // --bluey: #89f
            violet = Color(0xFFCC99FF), // --african-violet: #c9f
            tacticalGreen = Color(0xFF99CC33), // --green: #993
            alertRed = Color(0xFFFF3333), // --mars: #f20 / alert red
            background = Color.Black,
            panel = Color(0xFF070707),
            text = Color(0xFFFF9900),
            onColor = Color.Black,
            a1 = Color(0xFFFF9900), // golden-orange
            a2 = Color(0xFFFF9966), // butterscotch
            a3 = Color(0xFF8899FF), // bluey
            a4 = Color(0xFFCC99FF), // african-violet
            a5 = Color(0xFFFFCC99), // sunflower
            a6 = Color(0xFF9966FF), // moonlit-violet
            a7 = Color(0xFFFF8800), // orange
            a8 = Color(0xFFFFBBAA), // almond-creme
            a9 = Color(0xFFFF5555), // tomato
            blue = Color(0xFF8899FF),
            butterscotch = Color(0xFFFF9966),
            almondCreme = Color(0xFFFFBBAA),
            classicRed = Color(0xFFFF3333),
            tomato = Color(0xFFFF5555),
            gray = Color(0xFF666688),
            spaceWhite = Color(0xFFF5F6FA),
            framePrimary = Color(0xFF8899FF), // --bluey: #89f
            frameSecondary = Color(0xFFFF9966), // --butterscotch: #f96
            commandPrimary = Color(0xFFFF9900), // --golden-orange: #f90
            commandSecondary = Color(0xFFFF9966), // --butterscotch: #f96
            commandInactive = Color(0xFF666688),
            activeAccent = Color(0xFFFF9900),
            inactiveAccent = Color(0xFFFF9966),
            subHighlight = Color(0xFFFFCC99),
            meterInactive = Color(0xFF333333),
            sensorBar = Color(0xFF8899FF),
            readoutAccent = Color(0xFFFF9900),
        ),
        dimensions = NemesisBlueUltra.dimensions,
    )

    val LowerDecksPadd: LcarsThemeSpec = LcarsThemeSpec(
        name = "lower-decks-padd",
        colorScheme = LcarsColorScheme(
            monoAmber = Color(0xFF66CCFF),
            auxiliaryTan = Color(0xFF99CCFF),
            lightBlue = Color(0xFF5588EE),
            violet = Color(0xFF7799DD),
            tacticalGreen = Color(0xFF88FFFF),
            alertRed = Color(0xFFFF3300),
            background = Color.Black,
            panel = Color(0xFF05070C),
            text = Color(0xFFBBDDFF),
            onColor = Color.Black,
            a1 = Color(0xFF5588EE),
            a2 = Color(0xFF66CCFF),
            a3 = Color(0xFF99CCFF),
            a4 = Color(0xFFBBDDFF),
            a5 = Color(0xFF7799DD),
            a6 = Color(0xFF455580),
            a7 = Color(0xFF344470),
            a8 = Color(0xFF88FFFF),
            a9 = Color(0xFF455580),
            blue = Color(0xFF5588EE),
            butterscotch = Color(0xFF66CCFF),
            almondCreme = Color(0xFFBBDDFF),
            classicRed = Color(0xFF455580),
            tomato = Color(0xFFFF3300),
            gray = Color(0xFF344470),
            spaceWhite = Color(0xFFBBDDFF),
            framePrimary = Color(0xFF5588EE),
            frameSecondary = Color(0xFF88FFFF),
            commandPrimary = Color(0xFF5588EE),
            commandSecondary = Color(0xFF7799DD),
            commandInactive = Color(0xFF455580),
            activeAccent = Color(0xFF88FFFF),
            inactiveAccent = Color(0xFF344470),
            subHighlight = Color(0xFF99CCFF),
            meterInactive = Color(0xFF344470),
            sensorBar = Color(0xFF66CCFF),
            readoutAccent = Color(0xFF88FFFF),
        ),
        dimensions = LcarsDimensions(
            buttonMinWidth = 76.dp,
            buttonMinHeight = 48.dp,
            barHeight = 28.dp,
            elbowThickness = 34.dp,
            panelPadding = 10.dp,
            scaffoldControlWidth = 120.dp,
            commandRailWidth = 150.dp,
            commandRailCompactWidth = 112.dp,
            leftFrameWidth = 240.dp,
            navigationWidth = 240.dp,
            frameOuterRadius = 100.dp,
            frameInnerRadius = 44.dp,
            dividerHeight = 12.dp,
            barPrimaryFraction = 0.10f,
            barSecondaryFraction = 0.28f,
            barTertiaryFraction = 0.07f,
            barEndFraction = 0.05f,
        ),
    )
}

val LcarsColorScheme.weatherFrame: Color get() = framePrimary
val LcarsColorScheme.weatherBtnStyle: Color get() = commandPrimary
val LcarsColorScheme.weatherBtnSecondary: Color get() = commandSecondary
val LcarsColorScheme.weatherBtnInactive: Color get() = commandInactive
val LcarsColorScheme.weatherActiveAccent: Color get() = activeAccent
val LcarsColorScheme.weatherInactiveAccent: Color get() = inactiveAccent
val LcarsColorScheme.weatherSubHighlight: Color get() = subHighlight
val LcarsColorScheme.weatherMeterInactive: Color get() = meterInactive
val LcarsColorScheme.weatherSensorBar: Color get() = sensorBar
val LcarsColorScheme.weatherSensorLabel: Color get() = readoutAccent

/**
 * Stage 9 Color Principle: Alternating Rhythm.
 * Provides a harmonious, high-contrast color sequence for high-density button columns and matrices.
 */
fun LcarsColorScheme.keypadRhythm(index: Int): Color {
    val sequence = listOf(
        monoAmber,      // Okuda Orange / Primary Mode
        auxiliaryTan,   // Butter / Peach
        lightBlue,      // Federation Ice Blue
        violet,         // Soft Lilac
        butterscotch,   // Warm Amber
        tacticalGreen,  // Sub-System Accent
    )
    return sequence[index.mod(sequence.size)]
}

fun LcarsColorScheme.keypadRhythmList(): List<Color> = listOf(
    monoAmber,
    auxiliaryTan,
    lightBlue,
    violet,
    butterscotch,
    tacticalGreen,
)

enum class LcarsFunctionalDomain {
    Command,
    Engineering,
    Tactical,
    Science,
    Biomedical,
    Environmental,
    Alert,
}

fun LcarsColorScheme.colorForDomain(domain: LcarsFunctionalDomain): Color = when (domain) {
    LcarsFunctionalDomain.Command -> monoAmber
    LcarsFunctionalDomain.Engineering -> butterscotch
    LcarsFunctionalDomain.Tactical -> tomato
    LcarsFunctionalDomain.Science -> lightBlue
    LcarsFunctionalDomain.Biomedical -> auxiliaryTan
    LcarsFunctionalDomain.Environmental -> tacticalGreen
    LcarsFunctionalDomain.Alert -> alertRed
}


