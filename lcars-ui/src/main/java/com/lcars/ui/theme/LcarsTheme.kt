package com.lcars.ui.theme

import com.lcars.ui.R
import com.lcars.ui.foundation.*
import com.lcars.ui.controls.*
import com.lcars.ui.display.*
import com.lcars.ui.layout.*
import com.lcars.ui.scene.*
import com.lcars.ui.padd.*

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Immutable
data class LcarsColorScheme(
    val monoAmber: Color,
    val auxiliaryTan: Color,
    val lightBlue: Color,
    val violet: Color,
    val tacticalGreen: Color,
    val alertRed: Color,
    val background: Color,
    val panel: Color,
    val text: Color,
    val onColor: Color,
    val a1: Color,
    val a2: Color,
    val a3: Color,
    val a4: Color,
    val a5: Color,
    val a6: Color,
    val a7: Color,
    val a8: Color,
    val a9: Color,
    val blue: Color,
    val butterscotch: Color,
    val almondCreme: Color,
    val classicRed: Color,
    val tomato: Color,
    val gray: Color,
    val spaceWhite: Color,
    val framePrimary: Color,
    val frameSecondary: Color,
    val commandPrimary: Color,
    val commandSecondary: Color,
    val commandInactive: Color,
    val activeAccent: Color,
    val inactiveAccent: Color,
    val subHighlight: Color,
    val meterInactive: Color,
    val sensorBar: Color,
    val readoutAccent: Color,
)

val LcarsDefaultFontFamily: FontFamily = FontFamily(
    Font(R.font.antonio_variable, weight = FontWeight.Normal),
    Font(R.font.antonio_variable, weight = FontWeight.Bold),
)

@Immutable
data class LcarsTypography(
    val defaultFont: FontFamily = LcarsDefaultFontFamily,
    val header: TextStyle = TextStyle(
        fontFamily = defaultFont,
        fontWeight = FontWeight.Normal,
        fontSize = 34.sp,
        lineHeight = 36.sp,
    ),
    val button: TextStyle = TextStyle(
        fontFamily = defaultFont,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 24.sp,
    ),
    val telemetry: TextStyle = TextStyle(
        fontFamily = defaultFont,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 26.sp,
    ),
    val labelSmall: TextStyle = TextStyle(
        fontFamily = defaultFont,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 18.sp,
    ),
    val labelMedium: TextStyle = TextStyle(
        fontFamily = defaultFont,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 20.sp,
    ),
    val titleSmall: TextStyle = TextStyle(
        fontFamily = defaultFont,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 22.sp,
    ),
)


@Immutable
data class LcarsDimensions(
    val gapStandard: Dp = 4.dp,
    val gapLarge: Dp = 8.dp,
    val buttonMinWidth: Dp = 120.dp,
    val buttonMinHeight: Dp = 56.dp,
    val barHeight: Dp = 28.dp,
    val elbowThickness: Dp = 40.dp,
    val panelPadding: Dp = 12.dp,
    val scaffoldControlWidth: Dp = 150.dp,
    val commandRailWidth: Dp = 154.dp,
    val commandRailCompactWidth: Dp = 116.dp,
    val leftFrameWidth: Dp = 240.dp,
    val navigationWidth: Dp = 240.dp,
    val frameOuterRadius: Dp = 160.dp,
    val frameInnerRadius: Dp = 60.dp,
    val dividerHeight: Dp = 8.dp,
    val barPrimaryFraction: Float = 0.40f,
    val barSecondaryFraction: Float = 0.04f,
    val barTertiaryFraction: Float = 0.17f,
    val barEndFraction: Float = 0.04f,
)

@Immutable
data class LcarsShapes(
    val pill: Shape = RoundedCornerShape(percent = 50),
    val startCap: Shape = RoundedCornerShape(
        topStartPercent = 50,
        bottomStartPercent = 50,
    ),
    val endCap: Shape = RoundedCornerShape(
        topEndPercent = 50,
        bottomEndPercent = 50,
    ),
    val rectangle: Shape = RoundedCornerShape(0.dp),
    val panel: Shape = RoundedCornerShape(4.dp),
)

@Immutable
data class LcarsMotionScheme(
    val alertCriticalStepMillis: Int = 300,
    val alertWarningStepMillis: Int = 400,
    val alertAdvisoryStepMillis: Int = 600,
    val alertStepMillis: Int = 300,
    val revealMillis: Int = 240,
    val exitMillis: Int = 160,
    val staggerMillis: Int = 60,
    val cascadeStepMillis: Int = 45,
    val tactileFlashMillis: Int = 120,
    val telemetryTickMillis: Int = 100,
    val reactorPulseMillis: Int = 1000,
)

enum class LcarsMotionMode {
    System,
    Reduced,
    Off,
}

@Immutable
data class LcarsThemeSpec(
    val name: String,
    val colorScheme: LcarsColorScheme,
    val typography: LcarsTypography = LcarsTypography(),
    val dimensions: LcarsDimensions = LcarsDimensions(),
    val shapes: LcarsShapes = LcarsShapes(),
    val motionScheme: LcarsMotionScheme = LcarsMotionScheme(),
)

