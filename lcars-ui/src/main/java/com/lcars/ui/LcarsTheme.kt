package com.lcars.ui

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Immutable
data class LcarsColors(
    val monoAmber: Color = Color(0xFFFF9900),
    val auxiliaryTan: Color = Color(0xFFCC9966),
    val lightBlue: Color = Color(0xFF99CCFF),
    val violet: Color = Color(0xFF9999CC),
    val tacticalGreen: Color = Color(0xFF33FF33),
    val alertRed: Color = Color(0xFFFF3333),
    val background: Color = Color(0xFF000000),
    val panel: Color = Color(0xFF090909),
    val text: Color = Color(0xFFFFFFFF),
    val a1: Color = Color(0xFFEC943A),
    val a2: Color = Color(0xFFEB9870),
    val a3: Color = Color(0xFFC47D69),
    val a4: Color = Color(0xFFD29A7F),
    val a5: Color = Color(0xFFFAA41B),
    val a6: Color = Color(0xFFC082A9),
    val a7: Color = Color(0xFF9C698A),
    val a8: Color = Color(0xFFB6A5D1),
    val a9: Color = Color(0xFF8B72AA),
)

val LcarsDefaultFontFamily = FontFamily(
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
        letterSpacing = 0.sp,
    ),
    val button: TextStyle = TextStyle(
        fontFamily = defaultFont,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp,
    ),
    val telemetry: TextStyle = TextStyle(
        fontFamily = defaultFont,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.sp,
    ),
    val labelSmall: TextStyle = TextStyle(
        fontFamily = defaultFont,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.sp,
    ),
)

@Immutable
data class LcarsSpacing(
    val gapStandard: Dp = 4.dp,
    val gapLarge: Dp = 8.dp,
    val buttonMinWidth: Dp = 120.dp,
    val buttonMinHeight: Dp = 58.dp,
    val barHeight: Dp = 24.dp,
    val elbowThickness: Dp = 40.dp,
    val panelPadding: Dp = 12.dp,
    val scaffoldControlWidth: Dp = 132.dp,
    val commandRailWidth: Dp = 148.dp,
    val commandRailCompactWidth: Dp = 116.dp,
)

@Immutable
data class LcarsAdaptiveProfile(
    val mode: LcarsResponsiveMode = LcarsResponsiveMode.WideLandscape,
    val compact: Boolean = false,
)

val LocalLcarsColors = staticCompositionLocalOf { LcarsColors() }
val LocalLcarsTypography = staticCompositionLocalOf { LcarsTypography() }
val LocalLcarsSpacing = staticCompositionLocalOf { LcarsSpacing() }
val LocalLcarsAdaptiveProfile = staticCompositionLocalOf { LcarsAdaptiveProfile() }

@Composable
fun LcarsTheme(
    colors: LcarsColors = LcarsColors(),
    typography: LcarsTypography = LcarsTypography(),
    spacing: LcarsSpacing = LcarsSpacing(),
    adaptiveProfile: LcarsAdaptiveProfile = LcarsAdaptiveProfile(),
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalLcarsColors provides colors,
        LocalLcarsTypography provides typography,
        LocalLcarsSpacing provides spacing,
        LocalLcarsAdaptiveProfile provides adaptiveProfile,
        content = content,
    )
}

@Composable
fun LcarsAdaptiveTheme(
    colors: LcarsColors = LcarsColors(),
    typography: LcarsTypography = LcarsTypography(),
    spacing: LcarsSpacing = LcarsSpacing(),
    compactLandscapeHeight: Dp = 520.dp,
    compactWidth: Dp = 600.dp,
    content: @Composable () -> Unit,
) {
    BoxWithConstraints {
        val profile = resolveLcarsAdaptiveProfile(
            width = maxWidth,
            height = maxHeight,
            compactWidth = compactWidth,
            compactLandscapeHeight = compactLandscapeHeight,
        )
        LcarsTheme(
            colors = colors,
            typography = typography,
            spacing = resolveLcarsAdaptiveSpacing(spacing, profile),
            adaptiveProfile = profile,
            content = content,
        )
    }
}

internal fun resolveLcarsAdaptiveProfile(
    width: Dp,
    height: Dp,
    compactWidth: Dp = 600.dp,
    compactLandscapeHeight: Dp = 520.dp,
): LcarsAdaptiveProfile {
    val mode = resolveLcarsResponsiveMode(
        width = width,
        height = height,
        compactWidth = compactWidth,
        compactLandscapeHeight = compactLandscapeHeight,
    )
    return LcarsAdaptiveProfile(
        mode = mode,
        compact = mode != LcarsResponsiveMode.WideLandscape,
    )
}

internal fun resolveLcarsAdaptiveSpacing(
    spacing: LcarsSpacing,
    profile: LcarsAdaptiveProfile,
): LcarsSpacing = when (profile.mode) {
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
        buttonMinHeight = 44.dp,
        barHeight = 20.dp,
        elbowThickness = 30.dp,
        panelPadding = 8.dp,
        scaffoldControlWidth = 116.dp,
        commandRailWidth = 132.dp,
        commandRailCompactWidth = 108.dp,
    )
}
