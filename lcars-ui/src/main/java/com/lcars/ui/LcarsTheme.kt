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
    val auxiliaryTan: Color = Color(0xFFFFBBAA),
    val lightBlue: Color = Color(0xFF8899FF),
    val violet: Color = Color(0xFFCC99FF),
    val tacticalGreen: Color = Color(0xFF999933),
    val alertRed: Color = Color(0xFFFF2220),
    val background: Color = Color(0xFF000000),
    val panel: Color = Color(0xFF050505),
    val text: Color = Color(0xFFF5F6FA),
    val a1: Color = Color(0xFFFF8800),
    val a2: Color = Color(0xFFFF9966),
    val a3: Color = Color(0xFFFFAA90),
    val a4: Color = Color(0xFFFFCC99),
    val a5: Color = Color(0xFFFFAA00),
    val a6: Color = Color(0xFFCC55FF),
    val a7: Color = Color(0xFF9966FF),
    val a8: Color = Color(0xFFDDBBFF),
    val a9: Color = Color(0xFFCC5599),
    val blue: Color = Color(0xFF5566FF),
    val butterscotch: Color = Color(0xFFFF9966),
    val almondCreme: Color = Color(0xFFFFBBAA),
    val classicRed: Color = Color(0xFFCC4444),
    val tomato: Color = Color(0xFFFF5555),
    val gray: Color = Color(0xFF666688),
    val spaceWhite: Color = Color(0xFFF5F6FA),
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
    val buttonMinHeight: Dp = 56.dp,
    val barHeight: Dp = 28.dp,
    val elbowThickness: Dp = 40.dp,
    val panelPadding: Dp = 12.dp,
    val scaffoldControlWidth: Dp = 150.dp,
    val commandRailWidth: Dp = 154.dp,
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
    style: LcarsStyle = LcarsStyle.ClassicUltra,
    colors: LcarsColors? = null,
    typography: LcarsTypography = LcarsTypography(),
    spacing: LcarsSpacing? = null,
    adaptiveProfile: LcarsAdaptiveProfile = LcarsAdaptiveProfile(),
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalLcarsColors provides (colors ?: style.colors()),
        LocalLcarsTypography provides typography,
        LocalLcarsSpacing provides (spacing ?: style.spacing()),
        LocalLcarsAdaptiveProfile provides adaptiveProfile,
        content = content,
    )
}

@Composable
fun LcarsAdaptiveTheme(
    style: LcarsStyle = LcarsStyle.ClassicUltra,
    colors: LcarsColors? = null,
    typography: LcarsTypography = LcarsTypography(),
    spacing: LcarsSpacing? = null,
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
        val resolvedSpacing = spacing ?: style.spacing()
        LcarsTheme(
            style = style,
            colors = colors,
            typography = typography,
            spacing = resolveLcarsAdaptiveSpacing(resolvedSpacing, profile),
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