enum class LcarsSoundCue {
    Click,
    Alert,
    Adjust,
}

fun interface LcarsSoundPlayer {
    fun play(cue: LcarsSoundCue)
}

object NoOpLcarsSoundPlayer : LcarsSoundPlayer {
    override fun play(cue: LcarsSoundCue) = Unit
}

enum class LcarsSizeClass {
    Compact,
    Medium,
    Expanded,
}

fun resolveLcarsSizeClass(width: Dp): LcarsSizeClass = when {
    width < 600.dp -> LcarsSizeClass.Compact
    width < 840.dp -> LcarsSizeClass.Medium
    else -> LcarsSizeClass.Expanded
}

private val LocalLcarsThemeSpec = staticCompositionLocalOf { LcarsPresets.NemesisBlueUltra }
val LocalLcarsMotionMode = staticCompositionLocalOf { LcarsMotionMode.System }
val LocalLcarsColors = staticCompositionLocalOf { LcarsPresets.NemesisBlueUltra.colorScheme }
val LocalLcarsTypography = staticCompositionLocalOf { LcarsTypography() }
val LocalLcarsSpacing = staticCompositionLocalOf { LcarsDimensions() }
internal val LocalLcarsAdaptiveProfile = staticCompositionLocalOf { LcarsAdaptiveProfile() }
val LocalLcarsSoundService = staticCompositionLocalOf<LcarsSoundPlayer> { NoOpLcarsSoundPlayer }

object LcarsTheme {
    val spec: LcarsThemeSpec
        @Composable
        @ReadOnlyComposable
        get() = LocalLcarsThemeSpec.current

    val colorScheme: LcarsColorScheme
        @Composable
        @ReadOnlyComposable
        get() = LocalLcarsColors.current

    val typography: LcarsTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalLcarsTypography.current

    val dimensions: LcarsDimensions
        @Composable
        @ReadOnlyComposable
        get() = LocalLcarsSpacing.current

    val shapes: LcarsShapes
        @Composable
        @ReadOnlyComposable
        get() = spec.shapes

    val motionScheme: LcarsMotionScheme
        @Composable
        @ReadOnlyComposable
        get() = spec.motionScheme

    val motionMode: LcarsMotionMode
        @Composable
        @ReadOnlyComposable
        get() = LocalLcarsMotionMode.current
}

@Composable
fun LcarsTheme(
    spec: LcarsThemeSpec = LcarsPresets.NemesisBlueUltra,
    motionMode: LcarsMotionMode = LcarsMotionMode.System,
    soundPlayer: LcarsSoundPlayer = NoOpLcarsSoundPlayer,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalLcarsThemeSpec provides spec,
        LocalLcarsColors provides spec.colorScheme,
        LocalLcarsTypography provides spec.typography,
        LocalLcarsSpacing provides spec.dimensions,
        LocalLcarsMotionMode provides motionMode,
        LocalLcarsSoundService provides soundPlayer,
        content = content,
    )
}

internal typealias LcarsColors = LcarsColorScheme
internal typealias LcarsSpacing = LcarsDimensions

@Immutable
internal data class LcarsAdaptiveProfile(
    val mode: LcarsResponsiveMode = LcarsResponsiveMode.WideLandscape,
    val compact: Boolean = false,
)

internal fun resolveLcarsAdaptiveProfile(
    width: Dp,
    height: Dp,
    compactWidth: Dp = 600.dp,
    compactLandscapeHeight: Dp = 520.dp,
): LcarsAdaptiveProfile {
    val mode = resolveLcarsResponsiveMode(width, height, compactWidth, compactLandscapeHeight)
    return LcarsAdaptiveProfile(mode = mode, compact = mode != LcarsResponsiveMode.WideLandscape)
}

internal fun resolveLcarsAdaptiveSpacing(
    spacing: LcarsDimensions,
    profile: LcarsAdaptiveProfile,
): LcarsDimensions = when (profile.mode) {
    LcarsResponsiveMode.WideLandscape -> spacing
    LcarsResponsiveMode.Portrait -> spacing.copy(
        buttonMinWidth = 104.dp,
        buttonMinHeight = 52.dp,
        barHeight = 22.dp,
        elbowThickness = 34.dp,
        panelPadding = 10.dp,
        scaffoldControlWidth = 120.dp,
        commandRailWidth = 136.dp,
        commandRailCompactWidth = 112.dp,
    )
    LcarsResponsiveMode.CompactLandscape -> spacing.copy(
        gapLarge = 6.dp,
        buttonMinWidth = 96.dp,
        buttonMinHeight = 48.dp,
        barHeight = 20.dp,
        elbowThickness = 30.dp,
        panelPadding = 8.dp,
        scaffoldControlWidth = 116.dp,
        commandRailWidth = 132.dp,
        commandRailCompactWidth = 108.dp,
    )
}

internal fun LcarsSoundPlayer.playClick() = play(LcarsSoundCue.Click)
internal fun LcarsSoundPlayer.playAlert() = play(LcarsSoundCue.Alert)
internal fun LcarsSoundPlayer.playSliderAdjust() = play(LcarsSoundCue.Adjust)
